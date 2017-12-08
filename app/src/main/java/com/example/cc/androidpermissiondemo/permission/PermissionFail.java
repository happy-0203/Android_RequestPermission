package com.example.cc.androidpermissiondemo.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cc on 2017/12/8.
 */


@Target(ElementType.METHOD)//放在什么位置METHOD放在方法上
@Retention(RetentionPolicy.RUNTIME) //是编译是检测还是运行时检测
public @interface PermissionFail {

    public int requestCode();
}
