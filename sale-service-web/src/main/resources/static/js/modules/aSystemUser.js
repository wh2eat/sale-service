/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','alog' ], function(exports) {

    var $ = layui.$;
    var alog = layui.alog;

    var obj = {
        getDisplay : function(userType) {
           
            alog.d("userType:"+userType);
                       
            // 系统管理员
            if (10 == userType || "admin" == userType) {
                return "系统管理员";
            }
            
            // 维修站管理员
            if (11 == userType || "repairStationAdmin" == userType) {
                return "维修站管理员";
            }

            // 业务管理员
            if (12 == userType || "businessAdmin" == userType) {
                return "业务管理员";
            }

            // 企业管理员
            if (13 == userType || "corporateAdmin" == userType) {
                return "企业管理员";
            }

            // 维修工程师
            if (20 == userType || "maintenanceEngineer" == userType) {
                return "维修工程师";
            }

            return "未知(" + userType + ")";
        }
    };

    exports('aSystemUser', obj); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
