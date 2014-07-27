package com.an.lfs.vo;

//排名,球队,赛,胜,平,负,进球,失球,净胜,均得,均失,胜%,平%,负%,积分
public class BoardTeam {
    private int rank;
    private String team;
    private int total;
    private int win;
    private int draw;
    private int lose;
    private int goalFor;
    private int goalAgainst;
    private int goalNet;
    private float avgFor;
    private float avgAgainst;
    private int winPer;
    private int drawPer;
    private int losePer;
    private int score;

    public BoardTeam() {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getGoalFor() {
        return goalFor;
    }

    public void setGoalFor(int goalFor) {
        this.goalFor = goalFor;
    }

    public int getGoalAgainst() {
        return goalAgainst;
    }

    public void setGoalAgainst(int goalAgainst) {
        this.goalAgainst = goalAgainst;
    }

    public int getGoalNet() {
        return goalNet;
    }

    public void setGoalNet(int goalNet) {
        this.goalNet = goalNet;
    }

    public float getAvgFor() {
        return avgFor;
    }

    public void setAvgFor(float avgFor) {
        this.avgFor = avgFor;
    }

    public float getAvgAgainst() {
        return avgAgainst;
    }

    public void setAvgAgainst(float avgAgainst) {
        this.avgAgainst = avgAgainst;
    }

    public int getWinPer() {
        return winPer;
    }

    public void setWinPer(int winPer) {
        this.winPer = winPer;
    }

    public int getDrawPer() {
        return drawPer;
    }

    public void setDrawPer(int drawPer) {
        this.drawPer = drawPer;
    }

    public int getLosePer() {
        return losePer;
    }

    public void setLosePer(int losePer) {
        this.losePer = losePer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
