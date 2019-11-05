package cn.dave.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class GZIPUtils {  
	  
    /** 
     * 字符串压缩为GZIP字节数组 
     *  
     * @param str 
     * @param encoding 
     * @return 
     */  
    public static byte[] compress(byte[] bytes) {  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        GZIPOutputStream gzip;  
        try {  
            gzip = new GZIPOutputStream(out);  
            gzip.write(bytes);  
            gzip.close();  
        } catch (IOException e) {  
        	throw new RuntimeException("GZIP压缩error", e);
        }  
        return out.toByteArray();  
    }  
  
    /** 
     * GZIP解压缩 
     *  
     * @param bytes 
     * @return 
     */  
    public static byte[] uncompress(InputStream in) {  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        try {  
            GZIPInputStream ungzip = new GZIPInputStream(in);  
            byte[] buffer = new byte[256];  
            int n;  
            while ((n = ungzip.read(buffer)) >= 0) {  
                out.write(buffer, 0, n);  
            }  
        } catch (Exception e) {  
        	throw new RuntimeException("GZIP解压error", e);
        }  
        return out.toByteArray();  
    }  
    
    /** 
     * GZIP解压缩 
     *  
     * @param bytes 
     * @return 
     */  
    public static byte[] uncompress(byte[] bytes) {  
    	return uncompress(new ByteArrayInputStream(bytes));
    }  
    public static void main(String[] args) throws Exception {
		String bytesString = "D6998FF974DDAC5B5AC4EAD10B5ECFC10B659FBD128A1E09082B0467925BB6A9CDBE631C842BE2377CAE2E543531FBA534B97247E8D20F111B01E6F414546A1267F230C4D72C0B68AEB278416EE5E4A555B142376D1D214F9284EA3FC1184F39";
		
		byte[] decrypt = AES.Decrypt(bytesString, "0142170608995348414E474841495251");
		System.out.println(Utils.bytesToHexStringForWatch(decrypt, decrypt.length));
		decrypt = GZIPUtils.uncompress(decrypt);
		System.out.println(Utils.bytesToHexStringForWatch(decrypt, decrypt.length));
	}
}