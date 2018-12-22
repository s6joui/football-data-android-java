package tech.joeyck.livefootball.ui.competition_detail.standings;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class StandingsViewModelFactory implements ViewModelProvider.Factory {

    private final LiveFootballRepository mRepository;

    public StandingsViewModelFactory(LiveFootballRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new StandingsViewModel(mRepository);
    }
}