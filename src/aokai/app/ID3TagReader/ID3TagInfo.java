package aokai.app.ID3TagReader;

public class ID3TagInfo {
	public byte[] picture;	//アルバム画像:APIC
	public String Comment;	//コメント:COMM
	public String Volume;	//ボリューム:RVA2
	public String Album;	//アルバム:TALB
	public String texter;	//作曲者:TCOM
	public String Genlre;	//ジャンル:TCON
	public String group;	//TIT1
	public String title;	//タイトル:TIT2
	public String artist;	//アーティスト:TPE1
	public String A_Artist;	//アルバムアーティスト:TPE2
	public int disc;		//TPOS
	public int disc_all;	//TPOS
	public int  track;		//トラック番号:TRCK
	public int  track_all;	//トラック番号:TRCK
	public String year;		//年:TDRC
	public String kashi;	//非同期歌詞/文書のコピー:USLT
	
	public boolean compe;	//コンピレーションか:TCMP
	
}
