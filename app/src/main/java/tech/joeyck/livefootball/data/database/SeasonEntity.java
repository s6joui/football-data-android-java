package tech.joeyck.livefootball.data.database;

public class SeasonEntity extends BaseEntity {

    private int id;
    private int currentMatchday;

    public SeasonEntity(int id, int currentMatchday) {
        this.id = id;
        this.currentMatchday = currentMatchday;
    }

    public int getId() {
        return id;
    }

    public int getCurrentMatchday() {
        return currentMatchday;
    }
}
