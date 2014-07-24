package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsConst;
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
        return super.getBetResult(endRateResult, super.getScoreResult());
    }

    public String getBetResultStr() {
        StringBuilder result = new StringBuilder();
        BetResult betRet = this.getBetResult();
        if (betRet.isPass()) {
            result.append(LfsConst.PASS);
        } else if (betRet.isFail()) {
            result.append(LfsConst.FAIL);
        } else {
            result.append(LfsConst.IGNORE);
        }

        result.append(LfsConst.SEPARATOR);

        betRet = this.getEndBetResult();
        if (betRet.isPass()) {
            result.append(LfsConst.PASS);
        } else if (betRet.isFail()) {
            result.append(LfsConst.FAIL);
        } else {
            result.append(LfsConst.IGNORE);
        }

        return result.toString();
    }

    public BetResult getBetResult(String company) {
        ClaimRate rate = companyRateMap.get(company);
        if (rate == null) {
            logger.warn("Rate is null, company: " + company);
            return BetResult.INVALID;
        }

        RateResult rateResult = rate.getRateResult();
        return super.getBetResult(rateResult, super.getScoreResult());
    }

    public String getBetResultStr(String company) {
        BetResult betRet = this.getBetResult(company);

        if (betRet.isPass()) {
            return LfsConst.PASS;
        } else if (betRet.isFail()) {
            return LfsConst.FAIL;
        } else {
            return LfsConst.IGNORE;
        }
    }

    /**
     * @return average rate
     */
    public String getRate() {
        if (isValidEndRate()) {
            return getWin() + " " + getDraw() + " " + getLose() + LfsConst.SEPARATOR + winEnd + " " + drawEnd + " "
                    + loseEnd;
        } else {
            return getWin() + " " + getDraw() + " " + getLose() + LfsConst.SEPARATOR + " NULL";
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

        if (rate.isValidEndRate()) {
            return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose() + LfsConst.SEPARATOR + rate.getWinEnd()
                    + " " + rate.getDrawEnd() + " " + rate.getLoseEnd();
        } else {
            return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose() + LfsConst.SEPARATOR + " NULL";
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

    public ClaimRateSummary() {
    }

    public Map<String, ClaimRate> getRates() {
        return companyRateMap;
    }

    public void setRates(Map<String, ClaimRate> rates) {
        this.companyRateMap = rates;
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
}