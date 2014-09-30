package com.an.lfs.vo;

public class TeamSummary {
    private String league;
    private String team;
    private int year;
    private float winPer;
    private float drawPer;
    private float losePer;

    public TeamSummary() {
    }

    @Override
    public String toString() {
        return "TeamSummary [league=" + league + ", team=" + team + ", year=" + year + ", winPer=" + winPer
                + ", drawPer=" + drawPer + ", losePer=" + losePer + "]";
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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
