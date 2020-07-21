package com.example.moneycounter4.bean;

import java.util.ArrayList;

public class CounterData {
    ArrayList<DataItem> list = new ArrayList<>();
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

    public ArrayList<DataItem> getList() {
        return list;
    }

    public void setList(ArrayList<DataItem> mList) {
        this.list = mList;
    }
}
