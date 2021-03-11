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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.SystemSettingMasterSwitchPreference;
import com.tenx.support.preferences.CustomSeekBarPreference;
import com.tenx.support.colorpicker.ColorPickerPreference;

import java.util.ArrayList;
import java.util.List;

public class LockScreen extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    static final int DEFAULT = 0xffffffff;
    static final int TRANSPARENT = 0x99FFFFFF;

    private static final int MENU_RESET = Menu.FIRST;

    private static final String LOCK_CLOCK_FONTS = "lock_clock_fonts";
    private static final String LOCK_DATE_FONTS = "lock_date_fonts";
    private static final String CUSTOM_TEXT_CLOCK_FONTS = "custom_text_clock_fonts";
    private static final String CLOCK_FONT_SIZE  = "lockclock_font_size";
    private static final String DATE_FONT_SIZE  = "lockdate_font_size";
    private static final String CUSTOM_TEXT_CLOCK_FONT_SIZE  = "custom_text_clock_font_size";
    private static final String LOCK_OWNERINFO_FONTS = "lock_ownerinfo_fonts";
    private static final String LOCKOWNER_FONT_SIZE = "lockowner_font_size";
    private static final String LOCKSCREEN_PHONE_ICON_COLOR = "lockscreen_phone_icon_color";
    private static final String LOCKSCREEN_CAMERA_ICON_COLOR = "lockscreen_camera_icon_color";
    private static final String LOCKSCREEN_INDICATION_TEXT_COLOR = "lockscreen_indication_text_color";
    private static final String LOCKSCREEN_CLOCK_COLOR = "lockscreen_clock_color";
    private static final String LOCKSCREEN_CLOCK_DATE_COLOR = "lockscreen_clock_date_color";
    private static final String LOCKSCREEN_OWNER_INFO_COLOR = "lockscreen_owner_info_color";
    private static final String LOCKSCREEN_WEATHER_TEMP_COLOR = "lockscreen_weather_temp_color";
    private static final String LOCKSCREEN_WEATHER_CITY_COLOR = "lockscreen_weather_city_color";
    private static final String LOCKSCREEN_WEATHER_ICON_COLOR = "lockscreen_weather_icon_color";
    private static final String LOCKSCREEN_WEATHER_ENABLED = "lockscreen_weather_enabled";

    private ListPreference mLockClockFonts;
    private ListPreference mLockDateFonts;
    private ListPreference mLockOwnerInfoFonts;
    private ListPreference mTextClockFonts;
    private CustomSeekBarPreference mClockFontSize;
    private CustomSeekBarPreference mDateFontSize;
    private CustomSeekBarPreference mOwnerInfoFontSize;
    private CustomSeekBarPreference mCustomTextClockFontSize;
    private ColorPickerPreference mLockscreenPhoneColorPicker;
    private ColorPickerPreference mLockscreenCameraColorPicker;
    private ColorPickerPreference mLockscreenIndicationTextColorPicker;
    private ColorPickerPreference mLockscreenClockColorPicker;
    private ColorPickerPreference mLockscreenClockDateColorPicker;
    private ColorPickerPreference mLockscreenOwnerInfoColorPicker;
    private ColorPickerPreference mWeatherRightTextColorPicker;
    private ColorPickerPreference mWeatherLeftTextColorPicker;
    private ColorPickerPreference mWeatherIconColorPicker;
    private SystemSettingMasterSwitchPreference mWeatherEnabled;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_lockscreen);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();
        ContentResolver resolver = getActivity().getContentResolver();

        int intColor;
        String hexColor;

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

        mTextClockFonts = (ListPreference) findPreference(CUSTOM_TEXT_CLOCK_FONTS);
        mTextClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.CUSTOM_TEXT_CLOCK_FONTS, 32)));
        mTextClockFonts.setSummary(mTextClockFonts.getEntry());
        mTextClockFonts.setOnPreferenceChangeListener(this);

        mClockFontSize = (CustomSeekBarPreference) findPreference(CLOCK_FONT_SIZE);
        mClockFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKCLOCK_FONT_SIZE, 78));
        mClockFontSize.setOnPreferenceChangeListener(this);

        mDateFontSize = (CustomSeekBarPreference) findPreference(DATE_FONT_SIZE);
        mDateFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKDATE_FONT_SIZE, 18));
        mDateFontSize.setOnPreferenceChangeListener(this);

        mLockOwnerInfoFonts = (ListPreference) findPreference(LOCK_OWNERINFO_FONTS);
        mLockOwnerInfoFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_OWNERINFO_FONTS, 0)));
        mLockOwnerInfoFonts.setSummary(mLockOwnerInfoFonts.getEntry());
        mLockOwnerInfoFonts.setOnPreferenceChangeListener(this);

        mOwnerInfoFontSize = (CustomSeekBarPreference) findPreference(LOCKOWNER_FONT_SIZE);
        mOwnerInfoFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKOWNER_FONT_SIZE,21));
        mOwnerInfoFontSize.setOnPreferenceChangeListener(this);

        mCustomTextClockFontSize = (CustomSeekBarPreference) findPreference(CUSTOM_TEXT_CLOCK_FONT_SIZE);
        mCustomTextClockFontSize.setValue(Settings.System.getInt(getContentResolver(),
                Settings.System.CUSTOM_TEXT_CLOCK_FONT_SIZE, 40));
        mCustomTextClockFontSize.setOnPreferenceChangeListener(this);

        mLockscreenPhoneColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_PHONE_ICON_COLOR);
        mLockscreenPhoneColorPicker.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_PHONE_ICON_COLOR, TRANSPARENT);
        hexColor = String.format("#%08x", (0x99FFFFFF & intColor));
        mLockscreenPhoneColorPicker.setSummary(hexColor);
        mLockscreenPhoneColorPicker.setNewPreviewColor(intColor);

        mLockscreenCameraColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_CAMERA_ICON_COLOR);
        mLockscreenCameraColorPicker.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CAMERA_ICON_COLOR, TRANSPARENT);
        hexColor = String.format("#%08x", (0x99FFFFFF & intColor));
        mLockscreenCameraColorPicker.setSummary(hexColor);
        mLockscreenCameraColorPicker.setNewPreviewColor(intColor);

        mLockscreenIndicationTextColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_INDICATION_TEXT_COLOR);
        mLockscreenIndicationTextColorPicker.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_INDICATION_TEXT_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mLockscreenIndicationTextColorPicker.setSummary(hexColor);
        mLockscreenIndicationTextColorPicker.setNewPreviewColor(intColor);

        mLockscreenClockColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_CLOCK_COLOR);
        mLockscreenClockColorPicker.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CLOCK_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mLockscreenClockColorPicker.setSummary(hexColor);
        mLockscreenClockColorPicker.setNewPreviewColor(intColor);

        mLockscreenClockDateColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_CLOCK_DATE_COLOR);
        mLockscreenClockDateColorPicker.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(getContentResolver(),
                    Settings.System.LOCKSCREEN_CLOCK_DATE_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mLockscreenClockDateColorPicker.setSummary(hexColor);
        mLockscreenClockDateColorPicker.setNewPreviewColor(intColor);

        mLockscreenOwnerInfoColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_OWNER_INFO_COLOR);
        mLockscreenOwnerInfoColorPicker.setOnPreferenceChangeListener(this);
        intColor = Settings.System.getInt(resolver,
        		   Settings.System.LOCKSCREEN_OWNER_INFO_COLOR, DEFAULT);
        hexColor = String.format("#%08x", (0xffffffff & intColor));
        mLockscreenOwnerInfoColorPicker.setSummary(hexColor);
        mLockscreenOwnerInfoColorPicker.setNewPreviewColor(intColor);

       mWeatherRightTextColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_WEATHER_TEMP_COLOR);
       mWeatherRightTextColorPicker.setOnPreferenceChangeListener(this);
       intColor = Settings.System.getInt(getContentResolver(),
                   Settings.System.LOCK_SCREEN_WEATHER_TEMP_COLOR, DEFAULT);
       hexColor = String.format("#%08x", (0xffffffff & intColor));
       mWeatherRightTextColorPicker.setSummary(hexColor);
       mWeatherRightTextColorPicker.setNewPreviewColor(intColor);

       mWeatherLeftTextColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_WEATHER_CITY_COLOR);
       mWeatherLeftTextColorPicker.setOnPreferenceChangeListener(this);
       intColor = Settings.System.getInt(getContentResolver(),
                   Settings.System.LOCK_SCREEN_WEATHER_CITY_COLOR, DEFAULT);
       hexColor = String.format("#%08x", (0xffffffff & intColor));
       mWeatherLeftTextColorPicker.setSummary(hexColor);
       mWeatherLeftTextColorPicker.setNewPreviewColor(intColor);

       mWeatherIconColorPicker = (ColorPickerPreference) findPreference(LOCKSCREEN_WEATHER_ICON_COLOR);
       mWeatherIconColorPicker.setOnPreferenceChangeListener(this);
       intColor = Settings.System.getInt(getContentResolver(),
                   Settings.System.LOCK_SCREEN_WEATHER_ICON_COLOR, TRANSPARENT);
       hexColor = String.format("#%08x", (0x99FFFFFF & intColor));
       mWeatherIconColorPicker.setSummary(hexColor);
       mWeatherIconColorPicker.setNewPreviewColor(intColor);

      mWeatherEnabled = (SystemSettingMasterSwitchPreference) findPreference(LOCKSCREEN_WEATHER_ENABLED);
      mWeatherEnabled.setChecked((Settings.System.getInt(resolver,
              Settings.System.OMNI_LOCKSCREEN_WEATHER_ENABLED, 0) == 1));
      mWeatherEnabled.setOnPreferenceChangeListener(this);

       setHasOptionsMenu(true);
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
        } else if (preference == mTextClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.CUSTOM_TEXT_CLOCK_FONTS,
                    Integer.valueOf((String) newValue));
            mTextClockFonts.setValue(String.valueOf(newValue));
            mTextClockFonts.setSummary(mTextClockFonts.getEntry());
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
       } else if (preference == mLockOwnerInfoFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_OWNERINFO_FONTS,
                    Integer.valueOf((String) newValue));
            mLockOwnerInfoFonts.setValue(String.valueOf(newValue));
            mLockOwnerInfoFonts.setSummary(mLockOwnerInfoFonts.getEntry());
            return true;
        } else if (preference == mOwnerInfoFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LOCKOWNER_FONT_SIZE, top*1);
            return true;
        } else if (preference == mCustomTextClockFontSize) {
            int top = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.CUSTOM_TEXT_CLOCK_FONT_SIZE, top*1);
            return true;
        } else if (preference == mLockscreenCameraColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCKSCREEN_CAMERA_ICON_COLOR, intHex);
            return true;
        } else if (preference == mLockscreenPhoneColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCKSCREEN_PHONE_ICON_COLOR, intHex);
            return true;
        } else if (preference == mLockscreenIndicationTextColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCKSCREEN_INDICATION_TEXT_COLOR, intHex);
            return true;
        } else if (preference == mLockscreenClockColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCKSCREEN_CLOCK_COLOR, intHex);
            return true;
        } else if (preference == mLockscreenClockDateColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCKSCREEN_CLOCK_DATE_COLOR, intHex);
            return true;
         } else if (preference == mLockscreenOwnerInfoColorPicker) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                preference.setSummary(hex);
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                        Settings.System.LOCKSCREEN_OWNER_INFO_COLOR, intHex);
                return true;
        } else if (preference == mWeatherRightTextColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCK_SCREEN_WEATHER_TEMP_COLOR, intHex);
            return true;
        } else if (preference == mWeatherLeftTextColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCK_SCREEN_WEATHER_CITY_COLOR, intHex);
            return true;
        } else if (preference == mWeatherIconColorPicker) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.LOCK_SCREEN_WEATHER_ICON_COLOR, intHex);
            return true;
        } else if (preference == mWeatherEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.OMNI_LOCKSCREEN_WEATHER_ENABLED, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset)
                .setIcon(R.drawable.ic_action_reset_alpha)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefault();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void resetToDefault() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.lockscreen_colors_reset_title);
        alertDialog.setMessage(R.string.lockscreen_colors_reset_message);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetValues();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, null);
        alertDialog.create().show();
    }

    private void resetValues() {
	ContentResolver resolver = getActivity().getContentResolver();
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCKSCREEN_PHONE_ICON_COLOR, TRANSPARENT);
        mLockscreenPhoneColorPicker.setNewPreviewColor(TRANSPARENT);
        mLockscreenPhoneColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CAMERA_ICON_COLOR, TRANSPARENT);
        mLockscreenCameraColorPicker.setNewPreviewColor(TRANSPARENT);
        mLockscreenCameraColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCKSCREEN_INDICATION_TEXT_COLOR, DEFAULT);
        mLockscreenIndicationTextColorPicker.setNewPreviewColor(DEFAULT);
        mLockscreenIndicationTextColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CLOCK_COLOR, DEFAULT);
        mLockscreenClockColorPicker.setNewPreviewColor(DEFAULT);
        mLockscreenClockColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCKSCREEN_CLOCK_DATE_COLOR, DEFAULT);
        mLockscreenClockDateColorPicker.setNewPreviewColor(DEFAULT);
        mLockscreenClockDateColorPicker.setSummary(R.string.default_string);
	Settings.System.putInt(resolver,
                 Settings.System.LOCKSCREEN_OWNER_INFO_COLOR, DEFAULT);
        mLockscreenOwnerInfoColorPicker.setNewPreviewColor(DEFAULT);
        mLockscreenOwnerInfoColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCK_SCREEN_WEATHER_TEMP_COLOR, DEFAULT);
        mWeatherRightTextColorPicker.setNewPreviewColor(DEFAULT);
        mWeatherRightTextColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCK_SCREEN_WEATHER_CITY_COLOR, DEFAULT);
        mWeatherLeftTextColorPicker.setNewPreviewColor(DEFAULT);
        mWeatherLeftTextColorPicker.setSummary(R.string.default_string);
        Settings.System.putInt(getContentResolver(),
                Settings.System.LOCK_SCREEN_WEATHER_ICON_COLOR, DEFAULT);
        mWeatherIconColorPicker.setNewPreviewColor(DEFAULT);
        mWeatherIconColorPicker.setSummary(R.string.default_string);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
