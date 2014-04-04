package models.account;

import com.avaje.ebean.annotation.EnumValue;

/**
 * Created by Zheng Xuqiang on 2014/3/30 0030.
 * 用户性别枚举类
 */
public enum Gender {
    @EnumValue("male")
    MALE,
    @EnumValue("female")
    FEMALE,
    @EnumValue("other")
    OTHER
}
