package com.example.jsontrial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Model> items = new ArrayList<>();
    private List<Model> itemsFull;
    private final Context mContext;

    public RecyclerViewAdapter(ArrayList<Model> items, Context mContext) {
        this.items = items;
        this.itemsFull = new ArrayList<>(items);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.creator.setText(items.get(position).getCreator());
        holder.likes.setText("" + items.get(position).getLikes());

        String imageURL = items.get(position).getImageResource();
        String userImageURL = items.get(position).getUserImageResource();

        Glide.with(mContext).load(imageURL).fitCenter().centerInside().placeholder(R.drawable.ic_launcher_foreground).into(holder.imageView);
        Glide.with(mContext).load(imageURL).fitCenter().centerInside().placeholder(R.drawable.ic_launcher_foreground).into(holder.creatorImageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(mContext, "Creator of image " + items.get(position).getCreator(), Toast.LENGTH_SHORT, true).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        CircleImageView creatorImageView;
        TextView creator, likes;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            creator = itemView.findViewById(R.id.user);
            likes = itemView.findViewById(R.id.likes);
            creatorImageView = itemView.findViewById(R.id.creator_image_view);

            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Model> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(itemsFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Model item : itemsFull) {
                    if(item.getCreator().toLowerCase().contains(filterPattern))
                        filteredList.add(item);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            items.clear();
            items.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
