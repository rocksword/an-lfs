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
    private ScoreResult scoreResult;

    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String hostCat = null;
    private String guestCat = null;
    private String middleCat = null;

    public void addScore(String score) {
        this.score = score;
        this.scoreResult = LfsUtil.getScoreResult(score);
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
        return getBetResult(this.rateResult, this.scoreResult);
    }

    /**
     * @param rateResult
     * @param scoreResult
     * @return
     */
    protected BetResult getBetResult(RateResult rateResult, ScoreResult scoreResult) {
        if (rateResult == null || scoreResult == null) {
            return BetResult.INVALID;
        }
        if (rateResult.isInvalid() || scoreResult.isInvalid()) {
            return BetResult.INVALID;
        }
        if (rateResult.isWin() && scoreResult.isWin()) {
            return BetResult.PASS;
        } else if (rateResult.isDraw() && scoreResult.isDraw()) {
            return BetResult.PASS;
        } else if (rateResult.isLose() && scoreResult.isLose()) {
            return BetResult.PASS;
        }
        return BetResult.FAIL;
    }

    public boolean isWin() {
        return scoreResult.isWin();
    }

    public boolean isLose() {
        return scoreResult.isLose();
    }

    public boolean isDraw() {
        return scoreResult.isDraw();
    }

    public String getScoreResultStr() {
        if (scoreResult.isWin()) {
            return "+";
        } else if (scoreResult.isDraw()) {
            return "=";
        } else if (scoreResult.isLose()) {
            return "-";
        } else {
            return "NULL";
        }
    }

    public LfsBase() {
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

    public ScoreResult getScoreResult() {
        return scoreResult;
    }

    public void setScoreResult(ScoreResult scoreResult) {
        this.scoreResult = scoreResult;
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
