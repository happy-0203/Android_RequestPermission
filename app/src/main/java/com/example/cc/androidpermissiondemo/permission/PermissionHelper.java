package com.example.cc.androidpermissiondemo.permission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by cc on 2017/12/7.
 */

public class PermissionHelper {

    //传什么参数,以什么方式传参数
    //1 object Fragment or Activity 2.请求码 3. 请求的权限 String[]

    private Object mObject;
    private int mRequestCode;
    private String[] mPermissions;

    public PermissionHelper(Object object) {
        mObject = object;
    }

    //申请权限
    public static void requestPermission(Activity activity, int requestCode, String[] permissions) {

        PermissionHelper.with(activity).requestCode(requestCode).requestPermissions(permissions).request();
    }


    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }


    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }


    /**
     * 添加请求码
     *
     * @param requestCode
     * @return
     */
    public PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    /**
     * 添加请求权限的数组
     *
     * @param permissions
     * @return
     */
    public PermissionHelper requestPermissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }


    /**
     * 判断权限和请求权限
     */
    public void request() {
        if (!PermissionUtils.isOverMarshmallow()) {

            //如果是6.0以下就直接执行方法,通过反射执行方法 执行Activity里面的方法
            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);

            return;
        }

        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, mPermissions);
        if (deniedPermissions.size() == 0) {
            //权限授予过,执行方法
            PermissionUtils.executeSuccessMethod(mObject, mRequestCode);
        } else {
            //去申请权限
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),
                    deniedPermissions.toArray(new String[deniedPermissions.size()]),
                    mRequestCode);
        }

    }


    /**
     * 处理申请权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    public static void requestPermissionsResult(Object object, int requestCode,
                                                String[] permissions, int[] grantResults) {
        //再次获取没有获取没有授予的权限
        List<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object, permissions);

        if (deniedPermissions.size() == 0) {
            //权限用户都授予了,执行成功的方法
            PermissionUtils.executeSuccessMethod(object, requestCode);
        } else {
            //申请的权限中有用户不同意的
            PermissionUtils.executeFailMethod(object, requestCode);
        }
    }
}
