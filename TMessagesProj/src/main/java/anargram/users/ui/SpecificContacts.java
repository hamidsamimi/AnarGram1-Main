package anargram.users.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.LayoutHelper;

import java.util.List;

import anargram.constant;
import anargram.users.backend.userAdapter;
import anargram.users.database.user;
import anargram.users.database.userDBAdapter;



public class SpecificContacts extends BaseFragment {

    private final int MENU_SETTINGS = 1;

    private ListView listView;
    private userAdapter listAdapter;
    private userDBAdapter db;
    private List<user> users;


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
        actionBar.setTitle(LocaleController.getString("SpecificContacts", R.string.SpecificContacts));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == MENU_SETTINGS) {
                    specificSettings(context);
                }
            }
        });


        ActionBarMenu menu = actionBar.createMenu();
        menu.addItemWithWidth(MENU_SETTINGS, R.drawable.ic_setting_dark, AndroidUtilities.dp(56));

        fragmentView = new FrameLayout(context);
        fragmentView.setLayoutParams(new FrameLayout.LayoutParams(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        FrameLayout frameLayout = (FrameLayout) fragmentView;


        db = new userDBAdapter(context);
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
        refreshDisplay(context);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setItems(new CharSequence[]{context.getResources().getString(R.string.OpenSpecificContact), context.getResources().getString(R.string.EditSpecific), context.getResources().getString(R.string.DeleteSpecific)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Bundle args = new Bundle();
                            args.putInt("user_id", users.get(position).getUid());
                            presentFragment(new ChatActivity(args));
                        } else if (which == 1) {
                            editspecefic(users.get(position), context);
                        } else if (which == 2) {
                            deleteSpecefic(users.get(position), context);
                        }
                    }
                });
                showDialog(builder.create());

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setItems(new CharSequence[]{context.getResources().getString(R.string.OpenSpecificContact), context.getResources().getString(R.string.EditSpecific), context.getResources().getString(R.string.DeleteSpecific)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Bundle args = new Bundle();
                            args.putInt("user_id", users.get(position).getUid());
                            presentFragment(new ChatActivity(args));
                        } else if (which == 1) {
                            editspecefic(users.get(position), context);
                        } else if (which == 2) {
                            deleteSpecefic(users.get(position), context);
                        }
                    }
                });
                showDialog(builder.create());
                return false;
            }
        });

        return fragmentView;
    }

    public void editspecefic(final user user, final Context context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        //builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setTitle(context.getResources().getString(R.string.EditSpecific));

        final Boolean[] items = {user.getPicup() == 1, user.getStatusup() == 1, user.getPhoneup() == 1, user.getIsonetime() == 1};

        builder.setMultiChoiceItems(new CharSequence[]{context.getResources().getString(R.string.picup),
                        context.getResources().getString(R.string.statusup),
                        context.getResources().getString(R.string.phoneup),
                        context.getResources().getString(R.string.isonetime)},
                new boolean[]{user.getPicup() == 1, user.getStatusup() == 1, user.getPhoneup() == 1, user.getIsonetime() == 1},
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        items[which] = isChecked;

                    }
                });

        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                userDBAdapter db = new userDBAdapter(context);
                db.open();
                db.updateIsSpecific(user.getUid(), 1);
                db.updatePicUp(user.getUid(), items[0] ? 1 : 0);
                db.updateStatusUp(user.getUid(), items[1] ? 1 : 0);
                db.updatePhoneUp(user.getUid(), items[2] ? 1 : 0);
                db.updateIsOneTime(user.getUid(), items[3] ? 1 : 0);
                db.close();
                refreshDisplay(context);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    public void deleteSpecefic(final user user, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        //builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setTitle(context.getResources().getString(R.string.DeleteSpecific));
        builder.setMessage(context.getResources().getString(R.string.DeleteSpecificText));


        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                userDBAdapter db = new userDBAdapter(context);
                db.open();
                db.updateIsSpecific(user.getUid(), 0);
                db.updatePicUp(user.getUid(), 0);
                db.updateStatusUp(user.getUid(), 0);
                db.updatePhoneUp(user.getUid(), 0);
                db.updateIsOneTime(user.getUid(), 0);
                db.close();
                refreshDisplay(context);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    private void refreshDisplay(Context context) {

        db.open();
        users = db.getAllItms(1);
        db.close();
            listAdapter = new userAdapter(context, users);
            listView.setAdapter(listAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
        if (constant.AnalyticInitialized)
        ApplicationLoader.getInstance().trackScreenView("Specific Contacts");
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


    private void specificSettings(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.SpecificSettings));

//        builder.setMessage(context.getResources().getString(R.string.SpecificSettingsText));

        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        boolean mode = sharedPreferences.getBoolean("evrytime", false);


        builder.setSingleChoiceItems(new CharSequence[]{context.getResources().getString(R.string.SpecificSettings1), context.getResources().getString(R.string.SpecificSettings2)}, mode ? 1 : 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    edit.putBoolean("evrytime", false);
                } else {
                    edit.putBoolean("evrytime", true);
                }

            }
        });

        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                edit.commit();
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

}
