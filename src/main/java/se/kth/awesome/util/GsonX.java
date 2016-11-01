package se.kth.awesome.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface GsonX {
	static final Gson gson =  new Gson();
	static final Gson pretty =  new GsonBuilder().setPrettyPrinting().create();
}
