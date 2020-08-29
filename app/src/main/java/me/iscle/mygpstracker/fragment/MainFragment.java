package me.iscle.mygpstracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import me.iscle.mygpstracker.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private Button track;
    private Button route;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentMainBinding binding = FragmentMainBinding.inflate(inflater, container, false);
        track = binding.track;
        route = binding.route;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        track.setOnClickListener(v -> Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToTrackFragment()));
        route.setOnClickListener(v-> Navigation.findNavController(v).navigate(MainFragmentDirections.actionMainFragmentToRouteFragment()));
    }
}