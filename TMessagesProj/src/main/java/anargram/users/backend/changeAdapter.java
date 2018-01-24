package anargram.users.backend;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.anargram.messenger.R;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Adapters.BaseSectionsAdapter;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Components.LayoutHelper;

import java.util.List;

import anargram.users.database.Change;
import anargram.users.ui.CustomUserCell;


public class changeAdapter extends BaseSectionsAdapter {

    private Context context;
    private List<Change> changes;
    TextView typetext, timetext;
    private int nameColor = 0xff000000;
    private int typecolor = 0xffa8a8a8;


    public changeAdapter(Context context, List<Change> changes) {
        this.changes = changes;
        this.context = context;
    }

    @Override
    public Object getItem(int section, int position) {
        return changes.get(position);
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
        return changes.size();
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


            LinearLayout l = new LinearLayout(context);
            l.setOrientation(LinearLayout.VERTICAL);
            l.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT));
            l.setPadding(50, 0, 50, 0);

            typetext = new TextView(context);
            typetext.setPadding(28, 0, 28, 0);
            typetext.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            typetext.setTextColor(typecolor);
            typetext.setTextSize(14);

            timetext = new TextView(context);
            timetext.setPadding(28, 0, 28, 0);
            timetext.setGravity(LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT);
            timetext.setTextColor(nameColor);
            timetext.setTextSize(14);

            setText(changes.get(position), context);
            l.addView(typetext);
            l.addView(timetext);

            x = new CustomUserCell(context, 5, 1, false);
            base.addView(x, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            base.addView(l, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
            base.addView(new DividerCell(context), LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));


            convertView = base;
            ((CustomUserCell) x).setStatusColors(0xffa8a8a8, 0xff3b84c0);
            convertView.setTag("Contacts");
        }


        TLRPC.User user = MessagesController.getInstance().getUser(changes.get(position).getUid());
        ((CustomUserCell) x).setData(user, null, null, 0);
        convertView.setTag("Contacts");
        return convertView;
    }


    private void setText(Change change, Context context) {

        switch (change.getType()) {
            case 1:
                typetext.setText(context.getResources().getText(R.string.typeChangePic));
                break;
            case 2:
                typetext.setText(context.getResources().getText(R.string.typeChangephone));
                break;
            case 3:
                typetext.setText(context.getResources().getText(R.string.typeChangeusername));
                break;
        }

        timetext.setText(change.getTime());

    }


}


