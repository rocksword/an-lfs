package com.an.lfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.BetResult;
import com.an.lfs.vo.ClaimRateSummary;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.MatchCategory;

public class CompoundAnalyzer implements Analyzer {
    private static final Log logger = LogFactory.getLog(CompoundAnalyzer.class);
    // Key -> Match, get from match.txt
    public Map<String, Match> matchMap = new HashMap<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    public List<String> rateKeyList = new ArrayList<>();
    //
    // claimRateKey -> ClaimRateSummary
    public Map<String, ClaimRateSummary> rateSummaries = new HashMap<String, ClaimRateSummary>();

    private MatchCategory matchCategory = new MatchCategory();

    @Override
    public void exportReport(String country, int year) throws IOException {
        String dirName = LfsUtil.getMatchDirName(country, year);
        analyzeMatch(dirName, country, year);
        analyzeRate(country, dirName);

        logger.info("Generate reports.");
        exportSummary(country, dirName);
        String filepath = LfsUtil.getStatisFilepath(dirName);
        LfsUtil.exportStatis(filepath, matchCategory, matchMap.values());
    }

    public void generateRateFiles(String country, int year) throws IOException {
        String dirName = LfsUtil.getMatchDirName(country, year);
        analyzeMatch(dirName, country, year);
        analyzeRate(country, dirName);

        logger.info("Create claim rate files.");
        for (String key : rateKeyList) {
            String filename = key + ".txt";
            FileLineIterator.writeFile(filename, "中");
        }
    }

    private void analyzeRate(String country, String dir) {
        logger.info("Parse claim rate.");
        for (String key : rateKeyList) {
            String filename = key + ".txt";
            ClaimRateSummary sum = ClaimRateParser.parse(country, dir, filename);
            if (!sum.getCompanyRateMap().isEmpty()) {
                String score = matchMap.get(key).getScore();
                sum.addScore(score);
                rateSummaries.put(key, sum);
                matchCategory.addCategory(sum.getHostCat(), sum.getGuestCat(), sum.getMiddleCat(), sum.getBetResult());
            } else {
                logger.warn("Empty company rate map, " + sum);
            }
        }
    }

    private void exportSummary(String country, String outputFile) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append("KEY,H,G,S,R,H,G,M,START RATE,END,B");

        List<String> comps = LfsConfMgr.getCompany(country);
        for (String comp : comps) {
            content.append(LfsConst.COMMA).append(comp);
            content.append(LfsConst.COMMA).append("END");
            content.append(LfsConst.COMMA).append("B");
        }
        content.append(LfsConst.NEXT_LINE);

        for (String key : rateKeyList) {
            Match mat = matchMap.get(key);
            ClaimRateSummary sum = rateSummaries.get(key);
            if (sum == null) {
                logger.warn("Rate summary is null, " + key);
                continue;
            }
            content.append(mat.getSimpleKey());
            content.append(LfsConst.COMMA).append(mat.getHost());
            content.append(LfsConst.COMMA).append(mat.getGuest());
            content.append(LfsConst.COMMA).append(" " + mat.getScore());
            content.append(LfsConst.COMMA).append(matchMap.get(key).getMatchResultStr());

            content.append(LfsConst.COMMA).append(sum.getHostCat());
            content.append(LfsConst.COMMA).append(sum.getGuestCat());
            content.append(LfsConst.COMMA).append(sum.getMiddleCat());

            content.append(LfsConst.COMMA).append(sum.getRate());
            content.append(LfsConst.COMMA).append(sum.getEndRate());
            content.append(LfsConst.COMMA).append(LfsUtil.getBetStr(sum.getBetResult()));

            for (String comp : comps) {
                content.append(LfsConst.COMMA).append(sum.getRateStr(comp));
                content.append(LfsConst.COMMA).append(sum.getEndRateStr(comp));
                content.append(LfsConst.COMMA).append(LfsUtil.getBetStr(sum.getBetResult(comp)));
            }
            content.append(LfsConst.NEXT_LINE);
        }

        FileLineIterator.writeFile(outputFile + "_sum.csv", content.toString());
    }

    private void analyzeMatch(String dirName, String country, int year) {
        logger.info("Parse match.");
        MatchParser.parse(country, year, dirName, matchMap, rateKeyList);
    }
}
