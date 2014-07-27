package com.an.lfs.enu;

public enum TeamType {
    HOST("host"), //
    MID("mid"), //
    GUEST("guest");
    private String val;

    public static TeamType[] allTeamTypes = new TeamType[] { HOST, MID, GUEST };

    public boolean isHost() {
        return this.val.equals(HOST.val);
    }

    public boolean isMid() {
        return this.val.equals(MID.val);
    }

    public boolean isGuest() {
        return this.val.equals(GUEST.val);
    }

    public String getVal() {
        return val;
    }

    private TeamType(String val) {
        this.val = val;
    }
}
