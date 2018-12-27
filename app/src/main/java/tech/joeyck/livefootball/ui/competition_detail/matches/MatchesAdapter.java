package tech.joeyck.livefootball.ui.competition_detail.matches;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;

import java.util.Objects;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.MatchEntity;
import tech.joeyck.livefootball.data.database.ScoreEntity;
import tech.joeyck.livefootball.ui.BaseAdapter;
import tech.joeyck.livefootball.utilities.DateUtils;
import tech.joeyck.livefootball.utilities.NetworkUtils;

import static org.threeten.bp.temporal.ChronoUnit.MINUTES;

class MatchesAdapter extends BaseAdapter<MatchEntity> {

    MatchesAdapter(@NonNull Context context, AdapterOnItemClickHandler<MatchEntity> clickHandler) {
        super(context, clickHandler);
    }

    @Override
    public RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.match_item, viewGroup, false);
        return new MatchesAdapterViewHolder(view);
    }

    @Override
    public void onBindMainViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MatchEntity currentMatch = (MatchEntity) getItems().get(position);
        MatchesAdapterViewHolder vh = (MatchesAdapterViewHolder) viewHolder;
        vh.homeTeamNameText.setText(currentMatch.getHomeTeam().get("name"));
        vh.awayTeamNameText.setText(currentMatch.getAwayTeam().get("name"));
        vh.homeTeamScoreText.setText(currentMatch.getScore().getHomeTeamScore());
        vh.awayTeamScoreText.setText(currentMatch.getScore().getAwayTeamScore());

        int homeTeamId = Integer.parseInt(Objects.requireNonNull(currentMatch.getHomeTeam().get("id")));
        int awayTeamId = Integer.parseInt(Objects.requireNonNull(currentMatch.getAwayTeam().get("id")));
        String homeCrestUrl = NetworkUtils.getCrestUrl(homeTeamId,NetworkUtils.IMAGE_QUALITY_SD);
        String awayCrestUrl = NetworkUtils.getCrestUrl(awayTeamId,NetworkUtils.IMAGE_QUALITY_SD);
        Picasso.get().load(homeCrestUrl).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(vh.crestHome);
        Picasso.get().load(awayCrestUrl).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(vh.crestAway);

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
        LocalDateTime now = NetworkUtils.hasNetwork(getContext()) ? LocalDateTime.now() : currentMatch.getLastUpdatedLocalDateTime();
        vh.dateText.setText(DateUtils.getFormattedMatchDate(getContext(),matchTime));
        vh.liveText.setVisibility(View.VISIBLE);
        vh.liveText.setTextColor(getContext().getResources().getColor(R.color.green));
        vh.liveText.setTypeface(null, Typeface.BOLD);
        if(currentMatch.isInSecondHalf()) {
            long mins = MINUTES.between(matchTime, now) - 15;
            vh.liveText.setText(getContext().getResources().getString(R.string.live,mins));
        }else if(currentMatch.isInPlay()){
            vh.liveText.setText(getContext().getResources().getString(R.string.live,MINUTES.between(matchTime, now)));
        }else if(currentMatch.isPaused()){
            vh.liveText.setText(R.string.half_time);
        }else if(currentMatch.isFinished()){
            vh.liveText.setTypeface(null, Typeface.NORMAL);
            vh.liveText.setTextColor(getContext().getResources().getColor(R.color.gray));
            vh.liveText.setText(R.string.full_time);
        }else{
            vh.liveText.setVisibility(View.INVISIBLE);
        }
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
        final ImageView crestHome;
        final ImageView crestAway;

        MatchesAdapterViewHolder(View view) {
            super(view);
            homeTeamNameText = view.findViewById(R.id.home_name_text);
            awayTeamNameText = view.findViewById(R.id.away_name_text);
            homeTeamScoreText = view.findViewById(R.id.home_score_text);
            awayTeamScoreText = view.findViewById(R.id.away_score_tex);
            crestHome = view.findViewById(R.id.crestHomeTeam);
            crestAway = view.findViewById(R.id.crestAwayTeam);
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
            MatchEntity matchEntity = (MatchEntity) getItems().get(adapterPosition);
            getClickHandler().onItemClick(matchEntity);
        }
    }

}
