package k3.app.Exmuisc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "music.db";
	public final static String DB_TABLE = "music";
	public final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public DatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// テーブルの生成
		StringBuilder createSql = new StringBuilder();
		createSql.append("create table " + DB_TABLE + " (");
		createSql.append("id" + " INTEGER primary key,");
		createSql.append("song_title" + " TEXT,");
		createSql.append("artist" + " TEXT,");
		createSql.append("album" + " TEXT,");
		createSql.append("FilePath" + " TEXT,");
		createSql.append("Genre" + " TEXT,");
		createSql.append("track_number" + " INTEGER,");
		createSql.append("track_max" + " INTEGER,");
		createSql.append("disc_num" + " INTEGER,");
		createSql.append("disc_max" + " INTEGER,");
		createSql.append("star" + " INTEGER,");
		createSql.append("starDay" + " TEXT,");
		createSql.append("Text" + " TEXT,");
		createSql.append("texter" + " TEXT");
		
		createSql.append(")");
		db.execSQL(createSql.toString());
	}
	
	public void writeDB(SQLiteDatabase db,MusicData data){
		ContentValues values = new ContentValues();
		values.put("song_title" ,data.song_title);
		values.put("artist", data.artist);
		values.put("album" , data.album);
		values.put("FilePath" , data.FilePath);
		values.put("Genre" , data.Genre);
		values.put("track_number" , data.track_number);
		values.put("track_max" ,  data.track_max);
		values.put("disc_num" ,  data.disc_num);
		values.put("disc_max" ,  data.disc_max);
		if(data.star){
			values.put("star" ,  1);
		}else{
			values.put("star" ,  0);
		}
		values.put("starDay" ,  sdf.format(data.starDay));
		values.put("Text" ,  data.Text);
		values.put("texter" ,  data.texter);
		db.insert(DB_TABLE, null, values);
	}
	
	public void UpdateDB(SQLiteDatabase db,int id,ContentValues cv){
		db.update(DB_TABLE, cv, "id = " + id, null);
	}
	

    
	/**
	 * データベースの更新
	 * 
	 * 親クラスのコンストラクタに渡すversionを変更したときに呼び出される
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// データベースの更新
		db.execSQL("drop talbe if exists " + DB_TABLE);
		onCreate(db);
	}

	public MusicData getData(SQLiteCursor c) {
		MusicData data = new MusicData();
		data.id =c.getInt(0);
		data.song_title=c.getString(1);
		data.artist = c.getString(2);
		data.album = c.getString(3);
		data.FilePath = c.getString(4);
		data.Genre = c.getString(5);
		data.track_number = c.getInt(6);
		data.track_max = c.getInt(7);
		data.disc_num = c.getInt(8);
		data.disc_max = c.getInt(9);
		if(c.getInt(10)==0){
			data.star = false;
		}else{
			data.star = true;
		}
		try {
			data.starDay = sdf.parse(c.getString(11));
		} catch (ParseException e) {
			data.starDay = null;
		}
		data.Text = c.getString(12);
		data.texter = c.getString(13);
		return data;
	}
	public void AllDelete(SQLiteDatabase db) {
		 String query = "delete from " + DB_TABLE + " where id >-1;";
		 db.execSQL(query);
	}

}
