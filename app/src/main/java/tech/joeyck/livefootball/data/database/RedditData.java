package tech.joeyck.livefootball.data.database;

import java.util.List;

public class RedditData {

    private String modhash;
    private List<RedditPost> children;

    public String getModhash() {
        return modhash;
    }

    public List<RedditPost> getChildren() {
        return children;
    }
}
