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

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.ViewHolder> {
    ArrayList<RecyclerItems> recyclerItems;
    Context context;

    public RecyclerViewAdapterHome(Context context, ArrayList<RecyclerItems> recyclerItems) {
        this.context = context;
        this.recyclerItems = recyclerItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_home, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemImage.setImageBitmap(recyclerItems.get(position).rImage);
        holder.itemText.setText(recyclerItems.get(position).rName);
        holder.itemTextPrice.setText(recyclerItems.get(position).rPrice);
        holder.itemFavorite.setOnClickListener(view -> {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            if (ParseUser.getCurrentUser() != null) {
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
                    if (e == null) {
                        ParseQuery<ParseObject> query2 = new ParseQuery<>("Inventory");
                        query2.getInBackground(recyclerItems.get(position).rObjectId, (object2, e1) -> {
                            if (e1 == null) {
                                object.getRelation("inventory_favorites").add(object2);
                                try {
                                    object.save();
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
        bundle.putString("objectId", recyclerItems.get(position).rObjectId);
        holder.itemMain.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.itemFragment, bundle));

    }


    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemText;
        TextView itemTextPrice;
        CardView itemFavorite;
        CardView itemMain;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.imageItem);
            itemText = itemView.findViewById(R.id.textItemName);
            itemTextPrice = itemView.findViewById(R.id.textItemPrice);
            itemFavorite = itemView.findViewById(R.id.add_favorite);
            itemMain = itemView.findViewById(R.id.cardViewItem);

        }
    }


}
