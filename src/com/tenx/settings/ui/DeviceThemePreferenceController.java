/*
 * Copyright (C) 2020 TenX-OS
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

import android.app.UiModeManager;
import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settings.core.PreferenceControllerMixin;

import com.android.internal.util.tenx.ThemesUtils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import com.android.settings.R;

import com.tenx.support.preferences.SystemSettingListPreference;

public class DeviceThemePreferenceController extends AbstractPreferenceController implements
        PreferenceControllerMixin, Preference.OnPreferenceChangeListener {

    private static final String SYSTEM_THEME_STYLE = "system_theme_style";
    private SystemSettingListPreference mSystemThemeStyle;

    private UiModeManager mUiModeManager;
    private Context mContext;
    private Fragment mFragment;
    private ThemesUtils mThemesUtils;

    public DeviceThemePreferenceController(Context context) {
        super(context);
        mContext = context;
        mUiModeManager = context.getSystemService(UiModeManager.class);
        mThemesUtils = new ThemesUtils(context);
    }

    @Override
    public String getPreferenceKey() {
        return SYSTEM_THEME_STYLE;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mSystemThemeStyle = (SystemSettingListPreference) screen.findPreference(SYSTEM_THEME_STYLE);
        int systemThemeStyle = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.SYSTEM_THEME_STYLE, 0);
        int valueIndex = mSystemThemeStyle.findIndexOfValue(String.valueOf(systemThemeStyle));
        mSystemThemeStyle.setValueIndex(valueIndex >= 0 ? valueIndex : 0);
        mSystemThemeStyle.setSummary(mSystemThemeStyle.getEntry());
        mSystemThemeStyle.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSystemThemeStyle) {
            String value = (String) newValue;
            mThemesUtils.setTheme(Integer.valueOf(value));
        }
        return true;
    }
}
