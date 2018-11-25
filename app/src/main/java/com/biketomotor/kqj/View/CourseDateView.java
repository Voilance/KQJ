package com.biketomotor.kqj.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.biketomotor.kqj.R;


public class CourseDateView extends LinearLayout {

    public CourseDateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int colorGray = getResources().getColor(R.color.colorGray);
        int colorLightBlue = getResources().getColor(R.color.colorLightBlue);

        this.setOrientation(this.VERTICAL);
        View culomnLine = new View(context);
        culomnLine.setBackgroundColor(colorGray);
        LayoutParams tclParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        this.addView(culomnLine, tclParams);
    }

}
