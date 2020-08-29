package me.iscle.mygpstracker.fragment;

import androidx.fragment.app.Fragment;

import me.iscle.mygpstracker.MyGPSTracker;

public class BaseFragment extends Fragment {
    public MyGPSTracker getMyGPSTracker() {
        return (MyGPSTracker) getActivity().getApplication();
    }
}
