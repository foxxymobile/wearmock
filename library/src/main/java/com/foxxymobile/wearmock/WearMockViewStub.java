package com.foxxymobile.wearmock;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.wearable.view.WatchViewStub;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * A mock for WatchViewStub that can inflate the correct view according to the skin type: round or square
 */
public class WearMockViewStub extends FrameLayout {

    private int rectLayoutResId;
    private int roundLayoutResId;
    
    private WatchViewStub.OnLayoutInflatedListener listener;

    public WearMockViewStub(Context context) {
        super(context);
        init(null);
    }

    public WearMockViewStub(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WearMockViewStub(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
        
    private void init(AttributeSet attrs) {
        if(attrs!=null) {
            TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs,R.styleable.WearMockLayout,0, 0);
            try {
                rectLayoutResId = ta.getResourceId(R.styleable.WatchViewStub_rectLayout,0);
                roundLayoutResId = ta.getResourceId(R.styleable.WatchViewStub_roundLayout,0);
            } finally {
                ta.recycle();
            }
        }

        LayoutInflater.from(getContext()).inflate(roundLayoutResId,this,true);
        
        
    }

   
}
