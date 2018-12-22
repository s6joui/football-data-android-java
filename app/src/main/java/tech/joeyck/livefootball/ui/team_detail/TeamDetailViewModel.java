package tech.joeyck.livefootball.ui.team_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class TeamDetailViewModel extends ViewModel {

    private final LiveFootballRepository mRepository;
    private MutableLiveData<ApiResponse<TeamEntity>> mTeam = new MutableLiveData<>();
    private int mTeamId;

    TeamDetailViewModel(LiveFootballRepository repository, int id){
        mRepository = repository;
        mTeamId = id;
        fetchTeam();
    }

    public void fetchTeam(){
        mRepository.fetchTeamById(mTeamId,data -> mTeam.postValue(data));
    }

    LiveData<ApiResponse<TeamEntity>> getTeam(){
        return mTeam;
    }

}
