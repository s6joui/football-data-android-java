package tech.joeyck.livefootball.ui.match_detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

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
import tech.joeyck.livefootball.ui.competition_detail.matches.TeamMatchesFragment;
import tech.joeyck.livefootball.utilities.InjectorUtils;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class MatchDetailActivity extends AppCompatActivity {

    public static final String MATCH_ID_EXTRA = "MATCH_ID_EXTRA";

    private MatchDetailViewModel mViewModel;
    private ActivityMatchDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_match_detail);

        int matchId = getIntent().getIntExtra(MATCH_ID_EXTRA, -1);

        MatchDetailViewModelFactory factory = InjectorUtils.provideMatchDetailViewModelFactory(this.getApplicationContext(),matchId);
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
        fragmentManager.beginTransaction().replace(R.id.fragment_container,TeamMatchesFragment.newInstance(88),TeamMatchesFragment.FRAGMENT_TAG).commit();
    }

    private void bindMatchToUi(MatchResponse match) {
        MatchEntity matchEntity = match.getMatchEntity();
        String homeTeam = matchEntity.getHomeTeam().get("name");
        String awayTeam = matchEntity.getAwayTeam().get("name");
        String homeTeamScore = matchEntity.getScore().getHomeTeamScore();
        String awayTeamScore = matchEntity.getScore().getAwayTeamScore();
        mBinding.textHomeTeam.setText(homeTeam);
        mBinding.textAwayTeam.setText(awayTeam);
        mBinding.scoreHomeTeam.setText(homeTeamScore);
        mBinding.scoreAwayTeam.setText(awayTeamScore);
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
