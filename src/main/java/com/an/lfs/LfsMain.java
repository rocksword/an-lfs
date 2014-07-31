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
    private static int BEGIN_YEAR = 2013;
    private static int END_YEAR = 2014;
    private static int TYPE = 1;

    public static void main(String[] args) throws Exception {
        init();
        if (TYPE == 0) {
            for (Country country : Country.allCountries) {
                logger.info("country: " + country.getVal());
                // Generate board report
                BoardLoader board = new BoardLoader(country);
                Map<Integer, List<BoardTeam>> teamMap = board.getBoardTeamMap();
                ReportMaker.makeBoardReport(country, teamMap);

                // Generate match report
                MatchLoader match = new MatchLoader(country, BEGIN_YEAR, END_YEAR);
                Map<Integer, List<MatchInfo>> yearMatchMap = match.getYearMatchMap();
                ReportMaker.makeMatchReport(country, yearMatchMap);
            }
        } else if (TYPE == 1) {
            for (Country country : Country.leagueCountries) {
                // Generate board report
                LeagueMatchLoader league = new LeagueMatchLoader(country, 2014, 2014);
                Map<Integer, List<MatchInfo>> yearMatchMap = league.getYearMatchMap();
                ReportMaker.makeLeagueMatchReport(country, yearMatchMap);

            }
        } else if (TYPE == 2) {
            for (int year = BEGIN_YEAR; year < 2015; year++) {
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
