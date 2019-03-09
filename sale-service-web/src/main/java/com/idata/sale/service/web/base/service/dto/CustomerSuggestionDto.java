package com.idata.sale.service.web.base.service.dto;

import java.util.List;

public class CustomerSuggestionDto {

    private String uid;

    private Integer id;

    private String category;

    private String contact;

    private String phoneNumber;

    private String desc;

    private Integer deviceNumber;

    private List<String> storeUrl;

    public CustomerSuggestionDto() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Integer deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public List<String> getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(List<String> storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CustomerSuggestionDto [uid=" + uid + ", id=" + id + ", category=" + category + ", contact=" + contact
                + ", phoneNumber=" + phoneNumber + ", desc=" + desc + ", deviceNumber=" + deviceNumber + ", storeUrl="
                + storeUrl + "]";
    }

}
