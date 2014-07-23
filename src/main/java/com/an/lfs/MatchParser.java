package com.an.lfs;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.RateResult;
import com.an.lfs.vo.ScoreResult;

public class MatchParser {
    private static final Log logger = LogFactory.getLog(MatchParser.class);

    public MatchParser() {
    }

    public static boolean parse(String cty, int year, String relativeDir, Map<String, Match> matchMap,
            List<String> rateKeyList) {
        String filepath = LfsUtil.getInputFilePath(relativeDir, LfsConst.MATCH_FILE);
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            Pattern pat = Pattern.compile("\t");
            String line = null;
            while ((line = iter.nextLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] splits = pat.split(line.toString());
                if (splits.length < 8) {
                    logger.warn("Invalid line " + line);
                    continue;
                }
                Match mat = new Match();
                int index = Integer.parseInt(splits[0].trim());
                String time = splits[1].trim();
                String host = TeamMgr.getName(cty, splits[2].trim());
                String score = splits[3].trim();
                String guest = TeamMgr.getName(cty, splits[4].trim());
                // Simple
                float win = Float.parseFloat(splits[5].trim());
                float draw = Float.parseFloat(splits[6].trim());
                float lose = Float.parseFloat(splits[7].trim());

                mat.setIndex(index);
                mat.setYear(year);
                mat.setTime(time);
                mat.setHost(host);
                mat.setScore(score);
                mat.setGuest(guest);
                //
                mat.setWin(win);
                mat.setDraw(draw);
                mat.setLose(lose);
                //
                if (score.isEmpty()) {
                    logger.warn("Invalid line " + line);
                    continue;
                }
                ScoreResult scoreResult = LfsUtil.getScoreResult(score);
                RateResult rateResult = LfsUtil.getRateResult(win, draw, lose);
                mat.setScoreResult(scoreResult);
                mat.setRateResult(rateResult);

                String hostCat = LfsUtil.getCat(win);
                String guestCat = LfsUtil.getCat(lose);
                String middleCat = LfsUtil.getCat(draw);
                mat.setHostCat(hostCat);
                mat.setGuestCat(guestCat);
                mat.setMiddleCat(middleCat);

                String key = mat.getKey();
                matchMap.put(key, mat);

                // Compound
                if (rateKeyList != null) {
                    rateKeyList.add(key);
                }
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
        return true;
    }
}
