package com.an.lfs;

import java.util.Arrays;

public class MatchItem {
    private int id;
    private int index;
    private String year; // yyyy
    private String time; // MM-dd hh:mm
    private String host;
    private String guest;
    private String score;
    private float[] even;

    public String getKey() {
        String result = String.format("%s_%02d_%s_%s", year, index, host, guest);
        return result;
    }

    @Override
    public String toString() {
        return "MatchItem [id=" + id + ", index=" + index + ", year=" + year + ", time=" + time + ", host=" + host
                + ", guest=" + guest + ", score=" + score + ", even=" + Arrays.toString(even) + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public float[] getEven() {
        return even;
    }

    public void setEven(float[] even) {
        this.even = even;
    }
}
