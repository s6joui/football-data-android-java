package tech.joeyck.livefootball.data;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.View;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.joeyck.livefootball.BuildConfig;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;
import tech.joeyck.livefootball.data.network.LiveDataCallback;
import tech.joeyck.livefootball.data.network.LiveFootballAPI;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;

public class LiveFootballRepository {

    private static final String LOG_TAG = LiveFootballRepository.class.getSimpleName();
    public static final String API_KEY = BuildConfig.ApiKey;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LiveFootballRepository sInstance;
    private final LiveFootballAPI mApiService;
    private HashMap<Integer,String> mCrestCache;

    private LiveFootballRepository(LiveFootballAPI apiService) {
        mApiService = apiService;
    }

    public synchronized static LiveFootballRepository getInstance(LiveFootballAPI apiService) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LiveFootballRepository(apiService);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public void fetchCompetitionList(LiveDataCallback.OnDataFetched<ApiResponse<CompetitionResponse>> callback) {
        mApiService.getCompetitions().enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchTeamById(int teamId,LiveDataCallback.OnDataFetched<ApiResponse<TeamEntity>> callback) {
        mApiService.getTeamById(teamId).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchMatchesForCompetition(int competitionId, int matchday,LiveDataCallback.OnDataFetched<ApiResponse<MatchesResponse>> callback) {
        mApiService.getMatchesForCompetition(competitionId,matchday).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchMatchesForTeam(int teamId , LiveDataCallback.OnDataFetched<ApiResponse<MatchesResponse>> callback) {
        LocalDateTime to = LocalDateTime.now().plusDays(7);
        LocalDateTime from = LocalDateTime.now().minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mApiService.getMatchesForTeam(teamId,from.format(formatter),to.format(formatter)).enqueue(new LiveDataCallback<>(callback));
    }

    public void fetchCompetitionStandings(int competitionId, LiveDataCallback.OnDataFetched<ApiResponse<StandingsResponse>> callback) {
        mApiService.getCompetitionStandings(competitionId).enqueue(new LiveDataCallback<>(callback));
    }

}
