package tech.joeyck.livefootball.ui.competition_picker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class CompetitionPickerViewModel extends ViewModel {

    private LiveFootballRepository mRepository;

    public CompetitionPickerViewModel(LiveFootballRepository repository){
        mRepository = repository;
    }

    public LiveData<ApiResponse<CompetitionResponse>> getCompetitions(){
        return mRepository.getCompetitions();
    }

}
