package com.idata.sale.service.web.controller.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.idata.sale.service.web.base.dao.dbo.SystemRepairStationDbo;
import com.idata.sale.service.web.controller.BaseController;
import com.idata.sale.service.web.controller.LoginUserCheck;
import com.idata.sale.service.web.controller.SessionAttr;

@Controller
public class UserController extends BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController() {
        // TODO Auto-generated constructor stub
    }

    @LoginUserCheck
    @RequestMapping(path = { "/index" }, method = RequestMethod.GET)
    public String index(@RequestParam(name = "target", required = false) String target,
            @RequestParam(name = "iid", required = false) String iid,
            @RequestParam(name = "pid", required = false) String pid,
            @RequestParam(name = "rdid", required = false) String rdid,
            @RequestParam(name = "rdsn", required = false) String sn,
            @RequestParam(name = "rsid", required = false) String rsid,
            @RequestParam(name = "rbpid", required = false) String rbpid) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][index][target=" + target + ";iid:" + iid + ";pid:" + pid + ";rdid:" + rdid + ";rdsn:" + sn
                    + "]");
        }

        if (StringUtils.isNotBlank(iid)) {
            getRequest().setAttribute("repairInvoiceId", iid);
        }
        if (StringUtils.isNotBlank(pid)) {
            getRequest().setAttribute("repairPackageId", pid);
        }
        if (StringUtils.isNotBlank(rdid)) {
            getRequest().setAttribute("repairDeviceId", rdid);
        }
        if (StringUtils.isNotBlank(sn)) {
            getRequest().setAttribute("sn", sn);
        }

        if (StringUtils.isNotBlank(rbpid)) {
            getRequest().setAttribute("repairBackPackageId", rbpid);
        }

        if (StringUtils.isNotEmpty(rsid)) {
            getRequest().getSession().setAttribute(SessionAttr.RepairStationId.getName(), rsid);
        }
        else {
            Object repairStationIdObj = getRequest().getSession().getAttribute(SessionAttr.RepairStationId.getName());
            if (null == repairStationIdObj || StringUtils.isEmpty(repairStationIdObj.toString())) {
                Object repairStationObj = getRequest().getSession()
                        .getAttribute(SessionAttr.LoginRepairStation.getName());
                if (null != repairStationObj) {
                    SystemRepairStationDbo repairStationDbo = (SystemRepairStationDbo) repairStationObj;
                    getRequest().getSession().setAttribute(SessionAttr.RepairStationId.getName(),
                            repairStationDbo.getUdid());
                }
            }
        }

        String indexNavItem = "";

        try {
            // 系统管理
            if ("aboutSystem".equals(target)) {
                indexNavItem = "systemAboutSystem";
                return "user/aboutSystem";
            }
            else if ("users".equals(target)) {
                indexNavItem = "systemUserList";
                return "user/userList";
            }
            // 维修管理
            else if ("repairStations".equals(target)) {
                indexNavItem = "systemRepairStationList";
                return "user/repairStationList";
            }
            else if ("repairPackages".equals(target)) {
                indexNavItem = "repairPackageList";
                return "user/repairPackageList";
            }
            else if ("repairInvoices".equals(target)) {
                indexNavItem = "repairInvoiceList";
                return "user/repairInvoiceList";
            }
            else if ("repairDevices".equals(target)) {
                indexNavItem = "repairDeviceList";
                return "user/repairDeviceList";
            }
            // 维修设备管理
            else if ("repairDeviceManageNews".equals(target)) {
                indexNavItem = "rDeviceManageNewList";
                return "user/repairDeviceManageNewList";
            }
            else if ("repairDeviceManageReviews".equals(target)) {
                indexNavItem = "rDeviceManageReviewList";
                return "user/repairDeviceManageReviewList";
            }
            else if ("repairDeviceManageQuotations".equals(target)) {
                indexNavItem = "rDeviceManageQuotationList";

                if (StringUtils.isEmpty(rdid) || StringUtils.isEmpty(sn)) {
                    return "user/repairDeviceManageQuotationList";
                }

                return "user/repairDeviceQuotationList";
            }
            else if ("repairDeviceManageConfirms".equals(target)) {
                indexNavItem = "rDeviceManageConfirmList";

                if (StringUtils.isEmpty(rdid) || StringUtils.isEmpty(sn)) {
                    return "user/repairDeviceManageConfirmList";
                }

                return "user/repairDeviceQuotationList";
            }
            else if ("repairDeviceManagePays".equals(target)) {
                indexNavItem = "rDeviceManagePayList";

                if (StringUtils.isEmpty(rdid) || StringUtils.isEmpty(sn)) {
                    return "user/repairDeviceManagePayList";
                }

                return "user/repairDeviceQuotationList";
            }
            else if ("repairDeviceManageWaits".equals(target)) {
                indexNavItem = "rDeviceManageWaitList";
                return "user/repairDeviceManageWaitList";
            }
            else if ("repairDeviceManageSearchs".equals(target)) {
                indexNavItem = "rDeviceManageSearchList";
                return "user/repairDeviceManageSearchList";
            }

            // 返客管理
            else if ("repairBackPackages".equals(target)) {
                indexNavItem = "rBackPackageList";
                return "user/repairBackPackageList";
            }
            else if ("repairBackPackageDevices".equals(target)) {
                indexNavItem = "rBackPackageDeviceList";
                return "user/repairBackPackageDeviceList";
            }
            else if ("repairBackDevices".equals(target)) {
                indexNavItem = "rBackDeviceList";
                return "user/repairBackDeviceList";
            }
            else if ("repairBackDeviceWaits".equals(target)) {
                indexNavItem = "rBackDeviceWaitList";
                return "user/repairBackDeviceWaitList";
            }

            else if ("systemCustomers".equals(target)) {
                indexNavItem = "sDataCustomerList";
                return "user/systemCustomerList";
            }

            // else if ("repairDeviceManageBacks".equals(target)) {
            // indexNavItem = "repairDeviceManageBackList";
            // return "user/repairDeviceManageBackList";
            // }

            return "user/index";
        }
        finally {
            getRequest().setAttribute("indexNavItem", indexNavItem);
        }

    }

    @RequestMapping("/index/logout")
    public String logout() {

        return "redirect:/";
    }

}
