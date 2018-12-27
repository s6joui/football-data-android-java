package tech.joeyck.livefootball.ui.competition_detail.standings;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.TableEntryEntity;
import tech.joeyck.livefootball.ui.BaseAdapter;
import tech.joeyck.livefootball.utilities.NetworkUtils;

public class StandingsTableAdapter extends BaseAdapter<TableEntryEntity> {

    StandingsTableAdapter(@NonNull Context context, AdapterOnItemClickHandler<TableEntryEntity> clickHandler) {
        super(context,clickHandler);
    }

    @Override
    public RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.table_item, viewGroup, false);
        view.setFocusable(true);
        return new CompetitionAdapterViewHolder(view);
    }

    @Override
    public void onBindMainViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CompetitionAdapterViewHolder holder = (CompetitionAdapterViewHolder) viewHolder;
        TableEntryEntity tableEntry = (TableEntryEntity) getItems().get(position);
        holder.teamNameTextView.setText(tableEntry.getTeam().getName());
        holder.pointsTextView.setText(String.valueOf(tableEntry.getPoints()));
        holder.positionTextView.setText(String.valueOf(tableEntry.getPosition()));
        holder.playedTextView.setText(String.valueOf(tableEntry.getPlayedGames()));
        holder.lostTextView.setText(String.valueOf(tableEntry.getLost()));
        holder.drawnTextView.setText(String.valueOf(tableEntry.getDraw()));
        holder.wonTextView.setText(String.valueOf(tableEntry.getWon()));
        String crestUrl = NetworkUtils.getCrestUrl(tableEntry.getTeam().getId(),NetworkUtils.IMAGE_QUALITY_SD);
        Picasso.get().load(crestUrl).placeholder(R.drawable.default_crest).error(R.drawable.default_crest).into(holder.crestView);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class CompetitionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView crestView;
        final TextView teamNameTextView;
        final TextView positionTextView;
        final TextView pointsTextView;
        final TextView playedTextView;
        final TextView wonTextView;
        final TextView drawnTextView;
        final TextView lostTextView;

        CompetitionAdapterViewHolder(View view) {
            super(view);
            crestView = view.findViewById(R.id.team_crest);
            teamNameTextView = view.findViewById(R.id.team_name);
            pointsTextView = view.findViewById(R.id.team_points);
            positionTextView = view.findViewById(R.id.team_position);
            playedTextView = view.findViewById(R.id.matches_played);
            wonTextView = view.findViewById(R.id.matches_won);
            drawnTextView = view.findViewById(R.id.matches_drawn);
            lostTextView = view.findViewById(R.id.matches_lost);
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
            TableEntryEntity tableEntryEntity = (TableEntryEntity)getItems().get(adapterPosition);
            getClickHandler().onItemClick(tableEntryEntity);
        }
    }

}