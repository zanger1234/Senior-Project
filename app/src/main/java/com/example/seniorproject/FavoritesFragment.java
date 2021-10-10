package com.example.seniorproject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Objects;

public class FavoritesFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecyclerItems> recyclerItems;
    RecyclerViewAdapterFavorites recyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View favoritesView = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerItems = new ArrayList<>();
        recyclerView = favoritesView.findViewById(R.id.recyclerViewFavorites);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Page Loading...");
        progressDialog.show();


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        if (ParseUser.getCurrentUser() != null) {
            query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
                if (e == null) {
                    ParseQuery<ParseObject> query2 = object.getRelation("inventory_favorites").getQuery();
                    query2.findInBackground((objects, e1) -> {
                        progressDialog.dismiss();
                        if (e1 == null) {
                            for (ParseObject object2 : objects) {
                                ParseFile itemImage = object2.getParseFile("itemImage");
                                final String itemName = object2.getString("itemName");
                                final String itemBrand = object2.getString("itemBrand");
                                final String itemPrice = Objects.requireNonNull(object2.getNumber("itemPrice")).toString();

                                if (itemImage != null) {
                                    byte[] imageData = new byte[0];
                                    try {
                                        imageData = itemImage.getData();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                                    recyclerItems.add(new RecyclerItems(object2.getObjectId(), bmp, itemName+", " + itemBrand, itemPrice + " JD"));
                                }
                            }
                        } else
                            e1.printStackTrace();

                        LinearLayoutManager layoutManager = new LinearLayoutManager(FavoritesFragment.this.getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewAdapter = new RecyclerViewAdapterFavorites(FavoritesFragment.this.getActivity(), recyclerItems);
                        recyclerView.setAdapter(recyclerViewAdapter);

                    });
                } else
                    e.printStackTrace();
            });
        }
        return favoritesView;
    }
}
