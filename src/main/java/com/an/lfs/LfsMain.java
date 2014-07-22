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
import com.an.lfs.vo.Match;
import com.an.lfs.vo.ScoreResult;

@Configuration
@ComponentScan
public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);

    public static String ARGUMENT = "ger_2013";
    // Key -> Match, get from ger_2013.txt
    public Map<String, Match> matchMap = new HashMap<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    public List<String> claimRateKeys = new ArrayList<>();
    //
    // claimRateKey -> ClaimRateSummary
    public Map<String, ClaimRateSummary> rateSummaries = new HashMap<String, ClaimRateSummary>();
    // name -> Category
    private Map<String, Category> categoryMap = new HashMap<>();
    // 1.5 -> Category
    private Map<String, List<Category>> firstSegCategoryMap = new HashMap<>();
    // 1.5 -> Category
    private Map<String, List<Category>> lastSegCategoryMap = new HashMap<>();

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
        String year = ARGUMENT.substring((ARGUMENT.length() - 4), ARGUMENT.length());
        MatchParser.parse(year, matchMap, claimRateKeys);

        logger.info("Parse claim rate.");
        for (String key : claimRateKeys) {
            ClaimRateSummary sum = ClaimRateParser.parse(ARGUMENT, key);
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

        initSegCategoryMap();

        logger.info("Generate reports.");
        exportSummary();
        exportStatis();
    }

    private void initSegCategoryMap() {
        for (Category cat : categoryMap.values()) {
            String fseg = cat.getFirstSeg();
            if (!firstSegCategoryMap.containsKey(fseg)) {
                ArrayList<Category> list = new ArrayList<>();
                list.add(cat);
                firstSegCategoryMap.put(fseg, list);
            }
            firstSegCategoryMap.get(fseg).add(cat);

            String lseg = cat.getLastSeg();
            if (!lastSegCategoryMap.containsKey(lseg)) {
                ArrayList<Category> list = new ArrayList<>();
                list.add(cat);
                lastSegCategoryMap.put(lseg, list);
            }
            lastSegCategoryMap.get(lseg).add(cat);
        }
    }

    private void initCategoryMap(ClaimRateSummary sum) {
        String name = sum.getCategory();
        if (!categoryMap.containsKey(name)) {
            categoryMap.put(name, new Category(name));
        }
        if (sum.isPassStart()) {
            categoryMap.get(name).addPassNum();
        } else {
            categoryMap.get(name).addFailNum();
        }
    }

    private String STATIS_HEADER = "CATEGORY,NUM,PERCENT,P_NUM,F_NUM,P_PERCENT,F_PERCENT\n";
    private String MATCH_HEADER = "CATEGORY,NUM,PERCENT\n";

    private void exportStatis() throws IOException {
        List<String> cates = new ArrayList<>();
        cates.addAll(categoryMap.keySet());

        int total = claimRateKeys.size();
        StringBuilder content = new StringBuilder();

        content.append(STATIS_HEADER);

        Collections.sort(cates, firstSegCompare);
        for (String name : cates) {
            Category cate = categoryMap.get(name);
            int passNum = cate.getPassNum();
            int failNum = cate.getFailNum();
            int per = getPercent(total, passNum, failNum);
            int passPer = getPassPer(passNum, failNum);
            int failPer = getFailPer(passNum, failNum);

            appendLine(content, name, passNum, failNum, per, passPer, failPer);
        }

        content.append(",,,,,,\n");
        content.append(STATIS_HEADER);

        List<String> firstSegs = new ArrayList<>();
        firstSegs.addAll(firstSegCategoryMap.keySet());
        Collections.sort(firstSegs, floatCompare);

        for (String fseg : firstSegs) {
            int passNum = 0;
            int failNum = 0;
            List<Category> list = firstSegCategoryMap.get(fseg);
            for (Category cat : list) {
                passNum += cat.getPassNum();
                failNum += cat.getFailNum();
            }
            int per = getPercent(total, passNum, failNum);
            int passPer = getPassPer(passNum, failNum);
            int failPer = getFailPer(passNum, failNum);

            appendLine(content, fseg, passNum, failNum, per, passPer, failPer);
        }

        content.append(",,,,,,\n");
        content.append(STATIS_HEADER);

        Collections.sort(cates, lastSegCompare);
        for (String name : cates) {
            Category cat = categoryMap.get(name);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            int per = getPercent(total, passNum, failNum);
            int passPer = getPassPer(passNum, failNum);
            int failPer = getFailPer(passNum, failNum);

            appendLine(content, name, passNum, failNum, per, passPer, failPer);
        }

        content.append(",,,,,,\n");
        content.append(STATIS_HEADER);

        List<String> lastSegs = new ArrayList<>();
        lastSegs.addAll(firstSegCategoryMap.keySet());
        Collections.sort(lastSegs, floatCompare);

        for (String lseg : lastSegs) {
            int passNum = 0;
            int failNum = 0;
            List<Category> list = lastSegCategoryMap.get(lseg);
            for (Category cat : list) {
                passNum += cat.getPassNum();
                failNum += cat.getFailNum();
            }
            int per = getPercent(total, passNum, failNum);
            int passPer = getPassPer(passNum, failNum);
            int failPer = getFailPer(passNum, failNum);

            appendLine(content, lseg, passNum, failNum, per, passPer, failPer);
        }

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

        FileLineIterator.writeFile(ARGUMENT + "_statis.csv", content.toString());
    }

    private void appendLine(StringBuilder content, String name, int passNum, int failNum, int per, int passPer,
            int failPer) {
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

    private void exportSummary() throws IOException {
        StringBuilder content = new StringBuilder();
        content.append(SUM_HEADER);

        for (String key : claimRateKeys) {
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

        FileLineIterator.writeFile(ARGUMENT + "_sum.csv", content.toString());
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
        for (String key : claimRateKeys) {
            String filename = key + ".txt";
            FileLineIterator.writeFile(filename, "中");
        }
    }
}
