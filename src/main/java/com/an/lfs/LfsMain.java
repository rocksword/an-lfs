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
            logger.error("Error: " + e);
            System.exit(1);
        }

    }

    // 2013.txt
    public List<Match> matches = new ArrayList<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    public List<String> claimRateKeys = new ArrayList<>();
    //
    public Map<String, Boolean> passBets = new HashMap<>();
    // claimRateKey -> ClaimRateSummary
    public Map<String, ClaimRateSummary> rateSummaries = new HashMap<String, ClaimRateSummary>();

    private void startApp() {
        logger.info("Starting application.");

        MatchParser mParser = new MatchParser();
        mParser.parse("2013", matches, claimRateKeys);
        logger.debug(claimRateKeys);
        logger.debug(matches);

        for (Match mat : matches) {
            System.out.println(mat.getKey() + " -> " + mat.isPassBet());
            passBets.put(mat.getKey(), mat.isPassBet());
        }

        for (String key : claimRateKeys) {
            ClaimRateSummary sum = ClaimRateParser.parse(key);
            if (sum != null) {
                sum.setPassBet(passBets.get(key));
                rateSummaries.put(key, sum);
                logger.debug(key + ", " + sum);
            }
        }
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
            FileLineIterator.writeFile(filename);
        }
    }
}
