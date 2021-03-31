/*
 * Copyright (C) 2021 TenX-OS
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

package com.tenx.settings.ui;

import android.content.Context;
import android.content.ContentResolver;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;

import com.tenx.support.preferences.SystemSettingMasterSwitchPreference;

public class HeaderImagePreferenceController extends AbstractPreferenceController
             implements Preference.OnPreferenceChangeListener {


    private static final String STATUS_BAR_CUSTOM_HEADER = "status_bar_custom_header";

    private SystemSettingMasterSwitchPreference mHeaderImage;

    public HeaderImagePreferenceController(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return STATUS_BAR_CUSTOM_HEADER;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mHeaderImage = (SystemSettingMasterSwitchPreference) screen.findPreference(STATUS_BAR_CUSTOM_HEADER);
        int qsHeader = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER, 0);
        mHeaderImage.setChecked(qsHeader != 0);
        mHeaderImage.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHeaderImage) {
            boolean header = (Boolean) newValue;
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.STATUS_BAR_CUSTOM_HEADER, header ? 1 : 0);
            return true;
        }
        return false;
    }
}
