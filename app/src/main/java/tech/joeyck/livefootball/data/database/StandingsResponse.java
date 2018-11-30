package tech.joeyck.livefootball.data.database;

import java.util.List;

public class StandingsResponse {

    private CompetitionEntity competition;
    private List<StagesEntity> standings;

    public CompetitionEntity getCompetition() {
        return competition;
    }

    public List<StagesEntity> getStages() {
        return standings;
    }

}
