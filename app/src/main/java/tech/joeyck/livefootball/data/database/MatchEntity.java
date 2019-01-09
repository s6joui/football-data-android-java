package tech.joeyck.livefootball.data.database;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.Date;
import java.util.HashMap;

import tech.joeyck.livefootball.ui.BaseAdapter.BaseAdapterItem;

public class MatchEntity extends BaseEntity implements BaseAdapterItem {

    public static String STATUS_FINISHED = "FINISHED";
    public static String STATUS_IN_PLAY = "IN_PLAY";
    public static String STATUS_PAUSED = "PAUSED";
    public static String STATUS_SCHEDULED = "SCHEDULED";

    private int id;
    private SeasonEntity season;
    private Date utcDate;
    private String status;
    private int matchday;
    private String stage;
    private String group;
    private HashMap<String,String> homeTeam;
    private HashMap<String,String> awayTeam;
    private CompetitionEntity competition;
    private ScoreEntity score;

    @Override
    public int getId() {
        return id;
    }

    public SeasonEntity getSeason() {
        return season;
    }

    public Date getUtcDate() {
        return utcDate;
    }

    public LocalDateTime getLocalDateTime() {
        return Instant.ofEpochMilli(utcDate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
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

    public boolean isFinished(){
        return status.equals(STATUS_FINISHED);
    }

    public boolean isInSecondHalf(){
        return status.equals(STATUS_IN_PLAY) && (score.getHalfTime().get("homeTeam") != null);
    }

    public boolean isInPlay(){
        return status.equals(STATUS_IN_PLAY);
    }

    public boolean isPaused() {
        return status.equals(STATUS_PAUSED);
    }

    public CompetitionEntity getCompetition() {
        return competition;
    }

    @Override
    public int getType() {
        return TYPE_DEFAULT;
    }
}
