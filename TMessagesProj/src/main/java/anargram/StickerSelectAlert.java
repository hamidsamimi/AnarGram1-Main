package anargram;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.vision.face.Face;
import ir.anargram.messenger.R;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.query.StickersQuery;
import org.telegram.messenger.volley.DefaultRetryPolicy;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.StickerPreviewViewer;


public class StickerSelectAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private TextView f663a;
    private FrameLayout f664b;
    private TextView f665c;
    private View f666d;
    private BackupImageView f667e;
    private TextView f668f;
    private Drawable f669g;
    private View[] f670h;
    private TLRPC.Document f671i;
    private StickersAlert.StickersAlertDelegate f672j;
    private int f673k;
    private int f674l;

//
    class StickerSelectAlert2 extends FrameLayout {
        final /* synthetic */ StickerSelectAlert f660a;

        StickerSelectAlert2(StickerSelectAlert stickerSelectAlert, Context context) {
            super(context);
            this.f660a = stickerSelectAlert;
        }

        protected void onDraw(Canvas canvas) {
            if (VERSION.SDK_INT >= 11) {
               f669g.setBounds(0,f673k - StickerSelectAlert.backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
               f669g.draw(canvas);
            }
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0 ||f673k == 0 || motionEvent.getY() >= ((float)f673k)) {
                return super.onInterceptTouchEvent(motionEvent);
            }
           dismiss();
            return true;
        }

        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (VERSION.SDK_INT >= 11) {
               m637c();
            }
        }

        protected void onMeasure(int i, int i2) {
            int size = MeasureSpec.getSize(i2);
            if (VERSION.SDK_INT >= 21) {
                size -= AndroidUtilities.statusBarHeight;
            }
            int dp = (AndroidUtilities.dp(96.0f) + (Math.max(3, 0) * AndroidUtilities.dp(82.0f))) + StickerSelectAlert.backgroundPaddingTop;
            if (VERSION.SDK_INT < 11) {
                super.onMeasure(i, MeasureSpec.makeMeasureSpec(Math.min(dp, (AndroidUtilities.displaySize.y / 5) * 3), C.ENCODING_PCM_32BIT));
            } else {
                super.onMeasure(i, MeasureSpec.makeMeasureSpec(Math.min(dp, size), C.ENCODING_PCM_32BIT));
            }
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !isDismissed() && super.onTouchEvent(motionEvent);
        }

        public void requestLayout() {
            super.requestLayout();
        }
    }

//    /* renamed from: com.hanista.mobogram.mobo.component.h.2 */
//    class StickerSelectAlert implements OnClickListener {
//        final /* synthetic */ StickerSelectAlert f661a;
//
//        StickerSelectAlert(StickerSelectAlert stickerSelectAlert) {
//            this.f661a = stickerSelectAlert;
//        }
//
//        public void onClick(View view) {
//            dismiss();
//        }
//    }
//
//    /* renamed from: com.hanista.mobogram.mobo.component.h.3 */
//    class StickerSelectAlert implements OnClickListener {
//        final /* synthetic */ StickerSelectAlert f662a;
//
//        StickerSelectAlert(StickerSelectAlert stickerSelectAlert) {
//            this.f662a = stickerSelectAlert;
//        }
//
//        public void onClick(View view) {
//            f672j.onStickerSelected(f671i);
//           dismiss();
//        }
//    }

    public StickerSelectAlert(Context context, TLRPC.Document document, StickersAlert.StickersAlertDelegate stickersAlertDelegate) {
        super(context,false);
        Object obj;
        LayoutParams layoutParams;
        AnimatorSet animatorSet;

        this.f670h = new View[2];
        this.f673k = 0;
        this.f671i = document;
        this.f672j = stickersAlertDelegate;
        this.f669g = context.getResources().getDrawable(R.drawable.sheet_shadow);
        this.containerView = new StickerSelectAlert2(this, context);
        if (VERSION.SDK_INT < 11) {
            this.containerView.setBackgroundDrawable(this.f669g);
        } else {
            this.containerView.setWillNotDraw(false);
        }

        this.containerView.setPadding(backgroundPaddingLeft, VERSION.SDK_INT < 11 ? backgroundPaddingTop : 0, backgroundPaddingLeft, 0);
        this.f663a = new TextView(context);
        this.f670h[0] = new View(context);
        this.f670h[0].setBackgroundResource(R.drawable.header_shadow);
        this.f670h[0].setAlpha(0.0f);
        this.f670h[0].clearAnimation();
        this.f670h[0].setVisibility(View.INVISIBLE);
        this.f670h[0].setTag(Integer.valueOf(1));
        this.containerView.addView(this.f670h[0], LayoutHelper.createFrame(-1, 3.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        this.f670h[1] = new View(context);
        this.f670h[1].setBackgroundResource(R.drawable.header_shadow_reverse);
        this.containerView.addView(this.f670h[1], LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        this.f664b = new FrameLayout(context);
        this.f664b.setBackgroundColor(0xfffafaa);
        this.f664b.setVisibility(View.GONE);
        this.f664b.setSoundEffectsEnabled(false);
        this.containerView.addView(this.f664b, LayoutHelper.createFrame(-1, Face.UNCOMPUTED_PROBABILITY));
        containerView.setBackgroundColor(0x00fff);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.delete_reply);
        imageView.setScaleType(ScaleType.CENTER);
        if (VERSION.SDK_INT >= 21) {
            imageView.setBackgroundDrawable(Theme.createBarSelectorDrawable(Theme.INPUT_FIELD_SELECTOR_COLOR));
        }
        this.f664b.addView(imageView, LayoutHelper.createFrame(48, 48, 53));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  dismiss();
            }
        });
        this.f667e = new BackupImageView(context);
        this.f667e.setAspectFit(true);
        int min = (int) (((float) (Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) / 2)) / AndroidUtilities.density);
        this.f664b.addView(this.f667e, LayoutHelper.createFrame(min, min, 17));
        this.f668f = new TextView(context);
        this.f668f.setTextSize(1, BitmapDescriptorFactory.HUE_ORANGE);
        this.f668f.setGravity(85);
        this.f664b.addView(this.f668f, LayoutHelper.createFrame(min, min, 17));
        this.f665c = new TextView(context);
        this.f665c.setTextSize(1, 14.0f);
        this.f665c.setTextColor(Theme.STICKERS_SHEET_SEND_TEXT_COLOR);
        this.f665c.setGravity(17);
        this.f665c.setBackgroundColor(-1);
        this.f665c.setPadding(AndroidUtilities.dp(29.0f), 0, AndroidUtilities.dp(29.0f), 0);
        this.f665c.setText(LocaleController.getString("Close", R.string.Close).toUpperCase());
        this.f665c.setVisibility(View.GONE);
        this.f664b.addView(this.f665c, LayoutHelper.createFrame(-1, 48, 83));
        this.f665c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f672j.onStickerSelected(f671i);
                dismiss();
            }
        });
        this.f666d = new View(context);
        this.f666d.setBackgroundResource(R.drawable.header_shadow_reverse);
        this.f666d.setVisibility(View.GONE);
        this.f664b.addView(this.f666d, LayoutHelper.createFrame(-1, 3.0f, 83, 0.0f, 0.0f, 0.0f, 48.0f));
        if (this.f672j != null) {
            this.f665c.setText(LocaleController.getString("SendSticker", R.string.SendSticker).toUpperCase());
            this.f667e.setLayoutParams(LayoutHelper.createFrame(min, (float) min, 17, 0.0f, 0.0f, 0.0f, BitmapDescriptorFactory.HUE_ORANGE));
            this.f668f.setLayoutParams(LayoutHelper.createFrame(min, (float) min, 17, 0.0f, 0.0f, 0.0f, BitmapDescriptorFactory.HUE_ORANGE));
            this.f665c.setVisibility(View.VISIBLE);
            this.f666d.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < this.f671i.attributes.size(); i++) {
            TLRPC.DocumentAttribute documentAttribute = (TLRPC.DocumentAttribute) this.f671i.attributes.get(i);
            if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                if (documentAttribute.alt != null && documentAttribute.alt.length() > 0) {
                    this.f668f.setText(Emoji.replaceEmoji(documentAttribute.alt, this.f668f.getPaint().getFontMetricsInt(), AndroidUtilities.dp(BitmapDescriptorFactory.HUE_ORANGE), false));
                    obj = 1;
                    if (obj == null) {
                        this.f668f.setText(Emoji.replaceEmoji(StickersQuery.getEmojiForSticker(this.f671i.id), this.f668f.getPaint().getFontMetricsInt(), AndroidUtilities.dp(BitmapDescriptorFactory.HUE_ORANGE), false));
                    }
                    this.f667e.getImageReceiver().setImage(this.f671i, null, this.f671i.thumb.location, null, "webp", true);
                    layoutParams = (LayoutParams) this.f664b.getLayoutParams();
                    layoutParams.topMargin = this.f673k;
                    this.f664b.setLayoutParams(layoutParams);
                    this.f664b.setVisibility(View.VISIBLE);
                    animatorSet = new AnimatorSet();
                    animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.f664b, "alpha", new float[]{0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT})});
                    animatorSet.setDuration(200);
                    animatorSet.start();
                }
                obj = null;
                if (obj == null) {
                    this.f668f.setText(Emoji.replaceEmoji(StickersQuery.getEmojiForSticker(this.f671i.id), this.f668f.getPaint().getFontMetricsInt(), AndroidUtilities.dp(BitmapDescriptorFactory.HUE_ORANGE), false));
                }
                this.f667e.getImageReceiver().setImage(this.f671i, null, this.f671i.thumb.location, null, "webp", true);
                layoutParams = (LayoutParams) this.f664b.getLayoutParams();
                layoutParams.topMargin = this.f673k;
                this.f664b.setLayoutParams(layoutParams);
                this.f664b.setVisibility(View.VISIBLE);
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.f664b, "alpha", new float[]{0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT})});
                animatorSet.setDuration(200);
                animatorSet.start();
            }
        }
        obj = null;
        if (obj == null) {
            this.f668f.setText(Emoji.replaceEmoji(StickersQuery.getEmojiForSticker(this.f671i.id), this.f668f.getPaint().getFontMetricsInt(), AndroidUtilities.dp(BitmapDescriptorFactory.HUE_ORANGE), false));
        }
        this.f667e.getImageReceiver().setImage(this.f671i, null, this.f671i.thumb.location, null, "webp", true);
        layoutParams = (LayoutParams) this.f664b.getLayoutParams();
        layoutParams.topMargin = this.f673k;
        this.f664b.setLayoutParams(layoutParams);
        this.f664b.setVisibility(View.VISIBLE);
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.f664b, "alpha", new float[]{0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT})});
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    @SuppressLint({"NewApi"})
    private void m637c() {
        this.f663a.setTranslationY((float) this.f673k);
        this.f670h[0].setTranslationY((float) this.f673k);
        this.containerView.invalidate();
    }

    protected boolean canDismissWithSwipe() {
        return false;
    }

    public void didReceivedNotification(int i, Object... objArr) {
        if (i == NotificationCenter.emojiDidLoaded) {
            if (StickerPreviewViewer.getInstance().isVisible()) {
                StickerPreviewViewer.getInstance().close();
            }
            StickerPreviewViewer.getInstance().reset();
        }
    }

    public void dismiss() {
        super.dismiss();
        if (this.f674l != 0) {
            ConnectionsManager.getInstance().cancelRequest(this.f674l, true);
            this.f674l = 0;
        }
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }
}
