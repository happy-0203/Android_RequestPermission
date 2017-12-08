package com.example.cc.androidpermissiondemo.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 2017/12/8.
 * <p>
 * 处理权限请求的工具类
 */

public class PermissionUtils {

    private PermissionUtils() throws InstantiationException {
        throw new InstantiationException("cannot be Instantiation");
    }


    /**
     * 判断是否大于6.0
     *
     * @return
     */
    public static boolean isOverMarshmallow() {

        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * 执行成功的方法
     *
     * @param reflectObject
     * @param requestCode
     */
    public static void executeSuccessMethod(Object reflectObject, int requestCode) {
        //获取class所有的方法去遍历找到我们打了标记的方法@PermissionSucceed(requestCode = REQUEST_PERMISSION)
        // ,并且我们的请求码必须requestCode一样,反射执行该方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        for (Method method : methods) {
            Log.d("123", "executeSuccessMethod: " + method);
            PermissionSucceed permissionSucceed = method.getAnnotation(PermissionSucceed.class);

            if (permissionSucceed != null) {
                //该方法打了标记
                int methodCode = permissionSucceed.requestCode();

                if (methodCode == requestCode) {

                    Log.d("123", "executeSuccessMethod: " + "找到了这个方法");
                    //这个就是我们要找的方法,反射执行
                    executeMethod(reflectObject, method);

                }
            }
        }

    }

    private static void executeMethod(Object reflectObject, Method method) {
        //反射执行该方法,第一个参数传该方法属于哪一个类,第二个参数传该方法中的参数
        try {
            method.setAccessible(true);
            method.invoke(reflectObject, new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取没有授予的权限
     *
     * @param object
     * @param permissions
     * @return 没有授予的权限
     */
    public static List<String> getDeniedPermissions(Object object, String[] permissions) {

        List<String> deniedPermissions = new ArrayList<>();

        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(getActivity(object), permission) ==
                    PackageManager.PERMISSION_DENIED) {

                deniedPermissions.add(permission);
            }
        }

        return deniedPermissions;


    }

    public static Activity getActivity(Object object) {


        if (object instanceof Activity) {
            return (Activity) object;
        }
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        }

        return null;
    }


    /**
     * 执行失败的方法
     *
     * @param object
     * @param requestCode
     */
    public static void executeFailMethod(Object object, int requestCode) {



        //获取class所有的方法去遍历找到我们打了标记的方法@permissionFail(requestCode = REQUEST_PERMISSION)
        // ,并且我们的请求码必须requestCode一样,反射执行该方法
        Method[] methods = object.getClass().getDeclaredMethods();

        for (Method method : methods) {
            Log.d("123", "executeFailMethod: " + method);
            PermissionFail permissionFail = method.getAnnotation(PermissionFail.class);

            if (permissionFail != null) {
                //该方法打了标记
                int methodCode = permissionFail.requestCode();

                if (methodCode == requestCode) {

                    Log.d("123", "executeFailMethod: " + "找到了失败的方法");
                    //这个就是我们要找的方法,反射执行
                    executeMethod(object, method);

                }
            }
        }

    }
}
