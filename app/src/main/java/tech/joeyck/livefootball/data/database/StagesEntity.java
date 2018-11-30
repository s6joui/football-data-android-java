package tech.joeyck.livefootball.data.database;

import java.util.List;

public class StagesEntity {

    public static final String TYPE_TOTAL = "TOTAL";

    private String stage;
    private String type;
    private String group;
    private List<TeamTableEntity> table;

    public String getStageName() {
        return stage;
    }

    public String getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public List<TeamTableEntity> getTable() {
        return table;
    }

}
