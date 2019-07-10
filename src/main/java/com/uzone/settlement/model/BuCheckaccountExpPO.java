package com.uzone.settlement.model;

import java.util.Date;

public class BuCheckaccountExpPO {
    private Long id;

    private Date checkaccountTime;

    private String bizOrderNo;

    private String transferType;

    private String payType;

    private Long transAmount;

    private String allinpayOrderNo;

    private String allinpayTransferType;

    private Long allinpayTransAmount;

    private Date createTime;

    private Date updateTime;

    private Integer versions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCheckaccountTime() {
        return checkaccountTime;
    }

    public void setCheckaccountTime(Date checkaccountTime) {
        this.checkaccountTime = checkaccountTime;
    }

    public String getBizOrderNo() {
        return bizOrderNo;
    }

    public void setBizOrderNo(String bizOrderNo) {
        this.bizOrderNo = bizOrderNo == null ? null : bizOrderNo.trim();
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType == null ? null : transferType.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public String getAllinpayOrderNo() {
        return allinpayOrderNo;
    }

    public void setAllinpayOrderNo(String allinpayOrderNo) {
        this.allinpayOrderNo = allinpayOrderNo == null ? null : allinpayOrderNo.trim();
    }

    public String getAllinpayTransferType() {
        return allinpayTransferType;
    }

    public void setAllinpayTransferType(String allinpayTransferType) {
        this.allinpayTransferType = allinpayTransferType == null ? null : allinpayTransferType.trim();
    }

    public Long getAllinpayTransAmount() {
        return allinpayTransAmount;
    }

    public void setAllinpayTransAmount(Long allinpayTransAmount) {
        this.allinpayTransAmount = allinpayTransAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersions() {
        return versions;
    }

    public void setVersions(Integer versions) {
        this.versions = versions;
    }
}