package com.an.lfs.db;

import java.util.List;

import com.an.lfs.vo.BoardPo;

public interface SussDao {
    public static final String TBL_BOARD = "board";

    public boolean addBoards(List<BoardPo> boards);

    public BoardPo getBoard(int id);

    public void getBoardByLeague(String league, List<BoardPo> boards);

    public boolean clearBoard();
}
