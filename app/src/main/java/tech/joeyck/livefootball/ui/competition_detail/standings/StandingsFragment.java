package tech.joeyck.livefootball.ui.competition_detail.standings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TableEntryEntity;
import tech.joeyck.livefootball.ui.competition_detail.BaseListFragment;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionActivity;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.CompetitionTableAdapter;
import tech.joeyck.livefootball.ui.team_detail.TeamDetailActivity;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class StandingsFragment extends BaseListFragment implements CompetitionTableAdapter.CompetitionAdapterOnItemClickHandler{

    public static final String FRAGMENT_TAG = "StandingsFragment";
    private static final String LOG_TAG = StandingsFragment.class.getSimpleName();

    private StandingsViewModel mViewModel;

    public static StandingsFragment newInstance(int competitionId, String competitionName, int matchDay){
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putInt(CompetitionActivity.COMPETITION_ID_EXTRA, competitionId);
        args.putInt(CompetitionActivity.COMPETITION_MATCHDAY_EXTRA, matchDay);
        args.putString(CompetitionActivity.COMPETITION_NAME_EXTRA, competitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);

        showLoading();

        int competitionId = getArguments().getInt(CompetitionActivity.COMPETITION_ID_EXTRA, 0);
        int matchday = getArguments().getInt(CompetitionActivity.COMPETITION_MATCHDAY_EXTRA, 0);
        String competitionName = getArguments().getString(CompetitionActivity.COMPETITION_NAME_EXTRA);

        StandingsViewModelFactory factory = InjectorUtils.provideStandingsViewModelFactory(getActivity().getApplicationContext(),competitionId);
        mViewModel = factory.create(StandingsViewModel.class);

        CompetitionTableAdapter tableAdapter = new CompetitionTableAdapter(getActivity(), this);
        setAdapter(tableAdapter);

        mViewModel.getTableItems().observe(this,tableItems -> {
            if(tableItems != null && tableItems.size() != 0){
                tableAdapter.swapTable(tableItems);
                hideLoading();
            }else{
                showError(R.string.not_found);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(TableEntryEntity tableEntryEntity) {
        Log.i(LOG_TAG, tableEntryEntity.getTeam().getName());
        Intent teamDetailIntent = new Intent(getActivity(), TeamDetailActivity.class);
        teamDetailIntent.putExtra(TeamDetailActivity.TEAM_ID_EXTRA, tableEntryEntity.getTeam().getId());
        teamDetailIntent.putExtra(TeamDetailActivity.TEAM_NAME_EXTRA,tableEntryEntity.getTeam().getName());
        startActivity(teamDetailIntent);
    }
}
