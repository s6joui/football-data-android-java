package tech.joeyck.livefootball.ui.competition_detail.standings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class StandingsViewModel extends ViewModel {

    private MutableLiveData<ApiResponse<StandingsResponse>> mTableItems = new MutableLiveData<>();

    private int mCompetitionId = -1;
    private LiveFootballRepository mRepository;

    StandingsViewModel(LiveFootballRepository repository){
        mRepository = repository;
    }

    public void setCompetitionId(int competitionId){
        mCompetitionId = competitionId;
        fetchCompetitionStandings();
    }

    public void fetchCompetitionStandings(){
        mRepository.fetchCompetitionStandings(mCompetitionId,(standings) -> {
            mTableItems.postValue(standings);
        });
    }

    public LiveData<ApiResponse<StandingsResponse>> getTableItems() {
        return mTableItems;
    }

    public int getCompetitionId() {
        return mCompetitionId;
    }
}
