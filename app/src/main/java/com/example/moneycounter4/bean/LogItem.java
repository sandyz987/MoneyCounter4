package com.example.moneycounter4.bean;

public class LogItem extends DataItem {
    Long timeS = 0L;

    public Long getTimeS() {
        return timeS;
    }

    public void setTimeS(Long time) {
        this.timeS = time;
    }

    public LogItem(Long time) {
        this.timeS = time;
    }

}
