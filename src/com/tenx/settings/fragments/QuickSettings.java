/*
 * Copyright (C) 2020 TenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tenx.settings.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.CustomSeekBarPreference;

public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PREF_COLUMNS_PORTRAIT = "qs_columns_portrait";
    private static final String PREF_COLUMNS_LANDSCAPE = "qs_columns_landscape";

    private CustomSeekBarPreference mQsColumnsPortrait;
    private CustomSeekBarPreference mQsColumnsLandscape;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_quicksettings);
        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mQsColumnsPortrait = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_PORTRAIT);
        int columnsPortrait = Settings.System.getIntForUser(resolver,
                Settings.System.OMNI_QS_LAYOUT_COLUMNS, 4, UserHandle.USER_CURRENT);
        mQsColumnsPortrait.setValue(columnsPortrait);
        mQsColumnsPortrait.setOnPreferenceChangeListener(this);

        mQsColumnsLandscape = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_LANDSCAPE);
        int columnsLandscape = Settings.System.getIntForUser(resolver,
                Settings.System.OMNI_QS_LAYOUT_COLUMNS_LANDSCAPE, 4, UserHandle.USER_CURRENT);
        mQsColumnsLandscape.setValue(columnsLandscape);
        mQsColumnsLandscape.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    	ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mQsColumnsPortrait) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(resolver,
                    Settings.System.OMNI_QS_LAYOUT_COLUMNS, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsColumnsLandscape) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(resolver,
                    Settings.System.OMNI_QS_LAYOUT_COLUMNS_LANDSCAPE, value, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
