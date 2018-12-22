package tech.joeyck.livefootball.ui.competition_detail.matches;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class MatchesViewModelFactory implements ViewModelProvider.Factory {

    private final LiveFootballRepository mRepository;

    public MatchesViewModelFactory(LiveFootballRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MatchesViewModel(mRepository);
    }
}