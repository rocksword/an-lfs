package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.CompanyMgr;
import com.an.lfs.LfsConfMgr;
import com.an.lfs.LfsConst;

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
    //
    private boolean passStart = false;
    private boolean passEnd = false;
    private boolean passStartOdds = false;
    private Map<String, RateResult> companyRateResult = new HashMap<>();
    // 1.5~3~5
    private String category = null;
    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String hostCategory = null;
    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String guestCategory = null;

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
        return winAvg + " " + drawAvg + " " + loseAvg + LfsConst.SEPARATOR + winEnd + " " + drawEnd + " " + loseEnd;
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
        companyRateResult.put(company, rateResult);
    }

    public void initPass(ScoreResult scoreResult) {
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

    public String getCategory() {
        return category;
    }

    public String getHostCategory() {
        return hostCategory;
    }

    public String getGuestCategory() {
        return guestCategory;
    }

    // 1.20以内（不包括） 6胜
    // 1.20-1.40 26胜7平7负
    // 1.40-1.60 23胜11平4负
    // 1.60-1.80 14胜12平7负
    // 1.80-2.00 15胜3平12负
    // 2.00-2.30 20胜21平18负
    // 2.30-2.60 8胜8平15负
    // 2.60-3.00 3胜3平4负
    // 3.00-4.00 6胜12平22负
    // 4.00-5.00 2胜6平4负
    // 5.00以上（包括） 2胜3平2负
    public void initCategory() {
        if (Float.compare(1.5f, winAvg) > 0) {
            category = "1" + LfsConst.SEPARATOR + (int) Math.floor(drawAvg) + LfsConst.SEPARATOR
                    + (int) Math.floor(loseAvg);
        } else if (winAvg < 2 && Float.compare(winAvg, 1.5f) > 0) {
            category = "1.5" + LfsConst.SEPARATOR + (int) Math.floor(drawAvg) + LfsConst.SEPARATOR
                    + (int) Math.floor(loseAvg);
        } else if (Float.compare(1.5f, loseAvg) > 0) {
            category = (int) Math.floor(winAvg) + LfsConst.SEPARATOR + (int) Math.floor(drawAvg) + LfsConst.SEPARATOR
                    + 1;
        } else if (loseAvg < 2 && Float.compare(loseAvg, 1.5f) > 0) {
            category = (int) Math.floor(winAvg) + LfsConst.SEPARATOR + (int) Math.floor(drawAvg) + LfsConst.SEPARATOR
                    + 1.5;
        } else {
            category = (int) Math.floor(winAvg) + LfsConst.SEPARATOR + (int) Math.floor(drawAvg) + LfsConst.SEPARATOR
                    + (int) Math.floor(loseAvg);
        }

        if (Float.compare(1.2f, winAvg) >= 0) {
            hostCategory = "1.2";
        } else if (Float.compare(1.4f, winAvg) >= 0) {
            hostCategory = "1.4";
        } else if (Float.compare(1.6f, winAvg) >= 0) {
            hostCategory = "1.6";
        } else if (Float.compare(1.8f, winAvg) >= 0) {
            hostCategory = "1.8";
        } else if (Float.compare(2.0f, winAvg) >= 0) {
            hostCategory = "2.0";
        } else if (Float.compare(2.3f, winAvg) >= 0) {
            hostCategory = "2.3";
        } else if (Float.compare(2.6f, winAvg) >= 0) {
            hostCategory = "2.6";
        } else if (Float.compare(3.0f, winAvg) >= 0) {
            hostCategory = "3.0";
        } else if (Float.compare(4.0f, winAvg) >= 0) {
            hostCategory = "4.0";
        } else if (Float.compare(5.0f, winAvg) >= 0) {
            hostCategory = "5.0";
        } else if (Float.compare(6.0f, winAvg) >= 0) {
            hostCategory = "6.0";
        } else if (Float.compare(7.0f, winAvg) >= 0) {
            hostCategory = "7.0";
        } else if (Float.compare(8.0f, winAvg) >= 0) {
            hostCategory = "8.0";
        } else {
            hostCategory = "9.0";
        }

        if (Float.compare(1.2f, loseAvg) >= 0) {
            guestCategory = "1.2";
        } else if (Float.compare(1.4f, loseAvg) >= 0) {
            guestCategory = "1.4";
        } else if (Float.compare(1.6f, loseAvg) >= 0) {
            guestCategory = "1.6";
        } else if (Float.compare(1.8f, loseAvg) >= 0) {
            guestCategory = "1.8";
        } else if (Float.compare(2.0f, loseAvg) >= 0) {
            guestCategory = "2.0";
        } else if (Float.compare(2.3f, loseAvg) >= 0) {
            guestCategory = "2.3";
        } else if (Float.compare(2.6f, loseAvg) >= 0) {
            guestCategory = "2.6";
        } else if (Float.compare(3.0f, loseAvg) >= 0) {
            guestCategory = "3.0";
        } else if (Float.compare(4.0f, loseAvg) >= 0) {
            guestCategory = "4.0";
        } else if (Float.compare(5.0f, loseAvg) >= 0) {
            guestCategory = "5.0";
        } else if (Float.compare(6.0f, loseAvg) >= 0) {
            guestCategory = "6.0";
        } else if (Float.compare(7.0f, loseAvg) >= 0) {
            guestCategory = "7.0";
        } else if (Float.compare(8.0f, loseAvg) >= 0) {
            guestCategory = "8.0";
        } else {
            guestCategory = "9.0";
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
                + (companyRateResult != null ? "companyRateResult=" + companyRateResult + ", " : "")
                + (category != null ? "category=" + category + ", " : "")
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
        return companyRateResult;
    }

    public void setCompRateResult(Map<String, RateResult> compRateResult) {
        this.companyRateResult = compRateResult;
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