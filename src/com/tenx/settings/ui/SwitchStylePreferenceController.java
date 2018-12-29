/*
 * Copyright (C) 2018 Havoc-OS
 *           (C) 2021 TenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless reqswitchred by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.tenx.settings.ui;

import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settingslib.core.AbstractPreferenceController;

import com.tenx.support.preferences.SystemSettingListPreference;

import java.util.ArrayList;
import java.util.List;

public class SwitchStylePreferenceController extends AbstractPreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String SWITCH_STYLE = "switch_style";
    private SystemSettingListPreference mSwitchStyle;

    public SwitchStylePreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return SWITCH_STYLE;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mSwitchStyle = (SystemSettingListPreference) screen.findPreference(SWITCH_STYLE);
        int switchStyle = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.SWITCH_STYLE, 2);
        int valueIndex = mSwitchStyle.findIndexOfValue(String.valueOf(switchStyle));
        mSwitchStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
        mSwitchStyle.setSummary(mSwitchStyle.getEntry());
        mSwitchStyle.setOnPreferenceChangeListener(this);
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSwitchStyle) {
            String value = (String) newValue;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SWITCH_STYLE, Integer.valueOf(value));
            int valueIndex = mSwitchStyle.findIndexOfValue(value);
            mSwitchStyle.setSummary(mSwitchStyle.getEntries()[valueIndex]);
        }
        return true;
    }
}
