package tech.joeyck.livefootball.ui.team_detail;

import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.databinding.ActivityTeamDetailBinding;
import tech.joeyck.livefootball.ui.competition_detail.matches.TeamMatchesFragment;
import tech.joeyck.livefootball.utilities.InjectorUtils;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class TeamDetailActivity extends AppCompatActivity {

    public static final String TEAM_ID_EXTRA = "TEAM_ID_EXTRA";
    public static final String TEAM_NAME_EXTRA = "TEAM_NAME_EXTRA";

    private TeamDetailViewModel mViewModel;
    private ActivityTeamDetailBinding mBinding;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_team_detail);

        int teamId = getIntent().getIntExtra(TEAM_ID_EXTRA, -1);
        String teamName = getIntent().getStringExtra(TEAM_NAME_EXTRA);

        TeamDetailViewModelFactory factory = InjectorUtils.provideTeamDetailViewModelFactory(this.getApplicationContext(),teamId);
        mViewModel = ViewModelProviders.of(this,factory).get(TeamDetailViewModel.class);

        setTitle(teamName);
        mBinding.toolbar.setTitle(teamName);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RequestOptions glideRequestOptions = new RequestOptions();
        glideRequestOptions.placeholder(R.drawable.default_crest);
        glideRequestOptions.error(R.drawable.default_crest);
        glideRequestOptions.fallback(R.drawable.default_crest);

        mViewModel.getTeam().observe(this,new ApiResponseObserver<TeamEntity>(new ApiResponseObserver.ChangeListener<TeamEntity>() {
            @Override
            public void onSuccess(TeamEntity teamEntity) {
                bindTeamToUi(teamEntity,glideRequestOptions);
            }
            @Override
            public void onException(String errorMessage) {
            }
        }));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,TeamMatchesFragment.newInstance(teamId),TeamMatchesFragment.FRAGMENT_TAG).commit();
    }

    private void bindTeamToUi(TeamEntity teamEntity, RequestOptions glideRequestOptions) {
        setTitle(teamEntity.getName());
        mBinding.toolbar.setTitle(teamEntity.getName());
        mBinding.teamExtraText.setText(teamEntity.getArea().getName()+" | "+teamEntity.getFounded());
        Glide.with(this).load(NetworkUtils.getCoverImageUrl(teamEntity.getName())).into(mBinding.coverImage);
        Glide.with(this).load(NetworkUtils.getCrestUrl(teamEntity.getId(),NetworkUtils.IMAGE_QUALITY_HD)).apply(glideRequestOptions).into(mBinding.crestImageView);
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
