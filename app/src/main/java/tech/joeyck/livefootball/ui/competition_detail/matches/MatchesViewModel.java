package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class MatchesViewModel extends ViewModel {

    private LiveFootballRepository mRepository;
    private CompetitionEntity mCompetition;
    private int mTeamId;
    private MutableLiveData<ApiResponse<MatchesResponse>> matches = new MutableLiveData<>();

    MatchesViewModel(LiveFootballRepository repository){
        this.mRepository = repository;
    }

    void setCompetition(CompetitionEntity competition){
        this.mCompetition = competition;
        fetchCompetitionMatchData();
    }

    void setTeamId(int teamId){
        this.mTeamId = teamId;
        fetchTeamMatchData();
    }

    void fetchCompetitionMatchData(){
        mRepository.fetchMatchesForCompetition(matches,mCompetition.getId(),mCompetition.getCurrentSeason().getCurrentMatchday());
    }

    void fetchTeamMatchData(){
        mRepository.fetchMatchesForTeam(matches,mTeamId);
    }

    LiveData<ApiResponse<MatchesResponse>> getMatches(){
        return matches;
    }

}
