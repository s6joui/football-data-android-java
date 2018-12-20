package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionViewModel;

public class CompetitionMatchesFragment extends MatchesFragment {

    public static final String FRAGMENT_TAG = "CompetitionMatchesFragment";

    public static CompetitionMatchesFragment newInstance(){
        return new CompetitionMatchesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState,false);

        CompetitionViewModel sharedViewModel = ViewModelProviders.of(getActivity()).get(CompetitionViewModel.class);
        sharedViewModel.getCompetition().observe(this, competitionEntity -> {
            if(competitionEntity!=null) {
                mViewModel.setCompetition(competitionEntity);
                setSwipeRefreshColor(getResources().getColor(competitionEntity.getThemeColor()));
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
            mMatchesAdapter.swapMatches(matchEntities);
            mMatchesAdapter.setLastUpdated(responseBody.getCompetition().getLastUpdatedLocalDateTime());
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
