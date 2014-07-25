package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsUtil;

public class ClaimRateSummary extends LfsBase {
    private static final Log logger = LogFactory.getLog(ClaimRateSummary.class);
    // Company -> ClaimRate
    private Map<String, ClaimRate> companyRateMap = new HashMap<>();
    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    private RateResult endRateResult;

    public BetResult getEndBetResult() {
        return super.getBetResult(endRateResult, super.getMatchResult());
    }

    public BetResult getBetResult(String company) {
        ClaimRate rate = companyRateMap.get(company);
        if (rate == null) {
            logger.warn("Rate is null, " + this.toString());
            return BetResult.INVALID;
        }

        RateResult rateResult = rate.getRateResult();
        return super.getBetResult(rateResult, super.getMatchResult());
    }

    public String getBetResultStr(String company) {
        BetResult betRet = this.getBetResult(company);
        return LfsUtil.getBetStr(betRet);
    }

    /**
     * @return average rate
     */
    public String getRate() {
        return getWin() + " " + getDraw() + " " + getLose();
    }

    /**
     * @return end average rate
     */
    public String getEndRate() {
        if (isValidEndRate()) {
            return winEnd + " " + drawEnd + " " + loseEnd;
        } else {
            return "NULL";
        }
    }

    public boolean isValidEndRate() {
        return LfsUtil.isValidRate(winEnd, drawEnd, loseEnd);
    }

    public String getRateStr(String company) {
        ClaimRate rate = companyRateMap.get(company);
        if (rate == null) {
            return "NULL";
        }
        return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose();
    }

    public String getEndRateStr(String company) {
        ClaimRate rate = companyRateMap.get(company);
        if (rate == null) {
            return "NULL";
        }

        if (rate.isValidEndRate()) {
            return rate.getWinEnd() + " " + rate.getDrawEnd() + " " + rate.getLoseEnd();
        } else {
            return "NULL";
        }
    }

    public void addEndRate(float winEnd, float drawEnd, float loseEnd) {
        this.winEnd = winEnd;
        this.drawEnd = drawEnd;
        this.loseEnd = loseEnd;

        this.endRateResult = LfsUtil.getRateResult(winEnd, drawEnd, loseEnd);
    }

    public void addCompanyRate(ClaimRate rate) {
        companyRateMap.put(rate.getComp(), rate);
    }

    public void addCompanyEndRate(String company, float winEnd, float drawEnd, float loseEnd) {
        ClaimRate rate = companyRateMap.get(company);
        if (rate == null) {
            logger.warn("Not found rate by company: " + company);
            return;
        }

        rate.addEndRate(winEnd, drawEnd, loseEnd);
    }

    private String filepath;

    public ClaimRateSummary() {
    }

    @Override
    public String toString() {
        return "ClaimRateSummary [winEnd=" + winEnd + ", drawEnd=" + drawEnd + ", loseEnd=" + loseEnd + ", "
                + (endRateResult != null ? "endRateResult=" + endRateResult + ", " : "")
                + (filepath != null ? "filepath=" + filepath : "") + "]";
    }

    public Map<String, ClaimRate> getCompanyRateMap() {
        return companyRateMap;
    }

    public void setCompanyRateMap(Map<String, ClaimRate> companyRateMap) {
        this.companyRateMap = companyRateMap;
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

    public RateResult getEndRateResult() {
        return endRateResult;
    }

    public void setEndRateResult(RateResult endRateResult) {
        this.endRateResult = endRateResult;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}