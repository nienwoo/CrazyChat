/**
 * Created by 尼恩 at 疯狂创客圈
 */

package com.crazymakercircle.bean;

import lombok.Data;

import javax.persistence.*;

//如果实体类名字与数据库不一致又不使用注解会报错
//注解声明数据库某表明
@Data
@Table(name = "SystemConfig")
public class SystemConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 注解声明该表的字段名
    // 如果实体类变量与数据库列名不一致又不使用注解会报错
    @Column(name = "type")
    private String type;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

}