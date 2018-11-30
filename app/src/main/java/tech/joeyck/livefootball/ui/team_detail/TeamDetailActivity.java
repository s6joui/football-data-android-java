package tech.joeyck.livefootball.ui.team_detail;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.databinding.ActivityTeamDetailBinding;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public class TeamDetailActivity extends AppCompatActivity {

    public static final String TEAM_ID_EXTRA = "TEAM_ID_EXTRA";

    private TeamDetailViewModel mViewModel;
    private ActivityTeamDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_team_detail);

        int id = getIntent().getIntExtra(TEAM_ID_EXTRA, -1);
        TeamDetailViewModelFactory factory = InjectorUtils.provideTeamDetailViewModelFactory(this.getApplicationContext(),id);
        mViewModel = factory.create(TeamDetailViewModel.class);

        mViewModel.getTeam().observe(this,teamEntity -> {
            setTitle(teamEntity.getName());
            mBinding.teamNameTextview.setText(teamEntity.getName());
            Glide.with(this).load(teamEntity.getCrestUrl()).into(mBinding.crestImageView);
        });

    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}
