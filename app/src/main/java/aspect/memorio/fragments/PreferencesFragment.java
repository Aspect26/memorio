package aspect.memorio.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import aspect.memorio.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        this.setPreferencesFromResource(R.xml.preferences, rootKey);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.toolbar_title_preferences);
    }

}
