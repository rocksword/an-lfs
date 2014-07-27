package com.an.lfs;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.BetRet;
import com.an.lfs.enu.TeamType;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Rate;
import com.an.lfs.vo.CompanyMgr;
import com.an.lfs.vo.MatchSummary;
import com.an.lfs.vo.MatchRate;

public abstract class RateAnalyzer extends MatchAnalyzer {
    private static final Log logger = LogFactory.getLog(RateAnalyzer.class);
    protected MatchSummary matSum = new MatchSummary();
    // rateKey -> RateSummary
    protected Map<String, MatchRate> matRateMap = new HashMap<String, MatchRate>();

    /**
     * @param country
     * @param year
     */
    public RateAnalyzer(String country, int year) {
        super(country, year);
    }

    public void analyzeRate() {
        for (String matchKey : super.matchMap.keySet()) {
            String filepath = LfsUtil.getInputFilePath(country, year, matchKey + ".txt");
            logger.debug("filepath: " + filepath);
            MatchRate matRate = parse(filepath);
            matRate.setScoreType(super.getMatch(matchKey).getScoreType());

            if (!matRate.getCompanyRateMap().isEmpty()) {
                matRateMap.put(matchKey, matRate);

                matSum.addForecastScoreRet(matRate.getFcRet(), matchMap.get(matchKey).getScoreType());
                matSum.addBetRet(matRate.getBetRet());

                BetRet betRet = matRate.getBetRet();
                matSum.addTeamBetResult(TeamType.HOST, matRate.getRateType(TeamType.HOST), betRet);
                matSum.addTeamBetResult(TeamType.MID, matRate.getRateType(TeamType.MID), betRet);
                matSum.addTeamBetResult(TeamType.GUEST, matRate.getRateType(TeamType.GUEST), betRet);
            } else {
                logger.warn("Empty company rate map, " + matRate);
            }
        }
    }

    private MatchRate parse(String filepath) {
        logger.debug("filepath: " + filepath);
        MatchRate matRate = new MatchRate();
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
