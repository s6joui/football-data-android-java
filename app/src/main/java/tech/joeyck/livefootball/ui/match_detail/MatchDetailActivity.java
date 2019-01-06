package tech.joeyck.livefootball.ui.match_detail;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
import tech.joeyck.livefootball.data.database.RedditPost;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.databinding.ActivityMatchDetailBinding;
import tech.joeyck.livefootball.ui.BaseAdapter;
import tech.joeyck.livefootball.ui.competition_detail.matches.TeamMatchesFragment;
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,RedditPostsFragment.newInstance(),RedditPostsFragment.FRAGMENT_TAG).commit();
    }

    private void bindMatchToUi(MatchResponse match) {
        MatchEntity matchEntity = match.getMatchEntity();
        String homeTeam = matchEntity.getHomeTeam().get("name");
        String awayTeam = matchEntity.getAwayTeam().get("name");
        String homeTeamScore = matchEntity.getScore().getHomeTeamScore();
        String awayTeamScore = matchEntity.getScore().getAwayTeamScore();
        mBinding.textHomeTeam.setText(CompetitionUtils.simplifyTeamName(homeTeam));
        mBinding.textAwayTeam.setText(CompetitionUtils.simplifyTeamName(awayTeam));
        mBinding.scoreHomeTeam.setText(homeTeamScore);
        mBinding.scoreAwayTeam.setText(awayTeamScore);

        mBinding.timeText.setText(DateUtils.getFormattedMatchDate(this,matchEntity.getLocalDateTime()));
        LocalDateTime matchTime = matchEntity.getLocalDateTime();
        LocalDateTime now = NetworkUtils.hasNetwork(this) ? LocalDateTime.now() : matchEntity.getLastUpdatedLocalDateTime();
        if(matchEntity.isInSecondHalf()) {
            long mins = MINUTES.between(matchTime, now) - 15;
            mBinding.timeText.setText(getResources().getString(R.string.live,mins));
        }else if(matchEntity.isInPlay()){
            mBinding.timeText.setText(getResources().getString(R.string.live,MINUTES.between(matchTime, now)));
        }else if(matchEntity.isPaused()){
            mBinding.timeText.setText(R.string.half_time);
        }else if(matchEntity.isFinished()){
            mBinding.timeText.setTypeface(null, Typeface.NORMAL);
            mBinding.timeText.setTextColor(getResources().getColor(R.color.gray));
            mBinding.timeText.setText(R.string.full_time);
        }else{
            mBinding.timeText.setVisibility(View.INVISIBLE);
        }

        int homeTeamId = Integer.parseInt(Objects.requireNonNull(matchEntity.getHomeTeam().get("id")));
        int awayTeamId = Integer.parseInt(Objects.requireNonNull(matchEntity.getAwayTeam().get("id")));
        Picasso.get().load(NetworkUtils.getCrestUrl(homeTeamId,NetworkUtils.IMAGE_QUALITY_HD)).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(mBinding.crestHomeTeam);
        Picasso.get().load(NetworkUtils.getCrestUrl(awayTeamId,NetworkUtils.IMAGE_QUALITY_HD)).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(mBinding.crestAwayTeam);
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
