package com.fasten.wp4.optimizator.tactical.util;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {

	public static String getAbsolutePath(String fileName) throws Exception  {
		URL res = FileUtils.class.getClassLoader().getResource(fileName);
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}
}
