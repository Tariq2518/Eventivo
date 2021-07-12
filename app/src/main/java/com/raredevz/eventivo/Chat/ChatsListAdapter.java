package com.raredevz.eventivo.Chat;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ViewHolder> implements Filterable {

    Context mContext;
    ArrayList<ChatMessage> items;

    private SparseBooleanArray selectedItems;
    ArrayList<ChatMessage> original_items=new ArrayList<>();
    ArrayList<ChatMessage> filtered_items=new ArrayList<>();

    private ItemFilter mFilter;
    private SettingsAPI set;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener monItemLongClickListener;

    private int lastPosition=-1;

    public ChatsListAdapter(Context mContext, ArrayList<ChatMessage> items) {
        this.mContext = mContext;
        this.items = items;

        original_items = items;
        filtered_items = items;
        selectedItems = new SparseBooleanArray();
    }

    //int selectedItemCount=selectedItems.size();

    public interface OnItemClickListener{
        void onItemClick(View view, ChatMessage obj, int position);
    }
    interface OnItemLongClickListener{
        void onItemClick(View view, ChatMessage obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.row_chats,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        set=new SettingsAPI(mContext);
        final ChatMessage c=filtered_items.get(position);
        if (filtered_items.get(position).getFriendId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
        && (filtered_items.get(position).getRead()==false)){
            holder.unreadDot.setVisibility(View.VISIBLE);
        }else {
            holder.unreadDot.setVisibility(View.INVISIBLE);
        }

        holder.content.setText(c.getText());
        if (c.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.title.setText(c.getFriendName());
            Picasso.get().load(c.getFriendPhoto()).resize(100,100)
                    .transform(new CircleTransform()).into(holder.image);
        }else if (c.getFriendId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.title.setText(c.getSenderName());
            Picasso.get().load(c.getSenderPhoto()).resize(100,100)
                    .transform(new CircleTransform()).into(holder.image);
        }

        setAnimation(holder.itemView,position);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view,c,position);
            }
        });

        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                monItemLongClickListener.onItemClick(view,c,position);
                return false;
            }
        });

        holder.lyt_parent.setActivated(selectedItems.get(position,false));

    }
    private void setAnimation(View viewToAnimate, int position){
        if (position>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition=position;
        }
    }

    List<ChatMessage> getSelectedItems(){
        ArrayList<ChatMessage> items=new ArrayList<>();
        for (int i=0;i<selectedItems.size();i++){
            items.add(filtered_items.get(selectedItems.keyAt(i)));
        }
        return items;
    }

    @Override
    public int getItemCount() {
        return filtered_items.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,content;
        ImageView image;
        LinearLayout lyt_parent,unreadDot;
        public ViewHolder(@NonNull View v) {
            super(v);
            title = (TextView)v.findViewById(R.id.title);
                    content = (TextView) v.findViewById(R.id.content);
                    image = (ImageView) v.findViewById(R.id.image);
                    lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
                    unreadDot = (LinearLayout) v.findViewById(R.id.unread);
        }
    }



    private class ItemFilter extends
            Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String query=charSequence.toString().toLowerCase();
            FilterResults results=new FilterResults();
            ArrayList<ChatMessage> list=original_items;
            ArrayList<ChatMessage> result_list=new ArrayList<>(list.size());

            for (int i=0;i<list.size();i++){
                String str_title=list.get(i).getFriendName();
                if (str_title.toLowerCase().contains(query)){
                    result_list.add(list.get(i));
                }
            }
            results.values=result_list;
            results.count=result_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered_items=(ArrayList<ChatMessage>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
