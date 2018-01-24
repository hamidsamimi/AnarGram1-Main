package anargram.users.backend;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.anargram.messenger.R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Adapters.BaseSectionsAdapter;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Components.LayoutHelper;

import java.util.List;

import anargram.users.database.user;
import anargram.users.ui.CustomUserCell;

import static android.content.ContentValues.TAG;


public class userAdapter extends BaseSectionsAdapter {

    private Context context ;
    private List<user> users ;
    ImageView picup , statusup , phoneup , isonetime ;


    public userAdapter(Context context , List<user> users) {
        this.users = users ;
        this.context = context ;
        Log.i(TAG, "userAdapter: created");
    }

    @Override
    public Object getItem(int section, int position) {
        return users.get(position);
    }

    @Override
    public boolean isRowEnabled(int section, int row) {
        return true;
    }


    @Override
    public int getSectionCount() {
        return 1;
    }

    @Override
    public int getCountForSection(int section) {
        return users.size();
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        return null;
    }


    @Override
    public int getItemViewType(int section, int position) {
        return 0;
    }


    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        View x = null;
        if (x == null) {


            LinearLayout base = new LinearLayout(context);
            base.setOrientation(LinearLayout.VERTICAL);
            base.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout l = new LinearLayout(context);
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setGravity((LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT));
            l.setPadding(50,0,50,0);


            picup = new ImageView(context);
            picup.setScaleType(ImageView.ScaleType.CENTER);
            picup.setVisibility(View.GONE);
            picup.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attach_file));


            statusup = new ImageView(context);
            statusup.setScaleType(ImageView.ScaleType.CENTER);
            statusup.setVisibility(View.GONE);
            statusup.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attach_contact));

            phoneup = new ImageView(context);
            phoneup.setScaleType(ImageView.ScaleType.CENTER);
            phoneup.setVisibility(View.GONE);
            phoneup.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.specific_phoneup));

            isonetime = new ImageView(context);
            isonetime.setScaleType(ImageView.ScaleType.CENTER);
            isonetime.setVisibility(View.GONE);
            isonetime.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.specific_one_time));

            setVisiblity(users.get(position));

            l.addView(picup , 100 , 100);
            l.addView(statusup , 100 , 100);
            l.addView(phoneup , 100 , 100);
            l.addView(isonetime , 100 , 100);



            x = new CustomUserCell(context, 5, 1, false);
            base.addView(x , LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT , LayoutHelper.WRAP_CONTENT ));

            base.addView(l , LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT , LayoutHelper.WRAP_CONTENT ));
            base.addView(new DividerCell(context) , LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT , LayoutHelper.WRAP_CONTENT ));


            convertView = base ;
            ((CustomUserCell) x).setStatusColors(0xffa8a8a8, 0xff3b84c0);
            convertView.setTag("Contacts");
        }


        TLRPC.User user = MessagesController.getInstance().getUser(users.get(position).getUid());
        ((CustomUserCell) x).setData(user, null,null, 0);
        convertView.setTag("Contacts");
        return convertView;
    }


    private void setVisiblity(user user){

        if (user.getPicup() == 1){
            picup.setVisibility(View.VISIBLE);
        }

        if (user.getStatusup() == 1){
            statusup.setVisibility(View.VISIBLE);
        }

        if (user.getPhoneup()== 1){
            phoneup.setVisibility(View.VISIBLE);
        }

        if (user.getIsonetime() == 1){
            isonetime.setVisibility(View.VISIBLE);
        }
    }


}
