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
            mat.initPassBet();
            matchMap.put(mat.getKey(), mat);
        }

        for (String key : claimRateKeys) {
            ClaimRateSummary sum = ClaimRateParser.parse(key);
            if (sum != null) {
                Match mat = matchMap.get(key);
                sum.setValues(mat.isMinWin(), mat.isMinDraw(), mat.isMinLose());
                sum.initCompareAvgAndTrend();
                sum.initOddsPass(mat.getScoreResult());

                rateSummaries.put(key, sum);
                logger.debug(key + ", " + sum);
            } else {
                logger.error("ClaimRateSummary is null.");
            }
        }

        StringBuilder content = new StringBuilder();
        content.append("KEY,SCORE,MAT_PASS,MAT_AVG,RATE_AVG,ODDS,ODDS_PASS,ODDS_END,ODDS_AVG_W,ODDS_AVG_D,ODDS_AVG_L,ODDS_PASS,TREND_W,TREND_D,TREND_L\n");
        for (String key : claimRateKeys) {
            Match mat = matchMap.get(key);
            ClaimRateSummary rate = rateSummaries.get(key);
            content.append(key);
            content.append(",").append(" " + mat.getScore());
            content.append(",").append(mat.getMatPass());
            content.append(",").append(mat.getMatAvg());

            content.append(",").append(rate.getRateAvg());
            content.append(",").append(rate.getOdds());
            content.append(",").append(rate.getOddsPass());
            content.append(",").append(rate.getOddsEnd());

            for (String val : rate.getCompareAvg()) {
                content.append(",").append(val);
            }
            for (String val : rate.getTrendStartEnd()) {
                content.append(",").append(val);
            }
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
