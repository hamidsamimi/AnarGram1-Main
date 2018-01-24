/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import ir.anargram.messenger.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;

public class TextColorCell extends FrameLayout {

    private TextView textView;
    private boolean needDivider;
    private int currentColor;

    private Drawable colorDrawable;
    private static Paint paint;


    public final static int colors[] = new int[] {0xfff04444, 0xffff8e01, 0xffffce1f, 0xff79d660, 0xff40edf6, 0xff46beff, 0xffd274f9, 0xffff4f96, 0xffbbbbbb};
    public final static int colorsToSave[] = new int[] {0xffff0000, 0xffff8e01, 0xffffce1f, 0xff00ff00, 0xff40edf6, 0xff0000ff, 0xffd274f9, 0xffff4f96, 0xffffffff};


    public TextColorCell(Context context) {
        super(context);

        if (paint == null) {
            paint = new Paint();
            paint.setColor(0xffd9d9d9);
            paint.setStrokeWidth(1);
        }

        colorDrawable = getResources().getDrawable(R.drawable.switch_to_on2);

        textView = new TextView(context);
        textView.setTextColor(0xff212121);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
        addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.TOP, 17, 0, 45, 0));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48) + (needDivider ? 1 : 0), MeasureSpec.EXACTLY));
        setTheme();
    }

    public void setTextAndColor(String text, int color, boolean divider) {
        textView.setText(text);
        needDivider = divider;
        currentColor = color;
        colorDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        setWillNotDraw(!needDivider && currentColor == 0);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needDivider) {
            canvas.drawLine(getPaddingLeft(), getHeight() - 1, getWidth() - getPaddingRight(), getHeight() - 1, paint);
        }
        if (currentColor != 0 && colorDrawable != null) {
            int x;
            int y = (getMeasuredHeight() - colorDrawable.getMinimumHeight()) / 2;
            if (!LocaleController.isRTL) {
                x = getMeasuredWidth() - colorDrawable.getIntrinsicWidth() - AndroidUtilities.dp(14.5f);
            } else {
                x = AndroidUtilities.dp(14.5f);
            }
            colorDrawable.setBounds(x, y, x + colorDrawable.getIntrinsicWidth(), y + colorDrawable.getIntrinsicHeight());
            colorDrawable.draw(canvas);
        }
    }

    private void setTheme(){
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(AndroidUtilities.THEME_PREFS, AndroidUtilities.THEME_PREFS_MODE);
        int bgColor = preferences.getInt("prefBGColor", 0xffffffff);
        setBackgroundColor(bgColor);
        int divColor = preferences.getInt("prefDividerColor", 0xffd9d9d9);
        int titleColor = preferences.getInt("prefTitleColor", 0xff212121);
        textView.setTextColor(titleColor);
        paint.setColor(divColor);
    }

    public void setEnabled(boolean value, ArrayList<Animator> animators) {
        if (animators != null) {
            animators.add(ObjectAnimator.ofFloat(textView, "alpha", value ? 1.0f : 0.5f));
            animators.add(ObjectAnimator.ofFloat(this, "alpha", value ? 1.0f : 0.5f));
        } else {
            textView.setAlpha(value ? 1.0f : 0.5f);
            setAlpha(value ? 1.0f : 0.5f);
        }
    }
}
