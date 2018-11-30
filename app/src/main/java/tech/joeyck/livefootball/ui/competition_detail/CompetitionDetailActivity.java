package tech.joeyck.livefootball.ui.competition_detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.StagesEntity;
import tech.joeyck.livefootball.data.database.TeamTableEntity;
import tech.joeyck.livefootball.ui.competition_detail.adapter.CompetitionTableAdapter;
import tech.joeyck.livefootball.ui.competition_detail.adapter.CompetitionTableItem;
import tech.joeyck.livefootball.ui.competition_detail.adapter.HeaderItem;
import tech.joeyck.livefootball.ui.competition_detail.adapter.TeamItem;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class CompetitionDetailActivity extends AppCompatActivity implements CompetitionTableAdapter.CompetitionAdapterOnItemClickHandler{

    public static final String COMPETITION_ID_EXTRA = "COMPETITION_ID_EXTRA";

    private CompetitionDetailViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CompetitionTableAdapter mCompetitionTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        mRecyclerView = findViewById(R.id.table_recyclerview);

        setTitle("");

        int id = getIntent().getIntExtra(COMPETITION_ID_EXTRA, -1);
        CompetitionDetailViewModelFactory factory = InjectorUtils.provideCompetitionDetailViewModelFactory(this.getApplicationContext(),id);
        mViewModel = factory.create(CompetitionDetailViewModel.class);

        mViewModel.getCompetition().observe(this,competitionEntity -> {
            bindCompetitionToView(competitionEntity);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mCompetitionTableAdapter = new CompetitionTableAdapter(this, this);
        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mCompetitionTableAdapter);

        mViewModel.getTableItems().observe(this,tableItems -> {
            List<CompetitionTableItem> stages = tableItems;
            if(tableItems != null && tableItems.size() != 0){
                mCompetitionTableAdapter.swapTable(tableItems);
            }
        });
    }

    private void bindCompetitionToView(CompetitionEntity competitionEntity) {
        setTitle(competitionEntity.getName());
    }

    @Override
    public void onItemClick(TeamTableEntity competition) {

    }
}
