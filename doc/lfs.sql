CREATE DATABASE lfs;

DROP TABLE board;

CREATE TABLE board
(
    id SERIAL NOT NULL,
    league character varying(30) NOT NULL,
    playyear int NOT NULL,
    rank int NOT NULL,
    team character varying(30) NOT NULL,
    matchcnt int,
    wincnt int,
    drawcnt int,
    losecnt int,
    goalin int,
    goalagainst int,
    CONSTRAINT pk_board PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE board OWNER TO postgres;

INSERT INTO board VALUES (999999,'≤‚ ‘¡™»¸',2014,1,'≤‚ ‘',10,8,2,0,30,10);
SELECT * FROM board;