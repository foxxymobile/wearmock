package com.foxxymobile.wearmock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ViewGroup for the WearMockLayout
 */
public class WearMockLayout extends FrameLayout {

    private Drawable faceDrawable;
    private int containerSize;
    private int skinWidth;
    private int skinHeight;
    private int skinMarginLeft;
    private int skinMarginTop;

    private String[] skins;
    
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
        skins = getResources().getStringArray(R.array.skins_array);
        int curSkinIndex = 0;
        
        if(attrs!=null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs,R.styleable.WearMockLayout,0, 0);
            try {
                curSkinIndex = ta.getInteger(R.styleable.WearMockLayout_skin,0);
            } finally {
                ta.recycle();
            }
        }
        
        setSkin(curSkinIndex);

        //setBackgroundResource(android.R.color.white);
        setWillNotDraw(false);
    }

    /**
     * Sets the skin dimensions and drawable by skin index
     * @param skinIndex
     */
    public void setSkin(int skinIndex) {
        if(skinIndex>=skins.length)
            return;
        
        String skinName = skins[skinIndex];


        // Calculate the PPI factor between the skin and the device
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float ppiFactor = metrics.xdpi/getResources().getInteger(getSkinnedResId(skinName,"skin_PPI","integer"));

        // Get container size
        containerSize = (int) (getResources().getDimensionPixelSize(getSkinnedResId(skinName,"container_size","dimen"))*ppiFactor);
        skinWidth = (int) (getResources().getDimensionPixelSize(getSkinnedResId(skinName,"skin_width","dimen"))*ppiFactor);
        skinHeight = (int) (getResources().getDimensionPixelSize(getSkinnedResId(skinName,"skin_height","dimen"))*ppiFactor);
        skinMarginLeft = (int) (getResources().getDimensionPixelSize(getSkinnedResId(skinName,"skin_margin_left","dimen"))*ppiFactor);
        skinMarginTop = (int) (getResources().getDimensionPixelSize(getSkinnedResId(skinName,"skin_margin_top","dimen"))*ppiFactor);

        faceDrawable = getResources().getDrawable(getSkinnedResId(skinName,"skin","drawable"));
        
        invalidate();
        requestLayout();

        /*
        // Calculate the PPI factor between the skin and the device
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float ppiFactor = metrics.xdpi/getResources().getInteger(R.integer.moto360_skin_PPI);
        //float ppiFactor = metrics.xdpi/getResources().getInteger(getSkinnedResId(skinName,"skin_PPI","integer"));

        // Get container size
        containerSize = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_container_size)*ppiFactor);
        skinWidth = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_width)*ppiFactor);
        skinHeight = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_height)*ppiFactor);
        skinMarginLeft = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_margin_left)*ppiFactor);
        skinMarginTop = (int) (getResources().getDimensionPixelSize(R.dimen.moto360_skin_margin_top)*ppiFactor);

        faceDrawable = getResources().getDrawable(R.drawable.moto360_skin);
        */
    }

    private int getSkinnedResId(String skinName, String resName, String type) {
        return getResources().getIdentifier(skinName+"_"+resName,type, "com.foxxymobile.wearmock");
        
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
    }

}
