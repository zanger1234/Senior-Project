package com.example.seniorproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class RecyclerViewAdapterFavorites extends RecyclerView.Adapter<RecyclerViewAdapterFavorites.ViewHolder> {
    ArrayList<RecyclerItems> recyclerItems2;
    Context context;

    public RecyclerViewAdapterFavorites(Context context, ArrayList<RecyclerItems> recyclerItems2) {
        this.context = context;
        this.recyclerItems2 = recyclerItems2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_favorites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemImage.setImageBitmap(recyclerItems2.get(position).rImage);
        holder.itemTextName.setText(recyclerItems2.get(position).rName);
        holder.textItemPrice.setText(recyclerItems2.get(position).rPrice);
        holder.removeFavorite.setOnClickListener(view -> {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            if (ParseUser.getCurrentUser() != null) {
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
                    if (e == null) {
                        ParseQuery<ParseObject> query2 = new ParseQuery<>("Inventory");
                        query2.getInBackground(recyclerItems2.get(position).rObjectId, (object2, e1) -> {
                            if (e1 == null) {
                                object.getRelation("inventory_favorites").remove(object2);
                                try {
                                    object.save();
                                    recyclerItems2.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, recyclerItems2.size());
                                } catch (ParseException parseException) {
                                    parseException.printStackTrace();
                                }
                            } else {
                                e1.printStackTrace();
                            }
                        });
                    } else {
                        e.printStackTrace();
                    }
                });
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("objectId", recyclerItems2.get(position).rObjectId);
        holder.itemMain.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.itemFragment, bundle));

    }

    @Override
    public int getItemCount() {
        return recyclerItems2.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTextName;
        TextView textItemPrice;
        CardView removeFavorite;
        CardView itemMain;


        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.imageItem3);
            itemTextName = itemView.findViewById(R.id.textItemName3);
            textItemPrice = itemView.findViewById(R.id.textItemPrice3);
            removeFavorite = itemView.findViewById(R.id.remove_favorite);
            itemMain = itemView.findViewById(R.id.cardViewItem3);

        }
    }
}
