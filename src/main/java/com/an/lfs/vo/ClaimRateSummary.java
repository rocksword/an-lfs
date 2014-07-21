package com.an.lfs.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.CompanyMgr;

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

    private List<String> compareAvg = new ArrayList<>();
    private List<String> trendStartEnd = new ArrayList<>();
    private boolean passStart = false;
    private boolean passEnd = false;

    private boolean passStartOdds = false;

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

    // 1 2 3 -> WIN, 3 2 3 -> DRAW, 3 2 1 -> LOSE
    private Map<String, RateResult> compRateResult = new HashMap<>();

    public void initRateResult(String company) {
        float win = 0;
        float draw = 0;
        float lose = 0;
        ClaimRate rate = rates.get(company);
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
        initPassStart(score);
        initPassEnd(score);
    }

    private void initPassStart(ScoreResult score) {
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
    }

    private void initPassEnd(ScoreResult score) {
        float min = winEnd;
        RateResult rateResult = RateResult.WIN;
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

    public void initPassOdds(ScoreResult score) {
        initPassStartOdds(score);
    }

    private void initPassStartOdds(ScoreResult score) {
        float winOdds = 0;
        float drawOdds = 0;
        float loseOdds = 0;
        ClaimRate rate = rates.get(CompanyMgr.ODDSET);
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
        if (company.equals(rate.getComp())) {
            return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose() + " | " + rate.getWinEnd() + " "
                    + rate.getDrawEnd() + " " + rate.getLoseEnd();
        }
        return null;
    }

    public void computeRate(ClaimRate rate) {
        String compareAvgWin = " ";
        String compareAvgDraw = " ";
        String compareAvgLose = " ";
        if ((rate.getWin() - winAvg) < 0) {
            compareAvgWin = "-";
        } else if ((rate.getWin() - winAvg) == 0) {
            compareAvgWin = "-";
        } else {
            compareAvgWin = "+";
        }

        if ((rate.getDraw() - drawAvg) < 0) {
            compareAvgDraw = "-";
        } else if ((rate.getDraw() - drawAvg) == 0) {
            compareAvgDraw = "=";
        } else {
            compareAvgDraw = "+";
        }

        if ((rate.getLose() - loseAvg) < 0) {
            compareAvgLose = "-";
        } else if ((rate.getLose() - loseAvg) == 0) {
            compareAvgLose = "=";
        } else {
            compareAvgLose = "+";
        }

        compareAvg.add(compareAvgWin);
        compareAvg.add(compareAvgDraw);
        compareAvg.add(compareAvgLose);

        if (rate.getWinEnd() > 0 && rate.getDrawEnd() > 0 && rate.getLoseEnd() > 0) {
            String trendSEWin = " ";
            String trendSEDraw = " ";
            String trendSELose = " ";
            if ((rate.getWinEnd() - rate.getWin()) < 0) {
                trendSEWin = "-";
            } else if ((rate.getWinEnd() - rate.getWin()) == 0) {
                trendSEWin = "=";
            } else {
                trendSEWin = "+";
            }

            if ((rate.getDrawEnd() - rate.getDraw()) < 0) {
                trendSEDraw = "-";
            } else if ((rate.getDrawEnd() - rate.getDraw()) == 0) {
                trendSEDraw = "=";
            } else {
                trendSEDraw = "+";
            }

            if ((rate.getLoseEnd() - rate.getLose()) < 0) {
                trendSELose = "-";
            } else if ((rate.getLoseEnd() - rate.getLose()) == 0) {
                trendSELose = "=";
            } else {
                trendSELose = "+";
            }

            trendStartEnd.add(trendSEWin);
            trendStartEnd.add(trendSEDraw);
            trendStartEnd.add(trendSELose);
        }
    }

    private String latestComp = null;

    public void addClaimRate(ClaimRate rate) {
        logger.debug("add " + rate);
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
        } else {
            logger.error("latestComp is null.");
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

    public List<String> getCompareAvg() {
        return compareAvg;
    }

    public List<String> getTrendStartEnd() {
        return trendStartEnd;
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