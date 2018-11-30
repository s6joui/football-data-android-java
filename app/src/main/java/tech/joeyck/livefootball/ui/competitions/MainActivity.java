package tech.joeyck.livefootball.ui.competitions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionDetailActivity;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity implements CompetitionAdapter.CompetitionAdapterOnItemClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MainViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CompetitionAdapter mCompetitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.competition_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mCompetitionAdapter = new CompetitionAdapter(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mCompetitionAdapter);

        MainViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this.getApplicationContext());
        mViewModel = factory.create(MainViewModel.class);

        mViewModel.getCompetitions().observe(this,competitionEntries -> {
            mCompetitionAdapter.swapCompetitions(competitionEntries);
        });
    }

    @Override
    public void onItemClick(CompetitionEntity competition) {
        Log.i(LOG_TAG,competition.getName());
        Intent competitionDetailIntent = new Intent(MainActivity.this, CompetitionDetailActivity.class);
        competitionDetailIntent.putExtra(CompetitionDetailActivity.COMPETITION_ID_EXTRA, competition.getId());
        startActivity(competitionDetailIntent);
    }
}
