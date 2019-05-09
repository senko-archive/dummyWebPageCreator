package com.senko.grepRegexTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrepRegex {
	
	public static void main(String[] args) {
		String line = "This order was placed for QT3000! OK?";
		String regex = "\\d+";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher m = pattern.matcher(line);
		
		System.out.println(m.find());
		System.out.println(m.group(0));
	}

}
