package com.an.lfs;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.an.lfs.vo.ClaimRate;
import com.an.lfs.vo.ClaimRateSummary;

@Component
@Scope("singleton")
public class ClaimRateParser {
    private static final Log logger = LogFactory.getLog(ClaimRateParser.class);

    @Autowired
    private LfsConfMgr lfsConfMgr;

    public ClaimRateParser() {
    }

    private boolean matched = false;

    public ClaimRateSummary parse(String key) {
        ClaimRateSummary result = new ClaimRateSummary();
        result.setKey(key);

        String filename = key + ".txt";
        String filepath = LfsUtil.getInputFilePath(filename);
        logger.debug("Parse file: " + filepath);

        FileLineIterator iter = new FileLineIterator(filepath);
        String line = null;
        Pattern pat = Pattern.compile("\t");

        boolean avgLine = false;
        boolean endPart = false;
        while ((line = iter.nextLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            if (line.trim().startsWith("胜")) {
                endPart = true;
                continue;
            }

            if (endPart) {
                String[] strs = pat.split(line.toString());
                if (line.trim().startsWith("平均值")) {
                    avgLine = true;
                    float winAvg = Float.parseFloat(strs[1].trim());
                    float drawAvg = Float.parseFloat(strs[2].trim());
                    float loseAvg = Float.parseFloat(strs[3].trim());
                    result.addRateAvg(winAvg, drawAvg, loseAvg);
                    continue;
                }
                if (avgLine) {
                    float winEnd = Float.parseFloat(strs[0].trim());
                    float drawEnd = Float.parseFloat(strs[1].trim());
                    float loseEnd = Float.parseFloat(strs[2].trim());
                    result.addRateEnd(winEnd, drawEnd, loseEnd);
                    continue;
                }
                continue;
            }

            String[] strs = pat.split(line.toString());
            int len = strs.length;
            try {
                if (len == 13) {
                    int id = Integer.parseInt(strs[0].trim());
                    String comp = CompanyMgr.getName(strs[1].trim());
                    if (!lfsConfMgr.isContainCompany(comp)) {
                        matched = false;
                        continue;
                    }
                    matched = true;

                    float win = Float.parseFloat(strs[2].trim());
                    float draw = Float.parseFloat(strs[3].trim());
                    float lose = Float.parseFloat(strs[4].trim());
                    ClaimRate rate = new ClaimRate();
                    rate.setId(id);
                    rate.setComp(comp);
                    rate.setWin(win);
                    rate.setDraw(draw);
                    rate.setLose(lose);
                    logger.debug(rate);
                    result.addClaimRate(rate);
                } else if (len == 10) {
                    if (matched) {
                        float winEnd = Float.parseFloat(strs[0].trim());
                        float drawEnd = Float.parseFloat(strs[1].trim());
                        float loseEnd = Float.parseFloat(strs[2].trim());
                        result.addEndValues(winEnd, drawEnd, loseEnd);
                    }
                } else {
                    logger.error("key: " + key + ", invalid line: " + line);
                    continue;
                }
            } catch (Exception e) {
                logger.error("Key: " + key);
                logger.error("Error line: " + line);
                logger.error("strs len: " + len);
                logger.error("Error: " + e);
                iter.close();
                return null;
            }
        }
        iter.close();
        return result;
    }
}
