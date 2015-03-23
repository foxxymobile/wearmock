package com.foxxymobile.wearmock;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.wearable.view.WatchViewStub;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;

/**
 * Layout class for the WearMockLayout
 * Handles the measuring and layout of the single child which is the container of the wear layout
 * 
 */
public class WearMockLayout extends FrameLayout {

    private static final String PREFS_NAME = "wearMockLayoutPrefs";
    private static final String SKIN_PREF = "skinOrdinalPref";

    public enum SkinType {
        LG_G_WATCH_R,
        MOTO_360,
        SAMSUNG_GEAR_LIVE,
        ASUS_ZENWATCH,
        SONY_SMARTWATCH_3,
        LG_G_WATCH
    }
    
    private Drawable faceDrawable;
    private int containerSize;
    private int skinWidth;
    private int skinHeight;
    private int skinMarginLeft;
    private int skinMarginTop;
    private boolean isRound;
    
    private Drawable marginDrawable;

    private WatchViewStub watchViewStub;

    public WearMockLayout(Context context) {
        super(context);
        init(null);
    }

    public WearMockLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WearMockLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        // Get the last used skin ordinal from SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int curSkinOrdinal = prefs.getInt(SKIN_PREF,0);
        
        if(attrs!=null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs,R.styleable.WearMockLayout,0, 0);
            try {
                curSkinOrdinal = ta.getInteger(R.styleable.WearMockLayout_skin,0);
            } finally {
                ta.recycle();
            }
        }
        
        setSkin(SkinType.values()[curSkinOrdinal]);

        setWillNotDraw(false);
        
        marginDrawable = new ColorDrawable(Color.WHITE);
    }

    /**
     * Sets the dimensions and drawable by skin
     * @param skin The skin to use
     */
    public void setSkin(SkinType skin) {
        
        // Get the device metrics to calculate the PPI factor between the skin and the device
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // NOTE: Unfortunately Android studio's layout editor does not support loading resources dynamically
        // so we have to do it the long way so the skins are visible in the layout editor
        
        float ppiFactor = 1;
        switch(skin) {
            case LG_G_WATCH_R:
                ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.lgwr_skin_PPI);
                containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.lgwr_container_size)*ppiFactor);
                skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.lgwr_skin_width)*ppiFactor);
                skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.lgwr_skin_height)*ppiFactor);
                skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.lgwr_skin_margin_left)*ppiFactor);
                skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.lgwr_skin_margin_top)*ppiFactor);
                isRound = getResources().getBoolean(R.bool.lgwr_is_round);

                faceDrawable = getResources().getDrawable(R.drawable.lgwr_skin);
                break;
            case MOTO_360:
                ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.moto360_skin_PPI);
                containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_container_size)*ppiFactor);
                skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_width)*ppiFactor);
                skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_height)*ppiFactor);
                skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_margin_left)*ppiFactor);
                skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_margin_top)*ppiFactor);
                isRound = getResources().getBoolean(R.bool.moto360_is_round);

                faceDrawable = getResources().getDrawable(R.drawable.moto360_skin);    
                break;
            case SAMSUNG_GEAR_LIVE:
                ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.gear_skin_PPI);
                containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.gear_container_size)*ppiFactor);
                skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.gear_skin_width)*ppiFactor);
                skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.gear_skin_height)*ppiFactor);
                skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.gear_skin_margin_left)*ppiFactor);
                skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.gear_skin_margin_top)*ppiFactor);
                isRound = getResources().getBoolean(R.bool.gear_is_round);

                faceDrawable = getResources().getDrawable(R.drawable.gear_skin);
                break;
            case ASUS_ZENWATCH:
                ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.zenwatch_skin_PPI);
                containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.zenwatch_container_size)*ppiFactor);
                skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.zenwatch_skin_width)*ppiFactor);
                skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.zenwatch_skin_height)*ppiFactor);
                skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.zenwatch_skin_margin_left)*ppiFactor);
                skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.zenwatch_skin_margin_top)*ppiFactor);
                isRound = getResources().getBoolean(R.bool.zenwatch_is_round);

                faceDrawable = getResources().getDrawable(R.drawable.zenwatch_skin);
                break;
            case SONY_SMARTWATCH_3:
                ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.sony_skin_PPI);
                containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.sony_container_size)*ppiFactor);
                skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.sony_skin_width)*ppiFactor);
                skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.sony_skin_height)*ppiFactor);
                skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.sony_skin_margin_left)*ppiFactor);
                skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.sony_skin_margin_top)*ppiFactor);
                isRound = getResources().getBoolean(R.bool.sony_is_round);

                faceDrawable = getResources().getDrawable(R.drawable.sony_skin);
                break;
            case LG_G_WATCH:
                ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.lgw_skin_PPI);
                containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.lgw_container_size)*ppiFactor);
                skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.lgw_skin_width)*ppiFactor);
                skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.lgw_skin_height)*ppiFactor);
                skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.lgw_skin_margin_left)*ppiFactor);
                skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.lgw_skin_margin_top)*ppiFactor);
                isRound = getResources().getBoolean(R.bool.lgw_is_round);

                faceDrawable = getResources().getDrawable(R.drawable.lgw_skin);
                break;
        }


        updateWatchViewStub();

        invalidate();
        requestLayout();

        // Store the selected skin in SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(SKIN_PREF,skin.ordinal()).commit();
    }

    /**
     *  If watchViewStub exist, update it according to skin's isRound flag
     */
    private void updateWatchViewStub() {
        if(watchViewStub!=null) {
            try {
                // WatchViewStub determines which layout to use by looking at the WindowInset object.
                // Since WindowInset is final, a synthetic instance is created using reflection and the isRound param
                // is determined by the current selected skin
                Constructor<WindowInsets> constructor =  WindowInsets.class.getConstructor(Rect.class,Rect.class,Rect.class,Boolean.TYPE);
                Rect rect = new Rect(0,0,0,0);
                WindowInsets wi = constructor.newInstance(rect,rect,rect,isRound);

                watchViewStub.onApplyWindowInsets(wi);
            } catch (Exception e) {
                Log.e("WearMockLayout", "Cannot create WindowInset for stub", e);
            }
        }
    }

    /**
     * Helper method to use in an options menu for selecting skins 
     * @param item The menu item. The id of the item must be one that is defined in the skins_menu.xml
     * @return true if the skin was changed
     */
    public boolean setSkinByMenuItem(MenuItem item) {
        if(item.getItemId()==R.id.skin_lgwr) {
            setSkin(SkinType.LG_G_WATCH_R);
        } else if (item.getItemId()==R.id.skin_moto360) {
            setSkin(SkinType.MOTO_360);
        } else if (item.getItemId()==R.id.skin_gear) {
            setSkin(SkinType.SAMSUNG_GEAR_LIVE);
        } else if (item.getItemId()==R.id.skin_zenwatch) {
            setSkin(SkinType.ASUS_ZENWATCH);
        } else if (item.getItemId()==R.id.skin_sony) {
            setSkin(SkinType.SONY_SMARTWATCH_3);
        } else if (item.getItemId()==R.id.skin_lgw) {
            setSkin(SkinType.LG_G_WATCH);
        } else {
            return false;
        }
        return true;
        
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View container = getChildAt(0);
        if(container!=null)
            container.measure(MeasureSpec.makeMeasureSpec(containerSize,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(containerSize,MeasureSpec.EXACTLY));

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        View container = getChildAt(0);
        if(container!=null)
            container.layout((right - containerSize) / 2, (bottom - containerSize) / 2, (right - containerSize) / 2 + containerSize, (bottom - containerSize) / 2 + containerSize);

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int containerLeft = (getWidth()- containerSize) / 2;
        int containerTop = (getHeight() - containerSize) / 2;
        int skinLeft = containerLeft-skinMarginLeft;
        int skinTop = containerTop-skinMarginTop;

        // Draw skin above the container
        faceDrawable.setBounds(skinLeft,skinTop,skinLeft+skinWidth,skinTop+skinHeight);
        faceDrawable.draw(canvas);

        // Draw white margins
        marginDrawable.setBounds(0, 0, getWidth(), skinTop);
        marginDrawable.draw(canvas);
        marginDrawable.setBounds(0, skinTop + skinHeight, getWidth(), getHeight());
        marginDrawable.draw(canvas);
        marginDrawable.setBounds(0, skinTop, skinLeft, skinTop+skinHeight);
        marginDrawable.draw(canvas);
        marginDrawable.setBounds(skinLeft+skinWidth, skinTop, getWidth(), skinTop+skinHeight);
        marginDrawable.draw(canvas);
    }

    @Override
    public void addView(View child) {
        super.addView(child);

        watchViewStub = findStub(child);

        // Update the watch view stub
        // Must be posted on the message queue so it will be run after layout has finished
        Handler handler  = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateWatchViewStub();
            }
        });

    }

    /**
     * Recursively search for a WatchViewStub view
     */
    private WatchViewStub findStub(View root) {
        if(root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                if(child instanceof WatchViewStub)
                    return (WatchViewStub) child;
                else if (child instanceof ViewGroup) {
                    return findStub(child);
                }
            }
        }
        return null;
    }
}
