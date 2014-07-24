package com.an.lfs.vo;

import com.an.lfs.LfsUtil;

public class ClaimRate {
    private int id;
    private String comp;

    private float win;
    private float draw;
    private float lose;

    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    //
    private RateResult rateResult;
    private RateResult endRateResult;

    public boolean isValidEndRate() {
        return LfsUtil.isValidRate(winEnd, drawEnd, loseEnd);
    }

    public ClaimRate() {
    }

    public ClaimRate(int id, String comp) {
        this.id = id;
        this.comp = comp;
    }

    public void addRate(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.rateResult = LfsUtil.getRateResult(win, draw, lose);
    }

    public void addEndRate(float winEnd, float drawEnd, float loseEnd) {
        this.winEnd = winEnd;
        this.drawEnd = drawEnd;
        this.loseEnd = loseEnd;
        this.endRateResult = LfsUtil.getRateResult(winEnd, drawEnd, loseEnd);
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

    public RateResult getRateResult() {
        return rateResult;
    }

    public void setRateResult(RateResult rateResult) {
        this.rateResult = rateResult;
    }

    public RateResult getEndRateResult() {
        return endRateResult;
    }

    public void setEndRateResult(RateResult endRateResult) {
        this.endRateResult = endRateResult;
    }
}
