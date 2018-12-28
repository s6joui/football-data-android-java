package tech.joeyck.livefootball.ui.match_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchResponse;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class MatchDetailViewModel extends ViewModel {

    private final LiveFootballRepository mRepository;
    private MutableLiveData<ApiResponse<MatchResponse>> mMatch = new MutableLiveData<>();
    private int mMatchId;

    MatchDetailViewModel(LiveFootballRepository repository, int id){
        mRepository = repository;
        mMatchId = id;
        fetchMatch();
    }

    public void fetchMatch(){
        mRepository.fetchMatchById(mMatchId,data -> mMatch.postValue(data));
    }

    LiveData<ApiResponse<MatchResponse>> getMatch(){
        return mMatch;
    }

}
