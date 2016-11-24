package se.kth.awesome.util.gsonX;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public interface GsonX {
	static final Gson gson = (new GsonBuilder()).registerTypeAdapter(Date.class, new GsonDateAdapter()).create();
	static final Gson pretty =  new GsonBuilder().setPrettyPrinting().create();
//	static final Gson gson2 =
//			(new GsonBuilder())
//					.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
//					.registerTypeAdapter(Date.class, new GsonDateAdapter()).create();
}
