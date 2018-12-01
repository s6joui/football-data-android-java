package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.ViewModel;

class CompetitionViewModel extends ViewModel {

    private int mCompetitionId;
    private String mCompetitionName;
    private int mMatchDay;
    private int mActiveTab = -1;

    CompetitionViewModel(int competitionId, String competitionName, int matchday){
        this.mCompetitionId = competitionId;
        this.mCompetitionName = competitionName;
        this.mMatchDay = matchday;
    }

    public int getCompetitionId() {
        return mCompetitionId;
    }

    public String getCompetitionName() {
        return mCompetitionName;
    }

    public int getMatchDay() {
        return mMatchDay;
    }

    public int getActiveTab() {
        return mActiveTab;
    }

    public void setActiveTab(int mActiveTab) {
        this.mActiveTab = mActiveTab;
    }
}