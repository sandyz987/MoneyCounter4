package com.example.moneycounter4;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.moneycounter4.view.costom.ImageViewSelectZ;
import com.makeramen.roundedimageview.RoundedImageView;

public class BindingAdapter {

    //给home页面的页面选择按钮（自定义ImageViewSelectZ）设置文字月份
    @androidx.databinding.BindingAdapter("hint_a")
    public static void setMonth(ImageViewSelectZ view, int value) {
        view.setHint(value + "月");
    }

    @androidx.databinding.BindingAdapter("riv_border_width")
    public static void setWidth(RoundedImageView view, int value) {
        view.setBorderWidth((float)value);
    }

    @androidx.databinding.BindingAdapter("src")
    public static void setRes(RoundedImageView view, int resId) {
        view.setImageResource(resId);
    }

}