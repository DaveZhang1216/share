package cn.dave.utils;



import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES {

	
    // 加密
   /*public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }*/

    public static byte[] Decrypt(String sSrc, String sKey) throws Exception {
    	 if (sKey == null) {
             System.out.print("Key为空null");
             return null;
         }
    	byte[] src= hexStringToBytes(sSrc);
    	// byte[] raw = sKey.getBytes("ASCII");
    	byte[] raw =  hexStringToBytes(sKey);
    	 SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        // System.out.println(Utils.bytesToHexStringForWatch(src, src.length));;
         byte[] original = cipher.doFinal(src);
    	 return original;
    }
    // 解密
 /*   public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
         //   byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
              //  byte[] original = cipher.doFinal(encrypted1);
                String originalString;// = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }*/
    public static byte[] hexStringToBytes(String hexString) {   
        if ( hexString.length() % 2 !=0) {   
            return null;   
        }   
         //hexString的长度对2取整，作为bytes的长度    
        int length = hexString.length( ) / 2;   
        char[] hexChars = hexString.toCharArray( );   
        byte[] bytes = new byte[length];   
        for (int i = 0, j =0 ; j< length ; i++ , j++) {   
            String step = "" + hexChars[i++]+hexChars[i];//每次取2个十六进制字符“FF”   
            int  k = Integer.parseInt ( step , 16 );//使用指定基数的字符串参数表示的整数FF 表示255   
            bytes[ j ] = new Integer(k).byteValue(); //该方法的作用是以byte类型返回该 Integer 的值。只取低八位的值，高位不要。   
        }   
        return bytes;   
    }

    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "0142170608995348414E474841495251";
        // 需要加密的字串
       // String cSrc = "www.gowhere.so";
      //  String bytesToHexStringForWatch = Utils.bytesToHexStringForWatch(a, a.length);
      byte[] a=  Decrypt("BAC400668F618DA09FC946D6825D2AB895E756D5A9CF4046C472CA24992C1C30", cKey);
      
        System.out.println(Utils.bytesToHexStringForWatch(a,a.length));
        System.out.println(a.length);
       // System.out.println(cSrc);
        // 加密
       // String enString = AES.Encrypt(cSrc, cKey);
      //  System.out.println("加密后的字串是：" + enString);

        // 解密
      //  String DeString = AES.Decrypt(enString, cKey);
      //  System.out.println("解密后的字串是：" + DeString);
    }
}
