package k3.app.Exmuisc;

public interface MusicListener {
	public void set(String path,MusicData data,int i);
	public void OpenAlbum(String albume);
	public void OpenArtist(String artist);
	public void OpenPlayList(int id);
}
