package tech.joeyck.livefootball.ui.competition_detail.standings.adapter;

public abstract class CompetitionTableItem {

    public static final int TYPE_TEAM = 10;
    public static final int TYPE_HEADER = 11;

    abstract public int getType();
}
