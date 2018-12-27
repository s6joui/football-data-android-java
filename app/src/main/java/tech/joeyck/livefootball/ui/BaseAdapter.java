package tech.joeyck.livefootball.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tech.joeyck.livefootball.R;

import static tech.joeyck.livefootball.ui.BaseAdapter.BaseAdapterItem.TYPE_DEFAULT;
import static tech.joeyck.livefootball.ui.BaseAdapter.BaseAdapterItem.TYPE_FOOTER;
import static tech.joeyck.livefootball.ui.BaseAdapter.BaseAdapterItem.TYPE_HEADER;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final Context mContext;
    public final AdapterOnItemClickHandler<T> mClickHandler;

    public List<BaseAdapterItem> mItems;

    public BaseAdapter(@NonNull Context context, AdapterOnItemClickHandler<T> clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public abstract RecyclerView.ViewHolder onCreateMainViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void onBindMainViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_FOOTER){
            return onCreateFooterViewHolder(viewGroup,viewType);
        }else if(viewType == TYPE_HEADER){
            return onCreateHeaderViewHolder(viewGroup,viewType);
        }else if(viewType == TYPE_DEFAULT){
            return onCreateMainViewHolder(viewGroup,viewType);
        }
        return onCreateMainViewHolder(viewGroup,viewType);
    }

    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_footer, viewGroup, false);
        return new TextViewHolder(view);
    }

    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.table_header, viewGroup, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_DEFAULT){
            onBindMainViewHolder(viewHolder,position);
        }else if(type == TYPE_FOOTER){
            onBindFooterViewHolder(viewHolder,position);
        }else if(type == TYPE_HEADER){
            onBindHeaderViewHolder(viewHolder,position);
        }
    }

    public void onBindFooterViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        TextViewHolder vh = (TextViewHolder)viewHolder;
        vh.text.setText(((TextItem)mItems.get(position)).getTitle());
    }

    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        TextViewHolder vh = (TextViewHolder)viewHolder;
        vh.text.setText(((TextItem)mItems.get(position)).getTitle());
    }

    @Override
    public int getItemCount() {
        if(mItems!=null) return mItems.size();
        return 0;
    }

    public void swapItems(List<T> items) {
        mItems = (List<BaseAdapterItem>) items;
        notifyDataSetChanged();
    }

    public void swapRawItems(List<BaseAdapterItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addHeader(int position, final String headerText){
        mItems.add(position,new HeaderItem(headerText));
        notifyItemRangeInserted(position,1);
    }

    public void addFooter(int position, final String footerText){
        mItems.add(position,new FooterItem(footerText));
        notifyItemRangeInserted(position,1);
    }

    /**
     * The interface that receives onItemClick messages.
     */
    public interface AdapterOnItemClickHandler<T> {
        void onItemClick(T match);
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
        public HeaderItem(String text) {
            super(text);
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }

    public static class FooterItem extends TextItem implements BaseAdapterItem {
        FooterItem(String text) {
            super(text);
        }
        @Override
        public int getType() {
            return TYPE_FOOTER;
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
        int TYPE_FOOTER = R.layout.table_footer;
        int TYPE_HEADER = R.layout.table_header;

        int getType();

    }

}
