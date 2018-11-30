package tech.joeyck.livefootball.ui.team_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class TeamDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

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