package tech.joeyck.livefootball.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

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

    public LiveData<ApiResponse<CompetitionResponse>> getCompetitions() {
        return mApiService.getCompetitions();
    }

    public LiveData<ApiResponse<TeamEntity>> getTeamById(int teamId) {
        return mApiService.getTeamById(teamId);
    }

    public void fetchMatchesForCompetition(MutableLiveData<ApiResponse<MatchesResponse>> livedata, int competitionId, int matchday) {
        mApiService.getMatchesForCompetition(competitionId,matchday).enqueue(new LiveDataCallback<MatchesResponse>(livedata));
    }

    public void fetchMatchesForTeam(MutableLiveData<ApiResponse<MatchesResponse>> livedata, int teamId) {
        LocalDateTime to = LocalDateTime.now().plusDays(7);
        LocalDateTime from = LocalDateTime.now().minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        mApiService.getMatchesForTeam(teamId,from.format(formatter),to.format(formatter)).enqueue(new LiveDataCallback<>(livedata));
    }

    public void fetchCompetitionStandings(MutableLiveData<ApiResponse<StandingsResponse>> livedata, int competitionId){
        mApiService.getCompetitionStandings(competitionId).enqueue(new LiveDataCallback<>(livedata));
    }

}
