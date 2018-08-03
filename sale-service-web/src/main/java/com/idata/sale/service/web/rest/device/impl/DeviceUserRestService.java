package com.idata.sale.service.web.rest.device.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.idata.sale.service.web.base.dao.constant.SystemUserType;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.ISystemUserTokenService;
import com.idata.sale.service.web.base.service.ServiceException;
import com.idata.sale.service.web.base.service.constant.SystemUserLoginStatus;
import com.idata.sale.service.web.base.service.dto.SystemUserDto;
import com.idata.sale.service.web.base.service.dto.SystemUserTokenDto;
import com.idata.sale.service.web.rest.RestCode;
import com.idata.sale.service.web.rest.RestException;
import com.idata.sale.service.web.rest.device.DeviceApi;
import com.idata.sale.service.web.rest.device.DeviceTokenCheck;
import com.idata.sale.service.web.rest.device.dto.DeviceUserDto;
import com.idata.sale.service.web.util.AesUtil;

@RestController
@RequestMapping(path = "/api/device/user/", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DeviceUserRestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceUserRestService.class);

    public DeviceUserRestService() {

    }

    @Autowired
    private ISystemUserTokenService systemUserTokenService;

    @Autowired
    private ISystemUserService systemUserService;

    @RequestMapping(path = "login", method = RequestMethod.POST)
    public Object login(@RequestBody DeviceUserDto userLoginDto) throws RestException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][login][" + userLoginDto + "]");
        }

        String apiVersion = userLoginDto.getApiVersion();
        if (StringUtils.isEmpty(apiVersion)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][apiVersion is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "apiVersion");
        }

        String apiKey = DeviceApi.getKey(apiVersion);
        if (null == apiKey) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][apiVersion is error,apiVersion:" + apiVersion + "]");
            }
            throw new RestException(RestCode.DeviceApiError, apiVersion);
        }

        String data = userLoginDto.getData();
        if (StringUtils.isEmpty(data)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][data is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "data");
        }

        String dataSign = userLoginDto.getDataSignature();
        if (StringUtils.isEmpty(dataSign)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][dataSignature is empty]");
            }
            throw new RestException(RestCode.FieldIsEmpty, "dataSignature");
        }

        long requestMillis = userLoginDto.getRequestMillis();
        if (requestMillis <= 0) {
            throw new RestException(RestCode.FieldIsEmpty, "requestMillis");
        }

        String dataEncrypt = AesUtil.decrypt(data, apiKey);

        if (null == dataEncrypt) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][dataEncrypt decrypt failed]");
            }
            throw new RestException(RestCode.UnknownException);
        }

        String[] params = dataEncrypt.split("&");

        int length = params.length;
        if (3 != length) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][dataEncrypt foramt is error]");
            }
            throw new RestException(RestCode.UnknownException, "param error");
        }

        String dataMd5 = DigestUtils.md5Hex(dataEncrypt);
        if (!dataSign.equals(dataMd5)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][data sign check failed]");
            }
            throw new RestException(RestCode.UnknownException, "data sign check failed.");
        }

        String loginName = null;
        String password = null;
        long millis = -1l;

        try {
            loginName = URLDecoder.decode(params[0], "utf-8");
            password = URLDecoder.decode(params[1], "utf-8");
            millis = Long.parseLong(params[2]);
        }
        catch (Exception e) {
            LOGGER.error("", e);
            throw new RestException(RestCode.UnknownException);
        }

        SystemUserDto userDto = systemUserService.login(loginName, password);

        if (null == userDto) {
            LOGGER.info("[][][not found user ,loginName:" + loginName + "]");
            throw new RestException(RestCode.ObjectNotFound);
        }
        if (SystemUserType.MaintenanceEngineer.getCode() != userDto.getDbo().getType().intValue()) {
            LOGGER.info("[][][not allow login by device,loginName:" + loginName + "]");
            throw new RestException(RestCode.UserNotAllowLoginError);
        }

        SystemUserLoginStatus loginStatus = userDto.getLoginStatus();
        if (SystemUserLoginStatus.Success.equals(loginStatus)) {

            SystemUserTokenDto tokenDto = systemUserTokenService.getUserToken(loginName);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][token:" + tokenDto.getToken() + "]");
            }

            String encryptToken = AesUtil.encrypt(tokenDto.getToken() + "&" + tokenDto.getKey(), apiKey);
            userDto.setToken(encryptToken);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][success,loginName:" + userDto.getLoginName() + "]");
            }

            return userDto;
        }
        else if (SystemUserLoginStatus.Failed_Password_Error.equals(loginStatus)) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][login][failed,case:user password is error,loginName:" + userDto.getLoginName() + "]");
            }

            throw new RestException(RestCode.UserPasswordError);
        }
        else {
            throw new RestException(RestCode.UnknownException, "user login failed");
        }
    }

    @DeviceTokenCheck
    @RequestMapping(path = "/update/password", method = RequestMethod.POST)
    public Object updatePassword(@RequestBody DeviceUserDto deviceUserDto) throws RestException, ServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][updatePassword][" + deviceUserDto + "]");
        }

        String udid = deviceUserDto.getId();

        systemUserService.updatePassword(udid, deviceUserDto.getOldPassword(), deviceUserDto.getPassword());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][updatePassword][success]");
        }

        return true;

    }

    @DeviceTokenCheck
    @RequestMapping(path = "get/token", method = RequestMethod.POST)
    public Object getToken(@RequestHeader(name = "idata-ss-device-token", required = false) String token,
            @RequestBody DeviceUserDto userLoginDto) throws RestException {

        if (StringUtils.isEmpty(token)) {
            throw new RestException(RestCode.FieldIsEmpty);
        }

        SystemUserTokenDto tokenDto = systemUserTokenService.get(token);
        if (null == tokenDto) {
            throw new RestException(RestCode.UnknownException, "not found token");
        }

        String data = userLoginDto.getData();
        String decryptData = AesUtil.decrypt(data, tokenDto.getKey());

        String[] datas = decryptData.split("&");

        String loginName = null;
        try {
            loginName = URLDecoder.decode(datas[0], "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (null == loginName) {
            throw new RestException(RestCode.UnknownException, "get login name failed");
        }

        if (!loginName.equals(tokenDto.getLoginName())) {
            throw new RestException(RestCode.UnknownException, "login name validate failed");
        }

        String apiVersion = userLoginDto.getApiVersion();
        String apiKey = DeviceApi.getKey(apiVersion);
        if (null == apiKey) {
            throw new RestException(RestCode.DeviceApiError);
        }

        SystemUserTokenDto newTokenDto = systemUserTokenService.get(token);

        String newToken = null;
        try {
            newToken = URLEncoder.encode(newTokenDto.getToken(), "utf-8") + "&"
                    + URLEncoder.encode(newTokenDto.getKey(), "utf-8");
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("[][][]", e);
        }

        newToken = AesUtil.encrypt(newToken, apiKey);

        SystemUserDto userDto = new SystemUserDto();
        userDto.setToken(newToken);

        return userDto;
    }

}
