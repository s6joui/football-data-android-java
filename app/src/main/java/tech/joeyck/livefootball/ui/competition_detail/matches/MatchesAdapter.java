package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.ScoreEntity;
import tech.joeyck.livefootball.ui.competition_picker.CompetitionPickerActivity;
import tech.joeyck.livefootball.utilities.DateUtils;
import tech.joeyck.livefootball.utilities.NetworkUtils;

import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

class MatchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_DEFAULT = 10;
    public static final int TYPE_FOOTER = 15;

    // The context we use to utility methods, app resources and layout inflaters
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our CompetitionAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onItemClick method whenever
     * an item is clicked in the list.
     */
    private final MatchesAdapter.MatchesAdapterOnItemClickHandler mClickHandler;

    private List<MatchEntity> mMatches;
    private LocalDateTime mLastUpdated;
    private boolean mHasFooter = false;

    /**
     * Creates a CompetitionAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    MatchesAdapter(@NonNull Context context, MatchesAdapter.MatchesAdapterOnItemClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (like ours does) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new CompetitionAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.table_footer, viewGroup, false);
            return new MatchesAdapter.FooterViewHolder(view);
        }
        int layoutId = R.layout.match_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new MatchesAdapter.MatchesAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param viewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_DEFAULT){
            MatchEntity currentMatch = mMatches.get(position);
            MatchesAdapterViewHolder vh = (MatchesAdapterViewHolder) viewHolder;
            vh.homeTeamNameText.setText(currentMatch.getHomeTeam().get("name"));
            vh.awayTeamNameText.setText(currentMatch.getAwayTeam().get("name"));
            vh.homeTeamScoreText.setText(currentMatch.getScore().getHomeTeamScore());
            vh.awayTeamScoreText.setText(currentMatch.getScore().getAwayTeamScore());

            String winner = currentMatch.getScore().getWinner();
            if(currentMatch.isFinished() && winner!=null && !winner.equals(ScoreEntity.DRAW)){
                if(winner.equals(ScoreEntity.HOME_TEAM_WINNER)){
                    vh.homeTeamNameText.setTypeface(null, Typeface.BOLD);
                    vh.awayTeamNameText.setTypeface(null, Typeface.NORMAL);
                    vh.homeTeamScoreText.setTypeface(null, Typeface.BOLD);
                    vh.awayTeamScoreText.setTypeface(null, Typeface.NORMAL);
                }else if(winner.equals(ScoreEntity.AWAY_TEAM_WINNER)){
                    vh.homeTeamNameText.setTypeface(null, Typeface.NORMAL);
                    vh.awayTeamNameText.setTypeface(null, Typeface.BOLD);
                    vh.homeTeamScoreText.setTypeface(null, Typeface.NORMAL);
                    vh.awayTeamScoreText.setTypeface(null, Typeface.BOLD);
                }
            }else{
                vh.homeTeamNameText.setTypeface(null, Typeface.NORMAL);
                vh.awayTeamNameText.setTypeface(null, Typeface.NORMAL);
                vh.homeTeamScoreText.setTypeface(null, Typeface.NORMAL);
                vh.awayTeamScoreText.setTypeface(null, Typeface.NORMAL);
            }

            LocalDateTime matchTime = currentMatch.getLocalDateTime();
            LocalDateTime now = NetworkUtils.hasNetwork(mContext) ? LocalDateTime.now() : currentMatch.getLastUpdatedLocalDateTime();
            vh.dateText.setText(DateUtils.getFormattedMatchDate(mContext,matchTime));
            vh.liveText.setVisibility(View.VISIBLE);
            vh.liveText.setTextColor(mContext.getResources().getColor(R.color.green));
            vh.liveText.setTypeface(null, Typeface.BOLD);
            if(currentMatch.isInSecondHalf()) {
                long mins = MINUTES.between(matchTime, now) - 15;
                vh.liveText.setText("LIVE " + mins + "'");
            }else if(currentMatch.isInPlay()){
                vh.liveText.setText("LIVE " + MINUTES.between(matchTime, now) + "'");
            }else if(currentMatch.isPaused()){
                vh.liveText.setText(R.string.half_time);
            }else if(currentMatch.isFinished()){
                vh.liveText.setTypeface(null, Typeface.NORMAL);
                vh.liveText.setTextColor(mContext.getResources().getColor(R.color.gray));
                vh.liveText.setText(R.string.full_time);
            }else{
                vh.liveText.setVisibility(View.INVISIBLE);
            }
        }else if(type == TYPE_FOOTER){
            if(mLastUpdated!=null){
                FooterViewHolder vh = (FooterViewHolder) viewHolder;
                vh.footerText.setText(DateUtils.getLastUpdatedString(mContext,mLastUpdated));
            }
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mMatches) return 0;
        if (mHasFooter) return mMatches.size() + 1;
        return mMatches.size();
    }

    /**
     * Swaps the list used by the CompetitionAdapter for its weather data. This method is called by
     * {@link CompetitionPickerActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param matches the new list of forecasts to use as CompetitionAdapter's data source
     */
    void swapMatches(final List<MatchEntity> matches) {
        mMatches = matches;
        notifyDataSetChanged();
    }

    void setLastUpdated(final LocalDateTime mLastUpdated){
        mHasFooter = true;
        this.mLastUpdated = mLastUpdated;
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface MatchesAdapterOnItemClickHandler {
        void onItemClick(MatchEntity match);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==mMatches.size()){
            return TYPE_FOOTER;
        }
        return TYPE_DEFAULT;
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class MatchesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView homeTeamNameText;
        final TextView awayTeamNameText;
        final TextView homeTeamScoreText;
        final TextView awayTeamScoreText;
        final TextView liveText;
        final TextView dateText;

        MatchesAdapterViewHolder(View view) {
            super(view);
            homeTeamNameText = view.findViewById(R.id.home_name_text);
            awayTeamNameText = view.findViewById(R.id.away_name_text);
            homeTeamScoreText = view.findViewById(R.id.home_score_text);
            awayTeamScoreText = view.findViewById(R.id.away_score_tex);
            liveText = view.findViewById(R.id.live_text);
            dateText = view.findViewById(R.id.date_text);
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
            MatchEntity matchEntity = mMatches.get(adapterPosition);
            mClickHandler.onItemClick(matchEntity);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        final TextView footerText;

        FooterViewHolder(View view) {
            super(view);
            footerText = view.findViewById(R.id.footerText);
        }

    }

}
