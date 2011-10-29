package k3.app.Exmuisc;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<MusicData>{
	private LayoutInflater mInflater;
	private MusicListener listener;
	public Handler handler;

	public void setMusicListener(MusicListener temp){
		listener = temp;
	}
	
	public ListAdapter(Context context, List<MusicData> objects,Handler handler) {
		super(context,0, objects);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.handler = handler;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MusicData item = this.getItem(position);
		if(item.State == MusicData.song){
			convertView = mInflater.inflate(R.layout.song_column, null);
			TextView title = (TextView) convertView.findViewById(R.id.song_list_title);
			TextView num = (TextView) convertView.findViewById(R.id.num);
			LinearLayout colum = (LinearLayout) convertView.findViewById(R.id.column);
			colum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.set(item.FilePath,item,position);
				}
			});
			
			title.setText(item.song_title);
			num.setText(item.track_number + ".");
			if(item.isPlat){
				title.setTextColor(Color.WHITE);
				num.setTextColor(Color.rgb(0, 162, 232));
			}
		}else if(item.State == MusicData.Album){
			convertView = mInflater.inflate(R.layout.album_column, null);
			ImageView picture = (ImageView) convertView.findViewById(R.id.alubleListButton);
			TextView album = (TextView) convertView.findViewById(R.id.albumTItle);
			DownloadTask task = new DownloadTask(picture);  
			task.execute(item.FilePath);
			
			album.setText(item.album);
			picture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.OpenAlbum(item.album);
				}
			});
		}else if(item.State == MusicData.Artist ){
			convertView = mInflater.inflate(R.layout.list_column, null);
			TextView title = (TextView) convertView.findViewById(R.id.list_title);
			title.setText(item.artist);
			LinearLayout colum = (LinearLayout) convertView.findViewById(R.id.column);
			colum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.OpenArtist(item.artist);
				}
			});
		}else if(item.State == MusicData.List){
			convertView = mInflater.inflate(R.layout.list_column, null);
			TextView title = (TextView) convertView.findViewById(R.id.list_title);
			title.setText(item.song_title);
			LinearLayout colum = (LinearLayout) convertView.findViewById(R.id.column);
			colum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.OpenPlayList(item.id);
				}
			});
		}


		
		return convertView;
	}
}
