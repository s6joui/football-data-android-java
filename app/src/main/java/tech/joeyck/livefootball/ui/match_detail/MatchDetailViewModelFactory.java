package tech.joeyck.livefootball.ui.match_detail;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import tech.joeyck.livefootball.data.LiveFootballRepository;

public class MatchDetailViewModelFactory implements ViewModelProvider.Factory {

    private final LiveFootballRepository mRepository;
    private final int mMatchId;
    private String mHomeTeamName;
    private String mAwayTeamName;

    public MatchDetailViewModelFactory(LiveFootballRepository repository, int matchId, String homeTeamName, String awayTeamName){
        this.mRepository = repository;
        this.mMatchId = matchId;
        mHomeTeamName = homeTeamName;
        mAwayTeamName = awayTeamName;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MatchDetailViewModel(mRepository,mMatchId,mHomeTeamName,mAwayTeamName);
    }
}