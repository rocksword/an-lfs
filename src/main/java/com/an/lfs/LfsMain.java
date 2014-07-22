package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public static void main(String[] args) {
        LfsMain app = new LfsMain();

        try {
            init();
            app.startApp();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // 2013.txt
    public List<Match> matches = new ArrayList<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    public List<String> claimRateKeys = new ArrayList<>();
    //
    public Map<String, Match> matchMap = new HashMap<>();
    // claimRateKey -> ClaimRateSummary
    public Map<String, ClaimRateSummary> rateSummaries = new HashMap<String, ClaimRateSummary>();

    // name -> Category
    private Map<String, Category> categoryCount = new HashMap<>();

    private void startApp() {
        logger.info("Starting application.");

        String year = ARGUMENT.substring((ARGUMENT.length() - 4), ARGUMENT.length());
        MatchParser.parse(year, matches, claimRateKeys);
        logger.debug(claimRateKeys);
        logger.debug(matches);

        for (Match mat : matches) {
            mat.initScoreResult();
            matchMap.put(mat.getKey(), mat);
        }

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

            String category = sum.getCategory();
            if (!categoryCount.containsKey(category)) {
                categoryCount.put(category, new Category());
            }

            if (sum.isPassStart()) {
                categoryCount.get(category).addPassNum();
            } else {
                categoryCount.get(category).addFailNum();
            }
        }

        exportFile();
        exportStatis();
    }

    private void exportStatis() {
        List<String> cates = new ArrayList<>();
        cates.addAll(categoryCount.keySet());
        Collections.sort(cates, cmp);
        int total = claimRateKeys.size();

        StringBuilder content = new StringBuilder();
        content.append("CATEGORY,NUM,PERCENT,P_NUM,F_NUM,P_PERCENT,F_PERCENT\n");
        for (String name : cates) {
            Category cate = categoryCount.get(name);
            int passNum = cate.getPassNum();
            int failNum = cate.getFailNum();
            int per = (int) (100 * (float) (passNum + failNum) / (float) total);
            int passPer = (int) (100 * (float) passNum / (float) (passNum + failNum));
            int failPer = (int) (100 * (float) failNum / (float) (passNum + failNum));
            content.append(name);
            content.append(",").append(passNum + failNum);
            content.append(",").append(per);
            content.append(",").append(passNum);
            content.append(",").append(failNum);
            content.append(",").append(passPer);
            content.append(",").append(failPer);
            content.append("\n");
        }
        FileLineIterator.createFile("ger_2013_statis.csv");
        FileLineIterator.writeFile("ger_2013_statis.csv", content.toString());
    }

    private Comparator<String> cmp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            String[] str1 = o1.trim().split("-");
            String[] str2 = o2.trim().split("-");
            int ret = Float.compare(Float.parseFloat(str1[0]), Float.parseFloat(str2[0]));
            if (ret == 0) {
                ret = Float.compare(Float.parseFloat(str1[1]), Float.parseFloat(str2[1]));
                if (ret == 0) {
                    ret = Float.compare(Float.parseFloat(str1[2]), Float.parseFloat(str2[2]));
                    return ret;
                } else {
                    return ret;
                }
            } else {
                return ret;
            }
        }
    };

    private void exportFile() {
        StringBuilder content = new StringBuilder();
        content.append("KEY,HOST,GUEST,SCORE,RESULT,RATE_AVG,CATEGORY,PASS,Oddset,PASS,William,Libo\n");
        for (String key : claimRateKeys) {
            Match mat = matchMap.get(key);
            ClaimRateSummary rate = rateSummaries.get(key);
            content.append(mat.getSimpleKey());
            content.append(",").append(mat.getHost());
            content.append(",").append(mat.getGuest());
            content.append(",").append(" " + mat.getScore());
            content.append(",").append(matchMap.get(key).getScoreResultStr());
            content.append(",").append(rate.getRateAvg());
            content.append(",").append(rate.getCategory());
            content.append(",").append(rate.getPassResult());
            content.append(",").append(rate.getRateString(CompanyMgr.ODDSET));
            content.append(",").append(rate.getPassResultOdds());
            content.append(",").append(rate.getRateString(CompanyMgr.WILLIAM_HILL));
            content.append(",").append(rate.getRateString(CompanyMgr.LIBO));
            content.append("\n");
        }
        FileLineIterator.createFile("ger_2013.csv");
        FileLineIterator.writeFile("ger_2013.csv", content.toString());
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        File f = new File(filepath);
        try {
            PropertyConfigurator.configure(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            logger.error("Error: " + e);
        }

        AppContextFactory.init();
    }

    // Create claim rate files
    public void createClaimRateFiles() {
        for (String key : claimRateKeys) {
            String filename = key + ".txt";
            FileLineIterator.writeFile(filename, "中");
        }
    }
}
