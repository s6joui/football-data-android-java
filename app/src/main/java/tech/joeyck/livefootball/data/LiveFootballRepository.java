package tech.joeyck.livefootball.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.joeyck.livefootball.AppExecutors;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.LiveFootballAPI;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.database.StagesEntity;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.data.database.TableEntryEntity;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.CompetitionTableItem;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.HeaderItem;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.TeamItem;

public class LiveFootballRepository {

    private static final String LOG_TAG = LiveFootballRepository.class.getSimpleName();
    public static final String API_KEY = "football_data_org_api_key";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LiveFootballRepository sInstance;
    private final AppExecutors mExecutors;
    private final LiveFootballAPI mApiService;
    private boolean mInitialized = false;

    private MutableLiveData<List<CompetitionEntity>> mCompetitions;

    private LiveFootballRepository(LiveFootballAPI apiService, AppExecutors executors) {
        mExecutors = executors;
        mApiService = apiService;
        mCompetitions = new MutableLiveData<List<CompetitionEntity>>();
        fetchCompetitions();
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

    public LiveData<List<CompetitionEntity>> getCompetitions() {
        return mCompetitions;
    }

    public LiveData<CompetitionEntity> getCompetitionById(int id){
        return fetchCompetitionById(id);
    }

    public LiveData<List<CompetitionTableItem>> getCompetitionStandings(int competitionId){
        return fetchCompetitionStandings(competitionId);
    }

    public LiveData<TeamEntity> getTeamById(int teamId) {
        return fetchTeamById(teamId);
    }

    public LiveData<List<MatchEntity>> getMatchesForCompetition(int competitionId,int matchday) {
        return fetchMatches(competitionId,matchday);
    }

    public LiveData<List<MatchEntity>> getMatchesForTeam(int teamId) {
        return fetchMatchesForTeam(teamId);
    }

    private LiveData<List<MatchEntity>> fetchMatchesForTeam(int teamId) {
        MutableLiveData<List<MatchEntity>> matches = new MutableLiveData<>();
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = from.format(formatter);
        String toDate = to.format(formatter);
        mApiService.getMatchesForTeam(teamId,fromDate,toDate).enqueue(new Callback<MatchesResponse>() {
            @Override
            public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
                if(response.body()!=null){
                    //We reverse the list to show newer entries on top
                    List<MatchEntity> matchList = response.body().getMatches();
                    List<MatchEntity> revresedList = matchList.subList(0, matchList.size());
                    Collections.reverse(revresedList);
                    matches.postValue(revresedList);
                }
            }

            @Override
            public void onFailure(Call<MatchesResponse> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });
        return matches;
    }

    private LiveData<List<MatchEntity>> fetchMatches(int competitionId, int matchday) {
        MutableLiveData<List<MatchEntity>> matches = new MutableLiveData<>();
        mApiService.getMatches(competitionId,matchday).enqueue(new Callback<MatchesResponse>() {
            @Override
            public void onResponse(Call<MatchesResponse> call, Response<MatchesResponse> response) {
                if(response.body()!=null) matches.postValue(response.body().getMatches());
            }

            @Override
            public void onFailure(Call<MatchesResponse> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });
        return matches;
    }

    private LiveData<List<CompetitionTableItem>> fetchCompetitionStandings(int competitionId) {
        MutableLiveData<List<CompetitionTableItem>> competition = new MutableLiveData<>();
        mApiService.getCompetitionStandings(competitionId).enqueue(new Callback<StandingsResponse>(){
            @Override
            public void onResponse(Call<StandingsResponse> call, Response<StandingsResponse> response) {
                if(response.body()!=null){
                    List<CompetitionTableItem> list = formatTableData(response.body().getStages());
                    competition.postValue(list);
                }
            }

            @Override
            public void onFailure(Call<StandingsResponse> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });
        return competition;
    }

    private void fetchCompetitions(){
        mApiService.getCompetitions().enqueue(new Callback<CompetitionResponse>(){
            @Override
            public void onResponse(Call<CompetitionResponse> call, Response<CompetitionResponse> response) {
                if(response.body()!=null) mCompetitions.postValue(response.body().getCompetitions());
            }

            @Override
            public void onFailure(Call<CompetitionResponse> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });
    }

    private LiveData<TeamEntity> fetchTeamById(int teamId){
        MutableLiveData<TeamEntity> team = new MutableLiveData<>();
        mApiService.getTeamById(teamId).enqueue(new Callback<TeamEntity>(){
            @Override
            public void onResponse(Call<TeamEntity> call, Response<TeamEntity> response) {
                team.postValue(response.body());
            }

            @Override
            public void onFailure(Call<TeamEntity> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });
        return team;
    }

    private LiveData<CompetitionEntity> fetchCompetitionById(int competitionId){
        MutableLiveData<CompetitionEntity> competition = new MutableLiveData<>();
        mApiService.getCompetitionById(competitionId).enqueue(new Callback<CompetitionEntity>(){
            @Override
            public void onResponse(Call<CompetitionEntity> call, Response<CompetitionEntity> response) {
                competition.postValue(response.body());
            }

            @Override
            public void onFailure(Call<CompetitionEntity> call, Throwable t) {
                Log.e(LOG_TAG,t.getMessage());
            }
        });
        return competition;
    }

    private static List<CompetitionTableItem> formatTableData(List<StagesEntity> stages){
        List<CompetitionTableItem> tableItems = new ArrayList<>();
        for (StagesEntity stage : stages) {
            if(stage.getType().equals(StagesEntity.TYPE_TOTAL)){
                String text = stage.getGroup() != null ? stage.getGroup() : stage.getStageName();
                HeaderItem tableItem = new HeaderItem(text.replace("_"," "));
                tableItems.add(tableItem);
                for (TableEntryEntity team : stage.getTable()) {
                    TeamItem teamTableItem = new TeamItem(team);
                    tableItems.add(teamTableItem);
                }
            }
        }
        return tableItems;
    }

}
