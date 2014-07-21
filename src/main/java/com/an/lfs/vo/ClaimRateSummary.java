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

    // dynamic data
    private boolean minWin = false;
    private boolean minDraw = false;
    private boolean minLose = false;

    private List<String> compareAvg = new ArrayList<>();
    private List<String> trendStartEnd = new ArrayList<>();
    private boolean oddsPass = false;

    public String getOddsPass() {
        return oddsPass ? "T" : "F";
    }

    public void setValues(boolean minWin, boolean minDraw, boolean minLose) {
        this.minWin = minWin;
        this.minDraw = minDraw;
        this.minLose = minLose;
    }

    public String getRateAvg() {
        return winAvg + " " + drawAvg + " " + loseAvg;
    }

    public void initOddsPass(ScoreResult scoreResult) {
        float win = 0;
        float draw = 0;
        float lose = 0;
        for (ClaimRate rate : rates) {
            if ("Oddset".equals(rate.getComp())) {
                win = rate.getWin();
                draw = rate.getDraw();
                lose = rate.getLose();
                break;
            }
        }

        float min = win;
        minWin = true;
        minDraw = false;
        minLose = false;
        if (Float.compare(draw, min) < 0) {
            min = draw;
            minWin = false;
            minDraw = true;
            minLose = false;
        }
        if (Float.compare(lose, min) < 0) {
            min = lose;
            minWin = false;
            minDraw = false;
            minLose = true;
        }

        if (minDraw && (scoreResult.getVal() == ScoreResult.DRAW.getVal())) {
            oddsPass = true;
        } else if (minWin && (scoreResult.getVal() == ScoreResult.WIN.getVal())) {
            oddsPass = true;
        } else if (minLose && (scoreResult.getVal() == ScoreResult.LOSE.getVal())) {
            oddsPass = true;
        }
    }

    public String getOddsEnd() {
        for (ClaimRate rate : rates) {
            if ("Oddset".equals(rate.getComp())) {
                return rate.getWinEnd() + " " + rate.getDrawEnd() + " " + rate.getLoseEnd();
            }
        }
        return null;
    }

    public String getOdds() {
        for (ClaimRate rate : rates) {
            if ("Oddset".equals(rate.getComp())) {
                return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose();
            }
        }
        return null;
    }

    public void initCompareAvgAndTrend() {
        for (ClaimRate rate : rates) {
            if ("Oddset".equals(rate.getComp())) {
                computeRate(rate);
            }
        }
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
        if (minWin) {
            compareAvg.add(compareAvgWin);
        } else if (minDraw) {
            compareAvg.add(compareAvgDraw);
        } else if (minLose) {
            compareAvg.add(compareAvgLose);
        }

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

    public List<String> getCompareAvg() {
        return compareAvg;
    }

    public List<String> getTrendStartEnd() {
        return trendStartEnd;
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
