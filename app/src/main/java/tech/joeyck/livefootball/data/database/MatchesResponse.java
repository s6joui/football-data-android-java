package tech.joeyck.livefootball.data.database;

import java.util.List;

public class MatchesResponse {

    private int count;
    private List<MatchEntity> matches;

    public int getCount() {
        return count;
    }

    public List<MatchEntity> getMatches() {
        return matches;
    }
}
