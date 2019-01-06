package tech.joeyck.livefootball.ui.match_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.RedditPost;
import tech.joeyck.livefootball.data.database.RedditPostData;
import tech.joeyck.livefootball.ui.BaseAdapter;

class RedditPostsAdapter extends BaseAdapter<RedditPost> {

    private AdapterOnItemClickHandler mCommentClickHandler;

    RedditPostsAdapter(@NonNull Context context, AdapterOnItemClickHandler<RedditPost> clickHandler, AdapterOnItemClickHandler<RedditPost> commentClickHandler) {
        super(context, clickHandler);
        mCommentClickHandler = commentClickHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.post_item, viewGroup, false);
        return new PostsAdapterViewHolder(view);
    }

    @Override
    public void onBindMainViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RedditPost currentPost = (RedditPost) getItems().get(position);
        RedditPostData data = currentPost.getData();
        PostsAdapterViewHolder vh = (PostsAdapterViewHolder) viewHolder;
        vh.author.setText("/u/"+data.getAuthor()+" - "+data.getScore()+"\u2191");
        vh.title.setText(data.getTitle());
        String thumb = data.getThumbnail();
        if(thumb.length() > 0){
            Picasso.get().load(thumb).placeholder(R.drawable.ic_timer).error(R.drawable.ic_timer).into(vh.thumbnail);
        }else{
            if(data.getLink_flair_text()!=null && data.getLink_flair_text().equals("Media")){
                vh.thumbnail.setImageResource(R.drawable.ic_play);
            }else{
                vh.thumbnail.setImageResource(R.drawable.ic_timer);
            }
        }
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class PostsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final TextView author;
        final ImageButton comment;
        final ImageView thumbnail;

        PostsAdapterViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.post_title);
            author = view.findViewById(R.id.post_author);
            thumbnail = view.findViewById(R.id.post_thumbnail);
            comment = view.findViewById(R.id.post_link);
            comment.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onItemClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            RedditPost post = (RedditPost) getItems().get(adapterPosition);
            if(v.getId() == R.id.post_link){
                mCommentClickHandler.onItemClick(post);
            }else{
                getClickHandler().onItemClick(post);
            }
        }
    }

}
