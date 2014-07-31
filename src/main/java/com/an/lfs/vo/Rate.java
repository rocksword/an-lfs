package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.CmpType;
import com.an.lfs.enu.ForecastRet;

public class Rate {
    private int id;
    private String comp;

    private float win;
    private float draw;
    private float lose;

    private float winEnd;
    private float drawEnd;
    private float loseEnd;
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

    public Rate(int id, String comp) {
        this.id = id;
        this.comp = comp;
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
