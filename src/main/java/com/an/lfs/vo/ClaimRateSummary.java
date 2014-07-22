package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.CompanyMgr;
import com.an.lfs.LfsConfMgr;

public class ClaimRateSummary {
    private static final Log logger = LogFactory.getLog(ClaimRateSummary.class);
    private String key;
    // Company -> ClaimRate
    private Map<String, ClaimRate> rates = new HashMap<>();
    private float winAvg;
    private float drawAvg;
    private float loseAvg;
    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    private boolean passStart = false;
    private boolean passEnd = false;
    private boolean passStartOdds = false;
    private Map<String, RateResult> compRateResult = new HashMap<>();
    private String category = null;

    public String getPassResult() {
        StringBuilder result = new StringBuilder();
        result.append(passStart ? "T" : "F");
        result.append(" -> ");
        result.append(passEnd ? "T" : "F");
        return result.toString();
    }

    public String getPassResultOdds() {
        StringBuilder result = new StringBuilder();
        result.append(passStartOdds ? "T" : "F");
        return result.toString();
    }

    public String getRateAvg() {
        return winAvg + " " + drawAvg + " " + loseAvg + " | " + winEnd + " " + drawEnd + " " + loseEnd;
    }

    public void initRateResult(String company) {
        float win = 0;
        float draw = 0;
        float lose = 0;
        ClaimRate rate = rates.get(company);
        if (rate == null) {
            logger.warn(company + " rate is null, " + " " + this.toString());
            return;
        }
        win = rate.getWin();
        draw = rate.getDraw();
        lose = rate.getLose();

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
        compRateResult.put(company, rateResult);
    }

    public void initPass(ScoreResult score) {
        float min = winAvg;
        RateResult rateResult = RateResult.WIN;
        if (Float.compare(drawAvg, min) < 0) {
            min = drawAvg;
            rateResult = RateResult.DRAW;
        }
        if (Float.compare(loseAvg, min) < 0) {
            min = loseAvg;
            rateResult = RateResult.LOSE;
        }

        if ((rateResult.getVal() == RateResult.WIN.getVal()) && (score.getVal() == ScoreResult.WIN.getVal())) {
            passStart = true;
        } else if ((rateResult.getVal() == RateResult.DRAW.getVal()) && (score.getVal() == ScoreResult.DRAW.getVal())) {
            passStart = true;
        } else if ((rateResult.getVal() == RateResult.LOSE.getVal()) && (score.getVal() == ScoreResult.LOSE.getVal())) {
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

        if ((rateResult.getVal() == RateResult.WIN.getVal()) && (score.getVal() == ScoreResult.WIN.getVal())) {
            passEnd = true;
        } else if ((rateResult.getVal() == RateResult.DRAW.getVal()) && (score.getVal() == ScoreResult.DRAW.getVal())) {
            passEnd = true;
        } else if ((rateResult.getVal() == RateResult.LOSE.getVal()) && (score.getVal() == ScoreResult.LOSE.getVal())) {
            passEnd = true;
        }
    }

    public String getCategory() {
        return " " + category;
    }

    public void initCategory() {
        if (Float.compare(1.5f, winAvg) > 0) {
            category = "1" + "-" + (int) Math.floor(drawAvg) + "-" + (int) Math.floor(loseAvg);
        } else if (winAvg < 2 && Float.compare(winAvg, 1.5f) > 0) {
            category = "1.5" + "-" + (int) Math.floor(drawAvg) + "-" + (int) Math.floor(loseAvg);
        } else if (Float.compare(1.5f, loseAvg) > 0) {
            category = (int) Math.floor(winAvg) + "-" + (int) Math.floor(drawAvg) + "-" + 1;
        } else if (loseAvg < 2 && Float.compare(loseAvg, 1.5f) > 0) {
            category = (int) Math.floor(winAvg) + "-" + (int) Math.floor(drawAvg) + "-" + 1.5;
        } else {
            category = (int) Math.floor(winAvg) + "-" + (int) Math.floor(drawAvg) + "-" + (int) Math.floor(loseAvg);
        }
    }

    public void initPassOdds(ScoreResult score) {
        initPassStartOdds(score);
    }

    private void initPassStartOdds(ScoreResult score) {
        float winOdds = 0;
        float drawOdds = 0;
        float loseOdds = 0;
        ClaimRate rate = rates.get(CompanyMgr.ODDSET);
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

        if ((rateResult.getVal() == RateResult.WIN.getVal()) && (score.getVal() == ScoreResult.WIN.getVal())) {
            passStartOdds = true;
        } else if ((rateResult.getVal() == RateResult.DRAW.getVal()) && (score.getVal() == ScoreResult.DRAW.getVal())) {
            passStartOdds = true;
        } else if ((rateResult.getVal() == RateResult.LOSE.getVal()) && (score.getVal() == ScoreResult.LOSE.getVal())) {
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

    public void addClaimRate(ClaimRate rate) {
        if (!LfsConfMgr.isContains(rate.getComp())) {
            latestComp = null;
            return;
        }

        latestComp = rate.getComp();
        logger.debug("addClaimRate latestComp " + latestComp);
        rates.put(latestComp, rate);
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

    public void addRateAvg(float winAvg, float drawAvg, float loseAvg) {
        this.winAvg = winAvg;
        this.drawAvg = drawAvg;
        this.loseAvg = loseAvg;
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
                + (rates != null ? "rates=" + rates + ", " : "") + "winAvg=" + winAvg + ", drawAvg=" + drawAvg
                + ", loseAvg=" + loseAvg + ", winEnd=" + winEnd + ", drawEnd=" + drawEnd + ", loseEnd=" + loseEnd
                + ", passStart=" + passStart + ", passEnd=" + passEnd + ", passStartOdds=" + passStartOdds + ", "
                + (compRateResult != null ? "compRateResult=" + compRateResult + ", " : "")
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

    public Map<String, RateResult> getCompRateResult() {
        return compRateResult;
    }

    public void setCompRateResult(Map<String, RateResult> compRateResult) {
        this.compRateResult = compRateResult;
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
}