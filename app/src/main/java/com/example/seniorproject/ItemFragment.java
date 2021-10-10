package com.example.seniorproject;

import android.app.ProgressDialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.Objects;


public class ItemFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_item, container, false);

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Page Loading...");
        progressDialog.show();

        assert getArguments() != null;
        String objectId = getArguments().getString("objectId");
        ParseQuery<ParseObject> query = new ParseQuery<>("Inventory");
        query.getInBackground(objectId, (object, e) -> {
            progressDialog.dismiss();
            if (e == null) {
                ParseFile itemImage = object.getParseFile("itemImage");
                final String itemName = object.getString("itemName");
                final String itemBrand = object.getString("itemBrand");
                final String itemDescription = object.getString("itemDescription");
                final String itemCategory = object.getString("itemCategory");
                final String itemPrice = Objects.requireNonNull(object.getNumber("itemPrice")).toString();
                if (itemImage != null) {
                    byte[] imageData = new byte[0];
                    try {
                        imageData = itemImage.getData();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                    ImageView iImage = itemView.findViewById(R.id.imageItemMain);
                    iImage.setImageBitmap(bmp);
                }
                String itemNBC = itemName + ", " + itemBrand + " - " + itemCategory;
                String itemPriceN = itemPrice + " JOD";

                TextView iName = itemView.findViewById(R.id.textItemNameMain);
                TextView iPrice = itemView.findViewById(R.id.textItemPriceMain);
                TextView iDesc = itemView.findViewById(R.id.textItemDescriptionMain);

                iName.setText(itemNBC);
                iPrice.setText(itemPriceN);
                iDesc.setText(itemDescription);
            }
        });

        return itemView;
    }
}

