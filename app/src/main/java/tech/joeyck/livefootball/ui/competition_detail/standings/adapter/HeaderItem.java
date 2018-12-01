package tech.joeyck.livefootball.ui.competition_detail.standings.adapter;

public class HeaderItem extends CompetitionTableItem {

    private String title;

    public HeaderItem(String text) {
        this.title = text;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getType() {
        return CompetitionTableItem.TYPE_HEADER;
    }

}
