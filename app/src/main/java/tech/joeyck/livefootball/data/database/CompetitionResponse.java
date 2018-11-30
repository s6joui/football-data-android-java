package tech.joeyck.livefootball.data.database;

import java.util.List;

public class CompetitionResponse {

    private int count;
    private List<CompetitionEntity> competitions;

    public List<CompetitionEntity> getCompetitions() {
        return competitions;
    }

    public int getCount() {
        return count;
    }
}
