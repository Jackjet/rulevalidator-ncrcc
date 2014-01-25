package com.yonyou.nc.codevalidator.plugin.domain.mm.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * MapSetά����һ��key��һ��Set��ӳ���ϵ��ÿ�η���һ����ֵ��ʱ�� �Ὣֵ���ڸü���Ӧ��set�У�����б���
 * ���򴴽�set
 * 
 * @param <K> ��������
 * @param <V> ֵ������
 * @since 6.0
 * @version 2009-7-9 ����08:35:14
 * @author ����
 */
public class MapSet<K, V> implements Serializable {
  private static final long serialVersionUID = 3235650078973528702L;

  /**
   * ���key��setӳ���ϵ�����ݼ���
   */
  private Map<K, Set<V>> map = new HashMap<K, Set<V>>();

  /**
   * �Ƿ������ǰ��
   * 
   * @param key ��
   * @return ��ǰMapList�����˼�ʱ������true
   */
  public boolean containsKey(K key) {
    return this.map.containsKey(key);
  }

  /**
   * ��ȡ��ǰMapList����ͼ���������ٷ�������洢��Ԫ��
   * 
   * @return ��ǰMapList����ͼ
   */
  public Set<Entry<K, Set<V>>> entrySet() {
    return this.map.entrySet();
  }

  /**
   * ���ݼ���ȡset
   * 
   * @param key ��
   * @return ����Ӧ��set
   */
  public Set<V> get(K key) {
    return this.map.get(key);
  }

  /**
   * ��ȡ���ļ���
   * 
   * @return ���ļ���
   */
  public Set<K> keySet() {
    return this.map.keySet();
  }

  /**
   * ����һ����ֵ�ԡ���ǰ��������ʱ�����Զ�����set�����򣬽�ֵ���뵽��Ӧ��set��
   * 
   * @param key ��
   * @param value ֵ
   */
  public void put(K key, V value) {
    Set<V> l = this.map.get(key);
    if (l == null) {
      l = new HashSet<V>();
    }
    l.add(value);
    this.map.put(key, l);
  }

  /**
   * ���ݼ��Ƴ�ֵ
   * 
   * @param key ��
   * @return ����Ӧ��set
   */
  public Set<V> remove(K key) {
    return this.map.remove(key);
  }

  /**
   * �õ���������
   * 
   * @return ��ǰMapList�Ĵ�С
   */
  public int size() {
    return this.map.size();
  }

  /**
   * ת��Ϊԭʼ��Map����
   * 
   * @return JDK�������ݽṹ��ʽ
   */
  public Map<K, Set<V>> toMap() {
    return this.map;
  }
}
