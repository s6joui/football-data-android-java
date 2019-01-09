package tech.joeyck.livefootball.ui.match_detail;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchResponse;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.databinding.ActivityMatchDetailBinding;
import tech.joeyck.livefootball.ui.team_detail.TeamDetailActivity;
import tech.joeyck.livefootball.utilities.CompetitionUtils;
import tech.joeyck.livefootball.utilities.DateUtils;
import tech.joeyck.livefootball.utilities.InjectorUtils;
import tech.joeyck.livefootball.utilities.NetworkUtils;

import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

public class MatchDetailActivity extends AppCompatActivity {

    public static final String MATCH_ID_EXTRA = "MATCH_ID_EXTRA";
    public static final String MATCH_HOME_TEAM_EXTRA = "MATCH_HOME_TEAM_EXTRA";
    public static final String MATCH_AWAY_TEAM_EXTRA = "MATCH_AWAY_TEAM_EXTRA";

    private MatchDetailViewModel mViewModel;
    private ActivityMatchDetailBinding mBinding;
    private int mHomeTeamId,mAwayTeamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_match_detail);

        Intent intent = getIntent();
        int matchId = intent.getIntExtra(MATCH_ID_EXTRA, -1);
        String homeTeam = intent.getStringExtra(MATCH_HOME_TEAM_EXTRA);
        String awayTeam = intent.getStringExtra(MATCH_AWAY_TEAM_EXTRA);

        MatchDetailViewModelFactory factory = InjectorUtils.provideMatchDetailViewModelFactory(this.getApplicationContext(),matchId,homeTeam,awayTeam);
        mViewModel = ViewModelProviders.of(this,factory).get(MatchDetailViewModel.class);

        setSupportActionBar(mBinding.toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewModel.getMatch().observe(this,new ApiResponseObserver<MatchResponse>(new ApiResponseObserver.ChangeListener<MatchResponse>() {
            @Override
            public void onSuccess(MatchResponse match) {
                if(match!=null) bindMatchToUi(match);
            }
            @Override
            public void onException(String errorMessage) {

            }
        }));

        mBinding.crestHomeTeam.setOnClickListener(view -> showTeamActivity(mHomeTeamId,""));
        mBinding.crestAwayTeam.setOnClickListener(view -> showTeamActivity(mAwayTeamId,""));
        mBinding.textHomeTeam.setOnClickListener(view -> showTeamActivity(mHomeTeamId,""));
        mBinding.textAwayTeam.setOnClickListener(view -> showTeamActivity(mAwayTeamId,""));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,RedditPostsFragment.newInstance(),RedditPostsFragment.FRAGMENT_TAG).commit();
    }

    private void bindMatchToUi(MatchResponse match) {
        MatchEntity matchEntity = match.getMatchEntity();
        String homeTeam = CompetitionUtils.simplifyTeamName(matchEntity.getHomeTeam().get("name"));
        String awayTeam = CompetitionUtils.simplifyTeamName(matchEntity.getAwayTeam().get("name"));
        String homeTeamScore = matchEntity.getScore().getHomeTeamScore();
        String awayTeamScore = matchEntity.getScore().getAwayTeamScore();
        mBinding.textHomeTeam.setText(homeTeam);
        mBinding.textAwayTeam.setText(awayTeam);
        mBinding.scoreHomeTeam.setText(homeTeamScore);
        mBinding.scoreAwayTeam.setText(awayTeamScore);

        String title = matchEntity.getStatus().equals(MatchEntity.STATUS_SCHEDULED) ? getString(R.string.match_title,homeTeam,awayTeam) : getString(R.string.match_score,homeTeam,homeTeamScore,awayTeamScore,awayTeam);
        setTitle(title);
        mBinding.toolbar.setTitle(title);
        mBinding.toolbarLayout.setTitle(title);

        LocalDateTime matchTime = matchEntity.getLocalDateTime();
        LocalDateTime now = NetworkUtils.hasNetwork(this) ? LocalDateTime.now() : matchEntity.getLastUpdatedLocalDateTime();
        mBinding.timeText.setTextColor(getResources().getColor(R.color.green));
        if(matchEntity.isInSecondHalf()) {
            long mins = MINUTES.between(matchTime, now) - 15;
            mBinding.timeText.setText(getResources().getString(R.string.live,mins));
        }else if(matchEntity.isInPlay()){
            mBinding.timeText.setText(getResources().getString(R.string.live,MINUTES.between(matchTime, now)));
        }else if(matchEntity.isPaused()){
            mBinding.timeText.setText(getString(R.string.half_time).toUpperCase());
        }else if(matchEntity.isFinished()){
            mBinding.timeText.setTypeface(null, Typeface.NORMAL);
            mBinding.timeText.setTextColor(getResources().getColor(R.color.black));
            mBinding.timeText.setText(R.string.full_time);
        }else{
            mBinding.timeText.setTextColor(getResources().getColor(R.color.black));
            mBinding.timeText.setText(DateUtils.getFormattedMatchDate(this,matchEntity.getLocalDateTime()));
        }

        if(matchEntity.getCompetition()!=null){
            int bgColor = CompetitionUtils.getColorResourceId(matchEntity.getCompetition().getId());
            mBinding.appBar.setBackgroundResource(bgColor);
        }

        mHomeTeamId = Integer.parseInt(Objects.requireNonNull(matchEntity.getHomeTeam().get("id")));
        mAwayTeamId = Integer.parseInt(Objects.requireNonNull(matchEntity.getAwayTeam().get("id")));
        Picasso.get().load(NetworkUtils.getCrestUrl(mHomeTeamId,NetworkUtils.IMAGE_QUALITY_HD)).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(mBinding.crestHomeTeam);
        Picasso.get().load(NetworkUtils.getCrestUrl(mAwayTeamId,NetworkUtils.IMAGE_QUALITY_HD)).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(mBinding.crestAwayTeam);

    }

    private void showTeamActivity(int teamId, String teamName){
        Intent teamDetailIntent = new Intent(this, TeamDetailActivity.class);
        teamDetailIntent.putExtra(TeamDetailActivity.TEAM_ID_EXTRA, teamId);
        startActivity(teamDetailIntent);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

}
