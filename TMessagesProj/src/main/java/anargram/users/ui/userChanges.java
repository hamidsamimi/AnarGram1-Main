package anargram.users.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import ir.anargram.messenger.R;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ProfileActivity;

import java.util.List;

import anargram.constant;
import anargram.users.backend.changeAdapter;
import anargram.users.database.Change;
import anargram.users.database.changesDBAdapter;



public class userChanges extends BaseFragment {

    private final int MENU_DELETE = 1;
    private final int MENU_FILTER = 2;
    private ListView listView;

    private changeAdapter listAdapter;
    private changesDBAdapter db;
    private List<Change> changes;
    private int filter = 0;

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
        actionBar.setTitle(LocaleController.getString("contactChanges", R.string.contactChanges));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == MENU_DELETE) {
                    deleteAll(context);
                } else if (id == MENU_FILTER) {
                    setFilter(context);
                }
            }
        });


        db = new changesDBAdapter(context);


        ActionBarMenu menu = actionBar.createMenu();
        menu.addItemWithWidth(MENU_DELETE, R.drawable.ic_toolbar_delete_w, AndroidUtilities.dp(46));
        menu.addItemWithWidth(MENU_FILTER, R.drawable.ic_toolbar_filter, AndroidUtilities.dp(46));


        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        FrameLayout frameLayout = (FrameLayout) fragmentView;

       context. getSharedPreferences("mainconfig", Activity.MODE_PRIVATE).edit().putInt("ContactChangesCount" , 0 ).commit();

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
        refreshDisplay(context, 0);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putInt("user_id", changes.get(position).getUid());
                presentFragment(new ProfileActivity(args));
            }
        });

        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
        ApplicationLoader.getInstance().trackScreenView("user Changes");
    }

    private void refreshDisplay(Context context, int type) {
        try {
            db.open();
            if (type != 0) {
                changes = db.getAllItms(type);
            } else {
                changes = db.getAllItms();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.close();

        if (changes != null) {
            listAdapter = new changeAdapter(context, changes);
            listView.setAdapter(listAdapter);
        }
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

    private void setFilter(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.Filter));


        builder.setSingleChoiceItems(new CharSequence[]{context.getResources().getString(R.string.FilterALL),
                context.getResources().getString(R.string.FilterPics),
                context.getResources().getString(R.string.FilterPhones),
                context.getResources().getString(R.string.FilterUserName)}, filter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                filter = which;

            }
        });

        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                refreshDisplay(context, filter);


            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());

    }

    private void deleteAll(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        //builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setTitle(context.getResources().getString(R.string.DeleteAll));
        builder.setMessage(context.getResources().getString(R.string.DeleteAllText));


        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                changesDBAdapter db = new changesDBAdapter(context);
                db.open();
                db.deleteAll();
                db.close();
                refreshDisplay(context, 0);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());


    }

}

