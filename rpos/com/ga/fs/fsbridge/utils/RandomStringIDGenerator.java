package com.ga.fs.fsbridge.utils;

import java.util.UUID;

public class RandomStringIDGenerator {

	public static String generate(){
		String uuid = UUID.randomUUID().toString();
		
        return uuid;
	}
	
}
