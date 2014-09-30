package com.an.lfs.vo;

public class LeagueSummary implements Comparable<LeagueSummary> {
    private String league;
    private float winPer;
    private float drawPer;
    private float losePer;

    public LeagueSummary() {
    }

    @Override
    public int compareTo(LeagueSummary o) {
        if ((this.drawPer - o.getDrawPer()) > 0) {
            return -1;
        } else if ((this.drawPer - o.getDrawPer()) < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "LeagueSummary [league=" + league + ", winPer=" + winPer + ", drawPer=" + drawPer + ", losePer="
                + losePer + "]";
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
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
