package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.LfsConfMgr;
import com.an.lfs.vo.Rate;
import com.an.lfs.vo.RateMgr;

public class RateLoader {
    private static final Log logger = LogFactory.getLog(MatchLoader.class);
    private Country country;
    private RateMgr rateMgr;

    public RateMgr getRateMgr() {
        return rateMgr;
    }

    public RateLoader(Country country, int startYear, int endYear) {
        this.country = country;

        for (int year = startYear; year <= endYear; year++) {
            load(year);
        }
    }

    @Override
    public String toString() {
        return "RateLoader [country=" + country + ", rateMgr=" + rateMgr + "]";
    }

    private void load(int year) {
        String dir = LfsUtil.getInputCountryYearDirPath(country, year);
        File dirFile = new File(dir);
        List<String> comList = LfsConfMgr.getCompany(country);
        File[] files = dirFile.listFiles();
        for (File f : files) {
            try {
                rateMgr = readExcel(f.toString(), comList);
                System.out.println(rateMgr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static RateMgr readExcel(String filepath, List<String> coms) throws BiffException, IOException {
        Workbook wb = Workbook.getWorkbook(new FileInputStream(filepath));
        Sheet sheet = wb.getSheet(0);
        RateMgr mgr = new RateMgr();
        for (int i = 0; i < sheet.getRows(); i++) {
            if (i == 0) {
                Cell cell = sheet.getCell(0, i);
                String content = cell.getContents().trim();
                String[] strs = content.split(" ");
                if (strs.length != 4) {
                    logger.warn("Invalid title " + content);
                    continue;
                }
                String host = strs[0];
                String guest = strs[2];
                mgr.setKey(host + "_" + guest);
                continue;
            }
            if (i >= 8) {
                boolean targetRow = true;
                List<String> rowList = new ArrayList<>();
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j, i);
                    if (j == 1) {
                        if (coms.contains(cell.getContents().trim())) {
                            targetRow = false;
                            break;
                        }
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
                    mgr.setRate(rate.getCom(), rate);
                }
            }
        }

        return mgr;
    }
}
