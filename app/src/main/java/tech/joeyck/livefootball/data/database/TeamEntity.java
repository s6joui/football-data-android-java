package tech.joeyck.livefootball.data.database;

public class TeamEntity extends BaseEntity {

    private int id;
    private String name;
    private String crestUrl;
    private String clubColors;
    private String venue;
    private int founded;
    private AreaEntity area;
    private String website;
    private String tla;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public String getClubColors() {
        return clubColors;
    }

    public String getVenue() {
        return venue;
    }

    public int getFounded() {
        return founded;
    }

    public AreaEntity getArea() {
        return area;
    }

    public String getWebsite() {
        return website;
    }

    public String getTla() {
        return tla;
    }
}
