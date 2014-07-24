package com.an.lfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.Match;
import com.an.lfs.vo.MatchCategory;

public class SimpleAnalyzer implements Analyzer {
    private static final Log logger = LogFactory.getLog(SimpleAnalyzer.class);
    // year_index_host_guest -> Match, get from match.txt
    public Map<String, Match> matchMap = new HashMap<>();

    private MatchCategory matchCategory = new MatchCategory();

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
            matchCategory.addCategory(hostCat, guestCat, middleCat, mat.getBetResult());
        }

        logger.info("Generate reports.");
        String filepath = LfsUtil.getStatisFilepath(dirName);
        logger.debug("filepath: " + filepath);

        LfsUtil.exportStatis(filepath, matchCategory, matchMap.values());
    }

    @Override
    public void generateRateFiles(String country, int year) throws IOException {
        throw new NotImplementedException("Not supported");
    }
}
