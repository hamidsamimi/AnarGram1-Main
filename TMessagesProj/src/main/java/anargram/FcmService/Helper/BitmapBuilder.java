package anargram.FcmService.Helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.telegram.messenger.AndroidUtilities;


/**
 * Created by Saman on 12/10/2016.
 */
public class BitmapBuilder {
    public static Bitmap createTextBitmap(final String text, final float textSizePixels, final int textColour,Boolean bold)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(!bold) {
            paint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        }else{
            paint.setTypeface(AndroidUtilities.getTypeface("fonts/IRANSansMedium.ttf"));
        }
        paint.setTextSize(textSizePixels);
        paint.setColor(textColour);

        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
