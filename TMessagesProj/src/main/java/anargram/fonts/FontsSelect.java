package anargram.fonts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import ir.anargram.messenger.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;
import java.util.List;

import anargram.constant;



public class FontsSelect extends BaseFragment {


    private ListView listView;
    private List<font> Fonts = new ArrayList<>();

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
        actionBar.setTitle(LocaleController.getString("FontChange", R.string.FontChange));
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


        FrameLayout frameLayout = (FrameLayout) fragmentView;


        listView = new ListView(context);

        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        listView.setBackgroundColor(preferences.getInt("prefBGColor", 0xffffffff));
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setVerticalScrollBarEnabled(false);
        int def = preferences.getInt("themeColor", AndroidUtilities.defColor);
        int hColor = preferences.getInt("prefHeaderColor", def);
        AndroidUtilities.setListViewEdgeEffectColor(listView, /*AvatarDrawable.getProfileBackColorForId(5)*/ hColor);
        frameLayout.addView(listView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        listView.setLayoutParams(layoutParams);

        initFonts(context);



        return fragmentView;
    }


    private void initFonts(Context context) {

        font f = new font(context.getResources().getString(R.string.DefaultFont), "rmedium.ttf");
        Fonts.add(f);
        font f2 = new font(context.getResources().getString(R.string.IranSansLight), "iransans_light.ttf");
        Fonts.add(f2);
        font f3 = new font(context.getResources().getString(R.string.IranSans), "iransans.ttf");
        Fonts.add(f3);
        font f4 = new font(context.getResources().getString(R.string.IranSansMedium), "iransans_medium.ttf");
        Fonts.add(f4);
        font f5 = new font(context.getResources().getString(R.string.IranSansBold), "iransans_bold.ttf");
        Fonts.add(f5);
        font f6 = new font(context.getResources().getString(R.string.Yekan), "byekan.ttf");
        Fonts.add(f6);
        font f7 = new font(context.getResources().getString(R.string.Homa), "hama.ttf");
        Fonts.add(f7);
        font f8 = new font(context.getResources().getString(R.string.Handwrite), "dastnevis.ttf");
        Fonts.add(f8);
        font f9 = new font(context.getResources().getString(R.string.Morvarid), "morvarid.ttf");
        Fonts.add(f9);
        font f10 = new font(context.getResources().getString(R.string.Afsaneh), "afsaneh.ttf");
        Fonts.add(f10);

        refreshDisplay(context);

    }

    private void refreshDisplay(Context context) {

        FontAdapter listAdapter = new FontAdapter(context, R.layout.theme_row,Fonts);
        listView.setAdapter(listAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
            ApplicationLoader.getInstance().trackScreenView("Font Select Activity");
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
