package ru.insagent.model;

public class Entry<K, V> {
	private K key;
	private K value;

	public Entry(K key, K value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public K getValue() {
		return value;
	}
	public void setValue(K value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Entry [key=" + key + ", value=" + value + "]";
	}
}
