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
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
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
import com.tenx.support.preferences.SystemSettingEditTextPreference;
import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.colorpicker.ColorPickerPreference;

public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String PREF_COLUMNS_PORTRAIT = "qs_columns_portrait";
    private static final String PREF_COLUMNS_LANDSCAPE = "qs_columns_landscape";
    private static final String PREF_COLUMNS_QUICKBAR = "qs_quickbar_columns";
    private static final String KEY_QS_PANEL_ALPHA = "qs_panel_alpha";
    private static final String TENX_FOOTER_TEXT_STRING = "tenx_footer_text_string";
    private static final String TENX_FOOTER_TEXT_FONT = "tenx_footer_text_font";
    private static final String OOS_BG_COLOR = "dismiss_all_button_bg_color";
    private static final String OOS_ICON_COLOR = "dismiss_all_button_icon_color";
    private static final String OOS_BG_COLOR_CUST = "dismiss_all_button_bg_color_custom";
    private static final String OOS_ICON_COLOR_CUST = "dismiss_all_button_icon_color_custom";
    private static final String TENX_FOOTER_TEXT_COLOR = "tenx_footer_text_color";
    private static final String TENX_FOOTER_TEXT_COLOR_CUSTOM = "tenx_footer_text_color_custom";
    private static final String QQS_SIZE = "qs_quick_tile_size";
    private static final String QS_BG_SIZE = "qs_tile_bg_size";
    private static final String QS_ICON_SIZE = "qs_tile_icon_size";

    private CustomSeekBarPreference mQsColumnsPortrait;
    private CustomSeekBarPreference mQsColumnsLandscape;
    private CustomSeekBarPreference mQsColumnsQuickbar;
    private CustomSeekBarPreference mQsPanelAlpha;
    private CustomSeekBarPreference mQqsSize;
    private CustomSeekBarPreference mQsBgSize;
    private CustomSeekBarPreference mQsIconSize;
    private SystemSettingEditTextPreference mFooterString;
    private SystemSettingListPreference mQsFooterTextFont;
    private SystemSettingListPreference mBgColor;
    private SystemSettingListPreference mIconColor;
    private SystemSettingListPreference mTextColor;
    private ColorPickerPreference mBgColorCust;
    private ColorPickerPreference mIconColorCust;
    private ColorPickerPreference mTextColorCustom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_quicksettings);
        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        Resources res = null;
        Context ctx = getContext();
        float density = Resources.getSystem().getDisplayMetrics().density;

        try {
            res = ctx.getPackageManager().getResourcesForApplication("com.android.systemui");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        mQsColumnsPortrait = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_PORTRAIT);
        int columnsPortrait = Settings.System.getIntForUser(resolver,
                Settings.System.OMNI_QS_LAYOUT_COLUMNS, 3, UserHandle.USER_CURRENT);
        mQsColumnsPortrait.setValue(columnsPortrait);
        mQsColumnsPortrait.setOnPreferenceChangeListener(this);

        mQsColumnsLandscape = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_LANDSCAPE);
        int columnsLandscape = Settings.System.getIntForUser(resolver,
                Settings.System.OMNI_QS_LAYOUT_COLUMNS_LANDSCAPE, 3, UserHandle.USER_CURRENT);
        mQsColumnsLandscape.setValue(columnsLandscape);
        mQsColumnsLandscape.setOnPreferenceChangeListener(this);

        mQsColumnsQuickbar = (CustomSeekBarPreference) findPreference(PREF_COLUMNS_QUICKBAR);
        int columnsQuickbar = Settings.System.getInt(resolver,
                Settings.System.QS_QUICKBAR_COLUMNS, 6);
        mQsColumnsQuickbar.setValue(columnsQuickbar);
        mQsColumnsQuickbar.setOnPreferenceChangeListener(this);

        mQqsSize = (CustomSeekBarPreference) findPreference(QQS_SIZE);
        int qqsSize =  Settings.System.getIntForUser(ctx.getContentResolver(),
                Settings.System.QS_QUICK_TILE_SIZE, res.getIdentifier("com.android.systemui:dimen/qs_quick_tile_size", null, null), UserHandle.USER_CURRENT);
        mQqsSize.setValue(qqsSize);
        mQqsSize.setOnPreferenceChangeListener(this);

        mQsBgSize = (CustomSeekBarPreference) findPreference(QS_BG_SIZE);
        int qsBg = Settings.System.getIntForUser(ctx.getContentResolver(),
                Settings.System.QS_TILE_BG_SIZE, res.getIdentifier("com.android.systemui:dimen/qs_tile_background_size", null, null), UserHandle.USER_CURRENT);
        mQsBgSize.setValue(qsBg);
        mQsBgSize.setOnPreferenceChangeListener(this);

        mQsIconSize = (CustomSeekBarPreference) findPreference(QS_ICON_SIZE);
        int qsIcon =  Settings.System.getIntForUser(ctx.getContentResolver(),
                Settings.System.QS_TILE_ICON_SIZE, res.getIdentifier("com.android.systemui:dimen/qs_tile_icon_size", null, null), UserHandle.USER_CURRENT);
        mQsIconSize.setValue(qsIcon);
        mQsIconSize.setOnPreferenceChangeListener(this);

        mQsPanelAlpha = (CustomSeekBarPreference) findPreference(KEY_QS_PANEL_ALPHA);
        int qsPanelAlpha = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.QS_PANEL_BG_ALPHA, 255);
        mQsPanelAlpha.setValue((int)(((double) qsPanelAlpha / 255) * 100));
        mQsPanelAlpha.setOnPreferenceChangeListener(this);

        mFooterString = (SystemSettingEditTextPreference) findPreference(TENX_FOOTER_TEXT_STRING);
        mFooterString.setOnPreferenceChangeListener(this);
        String footerString = Settings.System.getString(getContentResolver(),
                TENX_FOOTER_TEXT_STRING);
        if (footerString != null && footerString != "")
            mFooterString.setText(footerString);
        else {
            mFooterString.setText("Ten-X");
            Settings.System.putString(getActivity().getContentResolver(),
                    Settings.System.TENX_FOOTER_TEXT_STRING, "Ten-X");
        }

        mQsFooterTextFont = (SystemSettingListPreference) findPreference(TENX_FOOTER_TEXT_FONT);
        mQsFooterTextFont.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.TENX_FOOTER_TEXT_FONT, 28)));
        mQsFooterTextFont.setSummary(mQsFooterTextFont.getEntry());
        mQsFooterTextFont.setOnPreferenceChangeListener(this);

        mBgColor = (SystemSettingListPreference) findPreference(OOS_BG_COLOR);
        mBgColor.setOnPreferenceChangeListener(this);
        int bgColor = Settings.System.getIntForUser(resolver,
                Settings.System.DISMISS_ALL_BUTTON_BG_COLOR, 0, UserHandle.USER_CURRENT);
        mBgColor.setValue(String.valueOf(bgColor));
        mBgColor.setSummary(mBgColor.getEntry());

        mIconColor = (SystemSettingListPreference) findPreference(OOS_ICON_COLOR);
        mIconColor.setOnPreferenceChangeListener(this);
        int iconColor = Settings.System.getIntForUser(resolver,
                Settings.System.DISMISS_ALL_BUTTON_ICON_COLOR, 0, UserHandle.USER_CURRENT);
        mIconColor.setValue(String.valueOf(iconColor));
        mIconColor.setSummary(mIconColor.getEntry());

        mBgColorCust = (ColorPickerPreference) findPreference(OOS_BG_COLOR_CUST);
        mBgColorCust.setOnPreferenceChangeListener(this);
        int bgColorCust = Settings.System.getInt(getContentResolver(),
                Settings.System.DISMISS_ALL_BUTTON_BG_COLOR_CUSTOM, 0xFFFFFFFF);
        String bgColorCustHex = String.format("#%08x", (0xFFFFFFFF & bgColorCust));
        if (bgColorCustHex.equals("#ffffffff")) {
            mBgColorCust.setSummary(R.string.default_string);
        } else {
            mBgColorCust.setSummary(bgColorCustHex);
        }
        mBgColorCust.setNewPreviewColor(bgColorCust);

        mIconColorCust = (ColorPickerPreference) findPreference(OOS_ICON_COLOR_CUST);
        mIconColorCust.setOnPreferenceChangeListener(this);
        int iconColorCust = Settings.System.getInt(getContentResolver(),
                Settings.System.DISMISS_ALL_BUTTON_ICON_COLOR_CUSTOM, 0xFFFFFFFF);
        String iconColorCustHex = String.format("#%08x", (0xFFFFFFFF & iconColorCust));
        if (iconColorCustHex.equals("#ffffffff")) {
            mIconColorCust.setSummary(R.string.default_string);
        } else {
            mIconColorCust.setSummary(iconColorCustHex);
        }
        mIconColorCust.setNewPreviewColor(iconColorCust);

        updateBgPrefs(bgColor);
        updateIconPrefs(iconColor);

        mTextColor = (SystemSettingListPreference) findPreference(TENX_FOOTER_TEXT_COLOR);
        mTextColor.setOnPreferenceChangeListener(this);
        int textColor = Settings.System.getIntForUser(resolver,
                Settings.System.TENX_FOOTER_TEXT_COLOR, 0, UserHandle.USER_CURRENT);
        mTextColor.setValue(String.valueOf(textColor));
        mTextColor.setSummary(mTextColor.getEntry());

        mTextColorCustom = (ColorPickerPreference) findPreference(TENX_FOOTER_TEXT_COLOR_CUSTOM);
        mTextColorCustom.setOnPreferenceChangeListener(this);
        int textColorCustom = Settings.System.getInt(getContentResolver(),
                Settings.System.TENX_FOOTER_TEXT_COLOR_CUSTOM, 0xFFFFFFFF);
        String textColorCustomHex = String.format("#%08x", (0xFFFFFFFF & textColorCustom));
        if (textColorCustomHex.equals("#ffffffff")) {
        mTextColorCustom.setSummary(R.string.default_string);
        } else {
        mTextColorCustom.setSummary(textColorCustomHex);
        }
        mTextColorCustom.setNewPreviewColor(textColorCustom);

        updateColorPrefs(textColor);
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
        } else if (preference == mQsColumnsQuickbar) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.QS_QUICKBAR_COLUMNS, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQqsSize) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.QS_QUICK_TILE_SIZE, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsBgSize) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.QS_TILE_BG_SIZE, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsIconSize) {
            int value = (Integer) newValue;
            Settings.System.putIntForUser(getContext().getContentResolver(),
                    Settings.System.QS_TILE_ICON_SIZE, value, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mQsPanelAlpha) {
            int bgAlpha = (Integer) newValue;
            int trueValue = (int) (((double) bgAlpha / 100) * 255);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QS_PANEL_BG_ALPHA, trueValue);
            return true;
        } else if (preference == mFooterString) {
            String value = (String) newValue;
            if (value != "" && value != null)
                Settings.System.putString(getActivity().getContentResolver(),
                        Settings.System.TENX_FOOTER_TEXT_STRING, value);
            else {
                mFooterString.setText("Ten-X");
                Settings.System.putString(getActivity().getContentResolver(),
                        Settings.System.TENX_FOOTER_TEXT_STRING, "Ten-X");
            }
            return true;
        } else if (preference == mQsFooterTextFont) {
            int value = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.TENX_FOOTER_TEXT_FONT, value);
            mQsFooterTextFont.setValue(String.valueOf(value));
            mQsFooterTextFont.setSummary(mQsFooterTextFont.getEntry());
            return true;
        } else if (preference ==  mBgColor) {
            int bgColor = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(resolver,
                    Settings.System.DISMISS_ALL_BUTTON_BG_COLOR, bgColor, UserHandle.USER_CURRENT);
            int index = mBgColor.findIndexOfValue((String) newValue);
            mBgColor.setSummary(
                    mBgColor.getEntries()[index]);
            updateBgPrefs(bgColor);
            return true;
        } else if (preference ==  mIconColor) {
            int iconColor = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(resolver,
                    Settings.System.DISMISS_ALL_BUTTON_ICON_COLOR, iconColor, UserHandle.USER_CURRENT);
            int index = mIconColor.findIndexOfValue((String) newValue);
            mIconColor.setSummary(
                    mIconColor.getEntries()[index]);
            updateIconPrefs(iconColor);
            return true;
        } else if (preference == mBgColorCust) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ffffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DISMISS_ALL_BUTTON_BG_COLOR_CUSTOM, intHex);
            return true;
        } else if (preference == mIconColorCust) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ffffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.DISMISS_ALL_BUTTON_ICON_COLOR_CUSTOM, intHex);
            return true;
        } else if (preference == mTextColor) {
            int textColor = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(resolver,
                    Settings.System.TENX_FOOTER_TEXT_COLOR, textColor, UserHandle.USER_CURRENT);
            int index = mTextColor.findIndexOfValue((String) newValue);
            mTextColor.setSummary(
                    mTextColor.getEntries()[index]);
            updateColorPrefs(textColor);
            return true;
        } else if (preference == mTextColorCustom) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ffffffff")) {
                preference.setSummary(R.string.default_string);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.TENX_FOOTER_TEXT_COLOR_CUSTOM, intHex);
            return true;
        }
        return false;
    }

    private void updateBgPrefs(int bgColor) {
       if (bgColor == 2) {
           mBgColorCust.setEnabled(true);
       } else {
           mBgColorCust.setEnabled(false);
       }
    }

    private void updateIconPrefs(int iconColor) {
       if (iconColor == 2) {
           mIconColorCust.setEnabled(true);
       } else {
           mIconColorCust.setEnabled(false);
       }
    }

    private void updateColorPrefs(int textColor) {
       if (textColor == 2) {
           mTextColorCustom.setEnabled(true);
       } else {
           mTextColorCustom.setEnabled(false);
       }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
