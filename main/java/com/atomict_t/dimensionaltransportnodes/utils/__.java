package com.atomict_t.dimensionaltransportnodes.utils;

public class __ {
	public static void log(Object o){
		System.out.println(o.getClass().cast(o));
	}
	public static void log(String key, Object o){
		System.out.print(key + ": ");
		log(o);
	}
}
