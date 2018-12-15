package tech.joeyck.livefootball.ui.competition_detail.standings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.StagesEntity;
import tech.joeyck.livefootball.data.database.StandingsResponse;
import tech.joeyck.livefootball.data.database.TableEntryEntity;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.ui.competition_detail.BaseListFragment;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionActivity;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.CompetitionTableAdapter;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.CompetitionTableItem;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.HeaderItem;
import tech.joeyck.livefootball.ui.competition_detail.standings.adapter.TeamItem;
import tech.joeyck.livefootball.ui.team_detail.TeamDetailActivity;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class StandingsFragment extends BaseListFragment implements CompetitionTableAdapter.CompetitionAdapterOnItemClickHandler{

    public static final String FRAGMENT_TAG = "StandingsFragment";
    private static final String LOG_TAG = StandingsFragment.class.getSimpleName();

    private StandingsViewModel mViewModel;

    public static StandingsFragment newInstance(int competitionId, String competitionName, int matchDay, int colorResource){
        StandingsFragment fragment = new StandingsFragment();
        Bundle args = new Bundle();
        args.putInt(CompetitionActivity.COMPETITION_ID_EXTRA, competitionId);
        args.putInt(CompetitionActivity.COMPETITION_MATCHDAY_EXTRA, matchDay);
        args.putString(CompetitionActivity.COMPETITION_NAME_EXTRA, competitionName);
        args.putInt(CompetitionActivity.COMPETITION_COLOR_RESOURCE_EXTRA, colorResource);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState,true,false);

        int competitionId = getArguments().getInt(CompetitionActivity.COMPETITION_ID_EXTRA, 0);
        int matchday = getArguments().getInt(CompetitionActivity.COMPETITION_MATCHDAY_EXTRA, 0);
        String competitionName = getArguments().getString(CompetitionActivity.COMPETITION_NAME_EXTRA);
        int colorResource = getArguments().getInt(CompetitionActivity.COMPETITION_COLOR_RESOURCE_EXTRA, R.color.gray);

        setSwipeRefreshColor(getResources().getColor(colorResource));

        StandingsViewModelFactory factory = InjectorUtils.provideStandingsViewModelFactory(getActivity().getApplicationContext(),competitionId);
        mViewModel = factory.create(StandingsViewModel.class);

        CompetitionTableAdapter tableAdapter = new CompetitionTableAdapter(getActivity(), this);
        setAdapter(tableAdapter);

        onDataRequest();

        return view;
    }

    @Override
    public void onDataRequest() {
        super.onDataRequest();
        mViewModel.getTableItems().observe(this,new ApiResponseObserver<StandingsResponse>(new ApiResponseObserver.ChangeListener<StandingsResponse>() {
            @Override
            public void onSuccess(StandingsResponse responseBody) {
                List<CompetitionTableItem> tableItems = formatTableData(responseBody.getStages());
                if(tableItems != null && tableItems.size() != 0){
                    ((CompetitionTableAdapter)getAdapter()).swapTable(tableItems);
                    hideLoading();
                }else{
                    showError(R.string.not_found);
                }
            }
            @Override
            public void onException(String errorMessage) {
                hideLoading();
                showError(R.string.no_connection);
            }
        }));
    }

    @Override
    public void onItemClick(TableEntryEntity tableEntryEntity) {
        Log.i(LOG_TAG, tableEntryEntity.getTeam().getName());
        Intent teamDetailIntent = new Intent(getActivity(), TeamDetailActivity.class);
        teamDetailIntent.putExtra(TeamDetailActivity.TEAM_ID_EXTRA, tableEntryEntity.getTeam().getId());
        teamDetailIntent.putExtra(TeamDetailActivity.TEAM_NAME_EXTRA,tableEntryEntity.getTeam().getName());
        startActivity(teamDetailIntent);
        if(getActivity() != null)getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    private List<CompetitionTableItem> formatTableData(List<StagesEntity> stages){
        List<CompetitionTableItem> tableItems = new ArrayList<>();
        for (StagesEntity stage : stages) {
            if(stage.getType().equals(StagesEntity.TYPE_TOTAL)){
                String text = stage.getGroup() != null ? stage.getGroup() : stage.getStageName();
                HeaderItem tableItem = new HeaderItem(text.replace("_"," "));
                tableItems.add(tableItem);
                for (TableEntryEntity team : stage.getTable()) {
                    TeamItem teamTableItem = new TeamItem(team);
                    tableItems.add(teamTableItem);
                }
            }
        }
        return tableItems;
    }
}
