package k3.app.Exmuisc;

import java.util.ArrayList;
import java.util.Date;

import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SetList {
	public final DatabaseOpenHelper dbHelpar;
	public final SQLiteDatabase db;
	public SetList(DatabaseOpenHelper dbHelpar,SQLiteDatabase db) {
		this.dbHelpar = dbHelpar;
		this.db = db;
	}
	public void SetAllAlbum(ArrayList<MusicData> list){
		list.clear();
		String sql = "select DISTINCT album from " + "music order by album asc;";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount; i++) {
				MusicData data = new MusicData();
				data.State = MusicData.Album;
				data.album =c.getString(0);
				sql = "select * from " + "music " + "WHERE album = '"+data.album+"';";
				SQLiteCursor c2 = (SQLiteCursor) db.rawQuery(sql, null);
				c2.moveToFirst();
				data.FilePath = c2.getString(4);
				list.add(data);
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("test", "SQLException");
			Log.e("test", sql);
		}
	}
	
	public void SetAlbum(ArrayList<MusicData> list,String album){
		list.clear();
		String sql = "select * from " + "music " +"WHERE album = '"+album+"'" +
				" order by disc_num asc, track_number asc"+";";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount; i++) {
				MusicData temp_rh;
				temp_rh = dbHelpar.getData(c);
				if(temp_rh !=null){
					list.add(temp_rh);
				}
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("test", "SQLException");
			Log.e("test", sql);
		}
	}
	
	public void SetArtist(ArrayList<MusicData> list,String artist){
		list.clear();
		String sql = "select * from " + "music " +"WHERE artist = '"+artist+"'" +
				" order by album asc, disc_num asc, track_number asc"+";";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount; i++) {
				MusicData temp_rh;
				temp_rh = dbHelpar.getData(c);
				if(temp_rh !=null){
					list.add(temp_rh);
				}
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("test", "SQLException");
			Log.e("test", sql);
		}
	}
	
	public void SetAllArtist(ArrayList<MusicData> list){
		list.clear();
		String sql = "select DISTINCT artist from " + "music order by artist asc;";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount; i++) {
				MusicData data = new MusicData();
				data.State = MusicData.Artist;
				data.artist = c.getString(0);
				list.add(data);
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("test", "SQLException");
			Log.e("test", sql);
		}
	}
	
	public void SetArtistToArtist(ArrayList<MusicData> list, String artist){
		list.clear();
		String sql = "select DISTINCT album from music " + 
						"WHERE artist = '"+artist+"' " +
						"order by artist asc;";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount; i++) {
				MusicData data = new MusicData();
				data.State = MusicData.Album;
				data.album =c.getString(0);
				sql = "select * from " + "music " + "WHERE album = '"+data.album+"';";
				SQLiteCursor c2 = (SQLiteCursor) db.rawQuery(sql, null);
				c2.moveToFirst();
				data.FilePath = c2.getString(4);
				list.add(data);
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("test", "SQLException");
			Log.e("test", sql);
		}
	}
	
	public void SetPlayList(ArrayList<MusicData> list){
		list.clear();
		
		MusicData temp = new MusicData();
		temp.State = MusicData.List;
		temp.id = -2;
		temp.song_title = "お気に入り";
		list.add(temp);
		temp = new MusicData();
		temp.State = MusicData.List;
		temp.id = -1;
		temp.song_title = "最近のお気に入り";
		list.add(temp);
		
		
		//TODO データベースよりPlayListを取得
		temp = new MusicData();
		temp.State = MusicData.List;
		temp.song_title = "Playlist1";
		list.add(temp);
		temp = new MusicData();
		temp.State = MusicData.List;
		temp.song_title = "Playlist2";
		list.add(temp);
		
	}
	
	public void SetFavoriteALL(ArrayList<MusicData> list){
		list.clear();
		String sql = "select * from " + "music " +"WHERE star = '1'" +
				" order by starDay asc"+";";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount; i++) {
				MusicData temp_rh;
				temp_rh = dbHelpar.getData(c);
				if(temp_rh !=null){
					list.add(temp_rh);
				}
				c.moveToNext();
			}
		} catch (SQLException e) {
			Log.e("test", "SQLException");
			Log.e("test", sql);
		}
		
	}
	
	public void SetAll(ArrayList<MusicData> list){
		list.clear();
		String sql = "select * from " + "music " + "order by "
				+ "artist asc" + ";";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);

			int rowcount = c.getCount();
			c.moveToFirst();

			for (int i = 0; i < rowcount; i++) {
				MusicData temp_rh;
				temp_rh = dbHelpar.getData(c);
				if(temp_rh !=null){
					list.add(temp_rh);
				}
				c.moveToNext();
			}
		} catch (SQLException e) {
		}
	}
	public void SetRecentlyFavoriteALL(ArrayList<MusicData> list) {
		list.clear();
		String sql = "select * from " + "music " + "order by "
						+ "starDay asc" + ";";
		try {
			SQLiteCursor c = (SQLiteCursor) db.rawQuery(sql, null);

			int rowcount = c.getCount();
			c.moveToFirst();
			Date date = new Date();
			for (int i = 0; i < rowcount; i++) {
				MusicData temp_rh;
				temp_rh = dbHelpar.getData(c);
				if(temp_rh !=null){
//					if(cal.after(temp_rh.starDay)){
						list.add(temp_rh);
//					}
				}
				c.moveToNext();
			}
		} catch (SQLException e) {
		}
	}
}
