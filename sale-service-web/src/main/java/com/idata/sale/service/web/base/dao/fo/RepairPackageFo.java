package com.idata.sale.service.web.base.dao.fo;

import java.util.Collection;

public class RepairPackageFo {

    private Collection<Integer> ids;

    public RepairPackageFo() {
        // TODO Auto-generated constructor stub
    }

    public Collection<Integer> getIds() {
        return ids;
    }

    public void setIds(Collection<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "RepairPackageFo [ids=" + ids + "]";
    }

}
