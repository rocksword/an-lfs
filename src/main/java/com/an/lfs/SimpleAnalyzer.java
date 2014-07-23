package com.an.lfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public void analyze(String country, int year) throws IOException {
        logger.info("Parse match.");

        String dirName = LfsUtil.getMatchDirName(country, year);
        MatchParser.parse(country, year, dirName, matchMap, null);
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

            if (mat.isPass()) {
                hostCatMap.get(hostCat).addPassNum();
                guestCatMap.get(guestCat).addPassNum();
                middleCatMap.get(middleCat).addPassNum();
            } else {
                hostCatMap.get(hostCat).addFailNum();
                guestCatMap.get(guestCat).addFailNum();
                middleCatMap.get(middleCat).addFailNum();
            }
        }

        logger.info("Generate reports.");
        String filepath = LfsUtil.getStatisFilepath(dirName);
        LfsUtil.exportStatis(filepath, hostCatMap, guestCatMap, middleCatMap, matchMap.values());
    }
}
