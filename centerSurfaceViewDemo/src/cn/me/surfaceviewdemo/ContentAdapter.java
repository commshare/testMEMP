package cn.me.surfaceviewdemo;

import java.util.List;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContentAdapter extends BaseAdapter{

	MediaItem selectedPlayItem=null;
	Boolean itemIsSelected=false;
//	private Drawable unSelectIcon;
//	private Drawable selectedIcon;
	//List<Boolean> mChecked;
	private List<MediaItem> mList;
	private Context mContext;
	private LayoutInflater  mInflator;
	public ContentAdapter(Context context,List<MediaItem> list)
	{
		mContext=context;
		//否则有空指针异常
		mInflator = LayoutInflater.from(context);
		//要不要在这里分配内存？
		mList=list;
//		Resources res=context.getResources();
//		unSelectIcon=res.getDrawable(R.drawable.unselect);
//		selectedIcon=res.getDrawable(R.drawable.press);
	}
	PlayMediaItemListener mPlayItemListener;
	public ContentAdapter(Context context,List<MediaItem> list,PlayMediaItemListener listener)
	{
		mContext=context;

		mInflator = LayoutInflater.from(context);

		mList=list;
		mPlayItemListener=listener;

	}
	//pass to activity
	public MediaItem getPlayItem()
	{
		return selectedPlayItem;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null )
		{
			convertView=mInflator.inflate(R.layout.dialog_list_item,null);
		}
		TextView textView=(TextView)convertView.findViewById(R.id.textView);
		final MediaItem item=(MediaItem)getItem(position);
		textView.setText(item.getURL());
		
		final ImageView icon=(ImageView)convertView.findViewById(R.id.imageView);
	//	icon.setBackgroundDrawable(unSelectIcon);
		icon.setImageResource(R.drawable.unselect);
		icon.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!itemIsSelected)
				{
					icon.setImageResource(R.drawable.press);
					itemIsSelected=true;	
					selectedPlayItem=item;
				}else
				{
					icon.setImageResource(R.drawable.unselect);
					itemIsSelected=false;		
					selectedPlayItem=null;
				}
				
			}
			
		});
		//这个button是alertdialog里头的，这里获取不到的。
		Button btnSetPlayItem=(Button)convertView.findViewById(R.id.btn_set_item_to_play);
		if(btnSetPlayItem==null)
			Log.e("zhangbin","null pointer ???");
		else
		btnSetPlayItem.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPlayItemListener.setPlayItem(selectedPlayItem);
			}
		});
		Button deleteBtn=(Button)convertView.findViewById(R.id.btn_delete_list_item);
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "删除 ["+item.getURL()+"]",Toast.LENGTH_SHORT).show();
				mList.remove(position);
				refreshData();
			}
		});
		return convertView;
	}
	
	//更新列表
	public void refreshData(List<MediaItem> list)
	{
		mList=list;
		notifyDataSetChanged();
	}
	public void refreshData()
	{
		notifyDataSetChanged();
	}
}
