package com.an.lfs.vo;

public class BoardSummary implements Comparable<BoardSummary> {
    private String league;
    private int year;
    private float winPer;
    private float drawPer;
    private float losePer;

    public BoardSummary() {
    }

    @Override
    public int compareTo(BoardSummary o) {
        int ret = this.league.compareTo(o.getLeague());
        if (ret == 0) {
            return this.year - o.getYear();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "BoardSummary [league=" + league + ", year=" + year + ", winPer=" + winPer + ", drawPer=" + drawPer
                + ", losePer=" + losePer + "]";
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getWinPer() {
        return winPer;
    }

    public void setWinPer(float winPer) {
        this.winPer = winPer;
    }

    public float getDrawPer() {
        return drawPer;
    }

    public void setDrawPer(float drawPer) {
        this.drawPer = drawPer;
    }

    public float getLosePer() {
        return losePer;
    }

    public void setLosePer(float losePer) {
        this.losePer = losePer;
    }
}
