package com.fasten.wp4.optimizator.tactical.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class FileWriter {

	public static void writeToFile(Collection<String> content, String path) throws IOException {
		 FileOutputStream fos = new FileOutputStream(path);
	        try (OutputStreamWriter osw =  new OutputStreamWriter(fos,StandardCharsets.UTF_8)) {
	        	for (String line : content) {
	        		osw.write(line+"\n");
				}
				osw.flush();
				osw.close();
	        }finally {
	        	fos.flush();
				fos.close();
			}
	}
}
