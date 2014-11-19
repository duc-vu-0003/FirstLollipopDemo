package com.ducva.lollipopdemo.utils;

import com.ducva.lollipopdemo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AppUtils {
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void configureFab(View fabButton) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabButton.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, fabSize, fabSize);
                }
            });
        } else {
            ((ImageButton) fabButton).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }
	
	public static DisplayImageOptions getOptionsQualityCache() {
        return new DisplayImageOptions.Builder()
        .resetViewBeforeLoading(true)
        .delayBeforeLoading(1000)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
    }
}
