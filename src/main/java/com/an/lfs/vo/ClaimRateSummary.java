package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsConfMgr;
import com.an.lfs.LfsConst;

public class ClaimRateSummary {
    private static final Log logger = LogFactory.getLog(ClaimRateSummary.class);
    private String key;
    // Company -> ClaimRate
    private Map<String, ClaimRate> rates = new HashMap<>();
    private float win;
    private float draw;
    private float lose;
    //
    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    //
    private boolean passStart = false;
    private boolean passStartOdds = false;
    private boolean passEnd = false;
    //
    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String hostCat = null;
    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String guestCat = null;
    //
    private String middleCat = null;

    public String getPassResult() {
        StringBuilder result = new StringBuilder();
        result.append(passStart ? LfsConst.PASS : LfsConst.FAIL);
        result.append(LfsConst.SEPARATOR);
        result.append(passEnd ? LfsConst.PASS : LfsConst.FAIL);
        return result.toString();
    }

    public String getPassResultOdds() {
        StringBuilder result = new StringBuilder();
        result.append(passStartOdds ? LfsConst.PASS : LfsConst.FAIL);
        return result.toString();
    }

    public String getRateAvg() {
        return win + " " + draw + " " + lose + LfsConst.SEPARATOR + winEnd + " " + drawEnd + " " + loseEnd;
    }

    public void initPass(ScoreResult scoreResult) {
        float min = win;
        RateResult rateResult = RateResult.WIN;
        if (Float.compare(draw, min) < 0) {
            min = draw;
            rateResult = RateResult.DRAW;
        }
        if (Float.compare(lose, min) < 0) {
            min = lose;
            rateResult = RateResult.LOSE;
        }

        if (rateResult.isWin() && scoreResult.isWin()) {
            passStart = true;
        } else if (rateResult.isDraw() && scoreResult.isDraw()) {
            passStart = true;
        } else if (rateResult.isLose() && scoreResult.isLose()) {
            passStart = true;
        }

        min = winEnd;
        rateResult = RateResult.WIN;
        if (Float.compare(drawEnd, min) < 0) {
            min = drawEnd;
            rateResult = RateResult.DRAW;
        }
        if (Float.compare(loseEnd, min) < 0) {
            min = loseEnd;
            rateResult = RateResult.LOSE;
        }
        if (rateResult.isWin() && scoreResult.isWin()) {
            passEnd = true;
        } else if (rateResult.isDraw() && scoreResult.isDraw()) {
            passEnd = true;
        } else if (rateResult.isLose() && scoreResult.isLose()) {
            passEnd = true;
        }
    }

    public void initPassOdds(ScoreResult score) {
        initPassStartOdds(score);
    }

    private void initPassStartOdds(ScoreResult score) {
        float winOdds = 0;
        float drawOdds = 0;
        float loseOdds = 0;
        ClaimRate rate = rates.get(LfsConst.ODDSET);
        if (rate == null) {
            logger.warn("Oddset rate is null. " + this.toString());
            return;
        }
        winOdds = rate.getWin();
        drawOdds = rate.getDraw();
        loseOdds = rate.getLose();
        float min = winOdds;
        RateResult rateResult = RateResult.WIN;
        if (Float.compare(drawOdds, min) < 0) {
            min = drawOdds;
            rateResult = RateResult.DRAW;
        }
        if (Float.compare(loseOdds, min) < 0) {
            min = loseOdds;
            rateResult = RateResult.LOSE;
        }

        if (rateResult.isWin() && score.isWin()) {
            passStartOdds = true;
        } else if (rateResult.isDraw() && score.isDraw()) {
            passStartOdds = true;
        } else if (rateResult.isLose() && score.isLose()) {
            passStartOdds = true;
        }
    }

    public String getRateString(String company) {
        ClaimRate rate = rates.get(company);
        if (rate == null) {
            return "NULL";
        }
        if (company.equals(rate.getComp())) {
            return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose() + " | " + rate.getWinEnd() + " "
                    + rate.getDrawEnd() + " " + rate.getLoseEnd();
        }
        return null;
    }

    private String latestComp = null;

    public void addClaimRate(String country, ClaimRate rate) {
        if (!LfsConfMgr.contains(country, rate.getComp())) {
            latestComp = null;
            return;
        }

        latestComp = rate.getComp();
        logger.debug("addClaimRate latestComp " + latestComp);
        rates.put(latestComp, rate);
    }

    public void addRates(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }

    public void addEndValues(float winEnd, float drawEnd, float loseEnd) {
        logger.debug("addEndValues latestComp " + latestComp);
        if (latestComp != null) {
            ClaimRate rate = rates.get(latestComp);
            rate.setWinEnd(winEnd);
            rate.setDrawEnd(drawEnd);
            rate.setLoseEnd(loseEnd);
        }
    }

    public void addRateEnd(float winEnd, float drawEnd, float loseEnd) {
        this.winEnd = winEnd;
        this.drawEnd = drawEnd;
        this.loseEnd = loseEnd;
    }

    public ClaimRateSummary() {
    }

    @Override
    public String toString() {
        return "ClaimRateSummary [" + (key != null ? "key=" + key + ", " : "")
                + (rates != null ? "rates=" + rates + ", " : "") + "win=" + win + ", draw=" + draw + ", lose=" + lose
                + ", winEnd=" + winEnd + ", drawEnd=" + drawEnd + ", loseEnd=" + loseEnd + ", passStart=" + passStart
                + ", passStartOdds=" + passStartOdds + ", passEnd=" + passEnd + ", "
                + (hostCat != null ? "hostCat=" + hostCat + ", " : "")
                + (guestCat != null ? "guestCat=" + guestCat + ", " : "")
                + (middleCat != null ? "middleCat=" + middleCat + ", " : "")
                + (latestComp != null ? "latestComp=" + latestComp : "") + "]";
    }

    public boolean isPassStart() {
        return passStart;
    }

    public void setPassStart(boolean passStart) {
        this.passStart = passStart;
    }

    public boolean isPassEnd() {
        return passEnd;
    }

    public void setPassEnd(boolean passEnd) {
        this.passEnd = passEnd;
    }

    public boolean isPassStartOdds() {
        return passStartOdds;
    }

    public void setPassStartOdds(boolean passStartOdds) {
        this.passStartOdds = passStartOdds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, ClaimRate> getRates() {
        return rates;
    }

    public void setRates(Map<String, ClaimRate> rates) {
        this.rates = rates;
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

    public String getHostCat() {
        return hostCat;
    }

    public void setHostCat(String hostCat) {
        this.hostCat = hostCat;
    }

    public String getGuestCat() {
        return guestCat;
    }

    public void setGuestCat(String guestCat) {
        this.guestCat = guestCat;
    }

    public String getMiddleCat() {
        return middleCat;
    }

    public void setMiddleCat(String middleCat) {
        this.middleCat = middleCat;
    }
}