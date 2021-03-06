package com.szhua.pagedemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.LogUtils;

public class MyLayout extends LinearLayout {



    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void computeScroll() {
        LogUtils.d("computeScroll");
        super.computeScroll();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.d("MyLayoutDisPatch",MotionEvent.actionToString(ev.getAction()));
        boolean handled = super.dispatchTouchEvent(ev);
        LogUtils.d("myLayoutHandled",handled);
        return handled;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String name = MotionEvent.actionToString(event.getAction()) ;
        LogUtils.d("myLayout",name);
        return super.onTouchEvent(event);
    }


}
