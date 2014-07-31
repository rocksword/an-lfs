package com.an.lfs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.TeamMgr;

public abstract class MatchAnalyzer implements Analyze {
    private static final Log logger = LogFactory.getLog(MatchAnalyzer.class);
    protected Country country;
    protected int year;
    // Match Key ( year_index_host_guest) -> Match, get from match.txt
    protected Map<String, Match> matchMap = new HashMap<>();

    /**
     * @param country
     * @param year
     */
    public MatchAnalyzer(Country country, int year) {
        this.country = country;
        this.year = year;
    }

    public Match getMatch(String matchKey) {
        return matchMap.get(matchKey);
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
                String host = TeamMgr.getName(country, splits[2].trim(), year);

                String score = splits[3].trim();
                mat.addScore(score);

                String guest = TeamMgr.getName(country, splits[4].trim(), year);
                mat.setIndex(index);
                mat.setYear(year);
                mat.setTime(time);
                mat.setHost(host);
                mat.setGuest(guest);

                matchMap.put(mat.getKey(), mat);
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
            for (String key : matchMap.keySet()) {
                String filepath = LfsUtil.getOutputFilePath(key + ".txt");
                FileLineIterator.writeFile(filepath, "中");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
