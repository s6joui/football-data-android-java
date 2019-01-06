package tech.joeyck.livefootball.data.database;

public class RedditPostData{

    private String id;
    private String subreddit;
    private String title;
    private String selftext;
    private String url;
    private String link_flair_text;
    private String thumbnail;
    private String author;
    private String permalink;
    private int score;
    private int created_utc;

    public String getSubreddit() {
        return subreddit;
    }

    public String getTitle() {
        return title;
    }

    public String getSelftext() {
        return selftext;
    }

    public String getUrl() {
        return url;
    }

    public String getLink_flair_text() {
        return link_flair_text;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public String getPermalink() {
        return permalink;
    }

    public int getCreated_utc() {
        return created_utc;
    }

    public int getScore() {
        return score;
    }
}
