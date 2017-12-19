package com.zhoukp.inform.view.wheelview;

import com.zhoukp.inform.view.datepick.DateObject;

import java.util.ArrayList;

/**
 * The simple Array wheel adapter
 */
public class StringWheelAdapter implements WheelAdapter {
	
	/** The default items length */
	public static final int DEFAULT_LENGTH = -1;
	
	// items
	private ArrayList<DateObject> list;
	
	// length
	private int length;

	/**
	 * Constructor
	 */
	public StringWheelAdapter(ArrayList<DateObject> list, int length) {
		this.list = list;
		this.length = length;
	}
	

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < list.size()) {
			return list.get(index).getListItem();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return list.size();
	}

	@Override
	public int getMaximumLength() {
		return length;
	}

}
