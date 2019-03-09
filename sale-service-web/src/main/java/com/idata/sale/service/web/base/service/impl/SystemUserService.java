package com.idata.sale.service.web.base.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.BaseDaoException;
import com.idata.sale.service.web.base.dao.PageInfo;
import com.idata.sale.service.web.base.dao.dbo.RepairDeviceDbo;
import com.idata.sale.service.web.base.dao.dbo.SystemUserDbo;
import com.idata.sale.service.web.base.dao.impl.SystemUserDao;
import com.idata.sale.service.web.base.service.ISystemRepairStationService;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.ServiceCode;
import com.idata.sale.service.web.base.service.constant.SystemUserLoginStatus;
import com.idata.sale.service.web.base.service.dto.SystemUserDto;
import com.idata.sale.service.web.util.AesUtil;

@Service
public class SystemUserService implements ISystemUserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUserService.class);

    public SystemUserService() {

    }

    @Autowired
    private SystemUserDao systemUserDao;

    @Autowired
    private SystemUserAsyncService userAsyncService;

    @Autowired
    private ISystemRepairStationService systemRepairStationService;

    @Override
    public SystemUserDto login(String loginName, String password) {

        SystemUserDto dto = new SystemUserDto();

        try {
            SystemUserDbo userDbo = get(loginName, password);

            if (null != userDbo) {
                dto.setRepairStation(systemRepairStationService.get(userDbo.getRepairStationId()));
                dto.setLoginStatus(SystemUserLoginStatus.Success);
                dto.setDbo(userDbo);
                userAsyncService.updateLoginTime(userDbo.getId());
                LOGGER.info("[][login][success,loginName:" + userDbo.getLoginName() + "]");
            }
            else {
                return null;
            }
        }
        catch (ServiceException e) {
            if (e.getCode().equals(ServiceCode.user_password_error)) {
                dto.setLoginStatus(SystemUserLoginStatus.Failed_Password_Error);
                LOGGER.info("[][login][failed,case:password error,loginName:" + loginName + "]");
            }
            else {
                LOGGER.error("[][login][failed]", e);
            }
        }

        return dto;
    }

    private SystemUserDbo get(String loginName, String password) throws ServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][login][loginName:" + loginName + ";password:" + password + "]");
        }

        SystemUserDbo userDbo = systemUserDao.getUser(loginName);

        if (null == userDbo) {
            LOGGER.info("[][login][failed,case:not found user by loginName:" + loginName + "]");
            return null;
        }

        Integer userId = userDbo.getId();
        String udid = userDbo.getUdid();
        String encryptPassword = getEncryptPassword(userId, udid, password);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][login][encryptPassword:" + encryptPassword + "]");
        }

        String dbPassword = userDbo.getPassword();
        if (dbPassword.equals(encryptPassword)) {
            return userDbo;
        }
        else {
            throw new ServiceException(ServiceCode.user_password_error,
                    "loginName:" + loginName + ";password:" + password + ",params is error ");
        }
    }

    @Override
    public SystemUserDto loginSupport(String loginName, String password) {

        SystemUserDto userDto = new SystemUserDto();

        try {
            SystemUserDbo userDbo = get(loginName, password);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][loginSupport][" + userDbo + "]");
            }
            if (null != userDbo) {

                if (null != userDbo.getSupportManager() && 1 == userDbo.getSupportManager().intValue()) {
                    userDto.setLoginStatus(SystemUserLoginStatus.Success);
                    userDto.setDbo(userDbo);
                    return userDto;
                }
                userDto.setLoginStatus(SystemUserLoginStatus.Failed_Unsupport_Error);
                return userDto;
            }
            return null;
        }
        catch (ServiceException e) {
            if (e.getCode().equals(ServiceCode.user_password_error)) {
                userDto.setLoginStatus(SystemUserLoginStatus.Failed_Password_Error);
            }
            else {
                LOGGER.error("[][][]", e);
            }
        }

        return userDto;

    }

    @Override
    public void add(SystemUserDbo userDbo) throws ServiceException {
        String loginName = userDbo.getLoginName();
        boolean hasExist = systemUserDao.existByLoginName(loginName);
        if (hasExist) {
            throw new ServiceException(ServiceCode.user_loginName_exist, "user add failed,case:user loginName exist");
        }

        userDbo.setCreateTime(new Date(System.currentTimeMillis()));
        userDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        String udid = getUdid(loginName);
        userDbo.setUdid(udid);

        try {
            int userId = (int) systemUserDao.save(userDbo);

            userDbo.setId(userId);

            String encryptPassword = getEncryptPassword(userId, udid, userDbo.getPassword());

            systemUserDao.updateUserPassword(userId, encryptPassword);

            LOGGER.info("[][add][success," + userDbo.toString() + "]");
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][][]", e);
            throw new ServiceException(ServiceCode.system_db_exception, "add user failed,case:" + e.getMessage());
        }
    }

    private String getEncryptPassword(Integer userId, String udid, String password) {
        String userKey = getUserEncryptKey(userId, udid);
        return encryptUserPassword(udid, password, userKey);
    }

    @Override
    public boolean update(SystemUserDbo userDbo) throws ServiceException {

        String udid = userDbo.getUdid();
        SystemUserDbo dbUser = systemUserDao.getUserByUdid(udid);
        if (null == dbUser) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][update][failed,case:not find user by udid:" + udid + "]");
            }
            return false;
        }

        userDbo.setId(dbUser.getId());
        userDbo.setUdid(null);

        String password = userDbo.getPassword();
        if (StringUtils.isNotBlank(password)) {
            password = getEncryptPassword(dbUser.getId(), dbUser.getUdid(), password);
            userDbo.setPassword(password);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][user password update]");
            }
        }

        userDbo.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            systemUserDao.update(userDbo);
            return true;
        }
        catch (BaseDaoException e) {
            LOGGER.error("[][update][failed]", e);
        }
        finally {
            dbUser = null;
        }

        return false;
    }

    @Override
    public void updatePassword(String udid, String oldPassword, String newPassword) throws ServiceException {

        SystemUserDbo userDbo = systemUserDao.getUserByUdid(udid);
        if (null == userDbo) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][updatePassword][not found user by udid:" + udid + "]");
            }
            throw new ServiceException(ServiceCode.user_not_fonud, "");
        }
        String dbPassword = userDbo.getPassword();
        int userId = userDbo.getId();

        String encryptOldPassword = getEncryptPassword(userId, udid, oldPassword);

        if (!dbPassword.equals(encryptOldPassword)) {
            throw new ServiceException(ServiceCode.user_password_error,
                    "update user password error,case:old password error");
        }

        String encryptNewPassword = getEncryptPassword(userId, udid, newPassword);

        systemUserDao.updateUserPassword(userId, encryptNewPassword);

        LOGGER.info("[][updatePassword][success,userId:" + userId + "]");
    }

    @Override
    public List<SystemUserDbo> getList(PageInfo pageInfo, RepairDeviceDbo filter) throws ServiceException {

        return systemUserDao.findPageList(pageInfo, filter);
    }

    @Override
    public boolean existByLoginName(String loginName, String udid) throws ServiceException {

        SystemUserDbo userDbo = new SystemUserDbo();
        userDbo.setLoginName(loginName);
        userDbo.setUdid(udid);
        try {
            return systemUserDao.exist(userDbo);
        }
        catch (SQLException e) {
            LOGGER.error("[][existByLoginName][failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
        finally {
            userDbo = null;
        }
    }

    @Override
    public boolean existByEmail(String email, String udid) throws ServiceException {
        SystemUserDbo userDbo = new SystemUserDbo();
        userDbo.setEmail(email);
        userDbo.setUdid(udid);
        try {
            return systemUserDao.exist(userDbo);
        }
        catch (SQLException e) {
            LOGGER.error("[][existByLoginName][failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
        finally {
            userDbo = null;
        }
    }

    @Override
    public boolean existByTelephone(String telephone, String udid) throws ServiceException {
        SystemUserDbo userDbo = new SystemUserDbo();
        userDbo.setTelephone(telephone);
        userDbo.setUdid(udid);
        try {
            return systemUserDao.exist(userDbo);
        }
        catch (SQLException e) {
            LOGGER.error("[][existByLoginName][failed]", e);
            throw new ServiceException(ServiceCode.system_db_exception, e.getMessage());
        }
        finally {
            userDbo = null;
        }
    }

    @Override
    public SystemUserDbo get(String udid) {

        return systemUserDao.getUserByUdid(udid);

    }

    @Override
    public Integer getId(String udid) {
        SystemUserDbo userDbo = systemUserDao.getUserByUdid(udid);
        if (null != userDbo) {
            try {
                return userDbo.getId();
            }
            finally {
                userDbo = null;
            }
        }
        return null;
    }

    @Override
    public Integer getRepairStationId(String udid) {
        SystemUserDbo userDbo = systemUserDao.getUserByUdid(udid);
        if (null != userDbo) {
            try {
                return userDbo.getRepairStationId();
            }
            finally {
                userDbo = null;
            }
        }
        return null;
    }

    @Override
    public void remove(Integer id) throws ServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][remove][id:" + id + "]");
        }

        systemUserDao.deleteById(id);
    }

    private String getUserEncryptKey(Integer userId, String udid) {

        String all = userId + "&" + udid;
        all = DigestUtils.md5Hex(all);
        return all.substring(0, 4) + all.substring(all.length() - 4, all.length());
    }

    private final static String userEncryptSalt = "iDtS2User!";

    private String encryptUserPassword(String udid, String password, String userKey) {
        try {
            String str = URLEncoder.encode(udid, "utf-8") + "&" + URLEncoder.encode(password, "utf-8");
            String strMd5 = DigestUtils.md5Hex(str) + userEncryptSalt;

            return URLEncoder.encode(AesUtil.encrypt(strMd5, userKey), "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("[][encryptPassword][encode field failed]", e);
        }
        return null;
    }

    private String getUdid(String loginName) {
        String serverName = loginName + "&" + System.currentTimeMillis() + "&" + RandomUtils.nextInt(10000);
        return UUID.nameUUIDFromBytes(serverName.getBytes()).toString();
    }

    @Override
    public void setUserOnlyame(List<RepairDeviceDbo> repairDeviceDbos) {

        Set<Integer> ids = new HashSet<>();
        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {
            Integer detectUserId = repairDeviceDbo.getDetectUserId();
            if (null != detectUserId && !ids.contains(detectUserId)) {
                ids.add(detectUserId);
            }

            Integer repairUserId = repairDeviceDbo.getRepairUserId();
            if (null != repairUserId && !ids.contains(repairUserId)) {
                ids.add(repairUserId);
            }

            Integer quotationUserId = repairDeviceDbo.getQuotationUserId();
            if (null != quotationUserId && !ids.contains(quotationUserId)) {
                ids.add(quotationUserId);
            }
        }

        if (CollectionUtils.isEmpty(ids)) {
            ids = null;
            return;
        }

        List<SystemUserDbo> userDbos = systemUserDao.getListWithName(ids);

        ids.clear();
        ids = null;

        Map<Integer, SystemUserDbo> map = new HashMap<>();
        for (SystemUserDbo systemUserDbo : userDbos) {
            map.put(systemUserDbo.getId(), systemUserDbo);
        }

        for (RepairDeviceDbo repairDeviceDbo : repairDeviceDbos) {

            Integer detectUserId = repairDeviceDbo.getDetectUserId();
            if (null != detectUserId) {
                repairDeviceDbo.setDetectUser(map.get(detectUserId));
            }

            Integer repairUserId = repairDeviceDbo.getRepairUserId();
            if (null != repairUserId) {
                repairDeviceDbo.setRepairUser(map.get(repairUserId));
            }
            Integer quotationUserId = repairDeviceDbo.getQuotationUserId();
            if (null != quotationUserId) {
                repairDeviceDbo.setQuotationUser(map.get(quotationUserId));
            }
        }

        map.clear();
        map = null;

        userDbos.clear();
        userDbos = null;

    }

    public static void main(String[] args) {
        Integer userId = 1;
        String udid = "bcceb108-56ac-3967-9a9a-bd30db69461a";

        String all = userId + "&" + udid;
        all = DigestUtils.md5Hex(all);
        String key = all.substring(0, 4) + all.substring(all.length() - 4, all.length());

        String encryptContent = "sooPhzNVB6%2FyQTn2TbgDWz2BsqhSjUw6f1HBWKLfXlcIjQwDy72ZMTt75NhTlugr%0D%0A";

        try {
            encryptContent = URLDecoder.decode(encryptContent, "utf-8");
            System.out.println(encryptContent);
            encryptContent = encryptContent.replaceAll("\r|\n", "");
            System.out.println(encryptContent);
        }
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encryptContent = "sooPhzNVB6/yQTn2TbgDWz2BsqhSjUw6f1HBWKLfXlcIjQwDy72ZMTt75NhTlugr";

        String content = AesUtil.decrypt(encryptContent, key);
        System.out.println(content);

        // String str = URLEncoder.encode(udid, "utf-8") + "&" +
        // URLEncoder.encode(password, "utf-8");
        // String strMd5 = DigestUtils.md5Hex(str) + userEncryptSalt;

        // return URLEncoder.encode(AesUtil.encrypt(strMd5, userKey), "utf-8");

    }

}
