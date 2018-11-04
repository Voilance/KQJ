package com.biketomotor.kqj.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.biketomotor.kqj.R;

public class InfoEditText extends LinearLayout {

    private ImageView ivIcon;
    private EditText etText;
    private ImageView ivClear;
    private String mHint;

    public InfoEditText(Context context) {
        super(context);
        initView();
    }

    public InfoEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public InfoEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.info_edit_text, InfoEditText.this);
        ivIcon = findViewById(R.id.iet_iv_icon);
        etText = findViewById(R.id.iet_et_text);
        ivClear = findViewById(R.id.iet_iv_clear);
        mHint = "";
        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etText.setText("");
            }
        });
        etText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ivClear.setVisibility(hasFocus ? VISIBLE : INVISIBLE);
                etText.setHint(hasFocus ? "" : mHint);
            }
        });
    }

    private void initView(Context context, AttributeSet attrs) {
        initView();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InfoEditText);
        Drawable icon = array.getDrawable(R.styleable.InfoEditText_icon);
        String hint = array.getString(R.styleable.InfoEditText_hint);
        String text = array.getString(R.styleable.InfoEditText_text);
        int hintColor = array.getColor(R.styleable.InfoEditText_hintColor, Color.GRAY);
        int textColor = array.getColor(R.styleable.InfoEditText_textColor, Color.BLACK);
        boolean inputEnabled = array.getBoolean(R.styleable.InfoEditText_inputEnabled, true);
        array.recycle();
        setIcon(icon);
        setHint(hint);
        setText(text);
        setHintColor(hintColor);
        setTextColor(textColor);
        setInputEnabled(inputEnabled);
    }

    private InfoEditText setIcon(Drawable icon) {
        if (icon != null) {
            ivIcon.setImageDrawable(icon);
        }
        return this;
    }

    private InfoEditText setHint(String hint) {
        if (hint != null) {
            mHint = hint;
            etText.setHint(mHint);
        }
        return this;
    }

    private InfoEditText setText(String text) {
        if (text != null) {
            etText.setText(text);
        }
        return this;
    }

    private InfoEditText setHintColor(int color) {
        etText.setHintTextColor(color);
        return this;
    }

    private InfoEditText setTextColor(int color) {
        etText.setTextColor(color);
        return this;
    }

    private InfoEditText setInputEnabled(boolean enabled) {
        etText.setFocusable(enabled);
        return this;
    }
}
