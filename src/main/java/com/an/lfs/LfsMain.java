package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.an.lfs.vo.ClaimRateSummary;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.ScoreResult;

@Configuration
@ComponentScan
public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);

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

    private void startApp() {
        logger.info("Starting application.");

        MatchParser mParser = new MatchParser();
        mParser.parse("2013", matches, claimRateKeys);
        logger.debug(claimRateKeys);
        logger.debug(matches);

        for (Match mat : matches) {
            mat.initScoreResult();
            matchMap.put(mat.getKey(), mat);
        }

        for (String key : claimRateKeys) {
            ClaimRateSummary sum = AppContextFactory.getClaimRateParser().parse(key);
            ScoreResult score = matchMap.get(key).getScoreResult();
            sum.initPass(score);
            sum.initPassOdds(score);
            sum.initRateResult(CompanyMgr.ODDSET);
            sum.initRateResult(CompanyMgr.WILLIAM_HILL);
            sum.initRateResult(CompanyMgr.LIBO);
            rateSummaries.put(key, sum);
        }

        StringBuilder content = new StringBuilder();
        content.append("KEY,HOST,GUEST,SCORE,RATE_AVG,PASS(S->E),ODDS,PASS,WilliamHill,Libo\n");
        for (String key : claimRateKeys) {
            Match mat = matchMap.get(key);
            ClaimRateSummary rate = rateSummaries.get(key);
            content.append(mat.getSimpleKey());
            content.append(",").append(mat.getHost());
            content.append(",").append(mat.getGuest());
            content.append(",").append(" " + mat.getScore());
            content.append(",").append(rate.getRateAvg());
            content.append(",").append(rate.getPassResult());
            content.append(",").append(rate.getRateString(CompanyMgr.ODDSET));
            content.append(",").append(rate.getPassResultOdds());
            content.append(",").append(rate.getRateString(CompanyMgr.WILLIAM_HILL));
            content.append(",").append(rate.getRateString(CompanyMgr.LIBO));
            content.append("\n");
        }
        FileLineIterator.createFile("2013.csv");
        FileLineIterator.writeFile("2013.csv", content.toString());
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
