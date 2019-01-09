package tech.joeyck.livefootball.ui.match_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.RedditListing;
import tech.joeyck.livefootball.data.database.RedditPost;
import tech.joeyck.livefootball.data.network.ApiResponseObserver;
import tech.joeyck.livefootball.ui.BaseAdapter;
import tech.joeyck.livefootball.ui.BaseRefreshListFragment;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class RedditPostsFragment extends BaseRefreshListFragment implements BaseAdapter.AdapterOnItemClickHandler<RedditPost> {

    public static final String FRAGMENT_TAG = "RedditPostsFragment";
    private static final String LOG_TAG = RedditPostsFragment.class.getSimpleName();

    private MatchDetailViewModel mViewModel;

    public static RedditPostsFragment newInstance(){
        return new RedditPostsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState,true,false);

        RedditPostsAdapter tableAdapter = new RedditPostsAdapter(getActivity(), this, new BaseAdapter.AdapterOnItemClickHandler<RedditPost>() {
            @Override
            public void onItemClick(RedditPost item) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(NetworkUtils.REDDIT_API_URL+item.getData().getPermalink()));
                startActivity(i);
            }
        });
        setAdapter(tableAdapter);

        mViewModel = ViewModelProviders.of(getActivity()).get(MatchDetailViewModel.class);

        mViewModel.getRedditPosts().observe(this,new ApiResponseObserver<RedditListing>(new ApiResponseObserver.ChangeListener<RedditListing>() {
            @Override
            public void onSuccess(RedditListing res) {
                List<RedditPost> posts = res.getData().getChildren();
                if(posts!=null && posts.size() > 0){
                    tableAdapter.swapItems(posts);
                    tableAdapter.addHeader(posts.size(),getString(R.string.reddit),R.layout.table_footer);
                    tableAdapter.addHeader(0,getString(R.string.latest_news));
                    hideLoading();
                }else{
                    showError(R.string.not_found);
                }
            }

            @Override
            public void onException(String errorMessage) {
                showError(R.string.no_connection);
            }
        }));

        return view;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mViewModel.fetchRedditPosts();
    }

    @Override
    public void onItemClick(RedditPost post) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(post.getData().getUrl()));
        startActivity(i);
    }

}
