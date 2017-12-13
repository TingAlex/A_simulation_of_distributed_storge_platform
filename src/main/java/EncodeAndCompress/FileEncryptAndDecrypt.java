package EncodeAndCompress;

import java.io.File;
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.io.RandomAccessFile;  
/** 
 * �ļ����ܡ����� 
 * @author admin_Hzw 
 * 
 */  
public class FileEncryptAndDecrypt {  
    /** 
     * �ļ�file���м��� 
     * @param fileUrl �ļ�·�� 
     * @param key ���� 
     * @throws Exception 
     */  
    public static void encrypt(String fileUrl, String key) throws Exception {  
        File file = new File(fileUrl);  
        String path = file.getPath();  
        if(!file.exists()){  
            return;  
        }  
        int index = path.lastIndexOf("\\");  
        String destFile = path.substring(0, index)+"\\"+"abc";  
        File dest = new File(destFile);  
        InputStream in = new FileInputStream(fileUrl);  
        OutputStream out = new FileOutputStream(destFile);  
        byte[] buffer = new byte[1024];  
        int r;  
        byte[] buffer2=new byte[1024];  
        while (( r= in.read(buffer)) > 0) {  
            for(int i=0;i<r;i++)  
            {  
                byte b=buffer[i];  
                buffer2[i]=b==255?0:++b;  
            }  
            out.write(buffer2, 0, r);  
            out.flush();  
        }  
        in.close();  
        out.close();  
        file.delete();  
        dest.renameTo(new File(fileUrl));  
        appendMethodA(fileUrl, key);  
        System.out.println("���ܳɹ�");  
    }  
  
    /** 
     *  
     * @param fileName 
     * @param content ��Կ 
     */  
    public static void appendMethodA(String fileName, String content) {  
        try {  
            // ��һ����������ļ���������д��ʽ  
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");  
            // �ļ����ȣ��ֽ���  
            long fileLength = randomFile.length();  
            //��д�ļ�ָ���Ƶ��ļ�β��  
            randomFile.seek(fileLength);  
            randomFile.writeBytes(content);  
            randomFile.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  

    public static String decrypt(String fileUrl, String tempUrl, int keyLength) throws Exception{  
        File file = new File(fileUrl);  
        if (!file.exists()) {  
            return null;  
        }  
        File dest = new File(tempUrl);  
        if (!dest.getParentFile().exists()) {  
            dest.getParentFile().mkdirs();  
        }  
  
        InputStream is = new FileInputStream(fileUrl);  
        OutputStream out = new FileOutputStream(tempUrl);  
  
        byte[] buffer = new byte[1024];  
        byte[] buffer2=new byte[1024];  
        byte bMax=(byte)255;  
        long size = file.length() - keyLength;  
        int mod = (int) (size%1024);  
        int div = (int) (size>>10);  
        int count = mod==0?div:(div+1);  
        int k = 1, r;  
        while ((k <= count && ( r = is.read(buffer)) > 0)) {  
            if(mod != 0 && k==count) {  
                r =  mod;  
            }  
  
            for(int i = 0;i < r;i++)  
            {  
                byte b=buffer[i];  
                buffer2[i]=b==0?bMax:--b;  
            }  
            out.write(buffer2, 0, r);  
            k++;  
        }  
        out.close();  
        is.close();  
        return tempUrl;  
    }  
  
    /** 
     * �ж��ļ��Ƿ���� 
     * @param fileName 
     * @return 
     */  
    public static String readFileLastByte(String fileName, int keyLength) {  
        File file = new File(fileName);  
        if(!file.exists())return null;  
        StringBuffer str = new StringBuffer();  
        try {  
            // ��һ����������ļ���������д��ʽ  
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");  
            // �ļ����ȣ��ֽ���  
            long fileLength = randomFile.length();  
            //��д�ļ�ָ���Ƶ��ļ�β��  
            for(int i = keyLength ; i>=1 ; i--){  
                randomFile.seek(fileLength-i);  
                str.append((char)randomFile.read());  
            }  
            randomFile.close();  
            return str.toString();  
        } catch (IOException e) {  
            e.printStackTrace();    
        }    
        return null;  
    }  
}  