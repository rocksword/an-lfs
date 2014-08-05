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
    private static int BEGIN_YEAR = 2014;
    private static int END_YEAR = 2014;
    private static int TYPE = 0;

    public static void main(String[] args) throws Exception {
        init();
        if (TYPE == 0) {
            for (Country cty : Country.allCountries) {
                logger.info("country: " + cty.getVal());
                // Generate board report
                Map<Integer, List<BoardTeam>> teamMap = new BoardLoader(cty).getBoardTeamMap();
                ReportMaker.makeBoardReport(cty, teamMap);

                RateLoader rateLoader = new RateLoader(cty, BEGIN_YEAR, END_YEAR);
                // Generate match report
                Map<Integer, List<MatchInfo>> yearMatchMap = new MatchLoader(cty, BEGIN_YEAR, END_YEAR)
                        .getYearMatchMap();
                ReportMaker.makeMatchReport(cty, yearMatchMap, rateLoader);
            }

            for (Country cty : Country.leagueCountries) {
                RateLoader rateLoader = new RateLoader(cty, BEGIN_YEAR, END_YEAR);
                // Generate board report
                LeagueMatchLoader league = new LeagueMatchLoader(cty, 2014, 2014);
                Map<Integer, List<MatchInfo>> yearMatchMap = league.getYearMatchMap();
                ReportMaker.makeLeagueMatchReport(cty, yearMatchMap, rateLoader);
            }
        } else if (TYPE == 1) {
            for (int year = BEGIN_YEAR; year <= END_YEAR; year++) {
                MatchReportMaker maker = new MatchReportMaker(Country.JPN_B, year);
                maker.analyzeMatch();
                maker.analyzeRate();
                maker.exportExcel();
                // maker.generateRateFiles();
            }
        }
    }

    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));
    }
}
