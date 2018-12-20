package tech.joeyck.livefootball.ui.competition_detail.matches;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
