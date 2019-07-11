package com.uzone.settlement.model;

import java.util.Date;

/**
 * 对账日志表(或对账主表)
 */
public class BuCheckaccountLogPO {
	
    private Long id;

    private Date checkaccountTime;

    private String befOrderNo;

    private String aftOrderNo;

    private String checkaccountStatus;

    private Long transAmount;

    private Long transType;

    private String payType;

    private Long transCount;

    private String url;

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

    public String getBefOrderNo() {
        return befOrderNo;
    }

    public void setBefOrderNo(String befOrderNo) {
        this.befOrderNo = befOrderNo == null ? null : befOrderNo.trim();
    }

    public String getAftOrderNo() {
        return aftOrderNo;
    }

    public void setAftOrderNo(String aftOrderNo) {
        this.aftOrderNo = aftOrderNo == null ? null : aftOrderNo.trim();
    }

    public String getCheckaccountStatus() {
        return checkaccountStatus;
    }

    public void setCheckaccountStatus(String checkaccountStatus) {
        this.checkaccountStatus = checkaccountStatus == null ? null : checkaccountStatus.trim();
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }

    public Long getTransType() {
        return transType;
    }

    public void setTransType(Long transType) {
        this.transType = transType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public Long getTransCount() {
        return transCount;
    }

    public void setTransCount(Long transCount) {
        this.transCount = transCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
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