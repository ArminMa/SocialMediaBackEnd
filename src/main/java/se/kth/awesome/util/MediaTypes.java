package se.kth.awesome.util;

import java.nio.charset.Charset;
import org.springframework.http.MediaType;


public class MediaTypes {


	public static final MediaType JsonUtf8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	public static final MediaType textPlain = new MediaType(MediaType.ALL.getType(), MediaType.ALL.getType(), Charset.forName("utf8"));
}
