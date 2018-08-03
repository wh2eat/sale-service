package com.idata.sale.service.web.base.dao.constant;

public enum SystemUserType {

    // 系统管理员
    Admin(10, "admin", "System Admin"),

    // 维修站管理员
    RepairStationAdmin(11, "repairStationAdmin", "Repair Station Admin"),

    // 业务管理员
    BusinessAdmin(12, "businessAdmin", "Business Admin"),

    // 企业管理员
    CorporateAdmin(13, "corporateAdmin", "Corporate Admin"),

    // 维修工程师
    MaintenanceEngineer(20, "maintenanceEngineer", "Maintenance Engineer");

    private int code;

    private String name;

    private String desc;

    private SystemUserType(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getName() {
        return this.name;
    }

    public static String getName(int code) {

        if (Admin.code == code) {
            return Admin.name;
        }
        else if (RepairStationAdmin.code == code) {
            return RepairStationAdmin.name;
        }
        else if (BusinessAdmin.code == code) {
            return BusinessAdmin.name;
        }
        else if (CorporateAdmin.code == code) {
            return CorporateAdmin.name;
        }
        else if (MaintenanceEngineer.code == code) {
            return MaintenanceEngineer.name;
        }

        return "";
    }

    public static int getCode(String name) {

        if (Admin.name.equals(name)) {
            return Admin.code;
        }
        else if (BusinessAdmin.name.equals(name)) {
            return BusinessAdmin.code;
        }
        else if (CorporateAdmin.name.equals(name)) {
            return CorporateAdmin.code;
        }
        else if (MaintenanceEngineer.name.equals(name)) {
            return MaintenanceEngineer.code;
        }
        else if (RepairStationAdmin.name.equals(name)) {
            return RepairStationAdmin.code;
        }

        throw new RuntimeException("not found user type code ,name" + name);
    }

}
