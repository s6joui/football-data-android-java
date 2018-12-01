package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.MatchEntity;

public class MatchesViewModel extends ViewModel {

    private LiveData<List<MatchEntity>> mMatches;

    MatchesViewModel(LiveFootballRepository repository, int competitionId){
        mMatches = repository.getMatchesForCompetition(competitionId);
    }

    LiveData<List<MatchEntity>> getMatches(){
        return mMatches;
    }

}
