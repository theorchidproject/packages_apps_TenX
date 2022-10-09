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

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.SystemSettingMasterSwitchPreference;
import com.tenx.support.preferences.SystemSettingListPreference;

public class StatusBar extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String NETWORK_TRAFFIC_STATE = "network_traffic_state";
    private static final String STATUS_BAR_LOGO = "status_bar_logo";
    private static final String CARRIER_LABEL = "carrier_label_enabled";
    private static final String STATUS_BAR_CLOCK = "status_bar_clock";

    private SystemSettingMasterSwitchPreference mNetworkTraffic;
    private SystemSettingMasterSwitchPreference mStatusBarLogo;
    private SystemSettingMasterSwitchPreference mCarrierLabel;
    private SystemSettingMasterSwitchPreference mStatusBarClockShow;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_statusbar);
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mNetworkTraffic = (SystemSettingMasterSwitchPreference) findPreference(NETWORK_TRAFFIC_STATE);
        mNetworkTraffic.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, 0) == 1));
        mNetworkTraffic.setOnPreferenceChangeListener(this);

        mStatusBarLogo = (SystemSettingMasterSwitchPreference) findPreference(STATUS_BAR_LOGO);
        mStatusBarLogo.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_LOGO, 0) == 1));
        mStatusBarLogo.setOnPreferenceChangeListener(this);

        mCarrierLabel = (SystemSettingMasterSwitchPreference) findPreference(CARRIER_LABEL);
        mCarrierLabel.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.CARRIER_LABEL_ENABLED, 1) == 1));
        mCarrierLabel.setOnPreferenceChangeListener(this);

        mStatusBarClockShow = (SystemSettingMasterSwitchPreference) findPreference(STATUS_BAR_CLOCK);
        mStatusBarClockShow.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_CLOCK, 1) == 1));
        mStatusBarClockShow.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mNetworkTraffic) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                   Settings.System.NETWORK_TRAFFIC_STATE, value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarLogo) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_LOGO, value ? 1 : 0);
            return true;
        } else if (preference == mCarrierLabel) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.CARRIER_LABEL_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarClockShow) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_CLOCK, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
