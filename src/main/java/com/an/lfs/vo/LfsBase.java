package com.an.lfs.vo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsUtil;

public class LfsBase {
    private static final Log logger = LogFactory.getLog(Match.class);
    private float win;
    private float draw;
    private float lose;
    private String score;

    private RateResult rateResult;
    private MatchResult matchResult;

    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String hostCat = null;
    private String guestCat = null;
    private String middleCat = null;

    public void addScore(String score) {
        this.score = score;
        this.matchResult = LfsUtil.getMatchResult(score);
    }

    public void addRate(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;

        this.rateResult = LfsUtil.getRateResult(win, draw, lose);
        this.hostCat = LfsUtil.getCat(win);
        this.guestCat = LfsUtil.getCat(lose);
        this.middleCat = LfsUtil.getCat(draw);
    }

    /**
     * @return
     */
    public BetResult getBetResult() {
        return getBetResult(this.rateResult, this.matchResult);
    }

    /**
     * @param rateResult
     * @param matchResult
     * @return
     */
    protected BetResult getBetResult(RateResult rateResult, MatchResult matchResult) {
        if (rateResult == null || matchResult == null) {
            return BetResult.INVALID;
        }
        if (rateResult.isInvalid() || matchResult.isInvalid()) {
            return BetResult.INVALID;
        }
        if (rateResult.isWin() && matchResult.isWin()) {
            return BetResult.PASS;
        } else if (rateResult.isDraw() && matchResult.isDraw()) {
            return BetResult.PASS;
        } else if (rateResult.isLose() && matchResult.isLose()) {
            return BetResult.PASS;
        }
        return BetResult.FAIL;
    }

    public boolean isWin() {
        return matchResult.isWin();
    }

    public boolean isLose() {
        return matchResult.isLose();
    }

    public boolean isDraw() {
        return matchResult.isDraw();
    }

    public String getMatchResultStr() {
        if (matchResult.isWin()) {
            return LfsUtil.WIN;
        } else if (matchResult.isDraw()) {
            return LfsUtil.DRAW;
        } else if (matchResult.isLose()) {
            return LfsUtil.LOSE;
        } else {
            return LfsUtil.NULL;
        }
    }

    public LfsBase() {
    }

    @Override
    public String toString() {
        return "LfsBase [win=" + win + ", draw=" + draw + ", lose=" + lose + ", score=" + score + ", rateResult="
                + rateResult + ", matchResult=" + matchResult + ", hostCat=" + hostCat + ", guestCat=" + guestCat
                + ", middleCat=" + middleCat + "]";
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public RateResult getRateResult() {
        return rateResult;
    }

    public void setRateResult(RateResult rateResult) {
        this.rateResult = rateResult;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
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

    public static Log getLogger() {
        return logger;
    }
}
