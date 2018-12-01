package tech.joeyck.livefootball.ui.competition_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.utilities.AnimationUtils;

public class BaseListFragment extends Fragment{

    private static final String LOG_TAG = BaseListFragment.class.getSimpleName();

    public RecyclerView mRecyclerView;
    private ImageView mLoaderImageView;
    private TextView mErrorText;
    private ImageView mErrorImageView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recyclerview, container, false);
        mRecyclerView = view.findViewById(R.id.table_recyclerview);
        mLoaderImageView = view.findViewById(R.id.loading_animation);
        mErrorImageView = view.findViewById(R.id.error_image);
        mErrorText = view.findViewById(R.id.error_text);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    public void showError(int resId){
        mErrorText.setText(resId);
        mErrorImageView.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }


    public void hideError(){
        mErrorImageView.setVisibility(View.GONE);
        mErrorText.setVisibility(View.GONE);
    }

    public void showLoading(){
        mLoaderImageView.setVisibility(View.VISIBLE);
        AnimationUtils.loopAnimation(mLoaderImageView);
    }

    public void hideLoading(){
        mLoaderImageView.setVisibility(View.GONE);
        AnimationUtils.stopAnimation(mLoaderImageView);
    }

}