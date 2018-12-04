package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.MatchEntity;

public class MatchesViewModel extends ViewModel {

    private LiveFootballRepository mRepository;
    private int mCompetitionId;
    private int mMatchday;
    private int mTeamId;

    MatchesViewModel(LiveFootballRepository repository, int competitionId, int matchday, int teamId){
        this.mRepository = repository;
        this.mCompetitionId = competitionId;
        this.mMatchday = matchday;
        this.mTeamId = teamId;
    }

    MatchesViewModel(LiveFootballRepository repository, int teamId){
        this.mRepository = repository;
        this.mTeamId = teamId;
    }

    LiveData<List<MatchEntity>> getMatches(){
        return mRepository.getMatchesForCompetition(mCompetitionId,mMatchday);
    }

    LiveData<List<MatchEntity>> getMatchesForTeam(){
        return mRepository.getMatchesForTeam(mTeamId);
    }

}
