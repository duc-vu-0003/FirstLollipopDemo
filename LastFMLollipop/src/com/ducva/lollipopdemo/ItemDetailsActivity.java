package com.ducva.lollipopdemo;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ducva.lollipopdemo.model.BaseModel;
import com.ducva.lollipopdemo.utils.AppUtils;
import com.ducva.lollipopdemo.utils.DebugLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

public class ItemDetailsActivity extends ActionBarActivity {

	private static final int SCALE_DELAY = 30;

	private Toolbar toolbar;
	private LinearLayout rowContainer;

	private BaseModel item = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		
		EventBus.getDefault().registerSticky(this);
		
		//initView();
	}

	private void initView(){
		toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemDetailsActivity.this.onBackPressed();
            }
        });

        // Row Container
        rowContainer = (LinearLayout) findViewById(R.id.row_container);
        
        for (int i = 0; i < rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate().setStartDelay(100 + i * SCALE_DELAY).scaleX(1).scaleY(1);
        }
        
        initData();
	}
	
	private void initData(){
		if(item != null){
			toolbar.setTitle(item.name);
			
			View view = rowContainer.findViewById(R.id.row_image);
			ImageView iv = (ImageView) view.findViewById(R.id.img_details);
            ImageLoader.getInstance().displayImage(item.imageLink, iv);
            
            extract(item.imageLink);
            
            DebugLog.v(item.imageLink);
			
			view = rowContainer.findViewById(R.id.row_name);
            fillRow(view, "Name: ", item.name);
            
            view = rowContainer.findViewById(R.id.row_play_count);
            fillRow(view, "Play Count: ", item.playCount + "");
            
            view = rowContainer.findViewById(R.id.row_mbid);
            fillRow(view, "MBID: ", item.mID);
		}
	}
	
	public void onEvent(BaseModel data){
		this.item = data;
		//initData();
		initView();
	}
	
	private void fillRow(View view, final String title, final String description) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);

        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        descriptionView.setText(description);
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//outState.put
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public void onBackPressed() {
		for (int i = rowContainer.getChildCount() - 1; i > 0; i--) {

            View rowView = rowContainer.getChildAt(i);
            ViewPropertyAnimator propertyAnimator = rowView.animate().setStartDelay((rowContainer.getChildCount() - 1 - i) * SCALE_DELAY)
                    .scaleX(0).scaleY(0);

            propertyAnimator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
				@Override
                public void onAnimationEnd(Animator animator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
        }
	}
	
	private void extract(String imageLink) {
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(imageLink, AppUtils.getOptionsQualityCache());
		
		Palette.generateAsync(bitmap, new PaletteAsyncListener() {
			
			@Override
			public void onGenerated(Palette palette) {
				// TODO Auto-generated method stub
                Palette.Swatch vibrant = palette.getVibrantSwatch();   
                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();   
                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();  
                Palette.Swatch muted = palette.getMutedSwatch();   
                Palette.Swatch darkMuted = palette.getDarkMutedSwatch();   
                Palette.Swatch lightMuted = palette.getLightMutedSwatch();   
                
                toolbar.setBackgroundDrawable(new ColorDrawable(vibrant.getRgb()));
                toolbar.setTitleTextColor(vibrant.getTitleTextColor());
			}
		});
	}
}
