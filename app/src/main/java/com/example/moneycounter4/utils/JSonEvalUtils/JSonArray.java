package com.example.moneycounter4.utils.JSonEvalUtils;


import android.util.Log;

import java.util.ArrayList;

public class JSonArray {

    private static String format(String s){
        if(s.length() == 0){
            return "";
        }
        if (s.charAt(0)=='"'){
            return s.substring(1,s.length()-1);
        }else {
            return s;
        }
    }

    private ArrayList<String> strings;
    public JSonArray(String s)throws JsonArrayParseException, JsonFormatException {
        s = JSonOuterSplit.deleteSpace(s);
        strings = new ArrayList<>();
        if(s.equals("[]")){
            return;
        }
        if(s.charAt(0)=='['&&s.charAt(s.length()-1)==']'){
            s=s.substring(1,s.length()-1);
            String[] elements = JSonOuterSplit.splitDot(s);
            //strings.addAll(Arrays.asList(elements));
            for(int i=0;i<elements.length;i++){
                strings.add(format(elements[i]));
            }
        }else {
            throw new JsonArrayParseException();
        }

    }
    public void remove(int index){
        strings.remove(index);
    }
    public void set(int index,String s){
        strings.set(index, s);
    }
    public void add(String s){
        strings.add(s);
    }
    public void add(int index,String s){
        strings.add(index,s);
    }
    public String get(int index){
        return strings.get(index);
    }
    public int size(){
        return strings.size();
    }
}