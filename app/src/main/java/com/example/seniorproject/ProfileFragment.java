package com.example.seniorproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        Button profileButtonLogout = profileView.findViewById(R.id.profileLogout);
        if (currentUser != null) {
            profileButtonLogout.setOnClickListener(v -> {
                Toast.makeText(getActivity(), "You have logged out successfully, " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
                ParseUser.logOutInBackground();
                Intent intentLogout = new Intent(getActivity(), MainActivity.class);
                startActivity(intentLogout);
                requireActivity().finish();
            });

        }

        Button profileButtonChangePassword = profileView.findViewById(R.id.profileChangePassword);

        ParseUser parseUser = ParseUser.getCurrentUser();
        profileButtonChangePassword.setOnClickListener(v ->
                ParseUser.requestPasswordResetInBackground(parseUser.getEmail(), e -> {
                    if (e == null) {
                        Toast.makeText(getActivity(), "A password reset request was sent to your registered email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }));


        return profileView;
    }


}
