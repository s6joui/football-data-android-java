package tech.joeyck.livefootball.ui.competition_detail.matches;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.MatchesResponse;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.ui.BaseRefreshListFragment;
import tech.joeyck.livefootball.utilities.InjectorUtils;

public abstract class MatchesFragment extends BaseRefreshListFragment implements MatchesAdapter.AdapterOnItemClickHandler<MatchEntity> {

    public static final String FRAGMENT_TAG = "MatchesFragment";

    MatchesViewModel mViewModel;
    MatchesAdapter mMatchesAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater, container, savedInstanceState,false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, boolean reverseOrder) {
        View view = super.onCreateView(inflater,container,savedInstanceState,true,reverseOrder);

        MatchesViewModelFactory factory = InjectorUtils.provideMatchesViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this,factory).get(MatchesViewModel.class);

        mMatchesAdapter = new MatchesAdapter(getActivity(), this);
        setAdapter(mMatchesAdapter);

        mViewModel.getMatches().observe(this, new ApiResponseObserver<MatchesResponse>(new ApiResponseObserver.ChangeListener<MatchesResponse>() {
            @Override
            public void onSuccess(MatchesResponse responseBody) {
                hideLoading();
                bindMatchesToUI(responseBody);
            }
            @Override
            public void onException(String errorMessage) {
                showError(R.string.no_connection);
            }
        }));

        return view;
    }

    abstract void bindMatchesToUI(MatchesResponse responseBody);

}
