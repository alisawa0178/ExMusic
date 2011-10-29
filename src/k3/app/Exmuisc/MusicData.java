package k3.app.Exmuisc;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import aokai.app.ID3TagReader.ID3TagInfo;
import aokai.app.ID3TagReader.ID3TagReader;


public class MusicData implements Parcelable{
	public static final int song = 0;
	public static final int Album = 1;
	public static final int Artist = 2;
	public static final int List = 3;
	public int State;
	public boolean isPlat;
	public int id;
	public String song_title;
	public String artist;
	public String album;
	public String FilePath;
	public String Genre;
	public int track_number;
	public int track_max;
	public int disc_num;
	public int disc_max;
	public boolean star;
	public Date starDay;
	public String Text; 
	public String texter;
	
	public MusicData() {
		song_title = "";
		artist = "";
		album = "";
		FilePath = "";
		texter = "";
		State = song;
		Text = "";
		star = false;
		isPlat = false;
	}
	public MusicData(MusicData temp){
		this.isPlat = temp.isPlat;
		this.song_title = temp.song_title;
		this.artist = temp.artist;
		this.album = temp.album;
		this.FilePath = temp.FilePath;
		this.Genre = temp.Genre;
		this.track_number = temp.track_number;
		this.id = temp.id;
		this.State = temp.State;
		this.track_max = temp.track_max;
		this.disc_num = temp.disc_num;
		this.disc_max = temp.disc_max;
		this.star = temp.star;
		this.Text = temp.Text;
		this.starDay = temp.starDay; 
		this.texter = temp.texter;
	}
	
	public MusicData(String path) {
		Log.e("test", "path = " + path);
		FilePath = path;
		ID3TagReader id3 = new ID3TagReader(path);
		ID3TagInfo data = id3.ReadID3Tag();
		if(data != null){
			if (data.artist != null) {
				artist = data.artist;
			}else{
				artist = "";
			}
			if (data.Album != null) {
				album = data.Album;
			}else{
				album ="";
			}
			if (data.title != null) {
				song_title = data.title;
			}else{
				song_title = "";
			}
			if (data.Genlre != null) {
				Genre = data.Genlre;
			}else{
				Genre = "";
			}
			track_number = data.track;
			this.disc_num = data.disc;
			this.disc_max = data.disc_all;
			this.star = false;
			this.starDay = new Date();
			if (data.kashi != null) {
				this.Text = data.kashi;
			}
			if (data.texter != null) {
				this.texter = data.texter;
			}
		}
	}
	
	
	public Bitmap getBitmap(){
		File src = new File(FilePath);
		
		MusicMetadataSet src_set;
		try {
			src_set = new MyID3().read(src); // read metadata
		} catch (IOException e) {
			Log.e("test", "IOException:MyID3().read(src)");
			return null;
		} 

		if (src_set == null){
			return null;
		} // perhaps no metadata
		MusicMetadata metadata = (MusicMetadata) src_set.getSimplified();
		
		@SuppressWarnings("unchecked")
		Vector<ImageData> imageList = metadata.getPictureList();
		if(imageList.size()>0){
			ImageData img = imageList.get(0);
			return BitmapFactory.decodeByteArray(img.imageData, 0, img.imageData.length);
		}
		return null;
	}
	@Override
	public int describeContents() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	public static final Parcelable.Creator<MusicData> CREATOR = new Parcelable.Creator<MusicData>() {
        public MusicData createFromParcel(Parcel in) {
            return new MusicData(in);
        }

        public MusicData[] newArray(int size) {
            return new MusicData[size];
        }
    };
    private MusicData(Parcel in) {
        readFromParcel(in);
    }
    public void readFromParcel(Parcel in) {
    	song_title = in.readString();
    	artist = in.readString();
    	album = in.readString();
    	FilePath = in.readString();
    	track_number = in.readInt();
    }
}
