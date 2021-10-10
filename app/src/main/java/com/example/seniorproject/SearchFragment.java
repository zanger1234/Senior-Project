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

import java.util.ArrayList;
import java.util.Objects;


public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<RecyclerItems> recyclerItems;
    RecyclerViewAdapterSearch recyclerViewAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View sView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerItems = new ArrayList<>();
        recyclerView = sView.findViewById(R.id.recyclerViewSearch);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Page Loading...");
        progressDialog.show();

        assert getArguments() != null;
        String searchQuery = getArguments().getString("query");
        System.out.println(searchQuery);
        ParseQuery<ParseObject> query = new ParseQuery<>("Inventory");
        query.whereMatches("itemName", searchQuery, "i");
        query.findInBackground((objects, e) -> {
            progressDialog.dismiss();
            if (e == null) {
                for (ParseObject object : objects) {
                    ParseFile itemImage = object.getParseFile("itemImage");
                    final String itemName = object.getString("itemName");
                    final String itemBrand = object.getString("itemBrand");
                    final String itemPrice = Objects.requireNonNull(object.getNumber("itemPrice")).toString();
                    if (itemImage != null) {
                        byte[] imageData = new byte[0];
                        try {
                            imageData = itemImage.getData();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        recyclerItems.add(new RecyclerItems(object.getObjectId(), bmp, itemName + " (" + itemBrand + ")", itemPrice + " JD"));

                    }
                }
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchFragment.this.getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAdapter = new RecyclerViewAdapterSearch(SearchFragment.this.getActivity(), recyclerItems);
            recyclerView.setAdapter(recyclerViewAdapter);
        });


        return sView;
    }


}
