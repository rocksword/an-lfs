package com.an.lfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Category;
import com.an.lfs.vo.ClaimRateSummary;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.ScoreResult;

public class CompoundAnalyzer implements Analyzer {
    private static final Log logger = LogFactory.getLog(CompoundAnalyzer.class);
    private String SUM_HEADER = "KEY,HOST,GUEST,SCORE,RESULT,RATE_AVG,HOST_CAT,GUEST_CAT,PASS,Oddset,PASS,William,Libo\n";
    // Key -> Match, get from match.txt
    public Map<String, Match> matchMap = new HashMap<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    public List<String> rateKeyList = new ArrayList<>();
    //
    // claimRateKey -> ClaimRateSummary
    public Map<String, ClaimRateSummary> rateSummaries = new HashMap<String, ClaimRateSummary>();
    // 1.2 -> Category
    private Map<String, Category> hostCatMap = new HashMap<>();
    // 1.2 -> Category
    private Map<String, Category> guestCatMap = new HashMap<>();
    //
    private Map<String, Category> middleCatMap = new HashMap<>();

    @Override
    public void analyze(String country, int year) throws IOException {
        logger.info("Parse match.");
        String dirName = LfsUtil.getMatchDirName(country, year);
        MatchParser.parse(country, year, dirName, matchMap, rateKeyList);

        logger.info("Parse claim rate.");
        for (String key : rateKeyList) {
            ClaimRateSummary sum = ClaimRateParser.parse(dirName, key);
            ScoreResult score = matchMap.get(key).getScoreResult();
            sum.initPass(score);
            sum.initPassOdds(score);
            logger.debug(sum);
            rateSummaries.put(key, sum);

            initCategoryMap(sum);
        }

        logger.info("Generate reports.");
        exportSummary(dirName);
        String filepath = LfsUtil.getStatisFilepath(dirName);
        LfsUtil.exportStatis(filepath, hostCatMap, guestCatMap, middleCatMap, matchMap.values());
        // logger.info("Create claim rate files.");
        // createClaimRateFiles();
    }

    private void initCategoryMap(ClaimRateSummary sum) {
        String hostCat = sum.getHostCat();
        String guestCat = sum.getGuestCat();
        String middleCat = sum.getMiddleCat();
        if (!hostCatMap.containsKey(hostCat)) {
            hostCatMap.put(hostCat, new Category(hostCat));
            guestCatMap.put(guestCat, new Category(guestCat));
            middleCatMap.put(middleCat, new Category(middleCat));
        }

        if (sum.isPassStart()) {
            hostCatMap.get(hostCat).addPassNum();
            guestCatMap.get(guestCat).addPassNum();
            middleCatMap.get(middleCat).addPassNum();
        } else {
            hostCatMap.get(hostCat).addFailNum();
            guestCatMap.get(guestCat).addFailNum();
            middleCatMap.get(middleCat).addFailNum();
        }
    }

    private void exportSummary(String outputFile) throws IOException {
        StringBuilder content = new StringBuilder();
        content.append(SUM_HEADER);

        for (String key : rateKeyList) {
            Match mat = matchMap.get(key);
            ClaimRateSummary rate = rateSummaries.get(key);
            content.append(mat.getSimpleKey());
            content.append(LfsConst.COMMA).append(mat.getHost());
            content.append(LfsConst.COMMA).append(mat.getGuest());
            content.append(LfsConst.COMMA).append(" " + mat.getScore());
            content.append(LfsConst.COMMA).append(matchMap.get(key).getScoreResultStr());
            content.append(LfsConst.COMMA).append(rate.getRateAvg());
            content.append(LfsConst.COMMA).append(rate.getHostCat());
            content.append(LfsConst.COMMA).append(rate.getGuestCat());
            content.append(LfsConst.COMMA).append(rate.getPassResult());
            content.append(LfsConst.COMMA).append(rate.getRateString(CompanyMgr.ODDSET));
            content.append(LfsConst.COMMA).append(rate.getPassResultOdds());
            content.append(LfsConst.COMMA).append(rate.getRateString(CompanyMgr.WILLIAM_HILL));
            content.append(LfsConst.COMMA).append(rate.getRateString(CompanyMgr.LIBO));
            content.append(LfsConst.NEXT_LINE);
        }

        FileLineIterator.writeFile(outputFile + "_sum.csv", content.toString());
    }

    // Create claim rate files
    public void createClaimRateFiles() throws IOException {
        for (String key : rateKeyList) {
            String filename = key + ".txt";
            FileLineIterator.writeFile(filename, "中");
        }
    }
}
