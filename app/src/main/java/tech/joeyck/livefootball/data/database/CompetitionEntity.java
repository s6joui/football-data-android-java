package tech.joeyck.livefootball.data.database;

import java.util.Date;

public class CompetitionEntity extends BaseEntity {

    private int id;
    private String name;
    private String code;
    private SeasonEntity currentSeason;
    private AreaEntity area;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public AreaEntity getArea() {
        return area;
    }

    public SeasonEntity getCurrentSeason() {
        return currentSeason;
    }
}
