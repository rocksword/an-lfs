package com.an.lfs;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.ClaimRate;
import com.an.lfs.vo.ClaimRateSummary;

public class ClaimRateParser {
    private static final Log logger = LogFactory.getLog(ClaimRateParser.class);

    public ClaimRateParser() {
    }

    public static ClaimRateSummary parse(String key) {
        ClaimRateSummary result = new ClaimRateSummary();
        result.setKey(key);

        String filename = key + ".txt";
        String filepath = LfsUtil.getInputFilePath(filename);
        logger.debug("Parse file: " + filepath);

        FileLineIterator iter = new FileLineIterator(filepath);
        String line = null;
        Pattern pat = Pattern.compile("\t");

        boolean end = false;
        while ((line = iter.nextLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            if (end) {
                if (line.trim().startsWith("平均值")) {
                    String[] strs = pat.split(line.toString());

                    float winAvg = Float.parseFloat(strs[1].trim());
                    float drawAvg = Float.parseFloat(strs[2].trim());
                    float loseAvg = Float.parseFloat(strs[3].trim());

                    String winPerStr = strs[4].trim();
                    String drawPerStr = strs[5].trim();
                    String losePerStr = strs[6].trim();
                    String retStr = strs[7].trim();
                    float winPerAvg = Float.parseFloat(winPerStr.substring(0, winPerStr.length() - 1));
                    float drawPerAvg = Float.parseFloat(drawPerStr.substring(0, drawPerStr.length() - 1));
                    float losePerAvg = Float.parseFloat(losePerStr.substring(0, losePerStr.length() - 1));
                    float retAvg = Float.parseFloat(retStr.substring(0, retStr.length() - 1));

                    float winKAvg = Float.parseFloat(strs[8].trim());
                    float drawKAvg = Float.parseFloat(strs[9].trim());
                    float loseKAvg = Float.parseFloat(strs[10].trim());
                    result.addAvgValues(winAvg, drawAvg, loseAvg, winPerAvg, drawPerAvg, losePerAvg, retAvg, winKAvg,
                            drawKAvg, loseKAvg);
                }
                continue;
            }

            String[] strs = pat.split(line.toString());
            int len = strs.length;
            try {
                if (len == 13) {
                    int id = Integer.parseInt(strs[0].trim());
                    String comp = CompanyMgr.getName(strs[1].trim());
                    float win = Float.parseFloat(strs[2].trim());
                    float draw = Float.parseFloat(strs[3].trim());
                    float lose = Float.parseFloat(strs[4].trim());

                    String winPerStr = strs[5].trim();
                    String drawPerStr = strs[6].trim();
                    String losePerStr = strs[7].trim();
                    String retStr = strs[8].trim();
                    float winPer = Float.parseFloat(winPerStr.substring(0, winPerStr.length() - 1));
                    float drawPer = Float.parseFloat(drawPerStr.substring(0, drawPerStr.length() - 1));
                    float losePer = Float.parseFloat(losePerStr.substring(0, losePerStr.length() - 1));
                    float ret = Float.parseFloat(retStr.substring(0, retStr.length() - 1));

                    float winK = Float.parseFloat(strs[9].trim());
                    float drawK = Float.parseFloat(strs[10].trim());
                    float loseK = Float.parseFloat(strs[11].trim());

                    ClaimRate rate = new ClaimRate();
                    rate.setId(id);
                    rate.setComp(comp);
                    rate.setWin(win);
                    rate.setDraw(draw);
                    rate.setLose(lose);
                    rate.setWinPer(winPer);
                    rate.setDrawPer(drawPer);
                    rate.setLosePer(losePer);
                    rate.setRet(ret);
                    rate.setWinK(winK);
                    rate.setDrawK(drawK);
                    rate.setLoseK(loseK);
                    logger.debug(rate);
                    result.addClaimRate(rate);
                } else if (len == 10) {
                    float winEnd = Float.parseFloat(strs[0].trim());
                    float drawEnd = Float.parseFloat(strs[1].trim());
                    float loseEnd = Float.parseFloat(strs[2].trim());

                    String winPerStr = strs[3].trim();
                    String drawPerStr = strs[4].trim();
                    String losePerStr = strs[5].trim();
                    String retStr = strs[6].trim();
                    float winPerEnd = Float.parseFloat(winPerStr.substring(0, winPerStr.length() - 1));
                    float drawPerEnd = Float.parseFloat(drawPerStr.substring(0, drawPerStr.length() - 1));
                    float losePerEnd = Float.parseFloat(losePerStr.substring(0, losePerStr.length() - 1));
                    float retEnd = Float.parseFloat(retStr.substring(0, retStr.length() - 1));

                    float winKEnd = Float.parseFloat(strs[7].trim());
                    float drawKEnd = Float.parseFloat(strs[8].trim());
                    float loseKEnd = Float.parseFloat(strs[9].trim());
                    result.addEndValues(winEnd, drawEnd, loseEnd, winPerEnd, drawPerEnd, losePerEnd, retEnd, winKEnd,
                            drawKEnd, loseKEnd);
                } else if (len == 8) {
                    end = true;
                } else {
                    logger.error("Invalid line: " + line);
                    continue;
                }
            } catch (Exception e) {
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
