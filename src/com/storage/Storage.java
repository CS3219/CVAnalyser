package com.storage;

import com.parser.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Storage {
	private String cvFile = "cv.json"; 
	private String jobDescFile = "jobDesc.json"; 
	private ArrayList<CVObject> cvObjs;
	private ArrayList<JobDescObject> jobDescObjs;
	private ArrayList<CVObject> allCV;
	private ArrayList<JobDescObject> allJobDesc;

	
	public Storage() throws IOException{
		cvObjs = new ArrayList<CVObject>();
		jobDescObjs = new ArrayList<JobDescObject>();
		ifFileExist(cvFile);
		ifFileExist(jobDescFile);
	}

	private void ifFileExist(String fileName) throws IOException {
		File file = new File(fileName);
		if(!file.exists()) {
		    file.createNewFile();
		} 		
	}

	public void writeCVToFile(ArrayList<CVObject> cvObj) throws IOException{
		
		String json = convertCvToString(cvObj);
		writeStringToFile(json, cvFile);
	}
	
		public void writeJobDescToFile(ArrayList<JobDescObject> jobDesObj) throws IOException{
			
			String json = convertJobDescToString(jobDesObj);
			writeStringToFile(json, jobDescFile);
		}
		
		private String convertCvToString(ArrayList<CVObject> cvObj) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting().registerTypeAdapter(CVObject.class, new CVSerializer());
			Gson gson = gsonBuilder.create();
			String json = gson.toJson(cvObj);
			
			return json;
		}
		
		private String convertJobDescToString(ArrayList<JobDescObject> jobDesObj) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setPrettyPrinting().registerTypeAdapter(CVObject.class, new jobDescSerializer());
			Gson gson = gsonBuilder.create();
			String json = gson.toJson(jobDesObj);
			
			return json;
		}
		
		private void writeStringToFile(String stringToWrite, String fileName) throws IOException{
			FileWriter writer;
			writer = new FileWriter(fileName);
			writer.write(stringToWrite);
			writer.flush();
			writer.close();
		}
		
		public ArrayList<CVObject> getCVData() throws IOException{
			String jsonString = "";
			jsonString = readFromFile(jsonString);
			convertToCV(jsonString);

			if (allCV == null) {
				allCV = new ArrayList<CVObject>();
			}

			return allCV;

		}
		
		private void convertToCV(String jsonString) {
			Gson gson = new GsonBuilder().registerTypeAdapter(CVObject.class,
					new CVSerializer()).create();
			Type listType = new TypeToken<ArrayList<CVObject>>() {}.getType();
			allCV = gson.fromJson(jsonString, listType);
		}
		
		public ArrayList<JobDescObject> getjobDescData() throws IOException{
			String jsonString = "";
			jsonString = readFromFile(jsonString);
			convertToJobDesc(jsonString);

			if (allJobDesc== null) {
				allJobDesc = new ArrayList<JobDescObject>();
			}

			return allJobDesc;

		}
		
		private void convertToJobDesc(String jsonString) {
			Gson gson = new GsonBuilder().registerTypeAdapter(JobDescObject.class,
					new jobDescSerializer()).create();
			Type listType = new TypeToken<ArrayList<JobDescObject>>() {}.getType();
			allJobDesc = gson.fromJson(jsonString, listType);
		}
		
		
		private String readFromFile(String s) throws IOException {

			BufferedReader br = null;

			String line;
			br = new BufferedReader(new FileReader(cvFile));
			while ((line = br.readLine()) != null) {
				s += line;
			}
			
			br.close();
			
			return s;
		}  
		

}
