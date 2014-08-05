package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.CmpType;
import com.an.lfs.enu.RateBet;
import com.an.lfs.enu.RateType;
import com.an.lfs.enu.Result;
import com.an.lfs.enu.TeamType;

// Rate from woying
public class MatchRateWy {
    private static final Log logger = LogFactory.getLog(MatchRateWy.class);
    // Company -> Rate
    private Map<String, Rate> comRateMap = new HashMap<>();
    protected float win;
    protected float draw;
    protected float lose;
    private float winEnd;
    private float drawEnd;
    private float loseEnd;
    // Come from Match
    private Result scoreRet;

    public CmpType getTrend() {
        return LfsUtil.getTrend(win, draw, lose, winEnd, drawEnd, loseEnd);
    }

    public CmpType getComparator(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            logger.warn("Rate is null, " + this.toString());
            return CmpType.NULL;
        }

        return LfsUtil.getTrend(win, draw, lose, rate.getWin(), rate.getDraw(), rate.getLose());
    }

    public CmpType getTrend(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            logger.warn("Rate is null, " + this.toString());
            return CmpType.NULL;
        }
        return rate.getTrend();
    }

    // Company rate bet result
    public RateBet getBetRet(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            logger.warn("Rate is null, " + this.toString());
            return RateBet.INVALID;
        }
        Result rateFc = LfsUtil.getRateFc(rate.getWin(), rate.getDraw(), rate.getLose());
        return LfsUtil.getRateBet(rateFc, scoreRet);
    }

    public void addRate(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }

    public float getWin() {
        return win;
    }

    public float getDraw() {
        return draw;
    }

    public float getLose() {
        return lose;
    }

    public RateType getRateType(TeamType teamType) {
        float val = 0.0f;
        if (teamType.isHost()) {
            val = win;
        } else if (teamType.isMid()) {
            val = draw;
        } else if (teamType.isGuest()) {
            val = lose;
        }

        if (Float.compare(1.2f, val) > 0) {
            return RateType.RT_1_0;
        } else if (Float.compare(1.4f, val) > 0) {
            return RateType.RT_1_2;
        } else if (Float.compare(1.6f, val) > 0) {
            return RateType.RT_1_4;
        } else if (Float.compare(1.8f, val) > 0) {
            return RateType.RT_1_6;
        } else if (Float.compare(2.0f, val) > 0) {
            return RateType.RT_1_8;
        } else if (Float.compare(2.3f, val) > 0) {
            return RateType.RT_2_0;
        } else if (Float.compare(2.6f, val) > 0) {
            return RateType.RT_2_3;
        } else if (Float.compare(3.0f, val) > 0) {
            return RateType.RT_2_6;
        } else if (Float.compare(4.0f, val) > 0) {
            return RateType.RT_3_0;
        } else if (Float.compare(5.0f, val) > 0) {
            return RateType.RT_4_0;
        } else if (Float.compare(6.0f, val) > 0) {
            return RateType.RT_5_0;
        } else if (Float.compare(7.0f, val) > 0) {
            return RateType.RT_6_0;
        } else if (Float.compare(8.0f, val) > 0) {
            return RateType.RT_7_0;
        } else {
            return RateType.RT_8_0;
        }
    }

    public String getWinEndStr() {
        if (winEnd != 0.0f) {
            return winEnd + "";
        }
        return LfsUtil.NULL;
    }

    public String getDrawEndStr() {
        if (drawEnd != 0.0f) {
            return drawEnd + "";
        }
        return LfsUtil.NULL;
    }

    public String getLoseEndStr() {
        if (loseEnd != 0.0f) {
            return loseEnd + "";
        }
        return LfsUtil.NULL;
    }

    public float getWinEnd() {
        return winEnd;
    }

    public float getDrawEnd() {
        return drawEnd;
    }

    public float getLoseEnd() {
        return loseEnd;
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

    public String getWin(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return LfsUtil.NULL;
        }
        return rate.getWin() + " ";
    }

    public String getDraw(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return LfsUtil.NULL;
        }
        return rate.getDraw() + " ";
    }

    public String getLose(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return LfsUtil.NULL;
        }
        return rate.getLose() + " ";
    }

    public String getEndWin(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return LfsUtil.NULL;
        }
        if (rate.getWinEnd() == 0.0f) {
            return LfsUtil.NULL;
        }
        return rate.getWinEnd() + " ";
    }

    public String getEndDraw(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return LfsUtil.NULL;
        }
        if (rate.getDrawEnd() == 0.0f) {
            return LfsUtil.NULL;
        }
        return rate.getDrawEnd() + " ";
    }

    public String getEndLose(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return LfsUtil.NULL;
        }
        if (rate.getLoseEnd() == 0.0f) {
            return LfsUtil.NULL;
        }
        return rate.getLoseEnd() + " ";
    }

    public String getRateStr(String com) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            return "NULL";
        }
        return rate.getWin() + " " + rate.getDraw() + " " + rate.getLose();
    }

    public String getEndRateStr(String company) {
        Rate rate = comRateMap.get(company);
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
    }

    public void addComRate(Rate rate) {
        comRateMap.put(rate.getCom(), rate);
    }

    public void addComEndRate(String com, float winEnd, float drawEnd, float loseEnd) {
        Rate rate = comRateMap.get(com);
        if (rate == null) {
            logger.warn("Not found rate by company: " + com);
            return;
        }

        rate.addEndRate(winEnd, drawEnd, loseEnd);
    }

    private String filepath;

    public MatchRateWy() {
    }

    public Result getScoreRet() {
        return scoreRet;
    }

    public void setScoreRet(Result scoreRet) {
        this.scoreRet = scoreRet;
    }

    public Map<String, Rate> getCompanyRateMap() {
        return comRateMap;
    }

    public void setCompanyRateMap(Map<String, Rate> companyRateMap) {
        this.comRateMap = companyRateMap;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}