package tech.joeyck.livefootball.ui.competition_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class CompetitionViewModel extends ViewModel {

    private final LiveFootballRepository mRepository;
    private MutableLiveData<ApiResponse<CompetitionResponse>> mCompetitionList = new MutableLiveData<>();
    private MutableLiveData<CompetitionEntity> mCompetition = new MutableLiveData<>();

    CompetitionViewModel(LiveFootballRepository repository, CompetitionEntity competition){
        this.mRepository = repository;
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

    public void fetchCompetitionList(){
        mRepository.fetchCompetitionList(data -> mCompetitionList.postValue(data));
    }

}