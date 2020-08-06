/*
 * Copyright (C) 2021 TenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
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

import java.util.ArrayList;
import java.util.List;

import com.tenx.support.preferences.SystemSettingListPreference;

public class UIStylePickerPreferenceController extends AbstractPreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String UI_STYLE_KEY = "ui_style";
    private SystemSettingListPreference mUIStyle;

    public UIStylePickerPreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return UI_STYLE_KEY;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mUIStyle = (SystemSettingListPreference) screen.findPreference(UI_STYLE_KEY);
        int uiStyle = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.UI_STYLE, 0);
        int valueIndex = mUIStyle.findIndexOfValue(String.valueOf(uiStyle));
        mUIStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
        mUIStyle.setSummary(mUIStyle.getEntry());
        mUIStyle.setOnPreferenceChangeListener(this);
    }
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mUIStyle) {
            String value = (String) newValue;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.UI_STYLE, Integer.valueOf(value));
            int valueIndex = mUIStyle.findIndexOfValue(value);
            mUIStyle.setSummary(mUIStyle.getEntries()[valueIndex]);
        }
        return true;
    }
}
