package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.ScoreType;

public class Match {
    private String score;
    protected ScoreType scoreType;
    // Common
    private int id;
    private int index;
    private int year; // yyyy
    private String time; // MM-dd hh:mm
    private String host;
    private String guest;

    /**
     * @return year_index
     */
    public String getSimpleKey() {
        String result = String.format("%s_%02d", year, index);
        return result;
    }

    /**
     * @return year_index_host_guest
     */
    public String getKey() {
        String result = String.format("%s_%02d_%s_%s", year, index, host, guest);
        return result;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public void addScore(String score) {
        scoreType = LfsUtil.getScoreType(score);

        this.score = " " + score;
    }

    public String getScore() {
        return score;
    }

    public Match() {
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
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
}
