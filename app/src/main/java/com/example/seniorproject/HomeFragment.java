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

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    ArrayList<RecyclerItems> recyclerItems;
    ArrayList<RecyclerItems> recyclerItems2;
    RecyclerViewAdapterHome recyclerViewAdapter;
    RecyclerViewAdapterHome recyclerViewAdapter2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerItems = new ArrayList<>();
        recyclerItems2 = new ArrayList<>();

        recyclerView = homeView.findViewById(R.id.recyclerView1);
        recyclerView2 = homeView.findViewById(R.id.recyclerView2);


        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Page Loading...");
        progressDialog.show();

        ParseQuery<ParseObject> query = new ParseQuery<>("Inventory");
        query.findInBackground((objects, e) -> {
            progressDialog.dismiss();
            if (e == null) {
                for (ParseObject object : objects) {
                    ParseFile itemImage = object.getParseFile("itemImage");
                    final String itemName = object.getString("itemName");
                    final String itemPrice = Objects.requireNonNull(object.getNumber("itemPrice")).toString();
                    final Integer itemQuantity = (Integer) Objects.requireNonNull(object.getNumber("itemQuantity"));
                    if (itemImage != null) {
                        byte[] imageData = new byte[0];
                        try {
                            imageData = itemImage.getData();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        if (itemQuantity != 0) {
                            recyclerItems.add(new RecyclerItems(object.getObjectId(), bmp, itemName, itemPrice + " JD"));
                            recyclerItems2.add(new RecyclerItems(object.getObjectId(), bmp, itemName, itemPrice + " JD"));
                        } else {
                            recyclerItems.add(new RecyclerItems(object.getObjectId(), bmp, itemName, "Out of Stock"));
                            recyclerItems2.add(new RecyclerItems(object.getObjectId(), bmp, itemName, "Out of Stock"));
                        }
                    }
                }
            }


            LinearLayoutManager layoutManager = new LinearLayoutManager(HomeFragment.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAdapter = new RecyclerViewAdapterHome(HomeFragment.this.getActivity(), recyclerItems);
            recyclerView.setAdapter(recyclerViewAdapter);


            LinearLayoutManager layoutManager2 = new LinearLayoutManager(HomeFragment.this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView2.setLayoutManager(layoutManager2);
            recyclerView2.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAdapter = new RecyclerViewAdapterHome(HomeFragment.this.getActivity(), recyclerItems);
            recyclerViewAdapter2 = new RecyclerViewAdapterHome(HomeFragment.this.getActivity(), recyclerItems);
            recyclerView2.setAdapter(recyclerViewAdapter);

        });
        return homeView;
    }
}

