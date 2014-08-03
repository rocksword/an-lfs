package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.CmpType;
import com.an.lfs.enu.ForecastRet;

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
    //
    private ForecastRet rateResult;
    private ForecastRet endRateResult;

    public boolean isValidEndRate() {
        return LfsUtil.isValidRate(winEnd, drawEnd, loseEnd);
    }

    public CmpType getTrend() {
        return LfsUtil.getTrend(win, draw, lose, winEnd, drawEnd, loseEnd);
    }

    public Rate() {
    }

    public Rate(int id, String com) {
        this.id = id;
        this.com = com;
    }

    public void addRate(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.rateResult = LfsUtil.getForecastRet(win, draw, lose);
    }

    public void addEndRate(float winEnd, float drawEnd, float loseEnd) {
        this.winEnd = winEnd;
        this.drawEnd = drawEnd;
        this.loseEnd = loseEnd;
        this.endRateResult = LfsUtil.getForecastRet(winEnd, drawEnd, loseEnd);
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

    public ForecastRet getRateResult() {
        return rateResult;
    }

    public void setRateResult(ForecastRet rateResult) {
        this.rateResult = rateResult;
    }

    public ForecastRet getEndRateResult() {
        return endRateResult;
    }

    public void setEndRateResult(ForecastRet endRateResult) {
        this.endRateResult = endRateResult;
    }
}
