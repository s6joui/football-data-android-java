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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.utilities.AnimationUtils;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class BaseListFragment extends Fragment{

    public RecyclerView mRecyclerView;
    private ImageView mLoaderImageView;
    private LinearLayout mErrorLayout;
    private TextView mErrorText;
    private TextView mOfflineText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater,container,savedInstanceState,false,false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, boolean showItemDivider, boolean reverseOrder) {
        View view = inflater.inflate(R.layout.layout_recyclerview, container, false);
        mRecyclerView = view.findViewById(R.id.table_recyclerview);
        mLoaderImageView = view.findViewById(R.id.loading_animation);
        mErrorLayout = view.findViewById(R.id.error_layout);
        mErrorText = view.findViewById(R.id.error_text);
        mOfflineText = view.findViewById(R.id.offline_text);

        ImageButton errorImageButton = view.findViewById(R.id.retry_button);
        errorImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataRequest();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        if(reverseOrder){
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
        }
        mRecyclerView.setLayoutManager(layoutManager);

        if(showItemDivider){
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),layoutManager.getOrientation());
            mRecyclerView.addItemDecoration(dividerItemDecoration);
        }

        mRecyclerView.setHasFixedSize(true);
        return view;
    }

    public void onDataRequest(){
        hideError();
        showLoading();

        if(!NetworkUtils.hasNetwork(getContext())){
            mOfflineText.setVisibility(View.VISIBLE);
        }else{
            mOfflineText.setVisibility(View.GONE);
        }
    };

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter(){
        return mRecyclerView.getAdapter();
    }

    public void showError(int resId){
        mErrorText.setText(resId);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    public void hideError(){
        mErrorLayout.setVisibility(View.GONE);
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