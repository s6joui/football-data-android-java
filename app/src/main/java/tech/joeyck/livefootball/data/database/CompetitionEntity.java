package tech.joeyck.livefootball.data.database;

import android.graphics.Color;

import java.util.Date;

public class CompetitionEntity extends BaseEntity {

    private int id;
    private String name;
    private String code;
    private SeasonEntity currentSeason;
    private AreaEntity area;
    private int themeColor;

    public CompetitionEntity(int id, String name, SeasonEntity currentSeason, int themeColor) {
        this.id = id;
        this.name = name;
        this.currentSeason = currentSeason;
        this.themeColor = themeColor;
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

    public AreaEntity getArea() {
        return area;
    }

    public SeasonEntity getCurrentSeason() {
        return currentSeason;
    }

    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }
}
