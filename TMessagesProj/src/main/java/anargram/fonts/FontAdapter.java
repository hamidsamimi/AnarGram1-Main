package anargram.fonts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ir.anargram.messenger.R;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;

import java.util.List;


public class FontAdapter extends ArrayAdapter<font> {

    Context context;

    public FontAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public FontAdapter(Context context, int resource, List<font> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.font_row, null);
        }

        final font myFont = getItem(position);

        if (myFont != null) {
            TextView name = (TextView) v.findViewById(R.id.font);
            final String assetPath = "fonts/" + myFont.getAddress();
            name.setText(myFont.getName());
            name.setTextSize(18);
            Typeface t = Typeface.createFromAsset(ApplicationLoader.applicationContext.getAssets(), assetPath);
            name.setTypeface(t);


            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getResources().getString(R.string.FontChange));
                    builder.setMessage(LocaleController.getString("FontApplied", R.string.FontApplied) + "\n" + LocaleController.getString("ClickOkToRestart", R.string.ClickOkToRestart));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                            sharedPreferences.edit().putString("myFont", myFont.getAddress()).commit();
                            Utilities.restartApp();
                        }
                    });
                    builder.show();

                }
            });

        }

        return v;
    }
}
