package com.example.moneycounter4.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.moneycounter4.bean.TypeItem;

import java.util.ArrayList;

public class TypeFinder {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int findTypePicIdByName(ArrayList<TypeItem> listIn, ArrayList<TypeItem> listOut, String s) {
        for (TypeItem e : listIn) {
            if (s.equals(e.getName())) {
                return e.getResId();
            }
        }
        for (TypeItem e : listOut) {
            if (s.equals(e.getName())) {
                return e.getResId();
            }
        }
        return 0;
    }
}
