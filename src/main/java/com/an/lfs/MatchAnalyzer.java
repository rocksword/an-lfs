package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.TeamMgr;

public abstract class MatchAnalyzer implements Analyze {
    private static final Log logger = LogFactory.getLog(MatchAnalyzer.class);
    protected String country;
    protected int year;
    // Key -> Match, get from match.txt
    protected Map<String, Match> matchMap = new HashMap<>();
    // year_index_host_guest: 2013_01_Bai_Men.txt
    protected List<String> rateKeyList = new ArrayList<>();

    /**
     * @param country
     * @param year
     */
    public MatchAnalyzer(String country, int year) {
        this.country = country;
        this.year = year;
    }

    @Override
    public boolean analyzeMatch() {
        String filepath = LfsUtil.getInputFilePath(country, year, LfsUtil.MATCH_FILE);
        File f = new File(filepath);
        if (!f.exists()) {
            logger.debug("Not found " + filepath);
            return false;
        }

        String line = null;
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            Pattern pat = Pattern.compile("\t");
            while ((line = iter.nextLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] splits = pat.split(line.toString());
                if (splits.length < 8) {
                    logger.warn("Invalid line: " + line);
                    continue;
                }
                Match mat = new Match();
                int index = Integer.parseInt(splits[0].trim());
                String time = splits[1].trim();
                String host = TeamMgr.getName(country, splits[2].trim());

                String score = splits[3].trim();
                mat.addScore(score);

                String guest = TeamMgr.getName(country, splits[4].trim());
                mat.setIndex(index);
                mat.setYear(year);
                mat.setTime(time);
                mat.setHost(host);
                mat.setGuest(guest);

                // Simple
                float win = 0f;
                float draw = 0f;
                float lose = 0f;
                try {
                    win = Float.parseFloat(splits[5].trim());
                    draw = Float.parseFloat(splits[6].trim());
                    lose = Float.parseFloat(splits[7].trim());
                } catch (Exception e) {
                    logger.debug("Line: " + line);
                }

                mat.addRate(win, draw, lose);

                matchMap.put(mat.getKey(), mat);
                rateKeyList.add(mat.getKey());
            }
        } catch (Exception e) {
            logger.error("Line: " + line);
            e.printStackTrace();
        }
        return true;

    }

    @Override
    public void generateRateFiles() {
        try {
            logger.info("Create rate files.");
            for (String key : rateKeyList) {
                String filename = key + ".txt";
                FileLineIterator.writeFile(filename, "中");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
