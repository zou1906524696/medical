package com.zzf.medical.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Category implements Serializable {
    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}