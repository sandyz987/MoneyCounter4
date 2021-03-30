package com.example.moneycounter4.bean;

import com.example.moneycounter4.beannew.CounterDataItem;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class CounterData {
    @Expose
    ArrayList<CounterDataItem> list = new ArrayList<>();
    ArrayList<TypeItem> typeListIn = new ArrayList<>();
    ArrayList<TypeItem> typeListOut = new ArrayList<>();

    public ArrayList<TypeItem> getTypeListIn() {
        return typeListIn;
    }

    public void setTypeListIn(ArrayList<TypeItem> typeListIn) {
        this.typeListIn = typeListIn;
    }

    public ArrayList<TypeItem> getTypeListOut() {
        return typeListOut;
    }

    public void setTypeListOut(ArrayList<TypeItem> typeListOut) {
        this.typeListOut = typeListOut;
    }

    public ArrayList<CounterDataItem> getList() {
        return list;
    }

    public void setList(ArrayList<CounterDataItem> mList) {
        this.list = mList;
    }
}
