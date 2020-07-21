package com.example.moneycounter4.utils;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculater {
    public static Map<String,Integer> priority=new HashMap();

    public static ArrayList<String> exchange(ArrayList<String> tmp1){
        Stack<String> stack=new Stack<>();
        ArrayList<String> tmp=new ArrayList<>();
        for(int i=0;i<tmp1.size();i++){
            String present =tmp1.get(i);
            if(!isSign(present)){
                tmp.add(present);
                continue;
            }
            if(present.equals("(")){
                stack.push(present);
                continue;
            }
            if(present.equals(")")){
                while (!stack.peek().equals("(")){
                    tmp.add(stack.pop());
                }
                stack.pop();
                continue;
            }
            if(stack.empty()){
                stack.push(present);
                continue;
            }
            if(stack.peek().equals("(")){
                stack.push(present);
                continue;
            }
            if(getPriority(present)>getPriority(stack.peek())){
                stack.push(present);
                continue;
            }else {
                while (!stack.empty()&&(!(stack.peek().equals("("))&&(getPriority(present)<=getPriority(stack.peek())))){
                    tmp.add(stack.pop());
                }
                stack.push(present);
                continue;
            }
        }
        while (!stack.empty()){
            tmp.add(stack.pop());
        }
        return tmp;
    }
    public static boolean isSign(String a){
        boolean flag=false;
        String[] list={"+","-","*","/","(",")"};
        for(String i : list){
            if(i.equals(a)){
                flag=true;
            }
        }
        return flag;
    }
    public static int getPriority(String a){
        return priority.get(a)!=null?priority.get(a):-1;
    }

    public static void setPriority(){

        priority.put("+",2);
        priority.put("-",2);
        priority.put("*",3);
        priority.put("/",3);
    }

    public static int getType(char ch){//0数字 1符号
        if((ch>=48 && ch<=57)||ch=='.'){
            return 0;
        }else return 1;
    }
    public static float eval(ArrayList<String> list){
        while (list.size()>1){
            for(int i=0;i<list.size();i++){
                String present =list.get(i);
                if(isSign(present)){
                    String num1str=list.get(i-2);
                    String num2str=list.get(i-1);
                    float num1=new Float(num1str);
                    float num2=new Float(num2str);
                    float ans=cal(num1,num2,present);
                    String ansStr=String.valueOf(ans);
                    list.remove(i);
                    list.remove(i-1);
                    list.remove(i-2);
                    list.add(i-2,ansStr);
                    break;
                }

            }
        }
        if(list.size()==1){
            String ansStr2=list.get(0);
            return new Float(ansStr2);
        }else {
            return 0.0f;
        }

    }
    public static float cal(float num1,float num2,String sign){
        switch (sign){
            case "+":return num1+num2;
            case "-":return num1-num2;
            case "*":return num1*num2;
            case "/":return num1/num2;
        }
        return 0;
    }
    @SuppressLint("DefaultLocale")
    public static float calculate(String s1){
        setPriority();
        StringBuffer s2=new StringBuffer();
        ArrayList<String> a=new ArrayList<>();

        try{
            for(int i=0;i<s1.length();i++){
                String presentChar;
                presentChar=s1.substring(i,i+1);
                if(getType(presentChar.charAt(0))==1){
                    if(s2.length()!=0){
                        String s3=s2.toString();
                        s2.delete(0,s2.length());
                        a.add(s3);
                    }
                    a.add(presentChar);
                }else{
                    s2.append(presentChar);
                }
            }
            if(s2.length()!=0){
                String s3=s2.toString();
                a.add(s3);
            }
            return Float.parseFloat(String.format("%.2f",eval(exchange(a))));
        }catch (Exception e){
            e.printStackTrace();
            return 0.0f;
        }


    }
}
