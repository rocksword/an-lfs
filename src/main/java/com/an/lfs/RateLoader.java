package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.LfsConfMgr;
import com.an.lfs.vo.MatchRate;
import com.an.lfs.vo.Rate;

public class RateLoader {
    private static final Logger logger = LoggerFactory.getLogger(RateLoader.class);
    private Country country;
    // match ID -> MatchRate
    private Map<String, MatchRate> matchRateMap = new HashMap<>();

    public List<String> getCompany() {
        List<String> nameList = new ArrayList<>();
        int maxSize = 0;
        for (MatchRate mr : matchRateMap.values()) {
            if (mr.getCompany().size() > maxSize) {
                maxSize = mr.getCompany().size();
                nameList = mr.getCompany();
            }
        }
        return nameList;
    }

    private static String getMatchId(String host, String guest) {
        return host + "_" + guest;
    }

    public MatchRate getMatchRate(String host, String guest) {
        return matchRateMap.get(getMatchId(host, guest));
    }

    public RateLoader(Country country, int startYear, int endYear) {
        this.country = country;

        for (int year = startYear; year <= endYear; year++) {
            load(year);
        }
    }

    private void load(int year) {
        String dir = LfsUtil.getInputCountryYearDirPath(country, year);
        File dirFile = new File(dir);
        List<String> comList = LfsConfMgr.getCompany(country);
        logger.info("Load rate {} {} {}", country.getVal(), year, comList);
        File[] files = dirFile.listFiles();
        if (files == null) {
            logger.warn("Not found rate files.");
            return;
        }

        for (File f : files) {
            try {
                MatchRate matchRate = readExcel(f.toString(), comList);
                matchRateMap.put(matchRate.getMatchId(), matchRate);
            } catch (Exception e) {
                logger.error("file: " + f.toString());
                e.printStackTrace();
            }
        }
    }

    private static MatchRate readExcel(String filepath, List<String> comList) throws BiffException, IOException {
        logger.debug("readExcel " + filepath);
        Workbook wb = Workbook.getWorkbook(new FileInputStream(filepath));
        Sheet sheet = wb.getSheet(0);
        MatchRate matRate = new MatchRate();
        List<String> allCom = new ArrayList<>();
        allCom.addAll(comList);
        for (int i = 0; i < sheet.getRows(); i++) {
            if (allCom.isEmpty()) {
                break;
            }
            if (i == 0) {
                Cell cell = sheet.getCell(0, i);
                String content = cell.getContents().trim();
                String[] strs = content.substring(0, content.indexOf("百家")).split("VS");
                if (strs.length != 2) {
                    logger.warn("Invalid title {}, length {}", content, strs.length);
                    continue;
                }
                String host = strs[0].trim();
                String guest = strs[1].trim();
                matRate.setMatchId(getMatchId(host, guest));
                continue;
            }
            if (i >= 8) {
                boolean targetRow = false;
                List<String> rowList = new ArrayList<>();
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    if (j == 1) {
                        if (!comList.contains(cell.getContents().trim())) {
                            break;
                        }
                        targetRow = true;
                    }
                    rowList.add(cell.getContents());
                }

                if (targetRow) {
                    Rate rate = new Rate();
                    rate.setId(Integer.parseInt(rowList.get(0)));
                    rate.setCom(rowList.get(1));
                    rate.setWin(Float.parseFloat(rowList.get(2)));
                    rate.setDraw(Float.parseFloat(rowList.get(3)));
                    rate.setLose(Float.parseFloat(rowList.get(4)));
                    rate.setWinEnd(Float.parseFloat(rowList.get(5)));
                    rate.setDrawEnd(Float.parseFloat(rowList.get(6)));
                    rate.setLoseEnd(Float.parseFloat(rowList.get(7)));
                    rate.setWinRatio(Float.parseFloat(rowList.get(8)));
                    rate.setDrawRatio(Float.parseFloat(rowList.get(9)));
                    rate.setLoseRatio(Float.parseFloat(rowList.get(10)));
                    matRate.addComRate(rate.getCom(), rate);
                    allCom.remove(rowList.get(1));
                }
            }
        }

        return matRate;
    }
}
