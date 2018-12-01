package tech.joeyck.livefootball.ui.competition_detail.standings.adapter;

import tech.joeyck.livefootball.data.database.TableEntryEntity;

public class TeamItem extends CompetitionTableItem {

    private TableEntryEntity team;

    public TeamItem(TableEntryEntity team) {
        this.team = team;
    }

    public TableEntryEntity getTeam() {
        return team;
    }

    @Override
    public int getType() {
        return CompetitionTableItem.TYPE_TEAM;
    }

}
