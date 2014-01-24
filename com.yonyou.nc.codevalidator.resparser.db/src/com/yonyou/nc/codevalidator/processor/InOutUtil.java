/*
 * �������� 2005-8-5
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package com.yonyou.nc.codevalidator.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * @nopublish User: ���� Date: 2005-6-2 Time: 15:20:52 InOutUtil���˵��
 */
public class InOutUtil {

	public static int readLine(InputStream in, OutputStream out)
			throws IOException {
		int count = 0;
		for (;;) {
			int b = in.read();

			if (b == -1) {
				break;
			}
			count++;
			out.write(b);
			if (b == '\n') {
				break;
			}
		}
		return count;
	}

	public static byte[] serialize(Serializable s) throws IOException {
		if (s == null)
			return null;
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bo);
		os.writeObject(s);
		return bo.toByteArray();
	}

	public static Serializable deserialize(byte[] ba) {
		Serializable value = null;
		try {
			if (ba == null)
				return null;
			ByteArrayInputStream bi = new ByteArrayInputStream(ba);
			ObjectInputStream is = new ObjectInputStream(bi);

			value = (Serializable) is.readObject();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			Logger.error("deserialize error", e);
			return ba;
		} catch (ClassNotFoundException e) {
			Logger.error("deserialize error", e);
			
		}
		return value;
	}
}
