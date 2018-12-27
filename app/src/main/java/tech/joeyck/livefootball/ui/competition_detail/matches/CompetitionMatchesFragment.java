package tech.joeyck.livefootball.ui.competition_detail.matches;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionViewModel;
import tech.joeyck.livefootball.utilities.DateUtils;

public class CompetitionMatchesFragment extends MatchesFragment {

    public static final String FRAGMENT_TAG = "CompetitionMatchesFragment";

    public static CompetitionMatchesFragment newInstance(){
        return new CompetitionMatchesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState, false);

        CompetitionViewModel sharedViewModel = ViewModelProviders.of(getActivity()).get(CompetitionViewModel.class);
        sharedViewModel.getCompetition().observe(this, competitionEntity -> {
            if (competitionEntity != null && (mViewModel.getCompetition()==null || competitionEntity.getId() != mViewModel.getCompetition().getId())) {
                hideError();
                showLoading();
                setSwipeRefreshColor(getResources().getColor(competitionEntity.getThemeColor()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.setCompetition(competitionEntity);
                    }
                }, 350);
            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mViewModel.fetchCompetitionMatchData();
    }

    void bindMatchesToUI(MatchesResponse responseBody){
        hideLoading();
        List<MatchEntity> matchEntities = responseBody.getMatches();
        if(matchEntities!=null){
            mMatchesAdapter.swapItems(matchEntities);
            mMatchesAdapter.addHeader(0,getString(R.string.matchday,mViewModel.getCompetition().getCurrentSeason().getCurrentMatchday()));
            LocalDateTime lastUpdated = responseBody.getCompetition().getLastUpdatedLocalDateTime();
            mMatchesAdapter.addHeader(matchEntities.size()+1,DateUtils.getLastUpdatedString(getContext(),lastUpdated),R.layout.table_footer);
            if(matchEntities.size() == 0){
                showError(R.string.no_recent_matches);
            }
        }else{
            showError(R.string.no_recent_matches);
        }
    }

    @Override
    public void onItemClick(MatchEntity match) {

    }

}