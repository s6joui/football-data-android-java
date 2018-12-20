package tech.joeyck.livefootball.ui.competition_detail;

import android.app.ActivityManager;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;

import java.lang.reflect.Field;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.data.database.SeasonEntity;
import tech.joeyck.livefootball.databinding.ActivityCompetitionBinding;
import tech.joeyck.livefootball.ui.competition_detail.matches.CompetitionMatchesFragment;
import tech.joeyck.livefootball.ui.competition_detail.standings.StandingsFragment;
import tech.joeyck.livefootball.ui.competition_picker.CompetitionPickerFragment;
import tech.joeyck.livefootball.utilities.ColorUtils;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class CompetitionActivity extends AppCompatActivity {

    private static final String LOG_TAG = CompetitionActivity.class.getSimpleName();

    public static final String COMPETITION_ID_PREF = "COMPETITION_ID_PREF";
    public static final String COMPETITION_NAME_PREF = "COMPETITION_NAME_PREF";
    public static final String COMPETITION_MATCHDAY_PREF = "COMPETITION_MATCHDAY_PREF";
    public static final String COMPETITION_COLOR_PREF = "COMPETITION_COLOR_PREF";

    private FragmentManager mFragmentManager;
    private ActivityCompetitionBinding mBinding;
    private CompetitionEntity mCurrentCompetition;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_matches:
                    switchFragments(CompetitionMatchesFragment.FRAGMENT_TAG);
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
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_competition);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int competitionId = prefs.getInt(COMPETITION_ID_PREF,-1);
        int matchday = prefs.getInt(COMPETITION_MATCHDAY_PREF, -1);
        int themeColor = prefs.getInt(COMPETITION_COLOR_PREF, R.color.colorPrimary);
        String competitionName = prefs.getString(COMPETITION_NAME_PREF,"");
        mCurrentCompetition = new CompetitionEntity(competitionId,competitionName,new SeasonEntity(0,matchday),themeColor);

        fixMinDrawerMargin(mBinding.drawerLayout);
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getSupportActionBar()!=null) getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_menu));

        CompetitionViewModelFactory factory = InjectorUtils.provideCompetitionViewModelFactory(this,mCurrentCompetition);
        CompetitionViewModel viewModel = ViewModelProviders.of(this, factory).get(CompetitionViewModel.class);

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            switchFragments(StandingsFragment.FRAGMENT_TAG);
            mFragmentManager.beginTransaction().add(R.id.side_content_frame, new CompetitionPickerFragment(),CompetitionPickerFragment.FRAGMENT_TAG).commit();
        }

        viewModel.getCompetition().observe(this, competition -> {
            if(competition!=null) bindCompetitionToUI(competition);
        });
    }

    private void bindCompetitionToUI(CompetitionEntity competition) {
        if(competition.getId() >= 0){
            mCurrentCompetition = competition;
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            setTitle(competition.getName());
            setThemeColor(competition.getThemeColor());
            if(mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)){
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        }else{
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
    }

    public void setTab(int tab){
        mBinding.navigation.setSelectedItemId(tab);
    }

    public void closeDrawer() {
        if(mCurrentCompetition.getId() >= 0)
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void switchFragments(String tag){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        Fragment curFrag = mFragmentManager.getPrimaryNavigationFragment();
        if (curFrag != null) {
            fragmentTransaction.hide(curFrag);
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if(tag.equals(CompetitionMatchesFragment.FRAGMENT_TAG)){
                fragment = CompetitionMatchesFragment.newInstance();
            }else if(tag.equals(StandingsFragment.FRAGMENT_TAG)){
                fragment = StandingsFragment.newInstance();
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
        mBinding.navigation.setItemTextColor(colorStateList);
        mBinding.navigation.setItemIconTintList(colorStateList);
        mBinding.navigation.setBackgroundColor(mainColor);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(darkerColor);

        //Customize task manager entry
        ActivityManager.TaskDescription td = new ActivityManager.TaskDescription(mCurrentCompetition.getName(), null, mainColor);
        setTaskDescription(td);
    }

    @Override
    public void onBackPressed() {
        if(mBinding.drawerLayout.isDrawerOpen(GravityCompat.START) && mCurrentCompetition.getId() >= 0){
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
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
                mBinding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.about:
                int themeColor =getResources().getColor(mCurrentCompetition.getThemeColor());
                Colors activityColor = new Colors(themeColor,ColorUtils.getDarkerColor(themeColor,0.75f));
                new LibsBuilder()
                        .withAutoDetect(true)
                        .withActivityColor(activityColor)
                        .withAboutAppName(getString(R.string.app_name))
                        .withActivityTitle(getString(R.string.title_about))
                        .withLicenseShown(true)
                        .withLicenseDialog(true)
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withLibraries("threetenabp")
                        .start(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void fixMinDrawerMargin(DrawerLayout drawerLayout) {
        try {
            Field f = DrawerLayout.class.getDeclaredField("mMinDrawerMargin");
            f.setAccessible(true);
            f.set(drawerLayout, 0);
            drawerLayout.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
