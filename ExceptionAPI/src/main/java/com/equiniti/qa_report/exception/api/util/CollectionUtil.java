package com.equiniti.qa_report.exception.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CollectionUtil {
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	@SuppressWarnings("unchecked")
	public static void fillMapData(Map<Object,Object> inputMap,String key,Object innerKey,Object innerValue){
		Map<Object,Object> innerMap=null;
		if(inputMap.containsKey(key)){
			innerMap=(Map<Object, Object>) inputMap.get(key);
		}else{
			innerMap=new HashMap<>();
		}
		innerMap.put(innerKey, innerValue);
		inputMap.put(key, innerMap);
	}

	@SuppressWarnings("unchecked")
	public static void fillListData(Map<String,Object> inputMap,String key,Object innerObjValue){
		List<Object> innerList=null;
		if(inputMap.containsKey(key)){
			innerList=(List<Object>) inputMap.get(key);
		}else{
			innerList=new ArrayList<>();
		}
		innerList.add(innerObjValue);
		inputMap.put(key, innerList);
	}
	
	public static List<?> getListFromMap(Map<?,Object> inputMap){
		List<?> resultList=new ArrayList<>(inputMap.keySet());
		return resultList;
	}

	public static List<?> suffleCollectionByRandom(List<?> input){
		int incr=getRandomNumber(input.size());
		while(incr > 0){
			Collections.shuffle(input);
			incr--;
		}
		return input;
	}

	public static int getRandomNumber(int seed){
		int randomNum=0;
		Random random=new Random();
		randomNum=random.nextInt(seed);
		while(randomNum <= 0){
			randomNum=getRandomNumber(seed);
		}
		return randomNum;
	}

	public static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();

	}

	
	public static String getRandomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}

}
