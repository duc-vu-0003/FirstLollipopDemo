package com.ducva.lollipopdemo;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ducva.lollipopdemo.adapter.CustomItemAnimator;
import com.ducva.lollipopdemo.adapter.DemoAdapter;
import com.ducva.lollipopdemo.api.GetMyAlbums;
import com.ducva.lollipopdemo.api.GetMyAlbums.LastfmMyAlbumsOnResult;
import com.ducva.lollipopdemo.api.GetMyArtists;
import com.ducva.lollipopdemo.api.GetMyArtists.LastfmMyArtistsOnResult;
import com.ducva.lollipopdemo.model.BaseModel;
import com.ducva.lollipopdemo.view.FloatingActionButton;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import de.greenrobot.event.EventBus;

public class MainActivity extends ActionBarActivity implements LastfmMyAlbumsOnResult, LastfmMyArtistsOnResult {

	private Toolbar toolbar;
	private DrawerLayout mDrawerLayout;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private RecyclerView mRecyclerView;
	private RelativeLayout layoutAlbum;
	private RelativeLayout layoutArtist;
	private ProgressBar progressBar;
	private FloatingActionButton fab;

	private DemoAdapter mAdapter;
	private ArrayList<BaseModel> listData;
	private int currentType;

	private int page = 1;
	private int previousTotal = 0;
	private boolean loading = true;
	private int visibleThreshold = 5;
	int firstVisibleItem, visibleItemCount, totalItemCount;
	private LinearLayoutManager mLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(getApplicationContext())) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);

		listData = new ArrayList<BaseModel>();

		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_list_vetical) {
			mLayoutManager = new LinearLayoutManager(this);
			mRecyclerView.setLayoutManager(mLayoutManager);
			return true;
		} else if (id == R.id.action_grid) {
			mLayoutManager = new GridLayoutManager(this, 2);
			mRecyclerView.setLayoutManager(mLayoutManager);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		// init toolbar
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// init drawer layout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				R.string.drawer_open, R.string.drawer_close);
		actionBarDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

		// init recycle view
		mRecyclerView = (RecyclerView) findViewById(R.id.list);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setItemAnimator(new CustomItemAnimator());

		mRecyclerView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				visibleItemCount = mRecyclerView.getChildCount();
				totalItemCount = mLayoutManager.getItemCount();
				firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

				if (loading) {
					if (totalItemCount > previousTotal) {
						loading = false;
						previousTotal = totalItemCount;
					}
				}
				if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

					page++;

					mDrawerLayout.closeDrawers();
					mSwipeRefreshLayout.setRefreshing(true);
					if (currentType == 1) {
						loadMyAlbum();
					} else {
						loadMyArtist();
					}

					loading = true;
				}
			}
		});

		mAdapter = new DemoAdapter(listData, R.layout.item_layout, this);
		mRecyclerView.setAdapter(mAdapter);

		// init swipe refresh layout
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData(currentType);
			}
		});

		layoutArtist = (RelativeLayout) findViewById(R.id.drawer_my_artist);
		layoutArtist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initData(0);
			}
		});

		layoutAlbum = (RelativeLayout) findViewById(R.id.drawer_myalbum);
		layoutAlbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initData(1);
			}
		});

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAdapter.addData(new BaseModel("Temp Data", "http://userserve-ak.last.fm/serve/_/101457319.png", "Test", 2410), firstVisibleItem);
				mRecyclerView.scrollToPosition(firstVisibleItem);
			}
		});
		//fab.attachToRecyclerView(mRecyclerView);

		initData(0);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void animateActivity(BaseModel item, View iv) {

		EventBus.getDefault().postSticky(item);
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, iv,
				getString(R.string.transition_item));

		Intent intent = new Intent();
		intent.setClass(this, ItemDetailsActivity.class);

		ActivityCompat.startActivity(this, intent, options.toBundle());

	}

	private void initData(int type) {
		mDrawerLayout.closeDrawers();
		mSwipeRefreshLayout.setRefreshing(true);
		page = 1;
		listData.clear();
		mAdapter.clearData();

		switch (type) {
		case 1:
			currentType = 1;
			loadMyAlbum();
			break;
		default:
			currentType = 0;
			loadMyArtist();
			break;
		}

	}

	private void loadMyAlbum() {
		GetMyAlbums getAlbums = new GetMyAlbums(this);
		getAlbums.setLastfmMyAlbumsOnResult(this);
		getAlbums.execute(page);
	}

	private void loadMyArtist() {
		GetMyArtists getArtist = new GetMyArtists(this);
		getArtist.setLastfmMyArtistsOnResult(this);
		getArtist.execute(page);
	}

	@Override
	public void onLastfmMyAlbumsResult(boolean result, ArrayList<BaseModel> info) {
		// TODO Auto-generated method stub
		if (result) {
			mAdapter.setData(info);
		}
		mSwipeRefreshLayout.setRefreshing(false);
	}

	@Override
	public void onLastfmMyArtistsResult(boolean result, ArrayList<BaseModel> info) {
		// TODO Auto-generated method stub
		if (result) {
			mAdapter.setData(info);
		}
		progressBar.setVisibility(View.GONE);
		mSwipeRefreshLayout.setRefreshing(false);
	}
}
