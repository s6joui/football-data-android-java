package tech.joeyck.livefootball.ui.competitions;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class MainViewModel extends ViewModel {

    private LiveFootballRepository mRepository;
    private LiveData<ApiResponse<CompetitionResponse>> mCompetitions;

    public MainViewModel(LiveFootballRepository repository){
        mRepository = repository;
        mCompetitions = repository.getCompetitions();
    }

    public LiveData<ApiResponse<CompetitionResponse>> getCompetitions(){
        return mCompetitions;
    }

}
