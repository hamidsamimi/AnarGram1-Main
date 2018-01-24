package anargram;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import ir.anargram.messenger.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.LayoutHelper;

public class voiceActivity extends BaseFragment {
    int userId;
    int selected = 0;
    int selectedchoice = 0;


    public voiceActivity(Bundle args) {

        if (args != null) {
            userId = args.getInt("user_id");
        }
    }

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();


        return true;
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    @Override
    public View createView(final Context context) {
        actionBar.setBackgroundColor(Theme.ACTION_BAR_MEDIA_PICKER_COLOR);
        actionBar.setItemsBackgroundColor(Theme.ACTION_BAR_PICKER_SELECTOR_COLOR);
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("voicechanger", R.string.voicechanger));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });


        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        fragmentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.voice_activity, null);
        RadioGroup radioGroup = (RadioGroup) fragmentView.findViewById(R.id.radio_group);
        Button save = (Button) fragmentView.findViewById(R.id.save_bottom);
        save.setBackgroundColor(AndroidUtilities.themeColor);
        LinearLayout ml = (LinearLayout) fragmentView.findViewById(R.id.confvoice);
        final TextCheckCell cell = new TextCheckCell(context);

        ml.addView(cell);

        final SharedPreferences mpreferences = ApplicationLoader.applicationContext.getSharedPreferences("AnarGramConfig", Activity.MODE_PRIVATE);
        cell.setTextAndCheck(LocaleController.getString("chatvoiceRow", R.string.chatvoiceRow), mpreferences.getBoolean("chatvoiceRow", true), false);
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hide = mpreferences.getBoolean("chatvoiceRow", true);
                SharedPreferences.Editor editor = mpreferences.edit();
                editor.putBoolean("chatvoiceRow", !hide);
                editor.apply();
                if (cell instanceof TextCheckCell) {
                    cell.setChecked(!hide);
                }
            }
        });


        final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE);
        int rec_int = preferences.getInt("voice_radio_id_" + userId, 1);
        selected = 1;
        selectedchoice = rec_int ;
        radioGroup.check(rec_int);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_01:
                        selected = 1 ;
                        break;
                    case R.id.radio_02:
                        selected = 2 ;
                        break;
                    case R.id.radio_03:
                        selected = 3 ;
                        break;
                    case R.id.radio_04:
                        selected = 4 ;
                        break;
                    case R.id.radio_05:
                        selected = 5 ;
                        break;
                    case R.id.radio_06:
                        selected = 6 ;
                        break;
                }
                selectedchoice = i ;

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putInt("voice_ch_" + userId, selected).commit();
                preferences.edit().putInt("voice_radio_id_" + userId, selectedchoice).commit();
                finishFragment();
            }
        });

        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
            ApplicationLoader.getInstance().trackScreenView("Voice Activity");
    }


    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();

    }

    private void updateTheme() {
        SharedPreferences themePrefs = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int def = themePrefs.getInt("themeColor", AndroidUtilities.defColor);
        actionBar.setBackgroundColor(themePrefs.getInt("prefHeaderColor", def));
        actionBar.setTitleColor(themePrefs.getInt("prefHeaderTitleColor", 0xffffffff));

        Drawable back = getParentActivity().getResources().getDrawable(R.drawable.ic_ab_back);
        back.setColorFilter(themePrefs.getInt("prefHeaderIconsColor", 0xffffffff), PorterDuff.Mode.MULTIPLY);
        actionBar.setBackButtonDrawable(back);
    }


}
