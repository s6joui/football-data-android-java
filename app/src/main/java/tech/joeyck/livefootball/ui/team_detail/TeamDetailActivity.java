package tech.joeyck.livefootball.ui.team_detail;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TeamEntity;
import tech.joeyck.livefootball.databinding.ActivityTeamDetailBinding;
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

        int id = getIntent().getIntExtra(TEAM_ID_EXTRA, -1);
        String teamName = getIntent().getStringExtra(TEAM_NAME_EXTRA);

        TeamDetailViewModelFactory factory = InjectorUtils.provideTeamDetailViewModelFactory(this.getApplicationContext(),id);
        mViewModel = factory.create(TeamDetailViewModel.class);

        setTitle(teamName);
        mBinding.teamNameText.setText(teamName);

        RequestOptions glideRequestOptions = new RequestOptions();
        glideRequestOptions.placeholder(R.drawable.default_crest);
        glideRequestOptions.error(R.drawable.default_crest);
        glideRequestOptions.fallback(R.drawable.default_crest);

        mViewModel.getTeam().observe(this,teamEntity -> {
            if(teamEntity!=null) bindTeamToUi(teamEntity,glideRequestOptions);
        });

    }

    private void bindTeamToUi(TeamEntity teamEntity, RequestOptions glideRequestOptions) {
        setTitle(teamEntity.getName());
        mBinding.teamNameText.setText(teamEntity.getName());
        mBinding.countryNameText.setText(teamEntity.getArea().getName()+" | "+teamEntity.getFounded());
        String coverUrl = "https://loremflickr.com/320/240/"+TextUtils.join(",",teamEntity.getName().toLowerCase().split(" "));
        Glide.with(this).load(coverUrl).into(mBinding.coverImage);
        Glide.with(this).load(NetworkUtils.getPngUrl(teamEntity.getCrestUrl())).apply(glideRequestOptions).into(mBinding.crestImageView);
    }
}
