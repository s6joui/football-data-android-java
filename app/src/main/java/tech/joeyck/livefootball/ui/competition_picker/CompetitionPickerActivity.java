package tech.joeyck.livefootball.ui.competition_picker;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.CompetitionResponse;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.ui.competition_detail.CompetitionActivity;
import tech.joeyck.livefootball.utilities.ColorUtils;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class CompetitionPickerActivity extends AppCompatActivity implements CompetitionAdapter.CompetitionAdapterOnItemClickHandler {

    private static final String LOG_TAG = CompetitionPickerActivity.class.getSimpleName();

    private CompetitionPickerViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CompetitionAdapter mCompetitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.competition_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mCompetitionAdapter = new CompetitionAdapter(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mCompetitionAdapter);

        CompetitionPickerViewModelFactory factory = InjectorUtils.provideMainViewModelFactory(this.getApplicationContext());
        mViewModel = factory.create(CompetitionPickerViewModel.class);

        mViewModel.getCompetitions().observe(this, new ApiResponseObserver<CompetitionResponse>(new ApiResponseObserver.ChangeListener<CompetitionResponse>() {
            @Override
            public void onSuccess(CompetitionResponse responseBody) {
                mCompetitionAdapter.swapCompetitions(responseBody.getCompetitions());
            }
            @Override
            public void onException(String errorMessage) {

            }
        }));

        //Customize task manager entry
        ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(getString(R.string.title_competitions), null,Color.WHITE);
        setTaskDescription(td);
    }

    @Override
    public void onItemClick(CompetitionEntity competition) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(CompetitionActivity.COMPETITION_ID_PREF,competition.getId());
        editor.putString(CompetitionActivity.COMPETITION_NAME_PREF,competition.getName());
        editor.putInt(CompetitionActivity.COMPETITION_MATCHDAY_PREF,competition.getCurrentSeason().getCurrentMatchday());
        editor.putInt(CompetitionActivity.COMPETITION_COLOR_PREF,ColorUtils.getColorResourceId(competition.getId()));
        editor.apply();
        Intent competitionDetailIntent = new Intent(CompetitionPickerActivity.this, CompetitionActivity.class);
        competitionDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(competitionDetailIntent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}
