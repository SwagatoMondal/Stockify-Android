package com.aas.stockify.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aas.stockify.R;
import com.aas.stockify.SplashscreenActivity;
import com.aas.stockify.databinding.FragmentSettingsBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                setProfileDetails();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setProfileDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            binding.name.setText(user.getDisplayName());
            binding.email.setText(user.getEmail());
            if (user.getPhotoUrl() != null)
                Glide.with(this).load(user.getPhotoUrl()).into(binding.profileImage);
            binding.signOut.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                openSplash();
            });
        }
    }

    private void openSplash() {
        Intent homeIntent = new Intent(getContext(), SplashscreenActivity.class);
        startActivity(homeIntent);
        if (getActivity() != null)
            getActivity().finish();
        Toast.makeText(getContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
    }
}