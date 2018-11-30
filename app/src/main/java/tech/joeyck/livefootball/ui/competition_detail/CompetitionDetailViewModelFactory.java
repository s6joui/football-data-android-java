package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.ui.competitions.MainViewModel;

public class CompetitionDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LiveFootballRepository mRepository;
    private final int mCompetitionId;

    public CompetitionDetailViewModelFactory(LiveFootballRepository repository,int competitionId){
        this.mRepository = repository;
        this.mCompetitionId = competitionId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CompetitionDetailViewModel(mRepository,mCompetitionId);
    }
}