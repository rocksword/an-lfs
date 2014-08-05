package com.an.lfs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.enu.RateBet;
import com.an.lfs.enu.Result;
import com.an.lfs.enu.TeamType;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.CompanyMgr;
import com.an.lfs.vo.MatchRateWy;
import com.an.lfs.vo.MatchSummary;
import com.an.lfs.vo.Rate;

public abstract class RateAnalyzer extends MatchAnalyzer {
    private static final Log logger = LogFactory.getLog(RateAnalyzer.class);
    protected MatchSummary matSum = new MatchSummary();
    // rateKey -> RateSummary
    protected Map<String, MatchRateWy> matRateMap = new HashMap<String, MatchRateWy>();

    /**
     * @param country
     * @param year
     */
    public RateAnalyzer(Country country, int year) {
        super(country, year);
    }

    public void analyzeRate() {
        for (String matchId : super.matchMap.keySet()) {
            String filepath = LfsUtil.getInputFilePath(country, year, matchId + ".txt");
            logger.debug("filepath: " + filepath);
            MatchRateWy mr = parse(filepath);
            mr.setScoreRet(LfsUtil.getScoreRet(super.getMatch(matchId).getScore()));

            if (!mr.getCompanyRateMap().isEmpty()) {
                matRateMap.put(matchId, mr);

                Result rateFc = LfsUtil.getRateFc(mr.getWinEnd(), mr.getDrawEnd(), mr.getLoseEnd());
                matSum.addRateFc(rateFc, LfsUtil.getScoreRet(matchMap.get(matchId).getScore()));
                RateBet rb = LfsUtil.getRateBet(rateFc, mr.getScoreRet());
                matSum.addBetRet(rb);
                matSum.addTeamBetResult(TeamType.HOST, mr.getRateType(TeamType.HOST), rb);
                matSum.addTeamBetResult(TeamType.MID, mr.getRateType(TeamType.MID), rb);
                matSum.addTeamBetResult(TeamType.GUEST, mr.getRateType(TeamType.GUEST), rb);
            } else {
                logger.warn("Empty company rate map, " + mr);
            }
        }
    }

    private MatchRateWy parse(String filepath) {
        logger.debug("filepath: " + filepath);
        MatchRateWy matRate = new MatchRateWy();
        matRate.setFilepath(filepath);

        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            String line = null;
            Pattern pat = Pattern.compile("\t");
            boolean avgLine = false;
            boolean lastSegBegin = false;
            String lastComp = null;
            while ((line = iter.nextLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("中")) {
                    continue;
                }

                if (line.startsWith("胜")) {
                    lastSegBegin = true;
                    continue;
                }

                String[] strs = pat.split(line);
                if (lastSegBegin) {

                    if (line.startsWith("平均值")) {
                        float win = Float.parseFloat(strs[1].trim());
                        float draw = Float.parseFloat(strs[2].trim());
                        float lose = Float.parseFloat(strs[3].trim());

                        matRate.addRate(win, draw, lose);
                        avgLine = true;
                    } else {
                        if (avgLine) {
                            float winEnd = Float.parseFloat(strs[0].trim());
                            float drawEnd = Float.parseFloat(strs[1].trim());
                            float loseEnd = Float.parseFloat(strs[2].trim());

                            matRate.addEndRate(winEnd, drawEnd, loseEnd);
                            avgLine = false;
                        }
                    }

                    continue;
                }

                try {
                    if (strs.length == 13) {
                        int id = Integer.parseInt(strs[0].trim());
                        String comp = CompanyMgr.getName(strs[1].trim());
                        float win = Float.parseFloat(strs[2].trim());
                        float draw = Float.parseFloat(strs[3].trim());
                        float lose = Float.parseFloat(strs[4].trim());

                        Rate rate = new Rate(id, comp);
                        rate.addRate(win, draw, lose);
                        matRate.addComRate(rate);

                        lastComp = comp;

                    } else if (strs.length == 10) {
                        if (lastComp == null) {
                            logger.warn("lastComp is null.");
                            continue;
                        }
                        float winEnd = Float.parseFloat(strs[0].trim());
                        float drawEnd = Float.parseFloat(strs[1].trim());
                        float loseEnd = Float.parseFloat(strs[2].trim());
                        matRate.addComEndRate(lastComp, winEnd, drawEnd, loseEnd);
                    } else {
                        logger.error("filepath: " + filepath + ", line: " + line);
                        lastComp = null;
                        continue;
                    }
                } catch (Exception e) {
                    logger.error("len: " + strs.length + ", filepath: " + filepath + ",  line: " + line);
                    logger.error("Error: " + e);
                    lastComp = null;
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }

        return matRate;
    }
}
