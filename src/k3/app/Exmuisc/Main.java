package k3.app.Exmuisc;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class Main extends Activity implements Runnable , ServiceConnection{
	
	//定数
    private final static int REPEAT_INTERVAL = 500;	
    private final static String rootPath = "mnt/sdcard/music/music";
    
    //リストまわり
    private final ArrayList<MusicData> list;
    private ListAdapter adapter;
	private SetList setList;
	
	//サービスより取得したインターフェイス
    private MusicServiceInterface playerInterface_;
	
    //環境
	public Handler handler;
	private Thread thread;
	private Vibrator mVib;
	private SharedPreferences sp;
	private MakeKASHIView kashi;

	//データベース
	private DatabaseOpenHelper dbHelpar;
	private SQLiteDatabase db;
	
	//フラグ
	private boolean thread_Flag;
	private boolean isPlay;
	private boolean isConnected_;
	private boolean onSeek;
	private boolean nowPlayList = false;
	
	//レイアウトのview
	private ListView listview;
	private TextView title;
	private TextView album;
	private TextView artist;
	private TextView genre;
	private TextView nowtime;
	private TextView endtime;
	private Button play;
	private ImageView star;
	private ImageView loop;
	private ImageView Random;
	private ImageView AB;
	private ImageView effect;
	private ImageView Picture;
	private ImageView AlbumButton;
	private ImageView ArtistButton;
	private ImageView PlaylistButton;
	private ImageView nowPlayButton;
	private SeekBar seek;
	
	public Main() {
		list = new ArrayList<MusicData>();
		
        playerInterface_ = null;
        
        handler=new Handler();
        
        thread_Flag = true;
        isPlay = false;
        isConnected_ = false;
        onSeek = false;
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setVolumeControlStream(AudioManager.FX_KEY_CLICK);
    	super.onCreate(savedInstanceState);
		// タイトルバーを非表示にする
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//縦固定;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.main);
        
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        
 		
 		// データーベースオブジェクトの取得(;´Д｀)ﾊｧﾊｧ
 		dbHelpar = new DatabaseOpenHelper(this);
 		db = dbHelpar.getWritableDatabase();
 		//リストセットの初期化
 		setList = new SetList(dbHelpar, db);
 		kashi = new MakeKASHIView(this);

 		//layoutオブジェクトの取得
        play = (Button)findViewById(R.id.play);
        star = (ImageView)findViewById(R.id.star);
        loop = (ImageView)findViewById(R.id.loop);
        AB = (ImageView)findViewById(R.id.ab);
        effect = (ImageView)findViewById(R.id.effect);
        Random = (ImageView)findViewById(R.id.random);
        seek = (SeekBar)findViewById(R.id.seekBar1);
        listview = (ListView)findViewById(R.id.listView1);
        title = (TextView)findViewById(R.id.title);
        album = (TextView)findViewById(R.id.album);
        artist = (TextView)findViewById(R.id.artist);
        genre = (TextView)findViewById(R.id.genre);
        nowtime = (TextView)findViewById(R.id.nowTime);
        endtime = (TextView)findViewById(R.id.endtime);
        Picture =  (ImageView)findViewById(R.id.Picture);
        
        AlbumButton =  (ImageView)findViewById(R.id.albumButton);
        ArtistButton =  (ImageView)findViewById(R.id.artistButton);
        PlaylistButton =  (ImageView)findViewById(R.id.PlayListButton);
        nowPlayButton =  (ImageView)findViewById(R.id.nowPlayButton);
        
        
        
        //layoutオブジェクトのイベント設定
        star.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(playerInterface_ !=null){
					try {
						MusicData data = playerInterface_.getData();
						if(data.star){
							star.setImageResource(R.drawable.star_n);
							data.star = false;
						}else{
							//カレンダーの更新
							data.starDay = new Date();
							star.setImageResource(R.drawable.star_y);
							data.star = true;
						}
						ContentValues values = new ContentValues();
						values.put("star", data.star);
						values.put("starDay", dbHelpar.sdf.format(data.starDay));
						String[] ids = {data.id+""};
						db.update("music", values,"id=?",ids);
					} catch (RemoteException e) {
					}
				}
			}
		});
        album.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				nowPlayList = false;
				AlbumButton.setImageResource(R.drawable.album_n);
		        ArtistButton.setImageResource(R.drawable.artist_n);
		        PlaylistButton.setImageResource(R.drawable.play_list_n);
		        nowPlayButton.setImageResource(R.drawable.now_play_n);
		        
				//押した時バイブ(卑猥)
				mVib.vibrate(100);
				setList.SetAlbum(list, album.getText().toString());
				adapter.notifyDataSetChanged();
				listview.invalidate();
				return true;
			}
		});
        
        artist.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				//押した時バイブ(卑猥)
				nowPlayList = false;
				AlbumButton.setImageResource(R.drawable.album_n);
		        ArtistButton.setImageResource(R.drawable.artist_n);
		        PlaylistButton.setImageResource(R.drawable.play_list_n);
		        nowPlayButton.setImageResource(R.drawable.now_play_n);
		        
				mVib.vibrate(100);
				setList.SetArtist(list, artist.getText().toString());
				adapter.notifyDataSetChanged();
				listview.invalidate();
				return true;
			}
		});
        
        loop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					playerInterface_.onLoop();
					SetState();
				} catch (RemoteException e) {
				}
			}
		});
        Random.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					playerInterface_.onRandom();
					SetState();
					if(nowPlayList){
						handler.post(new Runnable() {
							@Override
							public void run() {
								onNowPlayList(null);
							}
						});
					}
				} catch (RemoteException e) {
				}
			}
		});
        
        AB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					playerInterface_.onAB();
					SetState();
				} catch (RemoteException e) {
				}
			}
		});
        
        effect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, EffectActivity.class);
				startActivity(intent);
			}
		});
        
        seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {onSeek = false;}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {onSeek = true;}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(onSeek){
					try {
						playerInterface_.seekTo(progress);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		});
        
        Picture.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				try {
					kashi.MakeView(getWindowManager(),playerInterface_.getData().Text);
				} catch (RemoteException e) {
				}
			}
		});
        
        // Vibrator取得
     	mVib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        
        SetList();
        
        startService(new Intent(Main.this, MusicService.class));
        bindService(new Intent(this, MusicService.class),  this, Context.BIND_AUTO_CREATE);
        
    }
    

    
    private void SetList(){
    	setList.SetAll(list);
    	
    	adapter = new ListAdapter(this, list,handler);
    	adapter.setMusicListener(new MusicListener() {
			@Override
			public void set(String path,MusicData data,int i) {
		 		try {
		 			for(int j = 0;j<list.size();j++){
		 				list.get(j).isPlat =false;
		 			}
					playerInterface_.setListNum(list, i);
					nowPlayList = true;
					SetView_MusicData();
					SetState();
					
					play.setBackgroundResource(R.drawable.pouse);
					AlbumButton.setImageResource(R.drawable.album_n);
			        ArtistButton.setImageResource(R.drawable.artist_n);
			        PlaylistButton.setImageResource(R.drawable.play_list_n);
			        nowPlayButton.setImageResource(R.drawable.now_play_y);
				} catch (RemoteException e) {
				}
			}
			@Override
			public void OpenAlbum(String albume) {
				setList.SetAlbum(list, albume);
				adapter.notifyDataSetChanged();
		    	listview.invalidate();
		    	listview.setSelectionFromTop(0, 0);
			}
			@Override
			public void OpenArtist(String artist) {
				setList.SetArtistToArtist(list, artist);
				adapter.notifyDataSetChanged();
		    	listview.invalidate();
		    	listview.setSelectionFromTop(0, 0);
			}
			@Override
			public void OpenPlayList(int id) {
				switch(id){
				case -2:
					setList.SetFavoriteALL(list);
					break;
				case -1:
					setList.SetRecentlyFavoriteALL(list);
					// TODO 他のプレーリストも実装
				}
				adapter.notifyDataSetChanged();
		    	listview.invalidate();
			}
		});
    	listview.setAdapter(adapter);
    }

    private void SerchFile(File root){
    	//ディレクトリ内のファイル・ディレクトリ一覧取得
    	File[] Files = root.listFiles();
    	for(int i = 0;i<Files.length;i++){
    		//ディレクトリだった場合さらに、階層に入っていく
    		if(Files[i].isDirectory()){
    			SerchFile(Files[i]);
    		}else if(Files[i].isFile()){
    			//txtファイルだった場合パースする
    			String FileNmae = Files[i].getName();
    			if(FileNmae.endsWith(".mp3")){
    				MusicData musicdata = new MusicData(Files[i].getPath());
    				//パースが成功したらリストに追加する
    				if(musicdata != null){
    					dbHelpar.writeDB(db, musicdata);
    				}
    			}else{
    				//TODO 他の形式もガンバｗｗｗｗｗ
    			}
    		}
    	}
    }
    
    public String getTimeString(int data){
    	String ans = "";
    	data = data/1000;
    	int second = data%60;
    	int min = data/60;
    	if(min<=0){
    		ans += "00:";
    	}else if(min<10){
    		ans += "0" + min + ":";
    	}else{
    		ans += min + ":";
    	}
    	
    	if(second<=0){
    		ans += "00";
    	}else if(second<10){
    		ans += "0" + second;
    	}else{
    		ans += second;
    	}
    	return ans;
    }
    
    private void SetView_MusicData(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					if (playerInterface_ != null) {
						if(nowPlayList){
							list.clear();
							list.addAll(playerInterface_.getList());
							adapter.notifyDataSetChanged();
					    	listview.invalidate();
						}
						MusicData data = playerInterface_.getData();
						if (data != null) {
							title.setText(data.song_title);
							artist.setText(data.artist);
							album.setText(data.album);
							genre.setText(data.Genre);
							int num = playerInterface_.getFinishTime();
							endtime.setText(getTimeString(num));
							seek.setMax(num);
							Bitmap bit = data.getBitmap();
							if(bit != null){
								Picture.setImageBitmap(bit);
							}else{
								Picture.setImageResource(R.drawable.emptycase);
							}
							if(data.star){
								star.setImageResource(R.drawable.star_y);
							}else{
								star.setImageResource(R.drawable.star_n);
							}
						}
					}
				} catch (RemoteException e) {
				}
			}
		});
    }
    
    private void SetState(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				try {
					if (playerInterface_ != null) {
						Editor ed = sp.edit();
						//ループのボタン状態を取得
						if(playerInterface_.getLoop()){
							ed.putInt("loop", 1);
							loop.setImageResource(R.drawable.loop_y);
						}else if(playerInterface_.getOneLoop()){
							ed.putInt("loop", 2);
							loop.setImageResource(R.drawable.loop_one);
						}else{
							ed.putInt("loop", 0);
							loop.setImageResource(R.drawable.loop_n);
						}
						//ランダムボタン状態を取得
						if(playerInterface_.getRandom()){
							ed.putBoolean("random", true);
							Random.setImageResource(R.drawable.random_y);
						}else{
							ed.putBoolean("random", false);
							Random.setImageResource(R.drawable.random_n);
						}
						//ABボタン状態を取得
						switch(playerInterface_.getAB()){
						case 0:
							AB.setImageResource(R.drawable.ab_n);
							break;
						case 1:
							AB.setImageResource(R.drawable.ab_a);
							break;
						case 2:
							AB.setImageResource(R.drawable.ab_y);
							break;
						}
						
						ed.commit();
					}
				} catch (RemoteException e) {
				}
			}
		});
    }
    
    //ポーズボタン押したら呼ばれる(｀・ω・´)
    public void play(View v) {
    	try {
			playerInterface_.pause();
    		setPouseButton(playerInterface_.isPaly());
		} catch (RemoteException e) {
		}
    	
    }

    public void next(View v) {
    	try {
			playerInterface_.next();
			if(playerInterface_.getAB() != 2){
				seek.setProgress(0);
			}
			nowtime.setText(getTimeString(0));
			SetView_MusicData();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }
 
    public void back(View v) {
    	try {
			playerInterface_.back();
			SetView_MusicData();
			if(playerInterface_.getAB() != 2){
				seek.setProgress(0);
			}
			nowtime.setText(getTimeString(0));
		} catch (RemoteException e) {
		}
    }
 
    public void onABLoop(View v){
    	
    }
    
    public void OnAlubmButton(View v){
    	nowPlayList = false;
    	setList.SetAllAlbum(list);
    	adapter.notifyDataSetChanged();
    	listview.invalidate();
    	
    	AlbumButton.setImageResource(R.drawable.album_y);
        ArtistButton.setImageResource(R.drawable.artist_n);
        PlaylistButton.setImageResource(R.drawable.play_list_n);
        nowPlayButton.setImageResource(R.drawable.now_play_n);
        listview.setSelectionFromTop(0, 0);
    }
    
    public void OnArtistButton(View v){
    	nowPlayList = false;
    	setList.SetAllArtist(list);
    	adapter.notifyDataSetChanged();
    	listview.invalidate();
    	
    	AlbumButton.setImageResource(R.drawable.album_n);
        ArtistButton.setImageResource(R.drawable.artist_y);
        PlaylistButton.setImageResource(R.drawable.play_list_n);
        nowPlayButton.setImageResource(R.drawable.now_play_n);
        listview.setSelectionFromTop(0, 0);
    }
    
    public void onNowPlayList(View v){
    	nowPlayList = true;
		try {
			list.clear();
			list.addAll(playerInterface_.getList());
			adapter.notifyDataSetChanged();
	    	listview.invalidate();
	    	listview.setSelectionFromTop(0, 0);
		} catch (RemoteException e) {
		}
		
		AlbumButton.setImageResource(R.drawable.album_n);
        ArtistButton.setImageResource(R.drawable.artist_n);
        PlaylistButton.setImageResource(R.drawable.play_list_n);
        nowPlayButton.setImageResource(R.drawable.now_play_y);
    }
    
    public void onPlayListButton(View v){
    	nowPlayList = false;
    	list.clear();
    	setList.SetPlayList(list);
    	adapter.notifyDataSetChanged();
    	listview.invalidate();
    	
    	AlbumButton.setImageResource(R.drawable.album_n);
        ArtistButton.setImageResource(R.drawable.artist_n);
        PlaylistButton.setImageResource(R.drawable.play_list_y);
        nowPlayButton.setImageResource(R.drawable.now_play_n);
        listview.setSelectionFromTop(0, 0);
    }
    
    
    @Override
    protected void onPause() {
    	super.onPause();
    	thread_Flag =false;
    }
 
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	db.close();
    }
 
    @Override
    protected void onResume() {
    	super.onResume();
    	thread_Flag = true;
    	thread = new Thread(this);
    	thread.start();
    	SetView_MusicData();
    	SetState();
    	
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	//接続されていない場合は接続する
    	if(isConnected_ == false) {
    		bindService(new Intent(this, MusicService.class),  this, Context.BIND_AUTO_CREATE);
    		SetView_MusicData();
    		isConnected_ = true;
    	}
    }
    
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if(event.getAction() == KeyEvent.ACTION_DOWN) {
//    		if(event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
//    			// 
//    			return true;
//    		}
//    	}
//    	return super.dispatchKeyEvent(event);
//	}

    @Override
    protected void onRestart() {
    	super.onRestart();
    	
    }
    
    //プレーボタン状態のセット
    private void setPouseButton(boolean temp){
    	if(isPlay != temp){
	    	if(!temp){
	    		handler.post(new Runnable() {
					@Override
					public void run() {
						play.setBackgroundResource(R.drawable.play);
					}
	    		});
			}else{
				handler.post(new Runnable() {
					@Override
					public void run() {
						play.setBackgroundResource(R.drawable.pouse);
					}
	    		});
			}
	    	isPlay = temp;
    	}
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
    
    //メニューボタンのイベント処理
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
//			Intent intent = new Intent(Main.this, Preferences.class);
//			startActivity(intent);
			return true;
		case R.id.reload:
			//リロードを別スレッド実行する
			Thread thread = new Thread(new Progress());
			thread.start();
			return true;
		}
		return false;
	}
	
	//曲データベースのリロード
	private class Progress implements Runnable {
		public void run() {
			dbHelpar.AllDelete(db);
			SerchFile(new File(rootPath));
			setList.SetAll(list);
			handler.post(new Runnable() {
				@Override
				public void run() {
					
					//終了時にアラートだす？
					//とりあえず今回は(;´Д｀)ﾉθﾞﾞ ｳﾞｲｨｨｨｨﾝ
					mVib.vibrate(100);
					//adapterに変わったことを伝え
					adapter.notifyDataSetChanged();
					//リストの再描写
					listview.invalidate();
				}
    		});
		}
	}
 
	//アクティビティが存在する場合動き続けるループ
	@Override
	public void run() {
		while(thread_Flag ){
			//スクロースなどがカクツクのでとりあえずsleep
			try {
                Thread.sleep(REPEAT_INTERVAL);                        
            } catch (InterruptedException e) {
                Log.e("looper", "InterruptedException");
            }
			try{
				//再生状況取得
				boolean isPlay = playerInterface_.isPaly();
				//再生ボタン状況更新
				setPouseButton(isPlay);
				//再生中による更新
				if(!onSeek & isPlay){
					//シークバーの更新
					final int now = playerInterface_.getTime();
					handler.post(new Runnable() {
						@Override
						public void run() {
							seek.setProgress(now);
							nowtime.setText(getTimeString(now));
						}
					});
					//曲が変わった場合曲情報の更新
					if(playerInterface_.changeMusic()){
						SetView_MusicData();
					}
				}
			}catch (Exception e) {
			}
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
    	//決まり切ったインターフェースの取得処理
		Log.e("test", "onServiceConnected!");
    	playerInterface_ = MusicServiceInterface.Stub.asInterface(service);
    	//初期化、serviceに存在する再生リストを取得する
    	List<MusicData> temp;
		try {
			temp = playerInterface_.getList();
			if(temp.size() != 0){
				nowPlayList = true;
				list.clear();
				list.addAll(temp);
				adapter.notifyDataSetChanged();
		    	listview.invalidate();
		    	Log.e("test", "すでにあるリストセット");
			}else{
				if(sp.getBoolean("random", false)){
					playerInterface_.onRandom();
				}
				switch(sp.getInt("loop", 0)){
				case 2:
					playerInterface_.onLoop();
				case 1:
					playerInterface_.onLoop();
				}
				playerInterface_.setList(list);
				SetState();
			}
		} catch (RemoteException e) {
		}

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		//インターフェースを無効にする
		playerInterface_ = null;
	}
}