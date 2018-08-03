package com.idata.sale.service.web.base.dao.constant;

public enum RepairStatus {

    // 初始状态
    Received(10, "received"),

    // 检测状态
    CheckWait(20, "checkWait"), CheckAgain(21, "checkAgain"), Checking(22, "checking"), Checked(23,
            "checked"), CheckFinish(29, "checkFinish"),

    // 报价状态
    QuotationWait(30, "QuotationWait"), QuotationFinish(39, "QuotationFinish"),

    // 用户确认状态
    CustomerConfirmWait(40, "customerConfirmWait"), CustomerConfirmFinish(49, "CustomerConfirmFinish"),

    // 支付状态
    PayWait(50, "payWait"), PayFinish(59, "payFinish"),

    // 维修状态
    RepairWait(60, "repairWait"), RepairAgain(61, "repairAgain"), Repairing(62, "repairing"), RepairFinish(69,
            "repairFinish"),

    // 返客状态
    BackWait(70, "backWait"), Backing(71, "backing"), BackFinish(79, "backFinish"),

    // 终态
    Finish(100, "finish"),

    NotBacking(-71, "not backing");

    private int code;

    private String name;

    private RepairStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static int getCode(String name) {

        if (Received.name.equals(name)) {
            return Received.code;
        }
        else if (CheckWait.name.equals(name)) {
            return CheckWait.code;
        }
        else if (CheckAgain.name.equals(name)) {
            return CheckAgain.code;
        }
        else if (Checking.name.equals(name)) {
            return Checking.code;
        }
        else if (Checked.name.equals(name)) {
            return Checked.code;
        }
        else if (CheckFinish.name.equals(name)) {
            return CheckFinish.code;
        }
        else if (QuotationWait.name.equals(name)) {
            return QuotationWait.code;
        }
        else if (QuotationFinish.name.equals(name)) {
            return QuotationFinish.code;
        }
        else if (CustomerConfirmWait.name.equals(name)) {
            return CustomerConfirmWait.code;
        }
        else if (CustomerConfirmFinish.name.equals(name)) {
            return CustomerConfirmFinish.code;
        }
        else if (PayWait.name.equals(name)) {
            return PayWait.code;
        }
        else if (PayFinish.name.equals(name)) {
            return PayFinish.code;
        }
        else if (RepairWait.name.equals(name)) {
            return RepairWait.code;
        }
        else if (RepairAgain.name.equals(name)) {
            return RepairAgain.code;
        }
        else if (Repairing.name.equals(name)) {
            return Repairing.code;
        }
        else if (RepairFinish.name.equals(name)) {
            return RepairFinish.code;
        }
        else if (BackWait.name.equals(name)) {
            return BackWait.code;
        }
        else if (Backing.name.equals(name)) {
            return Backing.code;
        }
        else if (BackFinish.name.equals(name)) {
            return BackFinish.code;
        }
        else if (Finish.name.equals(name)) {
            return Finish.code;
        }

        return -1;

    }
}
