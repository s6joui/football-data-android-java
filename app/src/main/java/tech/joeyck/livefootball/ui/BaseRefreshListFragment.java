package tech.joeyck.livefootball.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class BaseRefreshListFragment extends BaseListFragment{
    private TextView mOfflineText;
    private SwipeRefreshLayout mSwipeRefresh;
    private boolean mReverseOrder = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateView(inflater,container,savedInstanceState,false,false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, boolean showItemDivider, boolean reverseOrder) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        mOfflineText = view.findViewById(R.id.offline_text);
        mSwipeRefresh = view.findViewById(R.id.swiperefresh);

        mOfflineText.setVisibility(View.GONE);

        ImageButton errorImageButton = view.findViewById(R.id.retry_button);
        errorImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BaseRefreshListFragment.this.onRefresh();
            }
        });

        mReverseOrder = reverseOrder;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        if(mReverseOrder){
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

    public void showLoading(){
        super.showLoading();
        mSwipeRefresh.setRefreshing(false);
    }

    public void hideLoading(){
        super.hideLoading();
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.scrollToPosition(mReverseOrder ? mRecyclerView.getAdapter().getItemCount()-1 : 0);

        if(!NetworkUtils.hasNetwork(getContext())){
            mOfflineText.setVisibility(View.VISIBLE);
        }else{
            mOfflineText.setVisibility(View.GONE);
        }
    }

    public void setSwipeRefreshColor(int color){
        if(mSwipeRefresh != null){
            mSwipeRefresh.setColorSchemeColors(color,color,color);
        }
    }

}