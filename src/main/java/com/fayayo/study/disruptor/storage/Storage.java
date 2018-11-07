package com.fayayo.study.disruptor.storage;

import com.fayayo.study.middleware.elements.Record;

public interface Storage {

	public void put(Record record);

	public void put(Record[] records);

	public boolean isEmpty();

	public int size();

	public void close();
}
