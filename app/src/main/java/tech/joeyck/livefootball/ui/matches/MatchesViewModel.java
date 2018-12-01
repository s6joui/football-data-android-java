package tech.joeyck.livefootball.ui.matches;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.TeamEntity;

public class MatchesViewModel extends ViewModel {

    private LiveData<List<MatchEntity>> mMatches;

    MatchesViewModel(LiveFootballRepository repository, int competitionId){
        mMatches = repository.getMatchesForCompetition(competitionId);
    }

    LiveData<List<MatchEntity>> getMatches(){
        return mMatches;
    }

}
