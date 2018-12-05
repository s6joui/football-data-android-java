package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

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

    LiveData<ApiResponse<MatchesResponse>> getMatchesForCompetition(){
        return mRepository.getMatchesForCompetition(mCompetitionId,mMatchday);
    }

    LiveData<ApiResponse<MatchesResponse>> getMatchesForTeam(){
        return mRepository.getMatchesForTeam(mTeamId);
    }

}
