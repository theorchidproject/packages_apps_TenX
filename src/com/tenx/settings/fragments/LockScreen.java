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
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.CustomSeekBarPreference;

public class LockScreen extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String LOCK_CLOCK_FONTS = "lock_clock_fonts";
    private static final String LOCK_DATE_FONTS = "lock_date_fonts";
    private static final String CLOCK_FONT_SIZE  = "lockclock_font_size";
    private static final String DATE_FONT_SIZE  = "lockdate_font_size";

    private ListPreference mLockClockFonts;
    private ListPreference mLockDateFonts;
    private CustomSeekBarPreference mClockFontSize;
    private CustomSeekBarPreference mDateFontSize;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_lockscreen);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();
        ContentResolver resolver = getActivity().getContentResolver();

        mLockClockFonts = (ListPreference) findPreference(LOCK_CLOCK_FONTS);
        mLockClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_CLOCK_FONTS, 34)));
        mLockClockFonts.setSummary(mLockClockFonts.getEntry());
        mLockClockFonts.setOnPreferenceChangeListener(this);

        mLockDateFonts = (ListPreference) findPreference(LOCK_DATE_FONTS);
        mLockDateFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_DATE_FONTS, 32)));
        mLockDateFonts.setSummary(mLockDateFonts.getEntry());
        mLockDateFonts.setOnPreferenceChangeListener(this);

        mClockFontSize = (CustomSeekBarPreference) findPreference(CLOCK_FONT_SIZE);
        mClockFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKCLOCK_FONT_SIZE, 78));
        mClockFontSize.setOnPreferenceChangeListener(this);

        mDateFontSize = (CustomSeekBarPreference) findPreference(DATE_FONT_SIZE);
        mDateFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKDATE_FONT_SIZE, 18));
        mDateFontSize.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mLockClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_CLOCK_FONTS,
                    Integer.valueOf((String) newValue));
            mLockClockFonts.setValue(String.valueOf(newValue));
            mLockClockFonts.setSummary(mLockClockFonts.getEntry());
            return true;
        } else if (preference == mLockDateFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_DATE_FONTS,
                    Integer.valueOf((String) newValue));
            mLockDateFonts.setValue(String.valueOf(newValue));
            mLockDateFonts.setSummary(mLockDateFonts.getEntry());
            return true;
        } else if (preference == mClockFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKCLOCK_FONT_SIZE, top*1);
            return true;
        } else if (preference == mDateFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKDATE_FONT_SIZE, top*1);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
