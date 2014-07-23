package com.an.lfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.Category;
import com.an.lfs.vo.Match;

public class SimpleAnalyzer implements Analyzer {
    private static final Log logger = LogFactory.getLog(SimpleAnalyzer.class);
    // year_index_host_guest -> Match, get from match.txt
    public Map<String, Match> matchMap = new HashMap<>();

    // 1.2 -> Category
    private Map<String, Category> hostCatMap = new HashMap<>();
    // 1.2 -> Category
    private Map<String, Category> guestCatMap = new HashMap<>();
    // 1.2 -> Category
    private Map<String, Category> middleCatMap = new HashMap<>();

    @Override
    public void exportReport(String country, int year) throws IOException {
        logger.info("Parse match.");

        String dirName = LfsUtil.getMatchDirName(country, year);
        logger.debug("dirName: " + dirName);

        MatchParser.parse(country, year, dirName, matchMap, null);
        logger.debug("matchMap size: " + matchMap.size());

        for (Match mat : matchMap.values()) {
            String hostCat = mat.getHostCat();
            String guestCat = mat.getGuestCat();
            String middleCat = mat.getMiddleCat();
            if (!hostCatMap.containsKey(hostCat)) {
                hostCatMap.put(hostCat, new Category(hostCat));
            }
            if (!guestCatMap.containsKey(guestCat)) {
                guestCatMap.put(guestCat, new Category(guestCat));
            }
            if (!middleCatMap.containsKey(middleCat)) {
                middleCatMap.put(middleCat, new Category(middleCat));
            }

            int pass = mat.getBetResult();
            if (pass == 1) {
                hostCatMap.get(hostCat).addPassNum();
                guestCatMap.get(guestCat).addPassNum();
                middleCatMap.get(middleCat).addPassNum();
            } else if (pass == 0) {
                hostCatMap.get(hostCat).addFailNum();
                guestCatMap.get(guestCat).addFailNum();
                middleCatMap.get(middleCat).addFailNum();
            } else {
                logger.debug("Ignore: " + mat);
            }
        }

        logger.info("Generate reports.");
        String filepath = LfsUtil.getStatisFilepath(dirName);
        logger.debug("filepath: " + filepath);

        LfsUtil.exportStatis(filepath, hostCatMap, guestCatMap, middleCatMap, matchMap.values());
    }

    @Override
    public void generateRateFiles(String country, int year) throws IOException {
        throw new NotImplementedException("Not supported");
    }
}
