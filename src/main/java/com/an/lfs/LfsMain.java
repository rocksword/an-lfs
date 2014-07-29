package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.MatchInfo;

public class LfsMain {
    private static final Log logger = LogFactory.getLog(LfsMain.class);

    // Arguments
    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));
    }

    private static int BEGIN_YEAR = 2014;
    private static int matchReport = 0;

    public static void main(String[] args) throws Exception {
        init();

        if (matchReport == 0) {
            int startYear = 2014;
            int endYear = 2014;
            MatchLoader loader = new MatchLoader(Country.JPN, startYear, endYear);
            Map<Integer, List<MatchInfo>> yearMatchMap = loader.getYearMatchMap();
            ReportMaker.exportBoardReport(cty, teamMap);
        } else if (matchReport == 1) {
            for (int year = BEGIN_YEAR; year < 2015; year++) {
                MatchReportMaker maker = new MatchReportMaker(Country.JPN, year);
                maker.analyzeMatch();
                maker.analyzeRate();
                maker.exportExcel();
                // maker.generateRateFiles();
            }
        } else {
            for (Country cty : Country.allCountries) {
                BoardLoader loader = new BoardLoader(cty);
                Map<Integer, List<BoardTeam>> teamMap = loader.getBoardTeamMap();
                ReportMaker.exportBoardReport(cty, teamMap);
            }
        }
    }
}
