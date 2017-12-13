package servlet;

import java.io.*;

/**
 * Created by Ting on 2017/7/18.
 */
public class FileTestPart {
    public static void main(String[] args){
        try {
            BufferedReader reader = null;
            BufferedWriter writer = null;
            reader = new BufferedReader(new FileReader("C:\\Users\\Ting\\OneDrive\\Applications\\lala.txt"));
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Ting\\OneDrive\\Applications\\lalala.txt"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                //tempString=DESUtil.encrypt(tempString,"A1B2C3D4E5F60708");
                writer.write(tempString);
            }
            writer.flush();
            writer.close();
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
