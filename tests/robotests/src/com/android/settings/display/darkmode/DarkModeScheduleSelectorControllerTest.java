/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.android.settings.display.darkmode;

import android.app.UiModeManager;
import android.content.Context;
import androidx.preference.DropDownPreference;
import androidx.preference.PreferenceScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import com.android.settings.R;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DarkModeScheduleSelectorControllerTest {
    private DarkModeScheduleSelectorController mController;
    private String mPreferenceKey = "key";
    @Mock
    private DropDownPreference mPreference;
    @Mock
    private PreferenceScreen mScreen;
    private Context mContext;
    @Mock
    private UiModeManager mService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mContext = spy(RuntimeEnvironment.application);
        when(mContext.getSystemService(UiModeManager.class)).thenReturn(mService);
        when(mContext.getString(R.string.dark_ui_auto_mode_never)).thenReturn("never");
        when(mContext.getString(R.string.dark_ui_auto_mode_auto)).thenReturn("auto");
        mPreference = spy(new DropDownPreference(mContext));
        when(mScreen.findPreference(anyString())).thenReturn(mPreference);
        when(mService.setNightModeActivated(anyBoolean())).thenReturn(true);
        mController = new DarkModeScheduleSelectorController(mContext, mPreferenceKey);
    }

    @Test
    public void nightMode_preferenceChange_preferenceChangeTrueWhenChangedOnly() {
        when(mService.getNightMode()).thenReturn(UiModeManager.MODE_NIGHT_YES);
        mController.displayPreference(mScreen);
        boolean changed = mController
                .onPreferenceChange(mScreen, mContext.getString(R.string.dark_ui_auto_mode_auto));
        assertTrue(changed);
        changed = mController
                .onPreferenceChange(mScreen, mContext.getString(R.string.dark_ui_auto_mode_auto));
        assertFalse(changed);
    }

    @Test
    public void nightMode_updateStateNone_dropDownValueChangedToNone() {
        when(mService.getNightMode()).thenReturn(UiModeManager.MODE_NIGHT_YES);
        mController.displayPreference(mScreen);
        mController.updateState(mPreference);
        verify(mPreference).setValue(mContext.getString(R.string.dark_ui_auto_mode_never));
    }

    @Test
    public void nightMode_updateStateNone_dropDownValueChangedToAuto() {
        when(mService.getNightMode()).thenReturn(UiModeManager.MODE_NIGHT_AUTO);
        mController.displayPreference(mScreen);
        mController.updateState(mPreference);
        verify(mPreference).setValue(mContext.getString(R.string.dark_ui_auto_mode_auto));
    }
}
