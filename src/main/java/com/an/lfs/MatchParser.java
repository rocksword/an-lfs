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

    public static boolean parse(String cty, int year, String dir, Map<String, Match> matchMap, List<String> rateKeyList) {
        String filepath = LfsUtil.getInputFilePath(dir, LfsConst.MATCH_FILE);
        logger.debug("filepath: " + filepath);

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
                String host = TeamMgr.getName(cty, splits[2].trim());
                String score = splits[3].trim();
                String guest = TeamMgr.getName(cty, splits[4].trim());

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
                ScoreResult scoreResult = ScoreResult.INVALID;
                if (!score.isEmpty()) {
                    scoreResult = LfsUtil.getScoreResult(score);
                } else {
                    logger.debug("Line: " + line);
                }
                mat.setScoreResult(scoreResult);

                RateResult rateResult = LfsUtil.getRateResult(win, draw, lose);
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
            logger.error("Line: " + line);
            e.printStackTrace();
        }
        return true;
    }
}
