package com.example.moneycounter4.bean;

import java.util.ArrayList;

public class TalkItem {
    private String userId;
    private String picUrl;
    private String text;
    private Long time;
    private String usrName;
    private String usrPic;
    private String sex;

    private ArrayList<ItemAccount> likeAccounts;
    private ArrayList<TalkItem> replies;

    public ArrayList<ItemAccount> getLikeAccounts() {
        return likeAccounts;
    }

    public void setLikeAccounts(ArrayList<ItemAccount> likeAccounts) {
        this.likeAccounts = likeAccounts;
    }

    public ArrayList<TalkItem> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<TalkItem> replies) {
        this.replies = replies;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUsrPic() {
        return usrPic;
    }

    public void setUsrPic(String usrPic) {
        this.usrPic = usrPic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
