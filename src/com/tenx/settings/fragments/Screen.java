/*
 * Copyright (C) 2018 Havoc-OS
 *           (C) 2021 TenX-OS
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

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.tenx.support.preferences.CustomSeekBarPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Screen extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String SYSUI_ROUNDED_SIZE = "sysui_rounded_size";
    //private static final String SYSUI_ROUNDED_CONTENT_PADDING = "sysui_rounded_content_padding";
    //private static final String SYSUI_STATUS_BAR_PADDING = "sysui_status_bar_padding";
    private static final String SYSUI_ROUNDED_FWVALS = "sysui_rounded_fwvals";
    private static final String CUSTOM_STATUSBAR_PADDING_START = "custom_statusbar_padding_start";
    private static final String CUSTOM_STATUSBAR_PADDING_END = "custom_statusbar_padding_end";

    private CustomSeekBarPreference mCornerRadius;
    private CustomSeekBarPreference mCustomStatusbarPaddingStart;
    private CustomSeekBarPreference mCustomStatusbarPaddingEnd;
    //private CustomSeekBarPreference mContentPadding;
    private SwitchPreference mRoundedFwvals;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_screen);
        PreferenceScreen pref = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        Resources res = null;
        Context ctx = getContext();
        float density = Resources.getSystem().getDisplayMetrics().density;

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        // Rounded Corner Radius
        mCornerRadius = (CustomSeekBarPreference) findPreference(SYSUI_ROUNDED_SIZE);
        int resourceIdRadius = (int) ctx.getResources().getDimension(com.android.internal.R.dimen.rounded_corner_radius);
        int cornerRadius = Settings.Secure.getIntForUser(ctx.getContentResolver(), Settings.Secure.SYSUI_ROUNDED_SIZE,
                ((int) (resourceIdRadius / density)), UserHandle.USER_CURRENT);
        mCornerRadius.setValue(cornerRadius);
        mCornerRadius.setOnPreferenceChangeListener(this);

        // Rounded Content Padding
        /*
        mContentPadding = (CustomSeekBarPreference) findPreference(SYSUI_ROUNDED_CONTENT_PADDING);
        int resourceIdPadding = res.getIdentifier("com.android.systemui:dimen/rounded_corner_content_padding", null,
                null);
        int contentPadding = Settings.Secure.getIntForUser(ctx.getContentResolver(),
                Settings.Secure.SYSUI_ROUNDED_CONTENT_PADDING,
                (int) (res.getDimension(resourceIdPadding) / density), UserHandle.USER_CURRENT);
        mContentPadding.setValue(contentPadding);
        mContentPadding.setOnPreferenceChangeListener(this);
        */

        // Rounded use Framework Values
        mRoundedFwvals = (SwitchPreference) findPreference(SYSUI_ROUNDED_FWVALS);
        mRoundedFwvals.setOnPreferenceChangeListener(this);

        mCustomStatusbarPaddingStart = (CustomSeekBarPreference) findPreference(CUSTOM_STATUSBAR_PADDING_START);
        int customStatusbarPaddingStart = Settings.System.getIntForUser(ctx.getContentResolver(),
                Settings.System.CUSTOM_STATUSBAR_PADDING_START, res.getIdentifier("com.android.systemui:dimen/status_bar_padding_start", null, null), UserHandle.USER_CURRENT);
        mCustomStatusbarPaddingStart.setValue(customStatusbarPaddingStart);
        mCustomStatusbarPaddingStart.setOnPreferenceChangeListener(this);

        mCustomStatusbarPaddingEnd = (CustomSeekBarPreference) findPreference(CUSTOM_STATUSBAR_PADDING_END);
        int customStatusbarPaddingEnd = Settings.System.getIntForUser(getActivity().getContentResolver(),
                Settings.System.CUSTOM_STATUSBAR_PADDING_END, res.getIdentifier("com.android.systemui:dimen/status_bar_padding_end", null, null), UserHandle.USER_CURRENT);
        mCustomStatusbarPaddingEnd.setValue(customStatusbarPaddingEnd);
        mCustomStatusbarPaddingEnd.setOnPreferenceChangeListener(this);
    }

    private void restoreCorners() {
        Resources res = null;
        float density = Resources.getSystem().getDisplayMetrics().density;
        Context ctx = getContext();

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        int resourceIdRadius = (int) ctx.getResources().getDimension(com.android.internal.R.dimen.rounded_corner_radius);
        int cornerRadius = Settings.Secure.getIntForUser(ctx.getContentResolver(), Settings.Secure.SYSUI_ROUNDED_SIZE,
                ((int) (resourceIdRadius / density)), UserHandle.USER_CURRENT);
        mCornerRadius.setValue(cornerRadius);
        //int resourceIdPadding = res.getIdentifier("com.android.systemui:dimen/rounded_corner_content_padding", null, null);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
       if (preference == mCornerRadius) {
            Settings.Secure.putIntForUser(getContext().getContentResolver(), Settings.Secure.SYSUI_ROUNDED_SIZE,
                    (int) newValue, UserHandle.USER_CURRENT);
        /*
        } else if (preference == mContentPadding) {
            Settings.Secure.putIntForUser(getContext().getContentResolver(), Settings.Secure.SYSUI_ROUNDED_CONTENT_PADDING,
                    (int) newValue, UserHandle.USER_CURRENT);
        */
        } else if (preference == mRoundedFwvals) {
            restoreCorners();
        } else if (preference == mCustomStatusbarPaddingStart) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.CUSTOM_STATUSBAR_PADDING_START, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mCustomStatusbarPaddingEnd) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.CUSTOM_STATUSBAR_PADDING_END, value, UserHandle.USER_CURRENT);
            return true;
        }
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }

    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {

                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.tenx_settings_screen;
                    return Arrays.asList(sir);
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    ArrayList<String> result = new ArrayList<String>();
                    return result;
                }
            };
}
