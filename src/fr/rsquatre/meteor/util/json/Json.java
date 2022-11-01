package fr.rsquatre.meteor.util.json;

import java.lang.reflect.Type;

import org.bukkit.Location;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {

	private static GsonBuilder builder = new GsonBuilder();

	public static Gson get() {

		return builder.create();
	}

	static {

		Type t = new TypeToken<Location>() { private static final long serialVersionUID = 1L; }.getType();

		builder.registerTypeAdapter(t, new LocationAdapter());
		builder.registerTypeAdapterFactory(new TransientPostProcessorFactory());

	}
}
