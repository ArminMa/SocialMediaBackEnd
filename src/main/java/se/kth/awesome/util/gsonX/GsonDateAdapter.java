package se.kth.awesome.util.gsonX;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GsonDateAdapter implements JsonSerializer<Date>,JsonDeserializer<Date> {

	private static DateFormat dateFormat;

	public GsonDateAdapter() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");      //This is the format I need
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));                               //This is the key line which converts the date to UTC which cannot be accessed with the default serializer
	}

	@Override public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
		return new JsonPrimitive(dateFormat.format(date));
	}

	@Override public synchronized Date deserialize(JsonElement jsonElement,Type type,JsonDeserializationContext jsonDeserializationContext) {
		try {
			System.out.println("jsonElement.getAsString() = " + jsonElement);
			return dateFormat.parse(jsonElement.getAsString());
		} catch (ParseException e) {
			try {
				return new Date(new Long(jsonElement.getAsString())) ;
			} catch (NumberFormatException nfe) {
				throw new JsonParseException(nfe);
			}
		}
	}




//	public static class DateFormatTypes{
//
//		private Pattern pattern;
//		private Matcher matcher;
//
//		private static final String DATE_PATTERN =
//				"(((0[1-9]|[12]\\d|3[01])\\/(0[13578]|1[02])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|[12]\\d|30)\\/(0[13456789]|1[012])\\/((1[6-9]|[2-9]\\d)\\d{2}))|((0[1-9]|1\\d|2[0-8])\\/02\\/((1[6-9]|[2-9]\\d)\\d{2}))|(29\\/02\\/((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))";
//
//		/**
//		 * This date regex is similar to the first one, but with the difference of matching only the whole string. So "01-01-2000-12345" won't pass with a match.
//		 * Keep in mind that String.matches tries to match only the whole string.
//		 */
//		private static final String DATE_REGEX_ONLY_WHOLE_STRING = "^" + DATE_PATTERN + "$";
//
//		public DateFormatTypes(){
//			pattern = Pattern.compile(DATE_PATTERN);
//		}
//
//		public static final  String mili = "'yyyy-MM-dd'T'HH:mm:ss.SSS'";
//		public static final  String miliZone = "'yyyy-MM-dd'T'HH:mm:ss.SSS'Z''";
//	}
}