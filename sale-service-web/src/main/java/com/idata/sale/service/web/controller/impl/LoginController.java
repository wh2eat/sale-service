package com.idata.sale.service.web.controller.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.idata.sale.service.web.base.dao.constant.SystemUserType;
import com.idata.sale.service.web.base.service.ISystemUserService;
import com.idata.sale.service.web.base.service.constant.SystemUserLoginStatus;
import com.idata.sale.service.web.base.service.dto.SystemUserDto;
import com.idata.sale.service.web.controller.BaseController;
import com.idata.sale.service.web.controller.ControllerErrorCode;
import com.idata.sale.service.web.controller.SessionAttr;

@Controller
@RequestMapping(path = { "/", "/web" })
public class LoginController extends BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public LoginController() {
        // TODO Auto-generated constructor stub
    }

    @RequestMapping(path = { "/" }, method = RequestMethod.GET)
    public String index() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][index]");
        }

        return "login";
    }

    @RequestMapping(path = { "/search" }, method = RequestMethod.GET)
    public String search() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][search]");
        }

        return "search";
    }

    @Autowired
    private ISystemUserService userServcice;

    @RequestMapping(path = { "/login" }, method = { RequestMethod.POST })
    public String login(SystemUserDto userDto) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][login]");
        }

        if (null == userDto) {
            return "login";
        }

        String loginName = userDto.getLoginName();
        if (StringUtils.isEmpty(loginName)) {

            return "login";
        }

        String password = userDto.getPassword();
        if (StringUtils.isEmpty(password)) {

            return "login";
        }

        SystemUserDto dto = userServcice.login(loginName, password);
        if (null == dto) {
            getRequest().setAttribute("errorCode", ControllerErrorCode.not_found_obj.getCode());
            getRequest().setAttribute("loginName", loginName);
            getRequest().setAttribute("password", password);
            return "login";
        }

        if (SystemUserLoginStatus.Success.equals(dto.getLoginStatus())) {

            if (dto.getDbo().getType().intValue() == SystemUserType.MaintenanceEngineer.getCode()) {
                getRequest().setAttribute("errorCode", ControllerErrorCode.user_not_allow_login_error.getCode());
                getRequest().setAttribute("loginName", loginName);
                getRequest().setAttribute("password", password);
                return "login";
            }

            clearSession();

            getRequest().getSession().setAttribute(SessionAttr.LoginUser.getName(), dto.getDbo());
            getRequest().getSession().setAttribute(SessionAttr.LoginRepairStation.getName(), dto.getRepairStation());

            return "redirect:index";
        }
        else if (SystemUserLoginStatus.Failed_Password_Error.equals(dto.getLoginStatus())) {
            getRequest().setAttribute("errorCode", ControllerErrorCode.user_password_error.getCode());
            getRequest().setAttribute("loginName", loginName);
            return "login";
        }

        getRequest().setAttribute("loginName", loginName);
        getRequest().setAttribute("password", password);
        getRequest().setAttribute("errorCode", ControllerErrorCode.unknown_exception.getCode());
        return "login";
    }

    private void clearSession() {
        getRequest().getSession().removeAttribute(SessionAttr.LoginUser.getName());
        getRequest().getSession().removeAttribute(SessionAttr.LoginRepairStation.getName());
        getRequest().getSession().removeAttribute(SessionAttr.RepairStationId.getName());
    }

}
