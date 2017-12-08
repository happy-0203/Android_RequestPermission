package com.example.cc.androidpermissiondemo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.cc.androidpermissiondemo.permission.PermissionFail;
import com.example.cc.androidpermissiondemo.permission.PermissionHelper;
import com.example.cc.androidpermissiondemo.permission.PermissionSucceed;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callPhone(View view) {

//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) ==
//                PackageManager.PERMISSION_GRANTED
//                ) {
//            //有权限,拨打电弧
//            call();
//
//
//        } else {
//            //申请权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
//        }


        PermissionHelper.with(this)
                .requestPermissions(new String[]{Manifest.permission.CALL_PHONE})
                .requestCode(REQUEST_PERMISSION)
                .request();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION) {
//            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                call();
//            } else {
//                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
//            }
//        }

        PermissionHelper.requestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSucceed(requestCode = REQUEST_PERMISSION)
    private void call() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:18679511272"));
        startActivity(intent);
    }

    @PermissionFail(requestCode = REQUEST_PERMISSION)
    private void callPhoneFail() {
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }
}
