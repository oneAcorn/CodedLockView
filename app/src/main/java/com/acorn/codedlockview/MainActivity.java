package com.acorn.codedlockview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.acorn.codedlocklib.drawable.NumberTextDrawable;
import com.acorn.codedlocklib.utils.DensityUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView testV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testV = (ImageView) findViewById(R.id.test1);
        setCodedDrawable("13989.54", testV);
        testV.setOnClickListener(this);
    }


    private void setCodedDrawable(String number, ImageView targetView) {
        final NumberTextDrawable numberTextDrawable = new NumberTextDrawable(number,
                DensityUtils.sp2px(this, 60), 0xff3d2800, 10000);
        numberTextDrawable.setBounds(0, 0, numberTextDrawable.getIntrinsicWidth(),
                numberTextDrawable.getIntrinsicHeight());
        ViewGroup.LayoutParams lp = targetView.getLayoutParams();
        lp.width = numberTextDrawable.getIntrinsicWidth();
        lp.height = numberTextDrawable.getIntrinsicHeight();
        targetView.setLayoutParams(lp);
        targetView.setImageDrawable(numberTextDrawable);
//        numberTextDrawable.startAnim();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                numberTextDrawable.startAnim();
//            }
//        }, 1500);
    }

    @Override
    public void onClick(View view) {
        if (view == testV) {
            NumberTextDrawable drawable = (NumberTextDrawable) testV.getDrawable();
//            drawable.startAnim();
            drawable.startAddToAnim("13980.36");
//            drawable.startFullScrollAnim();
        }
    }
}
