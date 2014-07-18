package com.an.lfs.vo;

public class ClaimRate {
    private int id;
    private String comp;
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

    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    private float winPerEnd;
    private float drawPerEnd;
    private float losePerEnd;
    private float retEnd;
    private float winKEnd;
    private float drawKEnd;
    private float loseKEnd;

    public ClaimRate() {
    }

    @Override
    public String toString() {
        return "ClaimRate [id=" + id + ", " + (comp != null ? "comp=" + comp + ", " : "") + "win=" + win + ", draw="
                + draw + ", lose=" + lose + ", winPer=" + winPer + ", drawPer=" + drawPer + ", losePer=" + losePer
                + ", ret=" + ret + ", winK=" + winK + ", drawK=" + drawK + ", loseK=" + loseK + ", winEnd=" + winEnd
                + ", drawEnd=" + drawEnd + ", loseEnd=" + loseEnd + ", winPerEnd=" + winPerEnd + ", drawPerEnd="
                + drawPerEnd + ", losePerEnd=" + losePerEnd + ", retEnd=" + retEnd + ", winKEnd=" + winKEnd
                + ", drawKEnd=" + drawKEnd + ", loseKEnd=" + loseKEnd + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
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

    public float getWinEnd() {
        return winEnd;
    }

    public void setWinEnd(float winEnd) {
        this.winEnd = winEnd;
    }

    public float getDrawEnd() {
        return drawEnd;
    }

    public void setDrawEnd(float drawEnd) {
        this.drawEnd = drawEnd;
    }

    public float getLoseEnd() {
        return loseEnd;
    }

    public void setLoseEnd(float loseEnd) {
        this.loseEnd = loseEnd;
    }

    public float getWinPerEnd() {
        return winPerEnd;
    }

    public void setWinPerEnd(float winPerEnd) {
        this.winPerEnd = winPerEnd;
    }

    public float getDrawPerEnd() {
        return drawPerEnd;
    }

    public void setDrawPerEnd(float drawPerEnd) {
        this.drawPerEnd = drawPerEnd;
    }

    public float getLosePerEnd() {
        return losePerEnd;
    }

    public void setLosePerEnd(float losePerEnd) {
        this.losePerEnd = losePerEnd;
    }

    public float getRetEnd() {
        return retEnd;
    }

    public void setRetEnd(float retEnd) {
        this.retEnd = retEnd;
    }

    public float getWinKEnd() {
        return winKEnd;
    }

    public void setWinKEnd(float winKEnd) {
        this.winKEnd = winKEnd;
    }

    public float getDrawKEnd() {
        return drawKEnd;
    }

    public void setDrawKEnd(float drawKEnd) {
        this.drawKEnd = drawKEnd;
    }

    public float getLoseKEnd() {
        return loseKEnd;
    }

    public void setLoseKEnd(float loseKEnd) {
        this.loseKEnd = loseKEnd;
    }
}
