package tech.joeyck.livefootball.ui.team_detail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class TeamDetailViewModelFactory implements ViewModelProvider.Factory {

    private final LiveFootballRepository mRepository;
    private final int mTeamId;

    public TeamDetailViewModelFactory(LiveFootballRepository repository, int teamId){
        this.mRepository = repository;
        this.mTeamId = teamId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TeamDetailViewModel(mRepository,mTeamId);
    }
}