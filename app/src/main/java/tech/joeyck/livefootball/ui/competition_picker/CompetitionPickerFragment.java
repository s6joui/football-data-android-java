package tech.joeyck.livefootball.ui.competition_picker;

import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.ui.BaseListFragment;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionActivity;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionViewModel;
import tech.joeyck.livefootball.utilities.CompetitionUtils;

public class CompetitionPickerFragment extends BaseListFragment implements CompetitionAdapter.CompetitionAdapterOnItemClickHandler {

    private static final String LOG_TAG = CompetitionPickerFragment.class.getSimpleName();
    public static final String FRAGMENT_TAG = "CompetitionPickerFragment";

    private CompetitionViewModel mViewModel;
    private CompetitionAdapter mCompetitionAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState, R.layout.competition_picker);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: replace with SingleLiveEvent
                CompetitionActivity activity = (CompetitionActivity) getActivity();
                if(activity!=null) activity.closeDrawer();
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mCompetitionAdapter = new CompetitionAdapter(getActivity(), this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mCompetitionAdapter);

        mViewModel = ViewModelProviders.of(getActivity()).get(CompetitionViewModel.class);

        mViewModel.fetchCompetitionList();
        mViewModel.getCompetitionList().observe(this, new ApiResponseObserver<>(new ApiResponseObserver.ChangeListener<CompetitionResponse>() {
            @Override
            public void onSuccess(CompetitionResponse responseBody) {
                hideLoading();
                mCompetitionAdapter.swapCompetitions(responseBody.getCompetitions());
            }
            @Override
            public void onException(String errorMessage) {
                hideLoading();
                showError(R.string.no_connection);
            }
        }));

        return view;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mViewModel.fetchCompetitionList();
    }

    @Override
    public void onItemClick(CompetitionEntity competition) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CompetitionActivity.COMPETITION_ID_PREF,competition.getId());
        editor.putString(CompetitionActivity.COMPETITION_NAME_PREF,competition.getName());
        editor.putInt(CompetitionActivity.COMPETITION_MATCHDAY_PREF,competition.getCurrentSeason().getCurrentMatchday());
        editor.putInt(CompetitionActivity.COMPETITION_COLOR_PREF,CompetitionUtils.getColorResourceId(competition.getId()));
        editor.apply();

        competition.setThemeColor(CompetitionUtils.getColorResourceId(competition.getId()));
        mViewModel.setCompetition(competition);
    }

}
