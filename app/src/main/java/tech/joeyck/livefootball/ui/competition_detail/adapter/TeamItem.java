package tech.joeyck.livefootball.ui.competition_detail.adapter;

import tech.joeyck.livefootball.data.database.TeamTableEntity;

public class TeamItem extends CompetitionTableItem {

    private TeamTableEntity team;

    public TeamItem(TeamTableEntity team) {
        this.team = team;
    }

    public TeamTableEntity getTeam() {
        return team;
    }

    @Override
    public int getType() {
        return CompetitionTableItem.TYPE_TEAM;
    }

}
