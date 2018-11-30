package tech.joeyck.livefootball.ui.competitions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LiveFootballRepository mRepository;

    public MainViewModelFactory(LiveFootballRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mRepository);
    }
}