package tech.joeyck.livefootball.ui.competition_detail.standings;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import tech.joeyck.livefootball.ui.BaseRefreshListFragment;
import tech.joeyck.livefootball.ui.BaseAdapter;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionViewModel;
import tech.joeyck.livefootball.ui.team_detail.TeamDetailActivity;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class StandingsFragment extends BaseRefreshListFragment implements BaseAdapter.AdapterOnItemClickHandler<TableEntryEntity> {

    public static final String FRAGMENT_TAG = "StandingsFragment";
    private static final String LOG_TAG = StandingsFragment.class.getSimpleName();

    private StandingsViewModel mViewModel;

    public static StandingsFragment newInstance(){
        return new StandingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState,true,false);

        StandingsTableAdapter tableAdapter = new StandingsTableAdapter(getActivity(), this);
        setAdapter(tableAdapter);

        StandingsViewModelFactory factory = InjectorUtils.provideStandingsViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this,factory).get(StandingsViewModel.class);

        CompetitionViewModel sharedViewModel  = ViewModelProviders.of(getActivity()).get(CompetitionViewModel.class);
        sharedViewModel.getCompetition().observe(this, competitionEntity -> {
            if(competitionEntity!=null && (mViewModel.getCompetitionId() < 0 || competitionEntity.getId() != mViewModel.getCompetitionId())) {
                setSwipeRefreshColor(getResources().getColor(competitionEntity.getThemeColor()));
                hideError();
                showLoading();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.setCompetitionId(competitionEntity.getId());
                    }
                },350);
            }
        });

        mViewModel.getTableItems().observe(this,new ApiResponseObserver<StandingsResponse>(new ApiResponseObserver.ChangeListener<StandingsResponse>() {
            @Override
            public void onSuccess(StandingsResponse res) {
                bindStandingsToUI(res);
            }

            @Override
            public void onException(String errorMessage) {
                showError(R.string.no_connection);
            }
        }));

        return view;
    }

    private void bindStandingsToUI(StandingsResponse res) {
        List<BaseAdapter.BaseAdapterItem> tableItems = formatTableData(res.getStages());
        if(tableItems != null && tableItems.size() != 0){
            ((StandingsTableAdapter)getAdapter()).swapItems(tableItems);
            hideLoading();
        }else{
            showError(R.string.not_found);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mViewModel.fetchCompetitionStandings();
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

    public static List<BaseAdapter.BaseAdapterItem> formatTableData(List<StagesEntity> stages){
        List<BaseAdapter.BaseAdapterItem> tableItems = new ArrayList<>();
        for (StagesEntity stage : stages) {
            if(stage.getType().equals(StagesEntity.TYPE_TOTAL)){
                String text = stage.getGroup() != null ? stage.getGroup() : stage.getStageName();
                BaseAdapter.HeaderItem tableItem = new BaseAdapter.HeaderItem(text.replace("_"," "),R.layout.table_match_header);
                tableItems.add(tableItem);
                for (TableEntryEntity team : stage.getTable()) {
                    tableItems.add(team);
                }
            }
        }
        return tableItems;
    }

}
