package tech.joeyck.livefootball.ui.competition_detail.standings.adapter;

import tech.joeyck.livefootball.data.database.TableEntryEntity;

public class TeamItem extends CompetitionTableItem {

    private TableEntryEntity tableEntryEntity;

    public TeamItem(TableEntryEntity tableEntryEntity) {
        this.tableEntryEntity = tableEntryEntity;
    }

    public TableEntryEntity getTableEntryEntity() {
        return tableEntryEntity;
    }

    @Override
    public int getType() {
        return CompetitionTableItem.TYPE_TEAM;
    }

}
