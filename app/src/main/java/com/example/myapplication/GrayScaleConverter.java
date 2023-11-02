package com.example.myapplication;

import android.graphics.Bitmap;

import android.graphics.Color;





public class GrayScaleConverter {


    public static double getLightness(Bitmap bitmap) {
        int totalLightness = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);

                // 计算灰度值（简单平均灰度）
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                int brightness = (red + green + blue) / 3;
                totalLightness += brightness;
            }
        }

        return (double) totalLightness / (double) (width * height);
    }


}
