package tech.joeyck.livefootball.ui.competition_detail;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.ui.competition_detail.matches.MatchesFragment;
import tech.joeyck.livefootball.ui.competition_detail.standings.StandingsFragment;
import tech.joeyck.livefootball.ui.competition_picker.CompetitionPickerActivity;
import tech.joeyck.livefootball.utilities.ColorUtils;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class CompetitionActivity extends AppCompatActivity {

    private static final String LOG_TAG = CompetitionActivity.class.getSimpleName();

    public static final String COMPETITION_ID_PREF = "COMPETITION_ID_PREF";
    public static final String COMPETITION_NAME_PREF = "COMPETITION_NAME_PREF";
    public static final String COMPETITION_MATCHDAY_PREF = "COMPETITION_MATCHDAY_PREF";
    public static final String COMPETITION_COLOR_PREF = "COMPETITION_COLOR_PREF";

    public static final String COMPETITION_ID_EXTRA = "COMPETITION_ID_EXTRA";
    public static final String COMPETITION_NAME_EXTRA = "COMPETITION_NAME_EXTRA";
    public static final String COMPETITION_MATCHDAY_EXTRA = "COMPETITION_MATCHDAY_EXTRA";
    public static final String COMPETITION_COLOR_RESOURCE_EXTRA = "COMPETITION_COLOR_RESOURCE_EXTRA";

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int competitionId = prefs.getInt(COMPETITION_ID_PREF,-1);
        if(competitionId < 0){
            showCompetitionPicker();
            finish();
            return;
        }
        int matchday = prefs.getInt(COMPETITION_MATCHDAY_PREF, -1);
        int themeColor = prefs.getInt(COMPETITION_COLOR_PREF, R.color.colorPrimary);
        String competitionName = prefs.getString(COMPETITION_NAME_PREF,"");

        setContentView(R.layout.activity_competition);
        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null) getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_menu));

        CompetitionViewModelFactory factory = InjectorUtils.provideCompetitionViewModelFactory(this.getApplicationContext(),competitionId,competitionName,matchday,themeColor);
        mViewModel = factory.create(CompetitionViewModel.class);

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            switchFragments(StandingsFragment.FRAGMENT_TAG);
        }

        setTitle(mViewModel.getCompetitionName());
        setThemeColor(mViewModel.getThemeColor());
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
                fragment = MatchesFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay(),mViewModel.getThemeColor());
            }else if(tag.equals(StandingsFragment.FRAGMENT_TAG)){
                fragment = StandingsFragment.newInstance(mViewModel.getCompetitionId(),mViewModel.getCompetitionName(),mViewModel.getMatchDay(),mViewModel.getThemeColor());
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
        int mainColor = getResources().getColor(colorResourceId)| 0xFF000000;
        int transWhite = getResources().getColor(R.color.translucent_white);
        int darkerColor = ColorUtils.getDarkerColor(mainColor,0.75f);
        if(getSupportActionBar()!=null) getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));

        int[][] states = new int[][] {
                new int[] { -android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_checked}, // checked
        };

        int[] colors = new int[] {
                transWhite,
                Color.WHITE,
        };

        ColorStateList colorStateList = new ColorStateList(states,colors);
        mBottomNavigationView.setItemTextColor(colorStateList);
        mBottomNavigationView.setItemIconTintList(colorStateList);
        mBottomNavigationView.setBackgroundColor(mainColor);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(darkerColor);

        //Customize task manager entry
        ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(mViewModel.getCompetitionName(), null, mainColor);
        setTaskDescription(td);
    }

    private void showCompetitionPicker(){
        Intent competitionPickerIntent = new Intent(CompetitionActivity.this, CompetitionPickerActivity.class);
        startActivity(competitionPickerIntent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                showCompetitionPicker();
                return true;
            case R.id.about:
                int themeColor =getResources().getColor(mViewModel.getThemeColor());
                Colors activityColor = new Colors(themeColor,ColorUtils.getDarkerColor(themeColor,0.75f));
                new LibsBuilder()
                        .withActivityColor(activityColor)
                        .withAboutAppName(getString(R.string.app_name))
                        .withActivityTitle(getString(R.string.title_about))
                        .withLicenseShown(true)
                        .withLicenseDialog(true)
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .start(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
