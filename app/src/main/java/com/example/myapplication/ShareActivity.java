package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {
    ImageView iv_aftercut;
    TextView tv_result;

    Button btn_share;
    double[] degree={254.97 ,222.21, 174.19, 127.59 ,89.96 ,1};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        tv_result = findViewById(R.id.tv_result);
        iv_aftercut = findViewById(R.id.iv_aftercut);
        btn_share = findViewById(R.id.btn_share);
        Uri image_uri=getIntent().getData();
        iv_aftercut.setImageURI(image_uri);
        Bitmap bitmap = ((BitmapDrawable) iv_aftercut.getDrawable()).getBitmap();
        double res=GrayScaleConverter.getLightness(bitmap);
        int i;
        int level;
        for(i=0;i<6;i++){
            if(res>degree[i]){
                break;
            }
        }
        double mid=(degree[i]+degree[i-1])/2;
        if(res>mid){
            level=i-1;
        }else {
            level=i;
        }
        res=(254.97-res)/253.87;
        res=res*100;
        String tmp = String.format("黑度值%.2f%%",res);

        tv_result.setText(tmp);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用截屏
                //shareImage(image_uri);
                shareScreenshot();
            }
        });
    }
    public void shareImage(Uri imageUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        //Uri uri = Uri.parse(imageUri);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
    }

    private void shareScreenshot() {// 获取当前界面的截图

        View rootView = getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap screenshot = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);


        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");


        String imagePath = MediaStore.Images.Media.insertImage(getContentResolver(), screenshot, System.currentTimeMillis()+"screenshot", "Screenshot");
        Uri imageUri = Uri.parse(imagePath);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);


        startActivity(Intent.createChooser(shareIntent, "分享到："));
    }
}