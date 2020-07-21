package com.example.moneycounter4.utils.JSonEvalUtils;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class JSonObject {

    private static String format(String s){
        if (s.charAt(0)=='"'){
            return s.substring(1,s.length()-1);
        }else {
            return s;
        }
    }

    private Map<String,String> map;
    public JSonObject(String s)throws JsonObjectParseException, JsonFormatException {
        map=new HashMap<>();
        s= JSonOuterSplit.deleteSpace(s);


        if(s.charAt(0)=='{'&&s.charAt(s.length()-1)=='}'){
            s=s.substring(1,s.length()-1);
            String[] elements = JSonOuterSplit.splitDot(s);
            for(int i=0;i<elements.length;i++){
                String[] items= JSonOuterSplit.splitColon(elements[i]);
                if(items.length!=2||!(items[0].charAt(0)=='"'&&items[0].charAt(items[0].length()-1)=='"')){
                    throw new JsonObjectParseException();
                }else {
                    map.put(items[0].substring(1,items[0].length()-1),format(items[1]));
                }

            }
        }else {
            Log.e("Sandyzhang",s);
            throw new JsonObjectParseException();
        }
    }
    public String getString(String name){
        return map.get(name);
    }
    public boolean getBoolean(String name){
        return Boolean.parseBoolean(map.get(name));
    }
    public int getInt(String name){
        return Integer.parseInt(map.get(name));
    }
    public float getFloat(String name){
        return Float.parseFloat(map.get(name));
    }
    public double getDouble(String name){
        return Double.parseDouble(map.get(name));
    }
    public short getShort(String name){
        return Short.parseShort(map.get(name));
    }
    public long getLong(String name){
        return Long.parseLong(map.get(name));
    }
    public char getChar(String name){
        return map.get(name).charAt(0);
    }
}