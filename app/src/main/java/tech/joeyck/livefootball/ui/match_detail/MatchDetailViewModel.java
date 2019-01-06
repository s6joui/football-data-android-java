package tech.joeyck.livefootball.ui.match_detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchResponse;
import tech.joeyck.livefootball.data.database.RedditListing;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.data.network.ApiResponse;

public class MatchDetailViewModel extends ViewModel {

    private final LiveFootballRepository mRepository;
    private MutableLiveData<ApiResponse<MatchResponse>> mMatch = new MutableLiveData<>();
    private MutableLiveData<ApiResponse<RedditListing>> mPosts = new MutableLiveData<>();
    private int mMatchId;
    private String mHomeTeamName;
    private String mAwayTeamName;

    MatchDetailViewModel(LiveFootballRepository repository, int id, String homeTeamName, String awayTeamName){
        mRepository = repository;
        mMatchId = id;
        mHomeTeamName = homeTeamName;
        mAwayTeamName = awayTeamName;
        fetchMatch();
        fetchRedditPosts();
    }

    public void fetchMatch(){
        mRepository.fetchMatchById(mMatchId,data -> mMatch.postValue(data));
    }

    public void fetchRedditPosts(){
        mRepository.fetchMatchRedditPosts(mHomeTeamName,mAwayTeamName,list -> mPosts.postValue(list));
    }

    LiveData<ApiResponse<MatchResponse>> getMatch(){
        return mMatch;
    }

    LiveData<ApiResponse<RedditListing>> getRedditPosts(){
        return mPosts;
    }

}
