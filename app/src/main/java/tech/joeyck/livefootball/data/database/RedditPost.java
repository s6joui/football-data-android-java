package tech.joeyck.livefootball.data.database;

import tech.joeyck.livefootball.ui.BaseAdapter;

public class RedditPost implements BaseAdapter.BaseAdapterItem {

    private String kind;
    private RedditPostData data;

    public String getKind() {
        return kind;
    }

    public RedditPostData getData() {
        return data;
    }

    @Override
    public int getType() {
        return TYPE_DEFAULT;
    }

    @Override
    public int getId() {
        return 0;
    }
}
