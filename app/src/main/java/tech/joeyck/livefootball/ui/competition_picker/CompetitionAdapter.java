package tech.joeyck.livefootball.ui.competition_picker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.joeyck.livefootball.R;
import tech.joeyck.livefootball.data.database.CompetitionEntity;
import tech.joeyck.livefootball.utilities.CompetitionUtils;

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.CompetitionAdapterViewHolder> {

    // The context we use to utility methods, app resources and layout inflaters
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our CompetitionAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onItemClick method whenever
     * an item is clicked in the list.
     */
    private final CompetitionAdapterOnItemClickHandler mClickHandler;

    private List<CompetitionEntity> mCompetitions;

    /**
     * Creates a CompetitionAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    CompetitionAdapter(@NonNull Context context, CompetitionAdapterOnItemClickHandler clickHandler) {
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
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new CompetitionAdapterViewHolder that holds the View for each list item
     */
    @Override
    public CompetitionAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId = R.layout.competition_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);
        return new CompetitionAdapterViewHolder(view);
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
    public void onBindViewHolder(CompetitionAdapterViewHolder viewHolder, int position) {
        int reorderedPosition = CompetitionUtils.getReorderedPosition(position);
        CompetitionEntity currentCompetition = mCompetitions.get(reorderedPosition);
        viewHolder.textView.setText(currentCompetition.getName());
        viewHolder.countryTextView.setText(currentCompetition.getArea().getName());
        viewHolder.cardView.setCardBackgroundColor(mContext.getResources().getColor(CompetitionUtils.getColorResourceId(currentCompetition.getId())));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mCompetitions) return 0;
        return mCompetitions.size();
    }

    /**
     * Swaps the list used by the CompetitionAdapter for its weather data. This method is called by
     * {@link CompetitionPickerActivity} after a load has finished. When this method is called, we assume we have
     * a new set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newForecast the new list of forecasts to use as CompetitionAdapter's data source
     */
    void swapCompetitions(final List<CompetitionEntity> newForecast) {
        mCompetitions = newForecast;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface CompetitionAdapterOnItemClickHandler {
        void onItemClick(CompetitionEntity competition);
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class CompetitionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final CardView cardView;
        final TextView textView;
        final TextView countryTextView;

        CompetitionAdapterViewHolder(View view) {
            super(view);

            cardView = (CardView) view;
            textView = view.findViewById(R.id.competition_name);
            countryTextView = view.findViewById(R.id.competition_country);

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
            int adapterPosition = CompetitionUtils.getReorderedPosition(getAdapterPosition());
            CompetitionEntity competitionEntity = mCompetitions.get(adapterPosition);
            mClickHandler.onItemClick(competitionEntity);
        }
    }
}