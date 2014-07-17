package com.an.lfs;

public class ClaimRate {
    private int id;
    private String compName;
    private float win;
    private float draw;
    private float lose;
    private float winPer;
    private float drawPer;
    private float losePer;
    private float ret;
    private float winK;
    private float drawK;
    private float loseK;

    public ClaimRate() {
    }

    @Override
    public String toString() {
        return "ClaimRate [id=" + id + ", compName=" + compName + ", win=" + win + ", draw=" + draw + ", lose=" + lose
                + ", winPer=" + winPer + ", drawPer=" + drawPer + ", losePer=" + losePer + ", ret=" + ret + ", winK="
                + winK + ", drawK=" + drawK + ", loseK=" + loseK + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public float getWin() {
        return win;
    }

    public void setWin(float win) {
        this.win = win;
    }

    public float getDraw() {
        return draw;
    }

    public void setDraw(float draw) {
        this.draw = draw;
    }

    public float getLose() {
        return lose;
    }

    public void setLose(float lose) {
        this.lose = lose;
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

    public float getRet() {
        return ret;
    }

    public void setRet(float ret) {
        this.ret = ret;
    }

    public float getWinK() {
        return winK;
    }

    public void setWinK(float winK) {
        this.winK = winK;
    }

    public float getDrawK() {
        return drawK;
    }

    public void setDrawK(float drawK) {
        this.drawK = drawK;
    }

    public float getLoseK() {
        return loseK;
    }

    public void setLoseK(float loseK) {
        this.loseK = loseK;
    }
}
