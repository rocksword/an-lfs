package com.an.lfs.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class SussDaoImpl implements SussDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SussDaoImpl.class);

    @Autowired
    private DataSource ds;

    @Override
    public boolean addBoards(List<BoardPo> boards) {
        LOGGER.info("size: {}", boards.size());
        Connection conn = null;
        try {
            conn = ds.getConnection();
            begin(conn);
            Statement st = conn.createStatement();
            for (BoardPo b : boards) {
                String sql = String.format(
                        "INSERT INTO %s (league, playyear, rank, team, matchcnt, wincnt, drawcnt, losecnt, goalin, goalagainst) "
                                + " VALUES ('%s', %s, %s, '%s', %s, %s, %s, %s, %s, %s);", TBL_BOARD, b.getLeague(),
                        b.getPlayYear(), b.getRank(), b.getTeam(), b.getMatchCnt(), b.getWinCnt(), b.getDrawCnt(),
                        b.getLoseCnt(), b.getGoalIn(), b.getGoalAgainst());
                LOGGER.info("Execute sql: " + sql);
                try {
                    st.executeUpdate(sql);
                    conn.commit();
                } catch (SQLException e) {
                    LOGGER.error("Error while executing sql: {}", sql, e);
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    rollback(conn);
                } catch (SQLException se) {
                    LOGGER.error("Error: ", se);
                }
            }
            LOGGER.error("Error: ", e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    end(conn);
                } catch (SQLException e) {
                    LOGGER.error("Error: ", e);
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error("Error: ", e);
                }
            }
        }
        return true;
    }

    @Override
    public void getAllBoards(List<BoardPo> boards) {
        String sql = String
                .format("SELECT id, league, playyear, rank, team, matchcnt, wincnt, drawcnt, losecnt, goalin, goalagainst FROM %s",
                        TBL_BOARD);
        LOGGER.info("Execute sql: " + sql);

        try (Connection conn = ds.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String league = rs.getString("league");
                int playYear = rs.getInt("playyear");
                int rank = rs.getInt("rank");
                String team = rs.getString("team");
                int matchCnt = rs.getInt("matchcnt");
                int winCnt = rs.getInt("wincnt");
                int drawCnt = rs.getInt("drawcnt");
                int loseCnt = rs.getInt("losecnt");
                int goalIn = rs.getInt("goalin");
                int goalAgainst = rs.getInt("goalagainst");

                BoardPo bp = new BoardPo();
                bp.setId(id);
                bp.setLeague(league);
                bp.setPlayYear(playYear);
                bp.setRank(rank);
                bp.setTeam(team);
                bp.setMatchCnt(matchCnt);
                bp.setWinCnt(winCnt);
                bp.setDrawCnt(drawCnt);
                bp.setLoseCnt(loseCnt);
                bp.setGoalIn(goalIn);
                bp.setGoalAgainst(goalAgainst);
                boards.add(bp);
            }
        } catch (SQLException e) {
            boards.clear();
            LOGGER.error("Error while executing sql {}, exception: {}", sql, e.getMessage());
        }
        LOGGER.info("boards size {}", boards.size());
    }

    @Override
    public BoardPo getBoard(int id) {
        String sql = String
                .format("SELECT league, playyear, rank, team, matchcnt, wincnt, drawcnt, losecnt, goalin, goalagainst FROM %s WHERE id=%s;",
                        TBL_BOARD, id);
        LOGGER.info("Execute sql: " + sql);

        try (Connection conn = ds.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);) {

            while (rs.next()) {
                String league = rs.getString("league");
                int playYear = rs.getInt("playyear");
                int rank = rs.getInt("rank");
                String team = rs.getString("team");
                int matchCnt = rs.getInt("matchcnt");
                int winCnt = rs.getInt("wincnt");
                int drawCnt = rs.getInt("drawcnt");
                int loseCnt = rs.getInt("losecnt");
                int goalIn = rs.getInt("goalin");
                int goalAgainst = rs.getInt("goalagainst");

                BoardPo bp = new BoardPo();
                bp.setId(id);
                bp.setLeague(league);
                bp.setPlayYear(playYear);
                bp.setRank(rank);
                bp.setTeam(team);
                bp.setMatchCnt(matchCnt);
                bp.setWinCnt(winCnt);
                bp.setDrawCnt(drawCnt);
                bp.setLoseCnt(loseCnt);
                bp.setGoalIn(goalIn);
                bp.setGoalAgainst(goalAgainst);
                return bp;
            }
        } catch (SQLException e) {
            LOGGER.error("Error while executing sql {}, exception: {}", sql, e.getMessage());
        }
        return null;
    }

    @Override
    public boolean clearBoard() {
        try (Connection conn = ds.getConnection(); Statement st = conn.createStatement();) {
            String sql = String.format("DELETE FROM %s WHERE id>%s", TBL_BOARD, 0);
            LOGGER.info("Execute sql: " + sql);
            try {
                st.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                LOGGER.error("Error while executing sql: {}", sql, e);
            }
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
        }
        return false;
    }

    private void begin(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
    }

    private void end(Connection conn) throws SQLException {
        conn.setAutoCommit(true);
    }

    private void commit(Connection conn) throws SQLException {
        conn.commit();
    }

    private void rollback(Connection conn) throws SQLException {
        conn.rollback();
        conn.setAutoCommit(true);
    }
}
