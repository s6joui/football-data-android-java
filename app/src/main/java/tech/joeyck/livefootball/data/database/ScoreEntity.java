package tech.joeyck.livefootball.data.database;

import java.util.HashMap;

public class ScoreEntity {

    private String winner;
    private HashMap<String,Integer> fullTime;
    private HashMap<String,Integer> halfTime;
    private HashMap<String,Integer> extraTime;
    private HashMap<String,Integer> penalties;

    public String getWinner() {
        return winner;
    }

    public HashMap<String, Integer> getFullTime() {
        return fullTime;
    }

    public HashMap<String, Integer> getHalfTime() {
        return halfTime;
    }

    public HashMap<String, Integer> getExtraTime() {
        return extraTime;
    }

    public HashMap<String, Integer> getPenalties() {
        return penalties;
    }
}
