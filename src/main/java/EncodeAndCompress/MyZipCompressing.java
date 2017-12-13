package EncodeAndCompress;

import java.io.*;
import java.util.zip.*;

/**
 * ����ʵ����ZIPѹ��������Ϊ2���� �� ѹ����compression�����ѹ��decompression��
 * <p>
 * ���¹��ܰ������˶�̬���ݹ��JAVA���ļ��������ԶԵ����ļ������⼶���ļ��н���ѹ���ͽ�ѹ�� ���ڴ������Զ���Դ����·����Ŀ�����·����
 * <p>
 * �ڱ��δ����У�ʵ�ֵ���ѹ�����֣���ѹ���ּ�������Decompression���֡�
 * 
 * @author HAN
 * 
 */

public class MyZipCompressing {
	private int k = 1; // ����ݹ��������

	public MyZipCompressing() {
		// TODO Auto-generated constructor stub
	}


	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyZipCompressing book = new MyZipCompressing();
		}*/
		public void zipfile(String s1,String s2){
		try {
			zip(s1,
					new File(s2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	

	private void zip(String zipFileName, File inputFile) throws Exception {
		System.out.println("ѹ����...");
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		BufferedOutputStream bo = new BufferedOutputStream(out);
		zip(out, inputFile, inputFile.getName(), bo);
		bo.close();
		out.close(); // ������ر�
		System.out.println("ѹ�����");
	}

	private void zip(ZipOutputStream out, File f, String base,
			BufferedOutputStream bo) throws Exception { // ��������
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (fl.length == 0) {
				out.putNextEntry(new ZipEntry(base + "/")); // ����zipѹ�������base
				System.out.println(base + "/");
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + "/" + fl[i].getName(), bo); // �ݹ�������ļ���
			}
			System.out.println("��" + k + "�εݹ�");
			k++;
		} else {
			out.putNextEntry(new ZipEntry(base)); // ����zipѹ�������base
			System.out.println(base);
			FileInputStream in = new FileInputStream(f);
			BufferedInputStream bi = new BufferedInputStream(in);
			int b;
			while ((b = bi.read()) != -1) {
				bo.write(b); // ���ֽ���д�뵱ǰzipĿ¼
			}
			bi.close();
			in.close(); // �������ر�
		}
	}
	
}

