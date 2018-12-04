package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;

public class MatchesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LiveFootballRepository mRepository;
    private final int mCompetitionId;
    private final int mMatchday;
    private final int mTeamId;

    public MatchesViewModelFactory(LiveFootballRepository repository, int competitionId, int matchday, int teamId){
        this.mRepository = repository;
        this.mMatchday = matchday;
        this.mCompetitionId = competitionId;
        this.mTeamId = teamId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MatchesViewModel(mRepository,mCompetitionId,mMatchday,mTeamId);
    }
}