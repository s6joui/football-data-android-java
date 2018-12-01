package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionActivity;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class MatchesFragment extends Fragment implements MatchesAdapter.MatchesAdapterOnItemClickHandler {

    public static final String FRAGMENT_TAG = "MatchesFragment";
    private static final String LOG_TAG = MatchesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MatchesViewModel mViewModel;

    public static MatchesFragment newInstance(int competitionId,String competitionName,int matchDay){
        MatchesFragment fragment = new MatchesFragment();
        Bundle args = new Bundle();
        args.putInt(CompetitionActivity.COMPETITION_ID_EXTRA, competitionId);
        args.putInt(CompetitionActivity.COMPETITION_MATCHDAY_EXTRA, matchDay);
        args.putString(CompetitionActivity.COMPETITION_NAME_EXTRA, competitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_competition_detail, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.table_recyclerview);

        int competitionId = getArguments().getInt(CompetitionActivity.COMPETITION_ID_EXTRA, 0);
        int matchday = getArguments().getInt(CompetitionActivity.COMPETITION_MATCHDAY_EXTRA, 0);
        String competitionName = getArguments().getString(CompetitionActivity.COMPETITION_NAME_EXTRA);

        MatchesViewModelFactory factory = InjectorUtils.provideMatchesViewModelFactory(getActivity().getApplicationContext(),competitionId);
        mViewModel = factory.create(MatchesViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        MatchesAdapter matchesAdapter = new MatchesAdapter(getActivity(), this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(matchesAdapter);

        mViewModel.getMatches().observe(this, matchEntities -> {
            if(matchEntities!=null) matchesAdapter.swapMatches(matchEntities);
        });

        return view;
    }

    @Override
    public void onItemClick(MatchEntity match) {

    }
}
