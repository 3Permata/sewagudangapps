package com.tigapermata.sewagudangapps.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {

    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public CustomTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.defStyle = defStyleAttr;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Andika-Regular.ttf");
        this.setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "Andika-Regular.ttf");
        super.setTypeface(tf);
    }


    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "Andika-Regular.ttf");
        super.setTypeface(tf);
    }
}
