package tech.joeyck.livefootball.ui.competition_detail;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.ui.competition_detail.matches.MatchesFragment;
import tech.joeyck.livefootball.ui.competition_detail.standings.StandingsFragment;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class CompetitionActivity extends AppCompatActivity {

    private static final String LOG_TAG = CompetitionActivity.class.getSimpleName();

    public static final String COMPETITION_ID_EXTRA = "COMPETITION_ID_EXTRA";
    public static final String COMPETITION_NAME_EXTRA = "COMPETITION_NAME_EXTRA";
    public static final String COMPETITION_MATCHDAY_EXTRA = "COMPETITION_MATCHDAY_EXTRA";
    public static final String COMPETITION_COLOR_EXTRA = "COMPETITION_COLOR_EXTRA";

    FragmentManager mFragmentManager;
    CompetitionViewModel mViewModel;
    BottomNavigationView mBottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_matches:
                    switchFragments(MatchesFragment.FRAGMENT_TAG);
                    return true;
                case R.id.navigation_standings:
                    switchFragments(StandingsFragment.FRAGMENT_TAG);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        int competitionId = getIntent().getIntExtra(COMPETITION_ID_EXTRA, -1);
        int matchday = getIntent().getIntExtra(COMPETITION_MATCHDAY_EXTRA, -1);
        int themeColor = getIntent().getIntExtra(COMPETITION_COLOR_EXTRA, R.color.colorPrimary);
        String competitionName = getIntent().getStringExtra(COMPETITION_NAME_EXTRA);

        setTitle(competitionName);
        setThemeColor(themeColor);

        CompetitionViewModelFactory factory = InjectorUtils.provideCompetitionViewModelFactory(this.getApplicationContext(),competitionId,competitionName,matchday);
        mViewModel = factory.create(CompetitionViewModel.class);

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            switchFragments(MatchesFragment.FRAGMENT_TAG);
        }
    }

    private void switchFragments(String tag){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment curFrag = mFragmentManager.getPrimaryNavigationFragment();
        if (curFrag != null) {
            fragmentTransaction.hide(curFrag);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if(tag.equals(MatchesFragment.FRAGMENT_TAG)){
                fragment = MatchesFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay());
            }else if(tag.equals(StandingsFragment.FRAGMENT_TAG)){
                fragment = StandingsFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay());
            }
            fragmentTransaction.add(R.id.fragment_container, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    private void setThemeColor(int colorResourceId){
        if(getSupportActionBar()!=null) getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorResourceId)));

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
                getResources().getColor(colorResourceId),
                getResources().getColor(colorResourceId),
                getResources().getColor(colorResourceId),
                getResources().getColor(colorResourceId)
        };

        ColorStateList colorStateList = new ColorStateList(states,colors);
        mBottomNavigationView.setItemTextColor(colorStateList);
        mBottomNavigationView.setItemIconTintList(colorStateList);
    }

}
