package anargram;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import ir.anargram.messenger.BuildConfig;
import ir.anargram.messenger.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;


public class aboutUsActivity extends BaseFragment {


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
        actionBar.setTitle(LocaleController.getString("aboutUs", R.string.aboutUs));
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

        fragmentView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.about_us_activity, null);

        CardView about_review, about_other_apps, about_official_Channel, about_contact;

        about_review = (CardView) fragmentView.findViewById(R.id.about_review);
        about_other_apps = (CardView) fragmentView.findViewById(R.id.about_other_apps);
        about_official_Channel = (CardView) fragmentView.findViewById(R.id.about_official_Channel);
        about_contact = (CardView) fragmentView.findViewById(R.id.about_contact);

        about_other_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = context.getPackageManager();
                if (isPackageInstalled("com.farsitel.bazaar", pm)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("bazaar://collection?slug=by_author&aid=" + constant.DEVELOPER_ID));
                    intent.setPackage("com.farsitel.bazaar");
                    context.startActivity(intent);
                }
            }
        });

        about_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = context.getPackageManager();
                if (isPackageInstalled("com.farsitel.bazaar", pm)) {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setData(Uri.parse("bazaar://details?id=" +( BuildConfig.DEBUG ? BuildVars.BUILD_PACKAGENAME + ".beta" :BuildVars.BUILD_PACKAGENAME)));
                    intent.setPackage("com.farsitel.bazaar");
                    context.startActivity(intent);
                }

            }
        });


        about_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = constant.OFFICIAL_ID_URL;
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

                if (!BuildConfig.DEBUG) {
                    telegram.setPackage(BuildVars.BUILD_PACKAGENAME);
                } else
                    telegram.setPackage(BuildVars.BUILD_PACKAGENAME + ".beta");

                context.startActivity(telegram);
            }
        });

        about_official_Channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = constant.OFFICIALCHANNEL_URL;
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

                if (!BuildConfig.DEBUG) {
                    telegram.setPackage(BuildVars.BUILD_PACKAGENAME);
                } else
                    telegram.setPackage(BuildVars.BUILD_PACKAGENAME + ".beta");

                context.startActivity(telegram);
            }
        });


        return fragmentView;
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
            ApplicationLoader.getInstance().trackScreenView("About us Activity");
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
