package com.ducva.lollipopdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ducva.lollipopdemo.MainActivity;
import com.ducva.lollipopdemo.R;
import com.ducva.lollipopdemo.model.BaseModel;
import com.ducva.lollipopdemo.utils.AppUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder>{
	
	private List<BaseModel> data;
    private int rowLayout;
    private MainActivity mContext;
	
	public DemoAdapter(ArrayList<BaseModel> arrayList, int rowLayout, MainActivity mContext) {
		this.data = arrayList;
		this.rowLayout = rowLayout;
		this.mContext = mContext;
	}
	
	public void clearData() {
		this.data.clear();
//        int size = this.data.size();
//        if (size > 0) {
//            this.data.clear();
//            this.notifyItemRangeRemoved(0, size - 1);
//        }
        //this.notifyItemRemoved(0);
		this.notifyDataSetChanged();
    }

    public void setData(List<BaseModel> data) {
        this.data.addAll(data);
        this.notifyItemRangeInserted(0, this.data.size() - 1);
        //this.notifyDataSetChanged();
    }
    
    public void addData(BaseModel item, int position){
    	this.data.add(position,item);
    	this.notifyItemInserted(position);
    }
	
	@Override
	public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int i) {
		final BaseModel item = data.get(i);
        viewHolder.name.setText(item.name);
        ImageLoader.getInstance().displayImage(item.imageLink, viewHolder.image, AppUtils.getOptionsQualityCache());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.animateActivity(item, viewHolder.cardView);
            }
        });
	}
	
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.itemName);
            image = (ImageView) itemView.findViewById(R.id.itemImage);
            cardView = (CardView) itemView.findViewById(R.id.itemCardView);
        }

    }
}
