package tech.joeyck.livefootball.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import tech.joeyck.livefootball.AppExecutors;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;
import tech.joeyck.livefootball.data.network.LiveFootballAPI;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;

public class LiveFootballRepository {

    private static final String LOG_TAG = LiveFootballRepository.class.getSimpleName();
    public static final String API_KEY = "football_data_org_api_key";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LiveFootballRepository sInstance;
    private final LiveFootballAPI mApiService;
    private boolean mInitialized = false;

    private LiveData<ApiResponse<CompetitionResponse>> mCompetitions;

    private LiveFootballRepository(LiveFootballAPI apiService, AppExecutors executors) {
        mApiService = apiService;
    }

    public synchronized static LiveFootballRepository getInstance(LiveFootballAPI apiService, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LiveFootballRepository(apiService,executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<ApiResponse<CompetitionResponse>> getCompetitions() {
        return mApiService.getCompetitions();
    }

    public LiveData<ApiResponse<CompetitionEntity>> getCompetitionById(int id){
        return mApiService.getCompetitionById(id);
    }

    public LiveData<ApiResponse<TeamEntity>> getTeamById(int teamId) {
        return mApiService.getTeamById(teamId);
    }

    public LiveData<ApiResponse<MatchesResponse>> getMatchesForCompetition(int competitionId,int matchday) {
        return mApiService.getMatchesForCompetition(competitionId,matchday);
    }

    public LiveData<ApiResponse<MatchesResponse>> getMatchesForTeam(int teamId) {
        LocalDateTime to = LocalDateTime.now().plusDays(2);
        LocalDateTime from = LocalDateTime.now().minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return mApiService.getMatchesForTeam(teamId,from.format(formatter),to.format(formatter));
    }

    public LiveData<ApiResponse<StandingsResponse>> getCompetitionStandings(int competitionId){
        return mApiService.getCompetitionStandings(competitionId);
    }

}
