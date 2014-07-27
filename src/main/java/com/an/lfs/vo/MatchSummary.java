package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.BetRet;
import com.an.lfs.enu.ForecastRet;
import com.an.lfs.enu.ForecastRetType;
import com.an.lfs.enu.RateType;
import com.an.lfs.enu.ScoreType;
import com.an.lfs.enu.TeamType;

public class MatchSummary {
    private BetResultNum betRetNum = new BetResultNum();
    // HOST -> {1.2 ->[3,3]}
    private Map<TeamType, Map<RateType, BetResultNum>> teamBetRetNumMap = new HashMap<>();
    private Map<ForecastRetType, Integer> fcRetNumMap = new HashMap<>();
    private Map<ScoreType, Integer> scoreTypeNumMap = new HashMap<>();

    public MatchSummary() {
        for (TeamType tt : TeamType.allTeamTypes) {
            Map<RateType, BetResultNum> betRetNumMap = new HashMap<>();
            for (RateType st : RateType.allRateTypes) {
                betRetNumMap.put(st, new BetResultNum());
            }
            teamBetRetNumMap.put(tt, betRetNumMap);
        }
        for (ForecastRetType ft : ForecastRetType.allFcRetTypes) {
            fcRetNumMap.put(ft, 0);
        }
        for (ScoreType st : ScoreType.allScoreTypes) {
            scoreTypeNumMap.put(st, 0);
        }
    }

    public void appendTeamBetResultNum(StringBuilder content) {
        for (TeamType tt : TeamType.allTeamTypes) {
            content.append(tt.getVal()).append(",,,,,,,,").append("\n");
            content.append("type,pass,percent,fail,percent\n");
            for (RateType st : RateType.allRateTypes) {
                BetResultNum br = getBetRetNum(tt, st);
                content.append(st.getVal()).append(",").append(br.getPassNum()).append(",").append(br.getPassPer())
                        .append(",").append(br.getFailNum()).append(",").append(br.getFailPer()).append("\n");
            }
        }
    }

    public BetResultNum getBetRetNum(TeamType tt, RateType st) {
        return teamBetRetNumMap.get(tt).get(st);
    }

    public void addForecastScoreRet(ForecastRet fcRet, ScoreType scoreType) {
        Integer num = scoreTypeNumMap.get(scoreType);
        scoreTypeNumMap.put(scoreType, num + 1);

        String val = "";
        if (fcRet.isWin()) {
            val = val + "W";
        } else if (fcRet.isDraw()) {
            val = val + "D";
        } else if (fcRet.isLose()) {
            val = val + "L";
        }
        val = val + "_";
        if (scoreType.isWin()) {
            val = val + "W";
        } else if (scoreType.isDraw()) {
            val = val + "D";
        } else if (scoreType.isLose()) {
            val = val + "L";
        }
        ForecastRetType ft = ForecastRetType.getForecastResultType(val);
        fcRetNumMap.put(ft, fcRetNumMap.get(ft) + 1);
    }

    public int getFcRetNum(ForecastRetType ft) {
        if (fcRetNumMap.containsKey(ft)) {
            return fcRetNumMap.get(ft);
        }
        return 0;
    }

    public String getFcRetPer(ForecastRetType ft) {
        if (fcRetNumMap.containsKey(ft)) {
            int total = 0;
            if (ForecastRetType.allWinFcRetSet.contains(ft)) {
                for (ForecastRetType t : ForecastRetType.allWinFcRetSet) {
                    total += fcRetNumMap.get(t);
                }
            } else if (ForecastRetType.allLoseFcRetSet.contains(ft)) {
                for (ForecastRetType t : ForecastRetType.allLoseFcRetSet) {
                    total += fcRetNumMap.get(t);
                }
            }

            int num = fcRetNumMap.get(ft);
            String ret = (int) (100 * (float) num / (float) (total)) + "%";
            return ret;
        }
        return LfsUtil.NULL;
    }

    public int getScoreTypeNum(ScoreType st) {
        if (scoreTypeNumMap.containsKey(st)) {
            return scoreTypeNumMap.get(st);
        }
        return 0;
    }

    public String getScoreTypePer(ScoreType st) {
        int total = 0;
        for (int val : scoreTypeNumMap.values()) {
            total += val;
        }

        int num = scoreTypeNumMap.get(st);
        String ret = (int) (100 * (float) num / (float) (total)) + "%";
        return ret;
    }

    public void addBetRet(BetRet betRet) {
        betRetNum.addBetResult(betRet);
    }

    /**
     * @param teamType
     * @param scoreType
     * @param betRet
     */
    public void addTeamBetResult(TeamType teamType, RateType scoreType, BetRet betRet) {
        teamBetRetNumMap.get(teamType).get(scoreType).addBetResult(betRet);
    }

    public BetResultNum getBetRetNum() {
        return betRetNum;
    }
}
