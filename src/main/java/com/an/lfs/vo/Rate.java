package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.CmpType;

public class Rate {
    private int id;
    private String com;
    private float win;
    private float draw;
    private float lose;
    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    private float winRatio;
    private float drawRatio;
    private float loseRatio;

    public boolean isValidEndRate() {
        return LfsUtil.isValidRate(winEnd, drawEnd, loseEnd);
    }

    public CmpType getTrend() {
        return LfsUtil.getTrend(win, draw, lose, winEnd, drawEnd, loseEnd);
    }

    public void addRate(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }

    public void addEndRate(float winEnd, float drawEnd, float loseEnd) {
        this.winEnd = winEnd;
        this.drawEnd = drawEnd;
        this.loseEnd = loseEnd;
    }

    public Rate() {
    }

    public Rate(int id, String com) {
        this.id = id;
        this.com = com;
    }

    @Override
    public String toString() {
        return "Rate [id=" + id + ", " + (com != null ? "com=" + com + ", " : "") + "win=" + win + ", draw=" + draw
                + ", lose=" + lose + ", winEnd=" + winEnd + ", drawEnd=" + drawEnd + ", loseEnd=" + loseEnd
                + ", winRatio=" + winRatio + ", drawRatio=" + drawRatio + ", loseRatio=" + loseRatio + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
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

    public float getWinRatio() {
        return winRatio;
    }

    public void setWinRatio(float winRatio) {
        this.winRatio = winRatio;
    }

    public float getDrawRatio() {
        return drawRatio;
    }

    public void setDrawRatio(float drawRatio) {
        this.drawRatio = drawRatio;
    }

    public float getLoseRatio() {
        return loseRatio;
    }

    public void setLoseRatio(float loseRatio) {
        this.loseRatio = loseRatio;
    }
}
