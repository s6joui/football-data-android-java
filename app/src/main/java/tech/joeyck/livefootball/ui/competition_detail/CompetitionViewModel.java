package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class CompetitionViewModel extends ViewModel {

    private LiveData<ApiResponse<CompetitionResponse>> mCompetitionList;
    private MutableLiveData<CompetitionEntity> mCompetition = new MutableLiveData<>();

    CompetitionViewModel(LiveFootballRepository repository, CompetitionEntity competition){
        this.mCompetitionList = repository.getCompetitions();
        this.mCompetition.setValue(competition);
    }

    public LiveData<CompetitionEntity> getCompetition(){
        return mCompetition;
    }

    public void setCompetition(CompetitionEntity competition){
        mCompetition.setValue(competition);
    }

    public LiveData<ApiResponse<CompetitionResponse>> getCompetitionList(){
        return mCompetitionList;
    }

}