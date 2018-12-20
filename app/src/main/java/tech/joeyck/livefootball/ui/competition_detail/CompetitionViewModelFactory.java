package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;

public class CompetitionViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CompetitionEntity mCompetition;
    private LiveFootballRepository mRepository;

    public CompetitionViewModelFactory(LiveFootballRepository repository, CompetitionEntity competition){
        this.mCompetition = competition;
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CompetitionViewModel(mRepository,mCompetition);
    }
}