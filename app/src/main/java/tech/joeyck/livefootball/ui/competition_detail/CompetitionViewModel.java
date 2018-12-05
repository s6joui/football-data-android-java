package tech.joeyck.livefootball.ui.competition_detail;

import android.arch.lifecycle.ViewModel;

class CompetitionViewModel extends ViewModel {

    private int mCompetitionId;
    private String mCompetitionName;
    private int mMatchDay;
    private int mThemeColor;

    CompetitionViewModel(int competitionId, String competitionName, int matchday, int themeColor){
        this.mCompetitionId = competitionId;
        this.mCompetitionName = competitionName;
        this.mMatchDay = matchday;
        this.mThemeColor = themeColor;
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

    public int getThemeColor(){
        return mThemeColor;
    }

    public void setCompetitionId(int mCompetitionId) {
        this.mCompetitionId = mCompetitionId;
    }

    public void setCompetitionName(String mCompetitionName) {
        this.mCompetitionName = mCompetitionName;
    }

    public void setMatchDay(int mMatchDay) {
        this.mMatchDay = mMatchDay;
    }

    public void setThemeColor(int mThemeColor) {
        this.mThemeColor = mThemeColor;
    }
}