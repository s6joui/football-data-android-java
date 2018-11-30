package tech.joeyck.livefootball.ui.competitions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;

public class MainViewModel extends ViewModel {

    private LiveFootballRepository mRepository;
    private LiveData<List<CompetitionEntity>> mCompetitions;

    public MainViewModel(LiveFootballRepository repository){
        mRepository = repository;
        mCompetitions = repository.getCompetitions();
    }

    public LiveData<List<CompetitionEntity>> getCompetitions(){
        return mCompetitions;
    }

}
