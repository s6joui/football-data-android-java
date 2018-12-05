package tech.joeyck.livefootball.ui.competition_detail.standings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class StandingsViewModel extends ViewModel {

    private LiveFootballRepository mRepository;
    private int mCompetitionId;

    StandingsViewModel(LiveFootballRepository repository, int competitionId){
        mRepository = repository;
        mCompetitionId = competitionId;
    }

    public LiveData<ApiResponse<StandingsResponse>> getTableItems() {
        return mRepository.getCompetitionStandings(mCompetitionId);
    }
}
