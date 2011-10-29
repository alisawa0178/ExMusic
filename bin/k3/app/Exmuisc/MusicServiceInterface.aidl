package k3.app.Exmuisc;
import k3.app.Exmuisc.MusicData;

interface MusicServiceInterface {
	//リストの登録
	void setList(in List<MusicData> data);
	//リストの登録(開始指定)
	void setListNum(in List<MusicData> data, int i);
	//リストの取得
	List<MusicData> getList();
	//指定された音楽ファイルを再生する
	int Musicstart();
	//一時停止する
	void pause();
	//現在の時間
	int getTime();
	void next();
	void back();
	//現在の曲を取得
	MusicData getData();
	//現在の終了時刻を取得
	int getFinishTime();
	//再生位置移動
	void seekTo(int i);
	//再生中か？
	boolean isPaly();
	//曲が変わったか？（曲情報の更新用に）
	boolean changeMusic();
	//
	void onLoop();
	boolean getLoop();
	boolean getOneLoop();
	void onRandom();
	boolean getRandom();
	void onAB();
	int getAB();
}