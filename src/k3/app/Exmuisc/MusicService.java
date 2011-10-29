package k3.app.Exmuisc;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MusicService extends Service {
	private boolean headphone = false;
	private PlayMusic music;
	private MusicServiceInterface.Stub interfaceImpl_;
	
	private BroadcastReceiver br = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
				boolean plugged = intent.getIntExtra("state", 0) == 1;
				if(plugged){
					headphone = true;
				}else{
					if(music.isPlay & headphone){
						try {
							interfaceImpl_.pause();
						} catch (RemoteException e) {
						}
					}
				}
			}
		}
	};
	@Override
	public void onCreate() {
		super.onCreate();
		music = new  PlayMusic(this, getPackageName());
		interfaceImpl_ = music.interfaceImpl_;
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		music.mp.stop();
		music.mp.release();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.e("test", "onBind");
		return interfaceImpl_;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		registerReceiver(br, new IntentFilter(Intent.ACTION_HEADSET_PLUG)); 
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		registerReceiver(br, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		return Service.START_STICKY_COMPATIBILITY;
	}
}
