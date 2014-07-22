package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.an.lfs.vo.Category;
import com.an.lfs.vo.ClaimRateSummary;
import com.an.lfs.vo.Country;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.ScoreResult;

@Configuration
@ComponentScan
public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);

    private String STATIS_HEADER = "CATEGORY,NUM,PER,PASS_NUM,FAIL_NUM,PASS_PER,FAIL_PER\n";
    private String MATCH_HEADER = "CATEGORY,NUM,PER\n";
    // Key -> Match, get from ger_2013.txt
    public Map<String, Match> matchMap = new HashMap<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    public List<String> rateKeyList = new ArrayList<>();
    //
    // claimRateKey -> ClaimRateSummary
    public Map<String, ClaimRateSummary> rateSummaries = new HashMap<String, ClaimRateSummary>();
    // name -> Category
    private Map<String, Category> categoryMap = new HashMap<>();
    // 1.2 -> Category
    private Map<String, Category> hostCategoryMap = new HashMap<>();
    // 1.2 -> Category
    private Map<String, Category> guestCategoryMap = new HashMap<>();

    // Arguments
    public static final int year = 2013;
    public static final Country country = Country.ENG;

    public static void main(String[] args) {
        LfsMain app = new LfsMain();
        try {
            app.init();
            app.execute();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void execute() throws IOException {
        logger.info("Parse match.");
        String dirName = LfsUtil.getMatchDirName(country, year);
        MatchParser.parse(country, year, dirName, matchMap, rateKeyList);

        logger.info("Parse claim rate.");
        for (String key : rateKeyList) {
            ClaimRateSummary sum = ClaimRateParser.parse(dirName, key);
            sum.initCategory();
            ScoreResult score = matchMap.get(key).getScoreResult();
            sum.initPass(score);
            sum.initPassOdds(score);
            sum.initRateResult(CompanyMgr.ODDSET);
            sum.initRateResult(CompanyMgr.WILLIAM_HILL);
            sum.initRateResult(CompanyMgr.LIBO);
            logger.debug(sum);
            rateSummaries.put(key, sum);

            initCategoryMap(sum);
        }

        logger.info("Generate reports.");
        exportSummary(dirName);
        exportStatis(dirName);

        // logger.info("Create claim rate files.");
        // createClaimRateFiles();
    }

    private void initCategoryMap(ClaimRateSummary sum) {
        String cat = sum.getCategory();
        if (!categoryMap.containsKey(cat)) {
            categoryMap.put(cat, new Category(cat));
        }
        String hostCat = sum.getHostCategory();
        if (!hostCategoryMap.containsKey(hostCat)) {
            hostCategoryMap.put(hostCat, new Category(hostCat));
        }
        String guestCat = sum.getGuestCategory();
        if (!guestCategoryMap.containsKey(guestCat)) {
            guestCategoryMap.put(guestCat, new Category(guestCat));
        }

        if (sum.isPassStart()) {
            categoryMap.get(cat).addPassNum();
            hostCategoryMap.get(hostCat).addPassNum();
            guestCategoryMap.get(guestCat).addPassNum();
        } else {
            categoryMap.get(cat).addFailNum();
            hostCategoryMap.get(hostCat).addFailNum();
            guestCategoryMap.get(guestCat).addFailNum();
        }
    }

    private void exportStatis(String outputFile) throws IOException {
        List<String> cats = new ArrayList<>();
        cats.addAll(categoryMap.keySet());

        int total = rateKeyList.size();

        StringBuilder content = new StringBuilder();

        // Host oriented category statis
        content.append(STATIS_HEADER);
        Collections.sort(cats, firstSegCompare);
        for (String name : cats) {
            Category cat = categoryMap.get(name);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";

            appendLine(content, name, passNum, failNum, per, passPer, failPer);
        }

        // Host oriented category statis
        content.append(",,,,,,\n");
        content.append(STATIS_HEADER);
        List<String> hostCats = new ArrayList<>();
        hostCats.addAll(hostCategoryMap.keySet());
        Collections.sort(hostCats, floatCompare);
        for (String hostCat : hostCats) {
            Category cat = hostCategoryMap.get(hostCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";
            appendLine(content, hostCat, passNum, failNum, per, passPer, failPer);
        }

        // Guest oriented category statis
        content.append(",,,,,,\n");
        content.append(STATIS_HEADER);
        Collections.sort(cats, lastSegCompare);
        for (String name : cats) {
            Category cat = categoryMap.get(name);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";
            appendLine(content, name, passNum, failNum, per, passPer, failPer);
        }

        // Guest oriented category statis
        content.append(",,,,,,\n");
        content.append(STATIS_HEADER);
        List<String> guestCats = new ArrayList<>();
        guestCats.addAll(guestCategoryMap.keySet());
        Collections.sort(guestCats, floatCompare);
        for (String guestCat : guestCats) {
            Category cat = guestCategoryMap.get(guestCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";
            appendLine(content, guestCat, passNum, failNum, per, passPer, failPer);
        }

        // All match statis
        int winNum = 0;
        int drawNum = 0;
        int loseNum = 0;
        for (Match mat : matchMap.values()) {
            if (mat.getScoreResult().isWin()) {
                winNum++;
            } else if (mat.getScoreResult().isDraw()) {
                drawNum++;
            } else {
                loseNum++;
            }
        }
        int winPer = (int) (100 * (float) winNum / (float) total);
        int drawPer = (int) (100 * (float) drawNum / (float) total);
        int losePer = (int) (100 * (float) loseNum / (float) total);
        content.append(",,,,,,\n");
        content.append(MATCH_HEADER);
        content.append("W").append(",").append(winNum).append(",").append(winPer).append("\n");
        content.append("D").append(",").append(drawNum).append(",").append(drawPer).append("\n");
        content.append("L").append(",").append(loseNum).append(",").append(losePer).append("\n");

        FileLineIterator.writeFile(outputFile + "_statis.csv", content.toString());
    }

    private void appendLine(StringBuilder content, String name, int passNum, int failNum, String per, String passPer,
            String failPer) {
        content.append(name);
        content.append(LfsConst.COMMA).append(passNum + failNum);
        content.append(LfsConst.COMMA).append(per);
        content.append(LfsConst.COMMA).append(passNum);
        content.append(LfsConst.COMMA).append(failNum);
        content.append(LfsConst.COMMA).append(passPer);
        content.append(LfsConst.COMMA).append(failPer);
        content.append(LfsConst.NEXT_LINE);
    }

    private int getFailPer(int passNum, int failNum) {
        return (int) (100 * (float) failNum / (float) (passNum + failNum));
    }

    private int getPassPer(int passNum, int failNum) {
        return (int) (100 * (float) passNum / (float) (passNum + failNum));
    }

    private int getPercent(int total, int passNum, int failNum) {
        return (int) (100 * (float) (passNum + failNum) / (float) total);
    }

    private String SUM_HEADER = "KEY,HOST,GUEST,SCORE,RESULT,RATE_AVG,CATEGORY,PASS,Oddset,PASS,William,Libo\n";

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
            content.append(LfsConst.COMMA).append(rate.getCategory());
            content.append(LfsConst.COMMA).append(rate.getPassResult());
            content.append(LfsConst.COMMA).append(rate.getRateString(CompanyMgr.ODDSET));
            content.append(LfsConst.COMMA).append(rate.getPassResultOdds());
            content.append(LfsConst.COMMA).append(rate.getRateString(CompanyMgr.WILLIAM_HILL));
            content.append(LfsConst.COMMA).append(rate.getRateString(CompanyMgr.LIBO));
            content.append(LfsConst.NEXT_LINE);
        }

        FileLineIterator.writeFile(outputFile + "_sum.csv", content.toString());
    }

    private void init() {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        File f = new File(filepath);
        try {
            PropertyConfigurator.configure(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            logger.error("Error: " + e);
        }
    }

    private Comparator<String> floatCompare = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Float.compare(Float.parseFloat(o1), Float.parseFloat(o2));
        }
    };

    private Comparator<String> firstSegCompare = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            String[] str1 = o1.trim().split(LfsConst.SEPARATOR);
            String[] str2 = o2.trim().split(LfsConst.SEPARATOR);
            int ret = Float.compare(Float.parseFloat(str1[0]), Float.parseFloat(str2[0]));
            if (ret != 0) {
                return ret;
            }
            ret = Float.compare(Float.parseFloat(str1[1]), Float.parseFloat(str2[1]));
            if (ret != 0) {
                return ret;
            }
            return Float.compare(Float.parseFloat(str1[2]), Float.parseFloat(str2[2]));
        }
    };

    private Comparator<String> lastSegCompare = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            String[] str1 = o1.trim().split(LfsConst.SEPARATOR);
            String[] str2 = o2.trim().split(LfsConst.SEPARATOR);
            int ret = Float.compare(Float.parseFloat(str1[2]), Float.parseFloat(str2[2]));
            if (ret != 0) {
                return ret;
            }
            ret = Float.compare(Float.parseFloat(str1[1]), Float.parseFloat(str2[1]));
            if (ret != 0) {
                return ret;
            }
            return Float.compare(Float.parseFloat(str1[0]), Float.parseFloat(str2[0]));
        }
    };

    // Create claim rate files
    public void createClaimRateFiles() throws IOException {
        for (String key : rateKeyList) {
            String filename = key + ".txt";
            FileLineIterator.writeFile(filename, "中");
        }
    }
}
