package tech.joeyck.livefootball.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.utilities.AnimationUtils;

public class BaseListFragment extends Fragment{

    public RecyclerView mRecyclerView;
    private ImageView mLoaderImageView;
    private LinearLayout mErrorLayout;
    private TextView mErrorText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater,container,savedInstanceState,R.layout.recyclerview_layout);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, int layoutResource) {
        View view = inflater.inflate(layoutResource, container, false);
        mRecyclerView = view.findViewById(R.id.table_recyclerview);
        mLoaderImageView = view.findViewById(R.id.loading_animation);
        mErrorLayout = view.findViewById(R.id.error_layout);
        mErrorText = view.findViewById(R.id.error_text);

        ImageButton errorImageButton = view.findViewById(R.id.retry_button);
        errorImageButton.setOnClickListener(button -> onRefresh());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if(mRecyclerView.getItemAnimator()!=null) mRecyclerView.getItemAnimator().setChangeDuration(500);
        if(mRecyclerView.getItemAnimator()!=null) mRecyclerView.getItemAnimator().setMoveDuration(500);
        if(mRecyclerView.getItemAnimator()!=null) mRecyclerView.getItemAnimator().setAddDuration(500);
        if(mRecyclerView.getItemAnimator()!=null) mRecyclerView.getItemAnimator().setRemoveDuration(500);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoading();
    }

    public void onRefresh(){
        hideError();
        showLoading();
    };

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter(){
        return mRecyclerView.getAdapter();
    }

    public void showError(int resId){
        hideLoading();
        mErrorText.setText(resId);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    public void hideError(){
        mErrorLayout.setVisibility(View.GONE);
    }

    public void showLoading(){
        AnimationUtils.loopAnimation(mLoaderImageView);
    }

    public void hideLoading(){
        AnimationUtils.stopAnimation(mLoaderImageView);
    }

}