package com.example.myapplication;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_PICTURES;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS_STORAGE = new String[]{
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    private static final String[] PERMISSIONS_CAMERA = new String[]{
            Manifest.permission.CAMERA
    };

    private static final int REQUEST_CODE_STORAGE = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    Uri image_uri;
    Uri my_uri;
    ImageView iv_cal;
    TextView tv_cal;
    Button btn_select;
    Button btn_calgray;
    Button btn_camera;
    Button btn_allfile;

    Button btn_cut;
    Button btn_ucrop;

    RadioGroup rg_cut;

    int x=1;
    int y=1;
    ActivityResultLauncher<Intent> mResultLauncher;
    ActivityResultLauncher<Intent> LauncherCamera;

    ActivityResultLauncher<Intent> Ucrop;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            image_uri = UCrop.getOutput(data);
            iv_cal=findViewById(R.id.iv_cal);
            iv_cal.setImageURI(image_uri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    //    ActivityResultLauncher<Intent> LanucherAllfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_cal = findViewById(R.id.iv_cal);
        //tv_cal = findViewById(R.id.tv_cal);
        btn_select = findViewById(R.id.btn_select);
        btn_calgray = findViewById(R.id.btn_calgray);
        btn_camera = findViewById(R.id.btn_camera);
        btn_allfile = findViewById(R.id.btn_allfile);
        btn_ucrop=findViewById(R.id.btn_ucrop);
        rg_cut=findViewById(R.id.rg_cut);

        rg_cut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rb_11){
                    x=1;
                    y=1;
                }else if(i==R.id.rb_34){
                    x=3;
                    y=4;
                }else if(i==R.id.rb_43){
                    x=4;
                    y=3;
                }else if(i==R.id.rb_169){
                    x=16;
                    y=9;
                }else if(i==R.id.rb_916){
                    x=9;
                    y=16;
                }
            }
        });
        btn_ucrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_uri=my_uri;
                if(image_uri==null){
                    String txt = "请选择图片";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainActivity.this, txt, duration);
                    toast.show();
                    return;
                }
                String path=System.currentTimeMillis()+".jpg";
                File destinationFile = new File(getExternalCacheDir().getAbsolutePath() +"/", path);
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }
                try {
                    destinationFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri destinationUri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.myapplication.fileprovider_camera",
                        destinationFile);
                UCrop.of(image_uri, destinationUri)
                        .withAspectRatio(x, y)
                        .withMaxResultSize(500, 500)
                        .start(MainActivity.this);
//                image_uri=destinationUri;
//                iv_cal.setImageURI(image_uri);
            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToSettings(REQUEST_CODE_STORAGE);
            /*if(!PermissonUtil.checkPermission(this, PERMISSIONS_STORAGE, REQUEST_CODE_STORAGE)){
                return;
            }*/
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                mResultLauncher.launch(intent);
            }
        });

        btn_calgray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image_uri==null){
                    String txt = "请选择图片";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainActivity.this, txt, duration);
                    toast.show();
                    return;
                }
                Intent intent=new Intent(MainActivity.this, ShareActivity.class);
                //intent.setAction("android.intent.action.Share");
                intent.setData(image_uri);
                startActivity(intent);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PermissonUtil.checkPermission(MainActivity.this, PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA)) {
                    return;
                }
                String filename=System.currentTimeMillis()+".jpg";
                File image_file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), filename);
                Log.d("111", Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).getAbsolutePath().toString());
                if (image_file.exists()) {
                    image_file.delete();
                }
                try {
                    image_file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image_uri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.myapplication.fileprovider_camera",
                        image_file);
                my_uri=image_uri;
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                LauncherCamera.launch(intent);
            }
        });

        btn_allfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                //startActivity(intent);
                mResultLauncher.launch(intent);
            }
        });

        //PermissonUtil.checkPermission(this, PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA);
        mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    Uri picuri = intent.getData();
                    image_uri = picuri;
                    my_uri=image_uri;
                    if (picuri != null) {
                        iv_cal.setImageURI(picuri);
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(picuri));
//                            //String tmp = Double.toString(GrayScaleConverter.getLightness(bitmap));
//                            //tv_cal.setText(tmp);
//                        } catch (FileNotFoundException e) {
//                            throw new RuntimeException(e);
//                        }
                    }
                }
            }
        });
        LauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    //Uri picuri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                    Uri picuri = image_uri;
                    my_uri=image_uri;
                    if (picuri != null) {
                        iv_cal.setImageURI(picuri);
                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (PermissonUtil.checkGrant(grantResults)) {
                    Log.d("111", "读写权限获取成功");
                } else {
                    String txt = "读写权限失败";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainActivity.this, txt, duration);
                    //toast.show();
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    //if (!Environment.isExternalStorageManager()){
                    jumpToSettings(REQUEST_CODE_STORAGE);
                    //}
                    //}
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (PermissonUtil.checkGrant(grantResults)) {
                    Log.d("111", "相机权限获取成功");
                } else {
                    String txt = "相机权限失败";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainActivity.this, txt, duration);
                    //toast.show();
                    jumpToSettings(REQUEST_CODE_CAMERA);
                }
                break;
        }
    }

    public void jumpToSettings(int requestcode) {
        switch (requestcode) {
            case REQUEST_CODE_STORAGE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(intent);
                    }
                }
                break;
            case REQUEST_CODE_CAMERA:
                Intent intent = new Intent();
//          Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }


    }
}