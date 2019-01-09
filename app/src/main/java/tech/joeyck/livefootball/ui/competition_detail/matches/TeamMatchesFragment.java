package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.ui.match_detail.MatchDetailActivity;
import tech.joeyck.livefootball.utilities.CompetitionUtils;

public class TeamMatchesFragment extends MatchesFragment {

    public static final String FRAGMENT_TAG = "TeamMatchesFragment";
    private static final String ARG_TEAM_ID = "TEAM_ID";

    public static TeamMatchesFragment newInstance(int teamId){
        TeamMatchesFragment fragment = new TeamMatchesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TEAM_ID,teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState,true);

        int teamId = getArguments().getInt(ARG_TEAM_ID,0);
        mViewModel.setTeamId(teamId);

        return view;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mViewModel.fetchTeamMatchData();
    }

    void bindMatchesToUI(MatchesResponse responseBody){
        hideLoading();
        List<MatchEntity> matchEntities = responseBody.getMatches();
        if(matchEntities!=null){
            mMatchesAdapter.swapItems(matchEntities);
            mMatchesAdapter.addHeader(matchEntities.size(),getString(R.string.recent_matches));
            if(matchEntities.size() == 0){
                showError(R.string.no_recent_matches);
            }
        }else{
            showError(R.string.no_recent_matches);
        }
    }

    @Override
    public void onItemClick(MatchEntity match) {
        Intent matchDetailIntent = new Intent(getActivity(), MatchDetailActivity.class);
        matchDetailIntent.putExtra(MatchDetailActivity.MATCH_ID_EXTRA, match.getId());
        matchDetailIntent.putExtra(MatchDetailActivity.MATCH_HOME_TEAM_EXTRA, match.getHomeTeam().get("name"));
        matchDetailIntent.putExtra(MatchDetailActivity.MATCH_AWAY_TEAM_EXTRA, match.getAwayTeam().get("name"));
        startActivity(matchDetailIntent);
        if(getActivity() != null)getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }


}
