package com.example.moneycounter4.utils.JSonEvalUtils;


import java.util.ArrayList;

public class JSonOuterSplit {
    public static String deleteSpace(String s){
        ArrayList<Integer> arrayList=new ArrayList<>();
        boolean isInString=false;
        StringBuffer stringBuffer=new StringBuffer(s);
        for (int i=0;i<stringBuffer.length();i++){
            if(!isInString &&stringBuffer.charAt(i)==' '){
                arrayList.add(i);
                continue;
            }
            if (stringBuffer.charAt(i)=='"'){
                isInString=!isInString;
            }
        }
        for(int i=0;i<arrayList.size();i++){
            stringBuffer.replace(arrayList.get(i),arrayList.get(i)+1, JSonEval.getInstance().splitString);
        }

        return stringBuffer.toString().replaceAll(JSonEval.getInstance().splitString, "");
    }
    public static String[] splitDot(String s) throws JsonFormatException {
        s = s.replace("\\\"","“");
        ArrayList<Integer> arrayList=new ArrayList<>();
        int inner=0;
        boolean isInString=false;
        StringBuffer stringBuffer=new StringBuffer(s);
        for (int i=0;i<stringBuffer.length();i++){
            if(inner==0&& !isInString &&stringBuffer.charAt(i)==','){
                arrayList.add(i);
                continue;
            }
            if (stringBuffer.charAt(i)=='"'){
                isInString=!isInString;
                continue;
            }
            if (!isInString&&(stringBuffer.charAt(i)=='['||stringBuffer.charAt(i)=='{')) {
                inner++;
            }
            if (!isInString&&(stringBuffer.charAt(i)==']'||stringBuffer.charAt(i)=='}')) {
                inner--;
            }
        }
        if(inner!=0 || isInString){
            throw new JsonFormatException();
        }else {
            for(int i=0;i<arrayList.size();i++){
                stringBuffer.replace(arrayList.get(i),arrayList.get(i)+1,JSonEval.getInstance().splitString);
            }
            return stringBuffer.toString().split(JSonEval.getInstance().splitString);
        }
    }
    public static String[] splitColon(String s) throws JsonFormatException{
        s = s.replace("\\\"","“");
        ArrayList<Integer> arrayList=new ArrayList<>();
        int inner=0;
        boolean isInString=false;
        StringBuffer stringBuffer=new StringBuffer(s);
        for (int i=0;i<stringBuffer.length();i++){
            if(inner==0&& !isInString &&stringBuffer.charAt(i)==':'){
                arrayList.add(i);
                continue;
            }
            if (stringBuffer.charAt(i)=='"'){
                isInString=!isInString;
                continue;
            }
            if (!isInString&&(stringBuffer.charAt(i)=='['||stringBuffer.charAt(i)=='{')) {
                inner++;
            }
            if (!isInString&&(stringBuffer.charAt(i)==']'||stringBuffer.charAt(i)=='}')) {
                inner--;
            }
        }
        if(inner!=0 || isInString){
            throw new JsonFormatException();
        }else {
            for(int i=0;i<arrayList.size();i++){
                stringBuffer.replace(arrayList.get(i),arrayList.get(i)+1,JSonEval.getInstance().splitString);
            }
            return stringBuffer.toString().split(JSonEval.getInstance().splitString);
        }
    }
}
