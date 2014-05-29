package com.asdamp.utility;

import com.asdamp.smartpizza.R;

public class StringUtils {

	public static String firstUp(String string) {
		if(string.length()>0){
		char first = Character.toUpperCase(string.charAt(0));
		return first + string.substring(1);
		}
		return string;
	}

}
