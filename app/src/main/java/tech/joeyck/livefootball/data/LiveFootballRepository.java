package tech.joeyck.livefootball.data;

import android.util.Log;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.HashMap;

import tech.joeyck.livefootball.BuildConfig;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.database.MatchResponse;
import tech.joeyck.livefootball.data.database.RedditListing;
import tech.joeyck.livefootball.data.network.ApiResponse;
import tech.joeyck.livefootball.data.network.LiveDataCallback;
import tech.joeyck.livefootball.data.network.LiveFootballAPI;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.data.network.RedditAPI;
import tech.joeyck.livefootball.utilities.CompetitionUtils;

public class LiveFootballRepository {

    private static final String LOG_TAG = LiveFootballRepository.class.getSimpleName();
    public static final String API_KEY = BuildConfig.ApiKey;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LiveFootballRepository sInstance;
    private final LiveFootballAPI mFootballApi;
    private final RedditAPI mRedditApi;

    private LiveFootballRepository(LiveFootballAPI footballAPI,RedditAPI redditAPI) {
        mFootballApi = footballAPI;
        mRedditApi = redditAPI;
    }

    public synchronized static LiveFootballRepository getInstance(LiveFootballAPI footballAPI, RedditAPI redditAPI) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LiveFootballRepository(footballAPI,redditAPI);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public void fetchCompetitionList(LiveDataCallback.OnDataFetched<ApiResponse<CompetitionResponse>> callback) {
        mFootballApi.getCompetitions().enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchTeamById(int teamId,LiveDataCallback.OnDataFetched<ApiResponse<TeamEntity>> callback) {
        mFootballApi.getTeamById(teamId).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchMatchById(int matchId,LiveDataCallback.OnDataFetched<ApiResponse<MatchResponse>> callback) {
        mFootballApi.getMatchById(matchId).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchMatchesForCompetition(int competitionId, int matchday,LiveDataCallback.OnDataFetched<ApiResponse<MatchesResponse>> callback) {
        mFootballApi.getMatchesForCompetition(competitionId,matchday).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchMatchesForTeam(int teamId , LiveDataCallback.OnDataFetched<ApiResponse<MatchesResponse>> callback) {
        LocalDateTime to = LocalDateTime.now().plusDays(7);
        LocalDateTime from = LocalDateTime.now().minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mFootballApi.getMatchesForTeam(teamId,from.format(formatter),to.format(formatter)).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchCompetitionStandings(int competitionId, LiveDataCallback.OnDataFetched<ApiResponse<StandingsResponse>> callback) {
        mFootballApi.getCompetitionStandings(competitionId).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchMatchRedditPosts(String homeTeamName, String awayTeamName, LiveDataCallback.OnDataFetched<ApiResponse<RedditListing>> callback){
        String query = CompetitionUtils.simplifyTeamName(homeTeamName+" "+awayTeamName);
        mRedditApi.search("soccer",query,1,"new","week").enqueue(new LiveDataCallback<>(callback));
    }

}
