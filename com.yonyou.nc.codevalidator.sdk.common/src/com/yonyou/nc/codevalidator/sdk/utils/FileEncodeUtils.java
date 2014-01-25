package com.yonyou.nc.codevalidator.sdk.utils;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.File;
import java.io.IOException;

/**
 * �����ļ�����Ĺ�����
 * 
 * @author mazhqa
 * 
 */
public final class FileEncodeUtils {
	
	private FileEncodeUtils() {
		
	}

	/**
	 * ���õ�������Դ��cpdetector��ȡ�ļ������ʽ
	 * 
	 * @param path
	 *            Ҫ�ж��ļ������ʽ��Դ�ļ���·��
	 * @return �ļ����룬һ����ļ���ʽ����
	 *         <table>
	 *         <tr>
	 *         <td>����</td>
	 *         <td>GB2312</td>
	 *         </tr>
	 *         <tr>
	 *         <td>Ӣ��</td>
	 *         <td>US-ASCII</td>
	 *         </tr>
	 *         <tr>
	 *         <td>����</td>
	 *         <td>windows-1252</td>
	 *         </tr>
	 *         </table>
	 * @throws IOException
	 *             - �ļ���ȡIO�쳣
	 */
	public static String getFileEncode(String path) throws IOException {
		/*
		 * detector��̽����������̽�����񽻸������̽��ʵ�����ʵ����ɡ�
		 * cpDetector������һЩ���õ�̽��ʵ���࣬��Щ̽��ʵ�����ʵ������ͨ��add���� �ӽ�������ParsingDetector��
		 * JChardetFacade��ASCIIDetector��UnicodeDetector��
		 * detector���ա�˭���ȷ��طǿյ�̽���������Ըý��Ϊ׼����ԭ�򷵻�̽�⵽��
		 * �ַ������롣ʹ����Ҫ�õ�����������JAR����antlr.jar��chardet.jar��cpdetector.jar
		 * cpDetector�ǻ���ͳ��ѧԭ��ģ�����֤��ȫ��ȷ��
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/*
		 * ParsingDetector�����ڼ��HTML��XML���ļ����ַ����ı���,���췽���еĲ�������
		 * ָʾ�Ƿ���ʾ̽����̵���ϸ��Ϣ��Ϊfalse����ʾ��
		 */
		detector.add(new ParsingDetector(true));
		/*
		 * JChardetFacade��װ����Mozilla��֯�ṩ��JChardet����������ɴ�����ļ��ı���
		 * �ⶨ�����ԣ�һ���������̽�����Ϳ�����������Ŀ��Ҫ������㻹�����ģ�����
		 * �ٶ�Ӽ���̽���������������ASCIIDetector��UnicodeDetector�ȡ�
		 */
		detector.add(JChardetFacade.getInstance()); // �õ�antlr.jar��chardet.jar
		// ASCIIDetector����ASCII����ⶨ
		detector.add(ASCIIDetector.getInstance());
		// UnicodeDetector����Unicode�������Ĳⶨ
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		File f = new File(path);
		charset = detector.detectCodepage(f.toURI().toURL());
		return charset == null ? null : charset.name();
	}

}
