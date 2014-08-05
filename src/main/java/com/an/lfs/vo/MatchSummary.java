package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.ForecastRetType;
import com.an.lfs.enu.RateBet;
import com.an.lfs.enu.RateType;
import com.an.lfs.enu.Result;
import com.an.lfs.enu.TeamType;

public class MatchSummary {
    private BetResultNum betRetNum = new BetResultNum();
    // HOST -> {1.2 ->[3,3]}
    private Map<TeamType, Map<RateType, BetResultNum>> teamBetRetNumMap = new HashMap<>();
    private Map<ForecastRetType, Integer> forecastNumMap = new HashMap<>();
    private Map<Result, Integer> scoreNumMap = new HashMap<>();

    public MatchSummary() {
        for (TeamType tt : TeamType.allTeamTypes) {
            Map<RateType, BetResultNum> betRetNumMap = new HashMap<>();
            for (RateType st : RateType.allRateTypes) {
                betRetNumMap.put(st, new BetResultNum());
            }
            teamBetRetNumMap.put(tt, betRetNumMap);
        }
        for (ForecastRetType ft : ForecastRetType.allFcRetTypes) {
            forecastNumMap.put(ft, 0);
        }
        for (Result ret : Result.allResults) {
            scoreNumMap.put(ret, 0);
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

    public void addRateFc(Result rateFc, Result scoreRet) {
        int num = scoreNumMap.get(scoreRet);
        scoreNumMap.put(scoreRet, num + 1);

        String val = "";
        if (rateFc.isWin()) {
            val = val + "W";
        } else if (rateFc.isDraw()) {
            val = val + "D";
        } else if (rateFc.isLose()) {
            val = val + "L";
        }
        val = val + "_";
        if (scoreRet.isWin()) {
            val = val + "W";
        } else if (scoreRet.isDraw()) {
            val = val + "D";
        } else if (scoreRet.isLose()) {
            val = val + "L";
        }
        ForecastRetType ft = ForecastRetType.getForecastResultType(val);
        forecastNumMap.put(ft, forecastNumMap.get(ft) + 1);
    }

    public int getFcRetNum(ForecastRetType ft) {
        if (forecastNumMap.containsKey(ft)) {
            return forecastNumMap.get(ft);
        }
        return 0;
    }

    public String getFcRetPer(ForecastRetType ft) {
        if (forecastNumMap.containsKey(ft)) {
            int total = 0;
            if (ForecastRetType.allWinFcRetSet.contains(ft)) {
                for (ForecastRetType t : ForecastRetType.allWinFcRetSet) {
                    total += forecastNumMap.get(t);
                }
            } else if (ForecastRetType.allLoseFcRetSet.contains(ft)) {
                for (ForecastRetType t : ForecastRetType.allLoseFcRetSet) {
                    total += forecastNumMap.get(t);
                }
            }

            int num = forecastNumMap.get(ft);
            String ret = (int) (100 * (float) num / (float) (total)) + "%";
            return ret;
        }
        return LfsUtil.NULL;
    }

    public int getScoreTypeNum(Result st) {
        if (scoreNumMap.containsKey(st)) {
            return scoreNumMap.get(st);
        }
        return 0;
    }

    public String getScoreTypePer(Result st) {
        int total = 0;
        for (int val : scoreNumMap.values()) {
            total += val;
        }

        int num = scoreNumMap.get(st);
        String ret = (int) (100 * (float) num / (float) (total)) + "%";
        return ret;
    }

    public void addBetRet(RateBet betRet) {
        betRetNum.addBetResult(betRet);
    }

    /**
     * @param teamType
     * @param scoreType
     * @param betRet
     */
    public void addTeamBetResult(TeamType teamType, RateType scoreType, RateBet betRet) {
        teamBetRetNumMap.get(teamType).get(scoreType).addBetResult(betRet);
    }

    public BetResultNum getBetRetNum() {
        return betRetNum;
    }
}
