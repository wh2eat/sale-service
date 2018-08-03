package com.idata.sale.service.web.rest.browser.dto;

public class RepairDeviceProcessDto {

    private String searchType;

    private String searchValue;

    public RepairDeviceProcessDto() {
        // TODO Auto-generated constructor stub
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    @Override
    public String toString() {
        return "RepairDeviceProcessDto [searchType=" + searchType + ", searchValue=" + searchValue + "]";
    }

}
