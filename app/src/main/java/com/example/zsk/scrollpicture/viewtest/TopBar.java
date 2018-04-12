package com.example.zsk.scrollpicture.viewtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zsk.scrollpicture.R;

/**
 * @author ZSK
 * @date 2018/4/2
 * @function
 */

public class TopBar extends RelativeLayout {

    private Button mLeftButton;
    private Button mRightButton;
    private TextView titleTextView;
    private TopbarClickListener listener;

    public void setOnCLickListener(TopbarClickListener listener) {
        this.listener = listener;
    }

    public TopBar(Context context) {
        super(context,null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        String leftTextStr = ta.getString(R.styleable.TopBar_leftText);
        int leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, Color.BLACK);
        int leftTextSize = ta.getInt(R.styleable.TopBar_leftTextSize,12);
        int leftBakColor = ta.getColor(R.styleable.TopBar_leftBackground,Color.GREEN);

        String rightTextStr = ta.getString(R.styleable.TopBar_rightText);
        int rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, Color.BLACK);
        int rightTextSize = ta.getInt(R.styleable.TopBar_rightTextSize,12);
        int rightBakColor = ta.getColor(R.styleable.TopBar_rightBackground,Color.RED);

        String titleTextStr = ta.getString(R.styleable.TopBar_titleText);
        int titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor, Color.BLACK);
        int titleTextSize = ta.getInt(R.styleable.TopBar_titleTextSize,12);
        int titleBakColor = ta.getColor(R.styleable.TopBar_titleBackground,Color.GREEN);

        mLeftButton = new Button(context);
        mRightButton = new Button(context);
        titleTextView = new TextView(context);

        mLeftButton.setText(leftTextStr);
        mLeftButton.setTextSize(leftTextSize);
        mLeftButton.setTextColor(leftTextColor);
        mLeftButton.setBackgroundColor(leftBakColor);

        mRightButton.setText(rightTextStr);
        mRightButton.setTextSize(rightTextSize);
        mRightButton.setTextColor(rightTextColor);
        mRightButton.setBackgroundColor(rightBakColor);

        titleTextView.setText(titleTextStr);
        titleTextView.setTextSize(titleTextSize);
        titleTextView.setTextColor(titleTextColor);
        titleTextView.setBackgroundColor(titleBakColor);

        ta.recycle();
                    
        LayoutParams mleftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        mleftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(mLeftButton,mleftParams);

        LayoutParams mRightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        mleftParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(mRightButton,mRightParams);

        LayoutParams mtitleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        mleftParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(titleTextView,mtitleParams);

        mLeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

        mRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
