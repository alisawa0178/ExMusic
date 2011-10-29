package k3.app.Exmuisc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;



public class PlayMusic{
	public static final int NOT_SET	= 0;
	public static final int A_SET 	= 1;
	public static final int B_SET 	= 2;
	
	public MediaPlayer mp = null;
	private int nowNum;
	private List<MusicData> list = null;
	private List<MusicData> rawlist = null;
	private boolean randomFlag = false;
	private boolean loopFlag = false;
	public boolean isPlay = false;
	private boolean changeMusic = false;
	private boolean oneLoop = false;
	private int ABState = NOT_SET;
	private int a_musicNum;
	private int b_musicNum;
	private int a_musicSec;
	private int b_musicSec;
	
	private NotificationManager mNotificationManager;
	private Notification notification;
	private RemoteViews contentView;
	private Context context;
	private String PackageName;
	
	public PlayMusic(Context context,String PackageName) {
		this.context = context;
		this.PackageName  =PackageName;
		
		mp = new MediaPlayer();
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				try {
					PlayMusic.this.interfaceImpl_.next();
				} catch (RemoteException e) {
				}
			}
		});
		list = new ArrayList<MusicData>();
		rawlist = new ArrayList<MusicData>();
		
		//通知設定
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager)context.getSystemService(ns);
	}
	public MusicServiceInterface.Stub interfaceImpl_ = new MusicServiceInterface.Stub() {
		@Override
		public int Musicstart() throws RemoteException {
			Log.e("test", "start");
			mp.stop();
	        mp.reset();
	        try {
				mp.setDataSource(list.get(nowNum).FilePath);
				list.get(nowNum).isPlat =true;
				mp.prepare();
				changeMusic = true;
				if(isPlay){
					mp.start();
				}
				makeNotification();
				return mp.getDuration();
			} catch (Exception e) {
				Log.e("test", "start Exception");
			}
			return -1;
		}

		@Override
		public void pause() throws RemoteException {
			if(isPlay){
				mp.pause();
				isPlay = false;
				threadFlag = false;
				mNotificationManager.cancel(305);
			}else{
				mp.start();
				isPlay = true;
				makeNotification();
				Thread thread = new Thread(new ABThread());
				thread.start();
			}
		}

		@Override
		public int getTime() throws RemoteException {
			try{
				return  mp.getCurrentPosition();
			}catch (Exception e) {
				return -1;
			}
		}

		@Override
		public void setList(List<MusicData> data) throws RemoteException {
			MysetList(data);
			nowNum = 0;
				
		}

		@Override
		public void setListNum(List<MusicData> data, int i)
				throws RemoteException {
			nowNum = i;
			MysetList(data);
			isPlay = true;
			Musicstart();
			
		}
		private void MysetList(List<MusicData> data){
			ABState = NOT_SET;
			threadFlag = false;
			list.clear();
			rawlist.clear();
			list.addAll(data);
			rawlist.addAll(data);
			if(randomFlag){
				Random();
			}
		}
		private void Random(){
			if(!list.isEmpty()){
				MusicData temp = list.get(nowNum);
				list.remove(nowNum);
				Collections.shuffle(list);
				nowNum = 0;
				list.add(nowNum,temp);
			}
		}

		@Override
		public void next() throws RemoteException {
			if(ABState == B_SET){
				if(nowNum == a_musicNum){
					mp.seekTo(a_musicSec);
				}else{
					list.get(nowNum).isPlat = false;
					nowNum = a_musicNum;
					Musicstart();
					mp.seekTo(a_musicSec);
				}
				changeMusic = true;
				return;
			}
			if(oneLoop){
				mp.seekTo(0);
				mp.start();
				return;
			}
			list.get(nowNum).isPlat =false;
			nowNum ++;
			if(list.size() <= nowNum){
				if(loopFlag){
					nowNum = 0;
				}else{
					nowNum --;
					isPlay = false;
					list.get(nowNum).isPlat =true;
					mp.pause();
					mp.seekTo(0);
					mNotificationManager.cancel(305);
					return ;
				}
			}
			try {
				interfaceImpl_.Musicstart();
			} catch (RemoteException e) {
			}
		}

		@Override
		public void back() throws RemoteException {
			if(ABState == B_SET){
				if(nowNum == a_musicNum){
					list.get(nowNum).isPlat = false;
					mp.seekTo(a_musicSec);
				}else{
					nowNum = a_musicNum;
					Musicstart();
					mp.seekTo(a_musicSec);
				}
				changeMusic = true;
				return;
			}
			if(oneLoop){
				mp.seekTo(0);
				return;
			}
			if(mp.getCurrentPosition()<3000){
				list.get(nowNum).isPlat =false;
				nowNum--;
				if(nowNum < 0 ){
					if(loopFlag){
						nowNum = list.size()-1;
						Musicstart();
						return;
					}else{
						nowNum = 0;
						mp.seekTo(0);
						isPlay = false;
						list.get(nowNum).isPlat =true;
						return;
					}
				}else{
					list.get(nowNum).isPlat =false;
					mp.stop();
					Musicstart();
					return;
				}
			}else{
				mp.seekTo(0);
				return;
			}
			
		}

		@Override
		public MusicData getData() throws RemoteException {
			if(list.size() <= 0){
				return null;
			}else{
				return list.get(nowNum);
			}
		}

		@Override
		public int getFinishTime() throws RemoteException {
			return mp.getDuration();
		}

		@Override
		public void seekTo(int i) throws RemoteException {
			mp.seekTo(i);
			
		}

		@Override
		public boolean isPaly() throws RemoteException {
			return isPlay;
		}

		@Override
		public boolean changeMusic() throws RemoteException {
			if(changeMusic){
				changeMusic = false;
				return true;
			}else{
				return false;
			}
		}

		@Override
		public void onLoop() throws RemoteException {
			if(loopFlag){
				oneLoop = true;
				loopFlag =false;
			}else if(oneLoop){
				oneLoop = false;
				loopFlag =false;
			}else{
				oneLoop = false;
				loopFlag =true;
			}
			
		}

		@Override
		public boolean getLoop() throws RemoteException {
			return loopFlag;
		}

		@Override
		public boolean getOneLoop() throws RemoteException {
			return oneLoop;
		}

		@Override
		public void onRandom() throws RemoteException {
			randomFlag = !randomFlag;
			if(randomFlag){
				Random();
			}else{
				MusicData temp = list.get(nowNum);
				list.clear();
				list.addAll(rawlist);
				for(int i = 0; i < list.size(); i++){
					if(list.get(i).equals(temp)){
						nowNum = i;
						break;
					}
				}
			}
		}

		@Override
		public boolean getRandom() throws RemoteException {
			return randomFlag;
		}

		@Override
		public List<MusicData> getList() throws RemoteException {
			Log.e("test", "getList");
			return list;
		}

		private boolean threadFlag = false;
		@Override
		public void onAB() throws RemoteException {
			switch(ABState){
			case NOT_SET:
				a_musicNum = nowNum;
				a_musicSec = mp.getCurrentPosition();
				ABState = A_SET;
				break;
			case A_SET:
				if(a_musicNum > nowNum){
					ABState = NOT_SET;
					threadFlag = false;
				}
				
				b_musicNum = nowNum;
				b_musicSec = mp.getCurrentPosition();
				if(a_musicNum == b_musicNum && a_musicSec > b_musicSec){
					ABState = NOT_SET;
					threadFlag = false;
				}
				ABState = B_SET;
				threadFlag = true;
				Thread thread = new Thread(new ABThread());
				thread.start();
				break;
			case B_SET:
				ABState = NOT_SET;
				threadFlag = false;
				break;
			}
			
		}

		@Override
		public int getAB() throws RemoteException {
			return ABState;
		}
		class ABThread implements Runnable{
			@Override
			public void run() {
				while(threadFlag){
					try {
						if(nowNum >= b_musicNum){
							Thread.sleep(100);
						}else{
							Thread.sleep(5000);
						}
					} catch (InterruptedException e1) {
					}
					if(nowNum >= b_musicNum && mp.getCurrentPosition() > b_musicSec){
						if(ABState == B_SET){
							if(nowNum == a_musicNum){
								mp.seekTo(a_musicSec);
							}else{
								list.get(nowNum).isPlat = false;
								nowNum = a_musicNum;
								try {
									Musicstart();
									mp.seekTo(a_musicSec);
								} catch (RemoteException e) {
								}
								changeMusic = true;
							}
						}
					}
				}
			}
		}
	};
	
	public void makeNotification(){
		int icon = R.drawable.icon;
		CharSequence tickerText = "Music start";
		long when = System.currentTimeMillis();

		notification = new Notification(icon, tickerText, when);
		
		contentView = new RemoteViews(PackageName, R.layout.notification);
		notification.contentView = contentView;
		
		Intent notificationIntent = new Intent(context, Main.class);
		notification.contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		contentView.setTextViewText(R.id.album, list.get(nowNum).album);
		contentView.setTextViewText(R.id.title, list.get(nowNum).song_title);
		contentView.setTextViewText(R.id.nowPos, nowNum +"/"+list.size());
		Bitmap temp = list.get(nowNum).getBitmap();
		if(temp != null){
			contentView.setImageViewBitmap(R.id.Picture, temp);
		}
		mNotificationManager.notify(305, notification);
	}

}
