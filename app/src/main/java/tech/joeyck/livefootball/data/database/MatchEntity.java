package tech.joeyck.livefootball.data.database;

import java.util.Date;
import java.util.HashMap;

public class MatchEntity {

    private int id;
    private SeasonEntity season;
    private Date utcDate;
    private String status;
    private int matchday;
    private String stage;
    private String group;
    private HashMap<String,String> homeTeam;
    private HashMap<String,String> awayTeam;
    private ScoreEntity score;

    public int getId() {
        return id;
    }

    public SeasonEntity getSeason() {
        return season;
    }

    public Date getUtcDate() {
        return utcDate;
    }

    public String getStatus() {
        return status;
    }

    public int getMatchday() {
        return matchday;
    }

    public String getStage() {
        return stage;
    }

    public String getGroup() {
        return group;
    }

    public HashMap<String, String> getHomeTeam() {
        return homeTeam;
    }

    public HashMap<String, String> getAwayTeam() {
        return awayTeam;
    }

    public ScoreEntity getScore() {
        return score;
    }

    @Override
    public String toString() {
        if(homeTeam != null && awayTeam != null)
            return homeTeam.get("name") + " - " + awayTeam.get("name");
        return "empty match";
    }
}
