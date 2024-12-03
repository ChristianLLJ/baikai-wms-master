package com.bupt.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateEnum {
    AUDITED(2,"已审核"),
    CREATING(0,"创建中"),
    CREATED(1,"已创建"),
    CHECKING(3,"质检中"),
    CHECKED(4,"已质检"),
    RECEIVING(5,"收货中"),
    RECEIVED(6,"已收货"),
    RECEIVE_CANCEL(7,"收货取消"),
    PACKAGING(8,"装箱中"),
    PACKAGED(9,"已装箱"),
    STACKING(10,"码盘中"),
    STACKED(11,"已码盘"),
    ONSHELFING(12,"上架中"),
    ONSHELFED(13,"已上架"),
    FINISHED(14,"已完成")
    ;
    private int code;
    private String name;
}
