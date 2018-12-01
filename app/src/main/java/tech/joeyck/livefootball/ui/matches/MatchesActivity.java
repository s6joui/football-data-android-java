package tech.joeyck.livefootball.ui.matches;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class MatchesActivity extends AppCompatActivity implements MatchesAdapter.MatchesAdapterOnItemClickHandler {

    private static final String LOG_TAG = MatchesActivity.class.getSimpleName();

    public static final String COMPETITION_ID_EXTRA = "COMPETITION_ID_EXTRA";
    public static final String COMPETITION_NAME_EXTRA = "COMPETITION_NAME_EXTRA";
    public static final String COMPETITION_MATCHDAY_EXTRA = "COMPETITION_MATCHDAY_EXTRA";

    private RecyclerView mRecyclerView;
    private MatchesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        mRecyclerView = findViewById(R.id.table_recyclerview);

        int id = getIntent().getIntExtra(COMPETITION_ID_EXTRA, -1);
        int matchday = getIntent().getIntExtra(COMPETITION_MATCHDAY_EXTRA, 0);
        String competitionName = getIntent().getStringExtra(COMPETITION_NAME_EXTRA);
        setTitle(competitionName);

        MatchesViewModelFactory factory = InjectorUtils.provideMatchesViewModelFactory(this.getApplicationContext(),id);
        mViewModel = factory.create(MatchesViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        MatchesAdapter matchesAdapter = new MatchesAdapter(this, this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(matchesAdapter);

        mViewModel.getMatches().observe(this, matchEntities -> {
            if(matchEntities!=null) matchesAdapter.swapMatches(matchEntities);
        });
    }

    @Override
    public void onItemClick(MatchEntity match) {

    }
}
