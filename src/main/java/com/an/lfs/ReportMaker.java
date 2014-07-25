package com.an.lfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Category;
import com.an.lfs.vo.ClaimRateSummary;
import com.an.lfs.vo.LfsConfMgr;
import com.an.lfs.vo.Match;

public class ReportMaker extends RateAnalyzer {
    private static final Log logger = LogFactory.getLog(RateAnalyzer.class);

    /**
     * @param country
     * @param year
     */
    public ReportMaker(String country, int year) {
        super(country, year);
    }

    @Override
    public void exportStatis() throws IOException {

        String filename = LfsUtil.getStatisFile(country, year);
        String filepath = LfsUtil.getOutputFilePath(filename);
        logger.info("Generate statis file " + filepath);
        int total = matchMap.keySet().size();

        int winNum = 0;
        int drawNum = 0;
        int loseNum = 0;
        for (Match mat : matchMap.values()) {
            if (mat.isWin()) {
                winNum++;
            } else if (mat.isDraw()) {
                drawNum++;
            } else {
                loseNum++;
            }
        }
        String winPer = (int) (100 * (float) winNum / (float) total) + "%";
        String drawPer = (int) (100 * (float) drawNum / (float) total) + "%";
        String losePer = (int) (100 * (float) loseNum / (float) total) + "%";

        StringBuilder content = new StringBuilder();
        content.append(LfsUtil.MATCH_HEADER);
        content.append(LfsUtil.WIN).append(",").append(winNum).append(",").append(winPer).append("\n");
        content.append(LfsUtil.DRAW).append(",").append(drawNum).append(",").append(drawPer).append("\n");
        content.append(LfsUtil.LOSE).append(",").append(loseNum).append(",").append(losePer).append("\n");

        // Host oriented category
        content.append(",,,,,,\n");
        content.append("Host,,,,,,\n");
        content.append(LfsUtil.STATIS_HEADER);
        List<String> hostCats = new ArrayList<>();
        Map<String, Category> hostCatMap = matchCategory.getHostCatMap();
        hostCats.addAll(hostCatMap.keySet());
        Collections.sort(hostCats, LfsUtil.floatCompare);

        for (String hostCat : hostCats) {
            Category cat = hostCatMap.get(hostCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = LfsUtil.getPercent(total, passNum, failNum) + "%";
            String passPer = LfsUtil.getPassPer(passNum, failNum) + "%";
            String failPer = LfsUtil.getFailPer(passNum, failNum) + "%";
            appendLine(content, hostCat, passNum, failNum, per, passPer, failPer);
        }

        // Guest oriented category
        content.append(",,,,,,\n");
        content.append("Guest,,,,,,\n");
        content.append(LfsUtil.STATIS_HEADER);
        List<String> guestCats = new ArrayList<>();
        Map<String, Category> guestCatMap = matchCategory.getGuestCatMap();
        guestCats.addAll(guestCatMap.keySet());
        Collections.sort(guestCats, LfsUtil.floatCompare);

        for (String guestCat : guestCats) {
            Category cat = guestCatMap.get(guestCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = LfsUtil.getPercent(total, passNum, failNum) + "%";
            String passPer = LfsUtil.getPassPer(passNum, failNum) + "%";
            String failPer = LfsUtil.getFailPer(passNum, failNum) + "%";
            appendLine(content, guestCat, passNum, failNum, per, passPer, failPer);
        }

        // Middle category
        content.append(",,,,,,\n");
        content.append("Middle,,,,,,\n");
        content.append(LfsUtil.STATIS_HEADER);
        List<String> middleCats = new ArrayList<>();
        Map<String, Category> middleCatMap = matchCategory.getMiddleCatMap();
        middleCats.addAll(middleCatMap.keySet());
        Collections.sort(middleCats, LfsUtil.floatCompare);

        for (String middleCat : middleCats) {
            Category cat = middleCatMap.get(middleCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = LfsUtil.getPercent(total, passNum, failNum) + "%";
            String passPer = LfsUtil.getPassPer(passNum, failNum) + "%";
            String failPer = LfsUtil.getFailPer(passNum, failNum) + "%";
            appendLine(content, middleCat, passNum, failNum, per, passPer, failPer);
        }

        FileLineIterator.writeFile(filepath, content.toString());
    }

    private static void appendLine(StringBuilder content, String name, int passNum, int failNum, String per,
            String passPer, String failPer) {
        content.append(name);
        content.append(LfsUtil.COMMA).append(passNum + failNum);
        content.append(LfsUtil.COMMA).append(per);
        content.append(LfsUtil.COMMA).append(passNum);
        content.append(LfsUtil.COMMA).append(failNum);
        content.append(LfsUtil.COMMA).append(passPer);
        content.append(LfsUtil.COMMA).append(failPer);
        content.append(LfsUtil.NEXT_LINE);
    }

    @Override
    public void exportSummary() throws IOException {
        String filename = LfsUtil.getSumFile(country, year);
        String filepath = LfsUtil.getOutputFilePath(filename);
        logger.info("Generate summary file " + filepath);
        StringBuilder content = new StringBuilder();
        content.append("KEY,H,G,S,R,H,G,M,START RATE,END,B");

        List<String> comps = LfsConfMgr.getCompany(country);
        for (String comp : comps) {
            content.append(LfsUtil.COMMA).append(comp);
            content.append(LfsUtil.COMMA).append("END");
            content.append(LfsUtil.COMMA).append("B");
        }
        content.append(LfsUtil.NEXT_LINE);

        for (String key : rateKeyList) {
            Match mat = matchMap.get(key);
            ClaimRateSummary sum = rateSummaries.get(key);
            if (sum == null) {
                logger.warn("Rate summary is null, " + key);
                continue;
            }
            content.append(mat.getSimpleKey());
            content.append(LfsUtil.COMMA).append(mat.getHost());
            content.append(LfsUtil.COMMA).append(mat.getGuest());
            content.append(LfsUtil.COMMA).append(" " + mat.getScore());
            content.append(LfsUtil.COMMA).append(matchMap.get(key).getMatchResultStr());

            content.append(LfsUtil.COMMA).append(sum.getHostCat());
            content.append(LfsUtil.COMMA).append(sum.getGuestCat());
            content.append(LfsUtil.COMMA).append(sum.getMiddleCat());

            content.append(LfsUtil.COMMA).append(sum.getRate());
            content.append(LfsUtil.COMMA).append(sum.getEndRate());
            content.append(LfsUtil.COMMA).append(LfsUtil.getBetStr(sum.getBetResult()));

            for (String comp : comps) {
                content.append(LfsUtil.COMMA).append(sum.getRateStr(comp));
                content.append(LfsUtil.COMMA).append(sum.getEndRateStr(comp));
                content.append(LfsUtil.COMMA).append(LfsUtil.getBetStr(sum.getBetResult(comp)));
            }
            content.append(LfsUtil.NEXT_LINE);
        }

        FileLineIterator.writeFile(filepath, content.toString());
    }
}
