package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.Result;

// Input rateFc and trendFc, output scoreResult
public class MatchRule implements Comparable<MatchRule> {
    // Rate forecast
    private Result rateFc;
    // Rate's trend forecast
    private Result trendFc;
    // Score result forecast
    private Result scoreRet;

    private Integer count = new Integer(1);

    private int percent = 0;

    public void incrementCount() {
        this.count += 1;
    }

    public MatchRule() {
    }

    public String getId() {
        return String.format("%s  %s  %s", LfsUtil.getResultStr(rateFc), LfsUtil.getResultStr(trendFc),
                LfsUtil.getResultStr(scoreRet));
    }

    public String getForecastId() {
        return String.format("%s  %s", LfsUtil.getResultStr(rateFc), LfsUtil.getResultStr(trendFc));
    }

    /**
     * @param rateFc
     * @param trendFc
     * @param scoreResult
     */
    public MatchRule(Result rateFc, Result trendFc, Result scoreRet) {
        this.rateFc = rateFc;
        this.trendFc = trendFc;
        this.scoreRet = scoreRet;
    }

    @Override
    public int compareTo(MatchRule o) {
        if (rateFc.getVal().compareTo(o.getRateFc().getVal()) > 0) {
            return 1;
        } else if (rateFc.getVal().compareTo(o.getRateFc().getVal()) < 0) {
            return -1;
        } else {
            if (trendFc.getVal().compareTo(o.getTrendFc().getVal()) > 0) {
                return 1;
            } else if (trendFc.getVal().compareTo(o.getTrendFc().getVal()) < 0) {
                return -1;
            } else {
                if (scoreRet.getVal().compareTo(o.getScoreRet().getVal()) > 0) {
                    return 1;
                } else if (scoreRet.getVal().compareTo(o.getScoreRet().getVal()) < 0) {
                    return -1;
                } else {
                    return -1 * count.compareTo(o.getCount());
                }
            }
        }
    }

    @Override
    public String toString() {
        return "MatchRule [rateFc=" + rateFc + ", trendFc=" + trendFc + ", scoreRet=" + scoreRet + ", count=" + count
                + ", percent=" + percent + "]";
    }

    public Result getRateFc() {
        return rateFc;
    }

    public void setRateFc(Result rateFc) {
        this.rateFc = rateFc;
    }

    public Result getTrendFc() {
        return trendFc;
    }

    public void setTrendFc(Result trendFc) {
        this.trendFc = trendFc;
    }

    public Result getScoreRet() {
        return scoreRet;
    }

    public void setScoreRet(Result scoreRet) {
        this.scoreRet = scoreRet;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
