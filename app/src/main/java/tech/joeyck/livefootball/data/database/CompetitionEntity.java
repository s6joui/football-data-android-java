package tech.joeyck.livefootball.data.database;

import java.util.Date;

public class CompetitionEntity {

    private int id;
    private String name;
    private String code;
    private Date lastUpdated;

    public CompetitionEntity(int id, String name, String code, Date lastUpdated) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

}
