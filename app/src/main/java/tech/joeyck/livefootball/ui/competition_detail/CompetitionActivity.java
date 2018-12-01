package tech.joeyck.livefootball.ui.competition_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.ui.competition_detail.matches.MatchesFragment;
import tech.joeyck.livefootball.ui.competition_detail.standings.StandingsFragment;
import tech.joeyck.livefootball.ui.competitions.MainViewModelFactory;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class CompetitionActivity extends AppCompatActivity {

    private static final String LOG_TAG = CompetitionActivity.class.getSimpleName();

    public static final String COMPETITION_ID_EXTRA = "COMPETITION_ID_EXTRA";
    public static final String COMPETITION_NAME_EXTRA = "COMPETITION_NAME_EXTRA";
    public static final String COMPETITION_MATCHDAY_EXTRA = "COMPETITION_MATCHDAY_EXTRA";

    FragmentManager mFragmentManager;
    CompetitionViewModel mViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_matches:
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container,MatchesFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay()),MatchesFragment.FRAGMENT_TAG).commit();
                    return true;
                case R.id.navigation_standings:
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container,StandingsFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay()),StandingsFragment.FRAGMENT_TAG).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int competitionId = getIntent().getIntExtra(COMPETITION_ID_EXTRA, -1);
        int matchday = getIntent().getIntExtra(COMPETITION_MATCHDAY_EXTRA, -1);
        String competitionName = getIntent().getStringExtra(COMPETITION_NAME_EXTRA);

        setTitle(competitionName);

        CompetitionViewModelFactory factory = InjectorUtils.provideCompetitionViewModelFactory(this.getApplicationContext(),competitionId,competitionName,matchday);
        mViewModel = factory.create(CompetitionViewModel.class);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.fragment_container,MatchesFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay()),MatchesFragment.FRAGMENT_TAG).commit();
    }

}
