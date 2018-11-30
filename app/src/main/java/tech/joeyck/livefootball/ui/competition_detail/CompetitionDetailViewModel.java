package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.ui.competition_detail.adapter.CompetitionTableItem;

public class CompetitionDetailViewModel extends ViewModel {

    private LiveData<CompetitionEntity> mCompetition;
    private LiveData<List<CompetitionTableItem>> mTableItems;

    CompetitionDetailViewModel(LiveFootballRepository repository, int id){
        mCompetition = repository.getCompetitionById(id);
        mTableItems = repository.getCompetitionStandings(id);
    }

    LiveData<CompetitionEntity> getCompetition(){
        return mCompetition;
    }

    public LiveData<List<CompetitionTableItem>> getTableItems() {
        return mTableItems;
    }
}
