package tech.joeyck.livefootball.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.joeyck.livefootball.data.database.RedditListing;

public interface RedditAPI {

    @GET("r/{subreddit}/search.json")
    Call<RedditListing> search(@Path("subreddit") String subreddit, @Query("q") String query, @Query("restrict_sr") int restrictToSubreddit, @Query("sort") String sort, @Query("t") String timeLimit);

}
