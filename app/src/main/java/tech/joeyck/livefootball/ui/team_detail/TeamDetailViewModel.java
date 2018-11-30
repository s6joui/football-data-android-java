package tech.joeyck.livefootball.ui.team_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.TeamEntity;

public class TeamDetailViewModel extends ViewModel {

    private LiveData<TeamEntity> mTeam;

    TeamDetailViewModel(LiveFootballRepository repository, int id){
        mTeam = repository.getTeamById(id);
    }

    LiveData<TeamEntity> getTeam(){
        return mTeam;
    }

}
