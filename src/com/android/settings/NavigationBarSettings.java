package com.android.settings;
import com.android.settings.R;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class NavigationBarSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String NAV_BUTTONS_SLOT_ONE = "nav_buttons_slot_one";
    private static final String NAV_BUTTONS_SLOT_FIVE = "nav_buttons_slot_five";
    private static final String USE_ALT_ICONS = "use_alt_icons";
    private static final String NAVIGATION_BUTTON_COLOR = "navigation_button_color";
    private ListPreference mSlotOne;
    private ListPreference mSlotFive;
    private CheckBoxPreference mUseAltIcons;
    private ColorPickerPreference mNavButtonColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.navigation_bar_settings);
        PreferenceScreen prefSet = getPreferenceScreen();

        mSlotOne = (ListPreference) prefSet.findPreference(NAV_BUTTONS_SLOT_ONE);
        int slotOneValue = Settings.System.getInt(getContentResolver(),
                Settings.System.NAV_BUTTONS_SLOT_ONE, 0);
        mSlotOne.setValueIndex(slotOneValue);
        mSlotOne.setOnPreferenceChangeListener(this);

        mSlotFive = (ListPreference) prefSet.findPreference(NAV_BUTTONS_SLOT_FIVE);
        int slotFiveValue = Settings.System.getInt(getContentResolver(),
                Settings.System.NAV_BUTTONS_SLOT_FIVE, 0);
        mSlotFive.setValueIndex(slotFiveValue);
        mSlotFive.setOnPreferenceChangeListener(this);

        mUseAltIcons = (CheckBoxPreference) prefSet.findPreference(USE_ALT_ICONS);
        mUseAltIcons.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.USE_ALT_ICONS, 0) == 1);

        mNavButtonColor = (ColorPickerPreference) prefSet.findPreference(NAVIGATION_BUTTON_COLOR);
        mNavButtonColor.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mUseAltIcons) {
            value = mUseAltIcons.isChecked();
            Settings.System.putInt(getContentResolver(),
                Settings.System.USE_ALT_ICONS, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNavButtonColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                .valueOf(newValue)));
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BUTTON_COLOR, intHex);
            return true;
        } else if (preference == mSlotOne) {
            int valOne = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAV_BUTTONS_SLOT_ONE, valOne);
            return true;
        } else if (preference == mSlotFive) {
            int valFive = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAV_BUTTONS_SLOT_FIVE, valFive);
            return true;
        }
        return false;
    }
}
