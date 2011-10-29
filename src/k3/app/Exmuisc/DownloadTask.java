package k3.app.Exmuisc;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadTask extends AsyncTask<String, Void, Bitmap> {  
    // アイコンを表示するビュー  
    private ImageView imageView;  
  
    // コンストラクタ  
    public DownloadTask(ImageView imageView) {  
        this.imageView = imageView;  
    }  
  
    // バックグラウンドで実行する処理  
    @Override  
    protected Bitmap doInBackground(String... urls) {  
		File src = new File(urls[0]);
		
		MusicMetadataSet src_set;
		try {
			src_set = new MyID3().read(src); // read metadata
		} catch (IOException e) {
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
  
    // メインスレッドで実行する処理  
    @Override  
    protected void onPostExecute(Bitmap result) {  
    	if(result != null){ 
    		this.imageView.setImageBitmap(result);  
    	}
        
    }  
} 
