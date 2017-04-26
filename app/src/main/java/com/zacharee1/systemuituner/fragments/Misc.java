package com.zacharee1.systemuituner.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.*;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.zacharee1.systemuituner.MainActivity;
import com.zacharee1.systemuituner.NoRootSystemSettingsActivity;
import com.zacharee1.systemuituner.R;
import com.zacharee1.systemuituner.SetupActivity;

import java.io.BufferedReader;

/**
 * Created by Zacha on 4/18/2017.
 */

@SuppressWarnings("ALL")
public class Misc extends Fragment {
    private View view;
    private MainActivity activity;

    private Switch show_full_zen;
    private Switch hu_notif;
    private Switch vol_warn;
    private Switch enable_custom_settings;

    private Button animApply;
    private Button transApply;
    private Button winApply;
    private Button globalApply;
    private Button secureApply;
    private Button systemApply;

    private TextInputEditText anim;
    private TextInputEditText trans;
    private TextInputEditText win;
    private TextInputEditText custom_global;
    private TextInputEditText custom_secure;
    private TextInputEditText custom_system;

    private float animScale;
    private float transScale;
    private float winScale;

    private LinearLayout custom_settings;

    private final int alertRed = R.drawable.ic_warning_red;

    private String global;
    private String secure;
    private String system;

    private boolean customSettingsEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            activity = (MainActivity) getActivity();
        }

        customSettingsEnabled = activity.setThings.sharedPreferences.getBoolean("customSettingsEnabled", false);

        global = new String();
        secure = new String();
        system = new String();

        activity.setTitle("Miscellaneous"); //set proper fragment title

        view = inflater.inflate(R.layout.fragment_misc, container, false);

        show_full_zen = (Switch) view.findViewById(R.id.show_full_zen);
        hu_notif = (Switch) view.findViewById(R.id.hu_notif);
        vol_warn = (Switch) view.findViewById(R.id.vol_warn);
        enable_custom_settings = (Switch) view.findViewById(R.id.enable_custom);

        enable_custom_settings.setChecked(customSettingsEnabled);

        custom_settings = (LinearLayout) view.findViewById(R.id.custom_settings);
        custom_settings.setVisibility(customSettingsEnabled ? View.VISIBLE : View.GONE);

        animApply = (Button) view.findViewById(R.id.apply_anim);
        transApply = (Button) view.findViewById(R.id.apply_trans);
        winApply = (Button) view.findViewById(R.id.apply_win);
        globalApply = (Button) view.findViewById(R.id.apply_global);
        secureApply = (Button) view.findViewById(R.id.apply_secure);
        systemApply = (Button) view.findViewById(R.id.apply_system);

        anim = (TextInputEditText) view.findViewById(R.id.anim_text);
        trans = (TextInputEditText) view.findViewById(R.id.trans_text);
        win = (TextInputEditText) view.findViewById(R.id.win_text);
        custom_global = (TextInputEditText) view.findViewById(R.id.global_settings);
        custom_secure = (TextInputEditText) view.findViewById(R.id.secure_settings);
        custom_system = (TextInputEditText) view.findViewById(R.id.system_settings);

        animScale = Settings.Global.getFloat(activity.getContentResolver(), "animator_duration_scale", (float)1.0);
        transScale = Settings.Global.getFloat(activity.getContentResolver(), "transition_animation_scale", (float)1.0);
        winScale = Settings.Global.getFloat(activity.getContentResolver(), "window_animation_scale", (float)1.0);

        anim.setHint(getResources().getText(R.string.animator_duration_scale) + " (" + String.valueOf(animScale) + ")");
        trans.setHint(getResources().getText(R.string.transition_animation_scale) + " (" + String.valueOf(transScale) + ")");
        win.setHint(getResources().getText(R.string.window_animation_scale) + " (" + String.valueOf(winScale) + ")");
        custom_global.setHint(getResources().getText(R.string.global));
        custom_secure.setHint(getResources().getText(R.string.secure));
        custom_system.setHint(getResources().getText(R.string.system));

        activity.setThings.switches(show_full_zen, "sysui_show_full_zen", "secure", view); //switch listener
        activity.setThings.switches(hu_notif, "heads_up_notifications_enabled", "global", view);
        activity.setThings.switches(vol_warn, "audio_safe_volume_state", "global", view);

        buttons(animApply);
        buttons(transApply);
        buttons(winApply);
        buttons(globalApply);
        buttons(secureApply);
        buttons(systemApply);

        textFields(anim);
        textFields(trans);
        textFields(win);
        textFields(custom_global);
        textFields(custom_secure);
        textFields(custom_system);

        switches(enable_custom_settings);

        return view;
    }

    private void switches(final Switch toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.setThings.editor.putBoolean("customSettingsEnabled", isChecked);
                activity.setThings.editor.apply();
                if (isChecked) {
                    new AlertDialog.Builder(view.getContext()) //show a dialog with the error and prompt user to set up permissions again
                            .setIcon(alertRed)
                            .setTitle(Html.fromHtml("<font color='#ff0000'>WRANING</font>"))
                            .setMessage("What lies ahead holds many dangers. I take no responsibility if you mess your device up using this feature. Please be cautious! Continue?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    custom_settings.setVisibility(View.VISIBLE);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    enable_custom_settings.setChecked(false);
                                }
                            })
                            .show();
                } else custom_settings.setVisibility(View.GONE);
            }
        });
    }

    private void textFields(final TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textInputEditText.getText().length() > 0) {
                    final String value = textInputEditText.getText().toString();
                    float val = (float)1.0;

                    try {
                        val = Float.valueOf(value);
                    } catch (NumberFormatException e) {}

                    if (textInputEditText == anim) animScale = val;
                    else if (textInputEditText == trans) transScale = val;
                    else if (textInputEditText == win) winScale = val;
                    else if (textInputEditText == custom_global) global = value;
                    else if (textInputEditText == custom_secure) secure = value;
                    else if (textInputEditText == custom_system) system = value;
                }
            }
        });
    }

    private void buttons(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pref;
                final String val;

                if (button == animApply) {
                    pref = "animator_duration_scale";
                    val = String.valueOf(animScale);
                } else if (button == transApply) {
                    pref = "transition_animation_scale";
                    val = String.valueOf(transScale);
                } else if (button == winApply) {
                    pref = "window_animation_scale";
                    val = String.valueOf(winScale);
                } else if (button == globalApply) {
                    pref = global.substring(0, global.indexOf(" "));
                    val = global.substring(global.indexOf(" ") + 1);
                } else if (button == secureApply) {
                    pref = secure.substring(0, secure.indexOf(" "));
                    val = secure.substring(secure.indexOf(" ") + 1);
                } else if (button == systemApply) {
                    pref = system.substring(0, system.indexOf(" "));
                    val = system.substring(system.indexOf(" ") + 1);
                    if (activity.setThings.sharedPreferences.getBoolean("isRooted", true)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                activity.setThings.sudo("settings put system " + pref + " " + val);
                            }
                        }).start();
                    }
                    else {
                        activity.setThings.editor.putString("isSystemSwitchEnabled", val);
                        activity.setThings.editor.putString("systemSettingKey", pref);
                        activity.setThings.editor.apply();
                        Intent intent = new Intent(activity.getApplicationContext(), NoRootSystemSettingsActivity.class);
                        activity.startActivity(intent);
                    }
                } else {
                    pref = new String();
                    val = new String();
                }

                Settings.Global.putString(activity.getContentResolver(), pref, val);
            }
        });
    }
}
