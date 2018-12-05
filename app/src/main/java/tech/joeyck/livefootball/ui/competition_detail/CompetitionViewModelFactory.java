package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import tech.joeyck.livefootball.data.LiveFootballRepository;
import tech.joeyck.livefootball.ui.competitions.MainViewModel;

public class CompetitionViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int mCompetitionId;
    private final String mCompetitionName;
    private final int mMatchDay;
    private final int mThemeColor;

    public CompetitionViewModelFactory(int competitionId, String competitionName, int matchday, int themeColor){
        this.mCompetitionId = competitionId;
        this.mCompetitionName = competitionName;
        this.mMatchDay = matchday;
        this.mThemeColor = themeColor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new CompetitionViewModel(mCompetitionId,mCompetitionName,mMatchDay,mThemeColor);
    }
}