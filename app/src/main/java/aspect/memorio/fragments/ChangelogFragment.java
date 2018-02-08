package aspect.memorio.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aspect.memorio.R;


public class ChangelogFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changelog, container, false);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.toolbar_title_changelog);

        return view;
    }

}
