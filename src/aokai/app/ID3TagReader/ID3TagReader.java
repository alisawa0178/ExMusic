package aokai.app.ID3TagReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class ID3TagReader {
	public static final int ID3v2_2 = 2;
	public static final int ID3v2_3 = 3;
	public static final int ID3v2_3_1 = 4;
	public static final int ID3v2_4 = 5;

	public String Path;
	public int Version;
	public boolean Unsynchronisation = false;
	public boolean Compression = false;
	public boolean Extended_header = false;
	public boolean  Experimental_indicator = false;
	public boolean Footer_present = false;
	public int size = 0;
	private ID3TagInfo info;
	

	public ID3TagReader(String path) {
		this.Path = path;
	}

	public ID3TagInfo ReadID3Tag() {
		try {
			FileInputStream in = new FileInputStream(Path);
			ReadHedder(in);
			
			//メインフレーム読み込み
			info = new ID3TagInfo();
			ReadMain(in);
			
			in.close();
			return info;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean ReadHedder(FileInputStream in) throws IOException {
		// ヘッダー読み込み
		int[] ch = new int[10];
		for (int i = 0; i < 10; i++) {
			ch[i] = in.read();
		}
		if(!(ch[0] == 'I'&& ch[1] == 'D'&& ch[2] == '3')){
			System.out.println("not ID3");
			return false;
		}
		System.out.println("yes ID3");
		switch(ch[3]){
		case 2:
			Version = ID3v2_2;
			System.out.println("ID3v2_2");
			break;
		case 3:
			if(ch[4] == 0){
				Version = ID3v2_3;
				System.out.println("ID3v2_3");
			}else if(ch[4] == 1){
				Version = ID3v2_3_1;
				System.out.println("ID3v2_3_1");
			}
			break;
		case 4:
			Version = ID3v2_4;
			System.out.println("ID3v2_4");
			break;
		default:
			System.out.println(""+ch[3] + ch[4]);	
			return false;
		}
		if((ch[5] & 0x80) !=0){
			Unsynchronisation = true;
		}
		if((ch[5] & 0x40) !=0){
			if(Version == ID3v2_2){
				Compression = true;
			}else{
				Extended_header =true;
			}
		}
		if((ch[5] & 0x20) !=0){
			Experimental_indicator = true;
		}
		if((ch[5] & 0x10) !=0){
			Footer_present = true;
		}
		//サイズの取得
		size = 0;
		for(int i = 0; i <4; i++){
			int temp = ch[6+i];
			for(int j = 0;j<(3-i);j++){
				temp *= 0x80;
			}
			size += temp;
		}
		System.out.println("size = " + size);
		
		
		//拡張ヘッダ
		if(Extended_header){
			System.out.println("Extended_header");
			for(int i = 0; i < 4; i++){
				ch[i] = in.read();
			}
			//拡張ヘッダサイズ
			int Extended_Size = 0;
			for(int i = 0; i <4; i++){
				int temp = ch[i];
				for(int j = 0;j<(3-i);j++){
					temp *= 0x80;
				}
				Extended_Size += temp;
			}
			for(int i = 0; i<Extended_Size; i++){
				ch[i] = in.read();
			}
			//TODO 解析めんどいわｗｗ
		}
		
		return true;

	}

	private boolean ReadMain(FileInputStream in) throws IOException{
		byte[] Fheader = new byte[14];
		int sumSize = 0;
		while(sumSize < size){
			if(Version == ID3v2_2){
				in.read(Fheader, 0, 6);
			}else{
				in.read(Fheader, 0, 10);
			}
			
			sumSize += 10;
			//frameSize
			int frameSize = 0;
			byte[] tempFID = new byte[4];
			if(Version == ID3v2_2){
				System.arraycopy(Fheader, 0, tempFID, 0, 3);
				for(int i = 0; i <3; i++){
					int temp = Fheader[i + 3] & 0xFF;
					for(int j = 0;j<(2-i);j++){
						temp *= 0x100;
					}
					frameSize += temp;
				}
			}else{
				System.arraycopy(Fheader, 0, tempFID, 0, 4);
				for(int i = 0; i <4; i++){
					int temp = Fheader[i + 4] & 0xFF;
					for(int j = 0;j<(3-i);j++){
						if(Version == ID3v2_3){
							temp *= 0x100;
						}else{
							temp *= 0x80;
						}
					}
					frameSize += temp;
				}
			}
			//もしタグが空文字だった場合break
			if(tempFID[0] == 0 &&
					tempFID[1] == 0 &&
					tempFID[2] == 0 &&
					tempFID[3] == 0){
				break;
			}
			//圧縮ビット
			if(Version == ID3v2_3 && (Fheader[9] & 0x80) != 0 ||
				Version == ID3v2_4 && (Fheader[9] & 0x8) != 0	){
				System.out.println("圧縮");
				for(int i = 0;i<4;i++){
					in.read();
				}
			}
			//暗号化
			if (Version == ID3v2_3 && (Fheader[9] & 0x40) != 0
					|| Version == ID3v2_4 && (Fheader[9] & 0x4) != 0) {
				System.out.println("暗号化");
				for (int i = 0; i < 1; i++) {
					in.read();
				}
			}
			
			
			byte[] main = new byte[frameSize];
			in.read(main);
			
			ReadFrame(new String(tempFID),main);
			sumSize += frameSize;
		}
		System.out.println("sumSize = " + sumSize);
		return true;
	}
	
	private boolean ReadFrame(String id,byte[] data){
		//画像
		if(id.equals("APIC")){
			int fin = 0;
			for(int i = 1;i<data.length;i++){
				if(data[i] == 0x0){
					if(i<data.length-1 &&data[i+1] == 0x0){
						fin = i+1;
						break;
					}
					fin = i;
					break;
				}
			}
//			System.out.println("MIME type = " + ByteToString(data[0], data, 1,fin-1));
			fin++;
//			System.out.println("Picture type = " + data[fin]);
			for(int i = fin;i<data.length;i++){
				if(data[i] == 0x0){
					if(i<data.length-1 &&data[i+1] == 0x0){
						fin = i+1;
						break;
					}
					fin = i;
					break;
				}
			}
			fin++;
			info.picture = new byte[data.length-fin];
			System.arraycopy(data, fin, info.picture, 0, data.length-fin);
			
			return true;
		}
		//コメント
		if(id.equals("COMM")){
			info.Comment = readString_GS(data);
			return true;
		}
		//アルバム
		if(id.equals("TALB") || id.equals("TOAL")){
			info.Album = readString_T(data);
			return true;
		}
		//作曲者
		if(id.equals("TCOM") || id.equals("TOFN")){
			info.texter = readString_T(data);
			return true;
		}
		//ジャンル
		if(id.equals("TCON")){
			info.Genlre = readString_T(data);
			return true;
		}
		//グループ
		if(id.equals("TIT1")){
			info.group = readString_T(data);
			return true;
		}
		//タイトル
		if(id.equals("TIT2")){
			info.title = readString_T(data);
			System.out.println(info.title + ":");
			return true;
		}
		//アーティスト
		if(id.equals("TPE1") || id.equals("TOPE")){
			info.artist = readString_T(data);
			System.out.println(info.artist);
			return true;
		}
		//アルバムアーティスト
		if(id.equals("TPE2")){
			info.A_Artist = readString_T(data);
			return true;
		}
		//トラック番号
		if(id.equals("TRCK")){
			String temp = readString_T(data);
			System.out.println("raw TRCK =" +temp);
			String[] trackdata= temp.split("[/ ]+");
			boolean state = true;
			for(int i = 0;i<trackdata.length;i++){
				if(!checkNum(trackdata[i])){
					continue;
				}
				if(state){
					info.track = (int) Double.parseDouble(trackdata[i]);
					state = false;
				}else{
					info.track_all = (int) Double.parseDouble(trackdata[i]);
				}
			}
			System.out.println(info.track +"/"+info.track_all);
			return true;
		}
		//ディスク番号
		if(id.equals("TPOS")){
			String[] trackdata= readString_T(data).split("[/ ]+");
			boolean state = true;
			for(int i = 0;i<trackdata.length;i++){
				if(!checkNum(trackdata[i])){
					continue;
				}
				if(state){
					info.disc= (int) Double.parseDouble(trackdata[i]);
					state = false;
				}else{
					info.disc_all = (int) Double.parseDouble(trackdata[i]);
				}
			}
			return true;
		}
		//年
		if(id.equals("TDRC") || id.equals("TYER")){
			info.year = readString_T(data);
			return true;
		}
		//非同期歌詞/文書のコピー
		if(id.equals("USLT")){
			info.kashi = readString_GS(data);
			System.out.println(info.kashi);
			return true;
		}		
		return true;
	}
	
	private String readString_T(byte[] data){
		return ByteToString(data[0], data, 1);
	}
	
	private String readString_GS(byte[] data){
		int fin = 0;
		for(int i = 4;i<data.length;i++){
			if(data[i] == 0x0){
				if(i<data.length-1 && data[i+1] == 0x0){
					fin = i+1;
					break;
				}
				fin = i;
				break;
			}
		}
		
		return ByteToString(data[0],data,fin+1);
	}
	
	private String ByteToString(int code,byte[] data,int start){
		return ByteToString(code,data,start,data.length-start);
	}
	private String ByteToString(int code,byte[] data,int start,int len){
		int temp_len = len;
		boolean dis = false;
		if(data.length < start + 2){
			return "";
		}
		for(int i = 0;i<2;i++){
			if(data[start + temp_len - 1 - i] == 0x00){
				len--;
				dis = true;
			}else{
				break;
			}
		}
		
		if(len<0){
			len = 0;
		}
		try {
			switch(code){
			case 0:
				byte[] temp = new byte[len];
				System.arraycopy(data, start, temp, 0, len);
				
				if(isSJIS(temp)){
					return new String(data,start,len,"Shift_JIS").replaceAll("\r", "\n");
				}else{
					return new String(data,start,len,"ISO-8859-1").replaceAll("\r", "\n");
				}
			case 1:
				if(len%2 != 0 && dis){
					len++;
				}
				return new String(data,start,len,"UTF-16").replaceAll("\r", "\n");
			}
		} catch (UnsupportedEncodingException e) {
		}
		
		System.out.println("error");
		return new String(data).replaceAll("\r\n", "\n"); 
	}
	 public boolean checkNum(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

	public boolean isSJIS(byte[] byteList) {
		for (int i = 0; i < byteList.length; i++) {
			short c = (short) byteList[i];
			c &= 0xFF;
			if (c < 0x7F || 0xA1 < c && c < 0xDF) {
				continue;
			}
			if (0x80 < c && c < 0xA0) {
				i++;
				c = (short) byteList[i];
				c &= 0xFF;
				if ((0x40 <= c && c <= 0x7E) || (0x80 <= c && c <= 0xFC)) {
				} else {
					return false;
				}
			} else if (0xE0 <= c && c <= 0xFC) {
				i++;
				c = (short) byteList[i];
				c &= 0xFF;
				if (0x40 <= c && c <= 0x7E) {
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	
}
