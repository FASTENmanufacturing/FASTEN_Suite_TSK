package com.fasten.wp4.optimizator.tactical.simulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;


public class JSONInterface {
	
	
	public JSONInterface(){
		
	}
	
	public void writeSIMResultsJSONFile(SupplyChain sc) throws JsonIOException, IOException {
		Gson gson = new Gson();

		int blockSize = 2048;
		
		// Java object to JSON, and assign to a String
		String jsonInString = gson.toJson(sc);
		
		// Write JSON String to a JSON file
		// jsonInString is so big that can't be written all at the same time, so it needs to be divided in blockSize
		FileWriter  fos = new FileWriter ("SC_SIM_Results.json");
		try(BufferedWriter  outStream = new BufferedWriter (fos)) {
			for(int i=0; i<jsonInString.length() ; i+=blockSize) {
				outStream.write(jsonInString.substring(i,Math.min(jsonInString.length(), i+blockSize)));
			}
		}
	}
}
