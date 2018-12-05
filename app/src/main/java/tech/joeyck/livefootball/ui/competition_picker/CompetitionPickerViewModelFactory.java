package tech.joeyck.livefootball.ui.competition_picker;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class CompetitionPickerViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LiveFootballRepository mRepository;

    public CompetitionPickerViewModelFactory(LiveFootballRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CompetitionPickerViewModel(mRepository);
    }
}