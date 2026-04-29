package com.ecommerce.campusai.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Merchant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String merchantName;
    private String contactPerson;
    private String contactPhone;
    private String address;
    private String businessLicense;
    private String description;
    private String logo;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private String rejectReason;
}
