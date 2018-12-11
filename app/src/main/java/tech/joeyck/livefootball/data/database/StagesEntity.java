package tech.joeyck.livefootball.data.database;

import java.util.List;

public class StagesEntity extends BaseEntity {

    public static final String TYPE_TOTAL = "TOTAL";

    private String stage;
    private String type;
    private String group;
    private List<TableEntryEntity> table;

    public String getStageName() {
        return stage;
    }

    public String getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public List<TableEntryEntity> getTable() {
        return table;
    }

}
