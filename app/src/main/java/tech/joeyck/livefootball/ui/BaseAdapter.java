package tech.joeyck.livefootball.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tech.joeyck.livefootball.R;

import static android.view.View.NO_ID;
import static tech.joeyck.livefootball.ui.BaseAdapter.BaseAdapterItem.TYPE_DEFAULT;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final AdapterOnItemClickHandler<T> mClickHandler;

    private List<BaseAdapterItem> mItems;

    public BaseAdapter(@NonNull Context context, AdapterOnItemClickHandler<T> clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        //setHasStableIds(true);
    }

    public abstract RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void onBindMainViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_DEFAULT){
            return onCreateMainViewHolder(viewGroup,viewType);
        }
        return onCreateHeaderViewHolder(viewGroup,viewType);
    }

    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(mContext).inflate(viewType, viewGroup, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_DEFAULT){
            onBindMainViewHolder(viewHolder,position);
        }else{
            onBindHeaderViewHolder(viewHolder,position);
        }
    }

    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        TextViewHolder vh = (TextViewHolder)viewHolder;
        vh.text.setText(((TextItem)mItems.get(position)).getTitle());
    }

    @Override
    public long getItemId(int position) {
        int type = getItemViewType(position);
        if (type == TYPE_DEFAULT) {
            return mItems.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        if(mItems!=null) return mItems.size();
        return 0;
    }

    public void swapItems(List<? extends BaseAdapterItem> items) {
        mItems = new ArrayList<>();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void addHeader(int position, final String headerText){
        addHeader(position,headerText,R.layout.table_header);
    }

    public void addHeader(int position, final String headerText, int layout){
        mItems.add(position,new HeaderItem(headerText,layout));
        notifyItemRangeInserted(position,1);
    }

    public List<? extends BaseAdapterItem> getItems() {
        return mItems;
    }

    public Context getContext() {
        return mContext;
    }

    public AdapterOnItemClickHandler<T> getClickHandler() {
        return mClickHandler;
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface AdapterOnItemClickHandler<T> {
        void onItemClick(T item);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        public TextViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.mainText);
        }

    }

    public static class HeaderItem extends TextItem implements BaseAdapterItem {
        int mLayout;

        public HeaderItem(String text,int layout) {
            super(text);
            this.mLayout = layout;
        }

        @Override
        public int getType() {
            return mLayout;
        }

        @Override
        public int getId() {
            return NO_ID;
        }
    }

    public static class TextItem {

        private String title;

        TextItem(String text) {
            this.title = text.toUpperCase();
        }

        public String getTitle() {
            return title;
        }

    }

    public interface BaseAdapterItem {

        int TYPE_DEFAULT = 14;
        int TYPE_HEADER = R.layout.table_header;

        int getType();
        int getId();

    }

}
