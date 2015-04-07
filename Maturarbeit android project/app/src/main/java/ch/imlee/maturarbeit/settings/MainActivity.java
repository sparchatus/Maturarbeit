package ch.imlee.maturarbeit.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import ch.imlee.maturarbeit.R;


public class MainActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

    }
}
