package com.example.moneycounter4.utils.JSonEvalUtils;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class JSonEval {
    private static JSonEval jsonEval;
    private Map<String, String> map;
    public String splitString="#";

    private JSonEval() {

    }

    public static JSonEval getInstance() {
        if (jsonEval == null) {
            synchronized (JSonEval.class) {
                if (jsonEval == null) {
                    jsonEval = new JSonEval();
                }
            }
        }
        return jsonEval;
    }

    public String toJson(Object object) throws IllegalAccessException{
        Class tClass=object.getClass();
        if(tClass.getName().equals("java.lang.String")){
            return (String)object;
        }
        if(tClass.getName().equals("java.lang.Integer")||tClass.getName().equals("int")){
            return String.valueOf(object);
        }
        if (tClass.getName().equals("java.lang.Float")||tClass.getName().equals("float")) {
            return String.valueOf(object);
        }
        if (tClass.getName().equals("java.lang.Double")||tClass.getName().equals("double")) {
            return String.valueOf(object);
        }
        if (tClass.getName().equals("java.lang.Boolean")||tClass.getName().equals("boolean")) {
            return String.valueOf(object);
        }
        if (tClass.getName().equals("java.lang.Short")||tClass.getName().equals("short")) {
            return String.valueOf(object);
        }
        if (tClass.getName().equals("java.lang.Long")||tClass.getName().equals("long")) {
            return String.valueOf(object);
        }
        if (tClass.getName().equals("java.lang.Character")||tClass.getName().equals("char")) {
            return String.valueOf(object);
        }
        //如果是数组
        if(tClass.getName().charAt(0)=='['){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            for(int i=0;i<Array.getLength(object)-1;i++){
                stringBuffer.append("\"").append(Array.get(object, i)).append("\",");
            }
            stringBuffer.append("\"").append(Array.get(object, Array.getLength(object)-1)).append("\"").append("]");
            return stringBuffer.toString();
        }
        //如果是对象
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("{");
        for(int i=0;i<fields.length;i++){
            fields[i].setAccessible(true);
            if(fields[i].getType().getName()=="java.lang.String"){
                stringBuffer.append("\"").append(fields[i].getName()).append("\":\"").append(fields[i].get(object).toString()).append("\",");
            }else {
                stringBuffer.append("\"").append(fields[i].getName()).append("\":").append(fields[i].get(object).toString()).append(",");
            }

        }
        stringBuffer.deleteCharAt(stringBuffer.length()-1);
        stringBuffer.append("}");
        return stringBuffer.toString();

    }


    public <T> T fromJson(String s, Class<T> tClass) throws JsonObjectParseException, JsonArrayParseException, JsonFormatException {
        if (tClass.getName().equals("java.lang.String")) {
            return tClass.cast(s);
        }
        if (tClass.getName().equals("java.lang.Integer")||tClass.getName().equals("int")) {
            return ((Class<T>) Integer.class).cast(Integer.valueOf(s));
        }
        if (tClass.getName().equals("java.lang.Float")||tClass.getName().equals("float")) {
            return ((Class<T>) Float.class).cast(Float.valueOf(s));
        }
        if (tClass.getName().equals("java.lang.Double")||tClass.getName().equals("double")) {
            return ((Class<T>) Double.class).cast(Double.valueOf(s));
        }
        if (tClass.getName().equals("java.lang.Boolean")||tClass.getName().equals("boolean")) {
            return ((Class<T>) Boolean.class).cast(Boolean.valueOf(s));
        }
        if (tClass.getName().equals("java.lang.Short")||tClass.getName().equals("short")) {
            return ((Class<T>) Short.class).cast(new Short(s));
        }
        if (tClass.getName().equals("java.lang.Long")||tClass.getName().equals("long")) {
            return ((Class<T>) Long.class).cast(Long.valueOf(s));
        }
        if (tClass.getName().equals("java.lang.Character")||tClass.getName().equals("char")) {
            return ((Class<T>) Character.class).cast(s.charAt(0));
        }
        //以上没执行说明不是基本类型和包装类型
        //如果是数组（只支持一维数组）
        if(tClass.getName().charAt(0)=='['){
            JSonArray jSonArray=new JSonArray(s);
            try{
                T instance=(T)Array.newInstance(tClass.getComponentType(),jSonArray.size());
                for (int i=0;i<jSonArray.size();i++){
                    Array.set(instance,i,JSonEval.getInstance().fromJson(jSonArray.get(i),tClass.getComponentType()));
                }
                return instance;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }



        //既不是基本类型数组也不是包装类型就是对象(有可能是ArrayList)
        //arrayList:java.util.ArrayList

        if(tClass.getName().equals("java.util.ArrayList")){
            return null;
        }


        //object
        JSonObject jsonObject = new JSonObject(s);
        try {
            T instance = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if(jsonObject.getString(fields[i].getName())!=null){
                    fields[i].set(instance, fromJson(jsonObject.getString(fields[i].getName()), fields[i].getType()));
                }else {
                    Log.e("JSonEval","Can't find the field in the jsonObject");
                }
            }
            return tClass.cast(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
