package com.an.lfs;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.ClaimRate;
import com.an.lfs.vo.ClaimRateSummary;

public class ClaimRateParser {
    private static final Log logger = LogFactory.getLog(ClaimRateParser.class);

    public ClaimRateParser() {
    }

    public static ClaimRateSummary parse(String country, String dir, String filename) {
        logger.debug("country: " + country + ", dir: " + dir + ", filename: " + filename);
        ClaimRateSummary result = new ClaimRateSummary();

        String filepath = LfsUtil.getInputFilePath(dir, filename);
        logger.debug("Parse file: " + filepath);

        try (FileLineIterator iter = new FileLineIterator(filepath);) {

            String line = null;
            Pattern pat = Pattern.compile("\t");
            boolean avgLine = false;
            boolean lastSegBegin = false;
            String lastComp = null;
            while ((line = iter.nextLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("胜")) {
                    lastSegBegin = true;
                    continue;
                }

                String[] strs = pat.split(line);
                if (lastSegBegin) {

                    if (line.startsWith("平均值")) {
                        float win = Float.parseFloat(strs[1].trim());
                        float draw = Float.parseFloat(strs[2].trim());
                        float lose = Float.parseFloat(strs[3].trim());

                        result.addRate(win, draw, lose);
                        avgLine = true;
                    } else {
                        if (avgLine) {
                            float winEnd = Float.parseFloat(strs[0].trim());
                            float drawEnd = Float.parseFloat(strs[1].trim());
                            float loseEnd = Float.parseFloat(strs[2].trim());

                            result.addEndRate(winEnd, drawEnd, loseEnd);
                            avgLine = false;
                        }
                    }

                    continue;
                }

                try {
                    if (strs.length == 13) {
                        int id = Integer.parseInt(strs[0].trim());
                        String comp = CompanyMgr.getName(strs[1].trim());
                        float win = Float.parseFloat(strs[2].trim());
                        float draw = Float.parseFloat(strs[3].trim());
                        float lose = Float.parseFloat(strs[4].trim());

                        ClaimRate rate = new ClaimRate(id, comp);
                        rate.addRate(win, draw, lose);
                        result.addCompanyRate(rate);

                        lastComp = comp;

                    } else if (strs.length == 10) {
                        if (lastComp == null) {
                            logger.warn("lastComp is null.");
                            continue;
                        }
                        float winEnd = Float.parseFloat(strs[0].trim());
                        float drawEnd = Float.parseFloat(strs[1].trim());
                        float loseEnd = Float.parseFloat(strs[2].trim());
                        result.addCompanyEndRate(lastComp, winEnd, drawEnd, loseEnd);
                    } else {
                        logger.error("filename: " + filename + ", line: " + line);
                        lastComp = null;
                        continue;
                    }
                } catch (Exception e) {
                    logger.error("len: " + strs.length + ", filename: " + filename + ",  line: " + line);
                    logger.error("Error: " + e);
                    lastComp = null;
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Error: " + e);
        }
        return result;
    }
}
