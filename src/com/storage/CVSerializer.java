package com.storage;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.parser.CVObject;

public class CVSerializer implements JsonSerializer<CVObject> {
	private static final String STRING_NAME = "Name";
	private static final String STRING_EDU = "Education";
	private static final String STRING_SKILLS = "Skills";

	public JsonElement serialize(final CVObject cv, final Type type,
			final JsonSerializationContext context) {

		JsonObject result = new JsonObject();
		result.add(STRING_NAME, new JsonPrimitive(cv.getName()));
		for (int i = 0; i < cv.getEducation().size(); i++) {
			result.add(STRING_EDU, new JsonPrimitive(cv.getEducation().get(i)));
		}
		for (int i = 0; i < cv.getSkills().size(); i++) {
			result.add(STRING_SKILLS, new JsonPrimitive(cv.getSkills().get(i)));
		}
		return result;
	}
}