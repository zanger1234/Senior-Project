package com.example.seniorproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View SettingsView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);
        return SettingsView;
    }


}
