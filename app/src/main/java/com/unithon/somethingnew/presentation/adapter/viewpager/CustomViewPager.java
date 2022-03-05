package com.unithon.somethingnew.presentation.adapter.viewpager;

//스크롤 막는 ViewPager

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CustomViewPager extends ViewPager {



    private boolean enabled; //이 것이 스크롤을 막아주는 중요 변수!



    public CustomViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.enabled = true;

    }



    @Override

    public boolean onTouchEvent(MotionEvent event) {

        try {

            if (this.enabled) {

//				Log.i("INFO", "스크롤 중..");

                return super.onTouchEvent(event);

            }

        } catch (Exception e) {

            StringWriter sw = new StringWriter();

            e.printStackTrace(new PrintWriter(sw));

            String exceptionAsStrting = sw.toString();

            Log.e("INFO", exceptionAsStrting);

        }





        return false;

    }



    @Override

    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (this.enabled) {

            return super.onInterceptTouchEvent(event);

        }



        return false;

    }



    public void setPagingEnabled() { //이 메소드를 이용해서 스크롤을 풀어주고

        this.enabled = true;

    }



    public void setPagingDisabled() { //이 메소드를 이용해서 스크롤을 막아줍니다.

        this.enabled = false;

    }



}