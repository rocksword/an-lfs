package com.an.lfs.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.Result;

public class MatchRuleMgr {
    private static final Logger logger = LoggerFactory.getLogger(MatchRuleMgr.class);
    // Rule -> Count
    private Map<String, MatchRule> matchRuleMap = new HashMap<>();

    public void addRule(Result rateFc, Result trendFc, Result scoreRet) {
        if (scoreRet.isNull()) {
            return;
        }

        MatchRule matchRule = new MatchRule(rateFc, trendFc, scoreRet);
        String ruleId = matchRule.getId();
        if (!matchRuleMap.containsKey(ruleId)) {
            matchRuleMap.put(ruleId, matchRule);
        } else {
            matchRuleMap.get(ruleId).incrementCount();
        }
    }

    public List<MatchRule> getMatchRule() {

        // RateFc_TrendFc -> count
        int winWin = 0;
        int winLose = 0;
        int winNull = 0;
        int winDraw = 0;
        int loseWin = 0;
        int loseLose = 0;
        int loseNull = 0;
        int loseDraw = 0;

        for (MatchRule rule : matchRuleMap.values()) {
            Result rateFc = rule.getRateFc();
            Result trendFc = rule.getTrendFc();
            Integer count = rule.getCount();

            if (rateFc.isWin() && trendFc.isWin()) {
                winWin += count;
            } else if (rateFc.isWin() && trendFc.isLose()) {
                winLose += count;
            } else if (rateFc.isWin() && trendFc.isNull()) {
                winNull += count;
            } else if (rateFc.isWin() && trendFc.isDraw()) {
                winDraw += count;
            } else if (rateFc.isLose() && trendFc.isWin()) {
                loseWin += count;
            } else if (rateFc.isLose() && trendFc.isLose()) {
                loseLose += count;
            } else if (rateFc.isLose() && trendFc.isNull()) {
                loseNull += count;
            } else if (rateFc.isLose() && trendFc.isDraw()) {
                loseDraw += count;
            }
        }
        logger.info(
                "winWin {}, winLose {}, winNull {}, winDraw {}, loseWin {}, loseLose {}, loseNull {}, loseDraw {}, ",
                winWin, winLose, winNull, winDraw, loseWin, loseLose, loseNull, loseDraw);

        List<MatchRule> ruleList = new ArrayList<>();
        for (MatchRule rule : matchRuleMap.values()) {
            Result rateFc = rule.getRateFc();
            Result trendFc = rule.getTrendFc();

            int per = 0;
            if (rateFc.isWin() && trendFc.isWin()) {
                per = 100 * rule.getCount() / winWin;
            } else if (rateFc.isWin() && trendFc.isLose()) {
                per = 100 * rule.getCount() / winLose;
            } else if (rateFc.isWin() && trendFc.isNull()) {
                per = 100 * rule.getCount() / winNull;
            } else if (rateFc.isWin() && trendFc.isDraw()) {
                per = 100 * rule.getCount() / winDraw;
            } else if (rateFc.isLose() && trendFc.isWin()) {
                per = 100 * rule.getCount() / loseWin;
            } else if (rateFc.isLose() && trendFc.isLose()) {
                per = 100 * rule.getCount() / loseLose;
            } else if (rateFc.isLose() && trendFc.isNull()) {
                per = 100 * rule.getCount() / loseNull;
            } else if (rateFc.isLose() && trendFc.isDraw()) {
                per = 100 * rule.getCount() / loseDraw;
            }

            rule.setPercent(per);

            ruleList.add(rule);
        }

        Collections.sort(ruleList);

        return ruleList;
    }
}
