package com.storage;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.parser.CVObject;

public class CVDeserializer implements JsonDeserializer<CVObject> {

	private static final String STRING_NAME = "Name";
	private static final String STRING_EDU = "Education";
	private static final String STRING_SKILLS = "Skills";

	private ArrayList<String> eduList = new ArrayList<String>();
	private ArrayList<String> skillList = new ArrayList<String>();

	@Override
	public CVObject deserialize(final JsonElement json, final Type typeOfT,
			final JsonDeserializationContext context) {
		if (json == null) {
			return null;
		}

		JsonObject jsonObject = json.getAsJsonObject();
		String name = jsonObject.get(STRING_NAME).getAsString();
		for (int i = 0; i < jsonObject.get(STRING_EDU).getAsJsonArray().size(); i++) {
			eduList.add(jsonObject.get(STRING_EDU).getAsJsonArray().get(i)
					.getAsString());
		}
		for (int i = 0; i < jsonObject.get(STRING_SKILLS).getAsJsonArray()
				.size(); i++) {
			skillList.add(jsonObject.get(STRING_SKILLS).getAsJsonArray().get(i)
					.getAsString());
		}

		final CVObject cvObj = new CVObject();
		if (!name.equals("")) {
			cvObj.setName(name);
		}
		if (!eduList.isEmpty()) {
			cvObj.setEducation(eduList);
		}
		if (!skillList.isEmpty()) {
			cvObj.setSkills(skillList);
		}

		return cvObj;
	}
}
