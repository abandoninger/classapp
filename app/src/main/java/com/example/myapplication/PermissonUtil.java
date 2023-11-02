package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissonUtil {


    //检查多个权限。返回true表示已完全启用权限，返回false表示未完全启用权限
    public static boolean checkPermission(Activity act, String[] permissions, int requestCode) {
        //Android6.0之后开始采用动态权限管理
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//
//                startActivity(intent);
//            }
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = PackageManager.PERMISSION_GRANTED;
            for (String permission : permissions) {
                check = ContextCompat.checkSelfPermission(act, permission);
                if (check != PackageManager.PERMISSION_GRANTED) {
                    break;
                }
            }
            if (check != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act, permissions, requestCode);
                return false;
            }
        }
        return true;
    }

    public static boolean checkGrant(int[] grantResults) {
        if (grantResults != null) {//遍历权限结果数组中的每条选择结果
            for (int grant : grantResults) {//末获得授权
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


}
