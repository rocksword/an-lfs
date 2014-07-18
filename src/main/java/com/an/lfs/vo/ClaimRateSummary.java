package com.an.lfs.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClaimRateSummary {
    private static final Log logger = LogFactory.getLog(ClaimRateSummary.class);
    private String key;
    private List<ClaimRate> rates = new ArrayList<>();
    private float winAvg;
    private float drawAvg;
    private float loseAvg;
    private float winPerAvg;
    private float drawPerAvg;
    private float losePerAvg;
    private float retAvg;
    private float winKAvg;
    private float drawKAvg;
    private float loseKAvg;

    public void addClaimRate(ClaimRate rate) {
        rates.add(rate);
    }

    public void addEndValues(float winEnd, float drawEnd, float loseEnd, float winPerEnd, float drawPerEnd,
            float losePerEnd, float retEnd, float winKEnd, float drawKEnd, float loseKEnd) {
        ClaimRate last = rates.get(rates.size() - 1);
        last.setWinEnd(winEnd);
        last.setDrawEnd(drawEnd);
        last.setLoseEnd(loseEnd);
        last.setWinPerEnd(winPerEnd);
        last.setDrawPerEnd(drawPerEnd);
        last.setLosePerEnd(losePerEnd);
        last.setRetEnd(retEnd);
        last.setWinKEnd(winKEnd);
        last.setDrawKEnd(drawKEnd);
        last.setLoseKEnd(loseKEnd);
    }

    public void addAvgValues(float winAvg, float drawAvg, float loseAvg, float winPerAvg, float drawPerAvg,
            float losePerAvg, float retAvg, float winKAvg, float drawKAvg, float loseKAvg) {
        this.winAvg = winAvg;
        this.drawAvg = drawAvg;
        this.loseAvg = loseAvg;
        this.winPerAvg = winPerAvg;
        this.drawPerAvg = drawPerAvg;
        this.losePerAvg = losePerAvg;
        this.winKAvg = winKAvg;
        this.drawKAvg = drawKAvg;
        this.loseKAvg = loseKAvg;
    }

    public void logClaimRates() {
        for (ClaimRate rate : rates) {
            logger.info(rate);
        }
    }

    @Override
    public String toString() {
        return "ClaimRateSummary [" + (key != null ? "key=" + key + ", " : "") + "winAvg=" + winAvg + ", drawAvg="
                + drawAvg + ", loseAvg=" + loseAvg + ", winPerAvg=" + winPerAvg + ", drawPerAvg=" + drawPerAvg
                + ", losePerAvg=" + losePerAvg + ", retAvg=" + retAvg + ", winKAvg=" + winKAvg + ", drawKAvg="
                + drawKAvg + ", loseKAvg=" + loseKAvg + "]";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<ClaimRate> getRates() {
        return rates;
    }

    public void setRates(List<ClaimRate> rates) {
        this.rates = rates;
    }

    public float getWinAvg() {
        return winAvg;
    }

    public void setWinAvg(float winAvg) {
        this.winAvg = winAvg;
    }

    public float getDrawAvg() {
        return drawAvg;
    }

    public void setDrawAvg(float drawAvg) {
        this.drawAvg = drawAvg;
    }

    public float getLoseAvg() {
        return loseAvg;
    }

    public void setLoseAvg(float loseAvg) {
        this.loseAvg = loseAvg;
    }

    public float getWinPerAvg() {
        return winPerAvg;
    }

    public void setWinPerAvg(float winPerAvg) {
        this.winPerAvg = winPerAvg;
    }

    public float getDrawPerAvg() {
        return drawPerAvg;
    }

    public void setDrawPerAvg(float drawPerAvg) {
        this.drawPerAvg = drawPerAvg;
    }

    public float getLosePerAvg() {
        return losePerAvg;
    }

    public void setLosePerAvg(float losePerAvg) {
        this.losePerAvg = losePerAvg;
    }

    public float getRetAvg() {
        return retAvg;
    }

    public void setRetAvg(float retAvg) {
        this.retAvg = retAvg;
    }

    public float getWinKAvg() {
        return winKAvg;
    }

    public void setWinKAvg(float winKAvg) {
        this.winKAvg = winKAvg;
    }

    public float getDrawKAvg() {
        return drawKAvg;
    }

    public void setDrawKAvg(float drawKAvg) {
        this.drawKAvg = drawKAvg;
    }

    public float getLoseKAvg() {
        return loseKAvg;
    }

    public void setLoseKAvg(float loseKAvg) {
        this.loseKAvg = loseKAvg;
    }
}
