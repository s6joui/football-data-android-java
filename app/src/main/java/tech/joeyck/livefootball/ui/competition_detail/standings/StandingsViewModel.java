package tech.joeyck.livefootball.ui.competition_detail.standings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class StandingsViewModel extends ViewModel {

    private LiveData<ApiResponse<StandingsResponse>> mTableItems;

    StandingsViewModel(LiveFootballRepository repository, int id){
        mTableItems = repository.getCompetitionStandings(id);
    }

    public LiveData<ApiResponse<StandingsResponse>> getTableItems() {
        return mTableItems;
    }
}
