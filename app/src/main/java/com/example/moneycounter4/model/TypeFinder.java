package com.example.moneycounter4.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.moneycounter4.bean.TypeItem;
import com.example.moneycounter4.viewmodel.MainViewModel;

public class TypeFinder {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int findTypePicIdByName(MainViewModel vm, String s){
        for(TypeItem e : vm.getTypeListIn()){
            if(s.equals(e.getName())){
                return e.getResId();
            }
        }
        for(TypeItem e : vm.getTypeListOut()){
            if(s.equals(e.getName())){
                return e.getResId();
            }
        }
        return 0;
    }
}
