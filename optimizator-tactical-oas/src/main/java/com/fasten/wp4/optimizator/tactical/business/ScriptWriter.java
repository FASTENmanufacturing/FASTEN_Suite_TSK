package com.fasten.wp4.optimizator.tactical.business;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public class ScriptWriter {
	
	public static void createScript(String requestId, String pathOplrun) throws Exception {
		
		
		//creating script dynamically
		String line1 = "cd \""+pathOplrun +"\"";
//		String line2 = "oplrun \""+FileUtils.getAbsolutePath("p_median.mod") +"\" \""+FileUtils.getAbsolutePath("p_median.dat")+"\"";
		String line2 = "oplrun \"p_median.mod\" \"p_median.dat\"";
//		String line2 = "oplrun \""+resourcesDirectory.getPath()+"/p_median.mod" +"\" \""+resourcesDirectory.getPath()+"/p_median.dat"+"\"";
//		String line3 = "oplrun \""+FileUtils.getAbsolutePath("TK_v9.mod") +"\" \""+FileUtils.getAbsolutePath("TK_v9.dat")+"\"";
		String line3 = "oplrun \"TK_v9.mod\" \"TK_v9.dat\"";
//		String line3 = "oplrun \""+resourcesDirectory.getPath()+"/TK_v9.mod" +"\" \""+resourcesDirectory.getPath()+"/TK_v9.dat"+"\"";
//		String line4 = "Start \"\"  \""+FileUtils.getAbsolutePath("SimulationModel.spfx") +"\"";
//		String line4 = "Start \"\"  \"SimulationModel.spfx\"";
//		String line4 = "Start \"\"  \""+resourcesDirectory.getPath()+"/SimulationModel.spfx" +"\"";
		String line5 = "echo end";
		
		
		
		Collection<String> scriptContent = new ArrayList<String>();
//		scriptContent.add(line1);
		scriptContent.add(line2);
		scriptContent.add(line3);
//		scriptContent.add(line4);
		scriptContent.add(line5);
		
		Files.write(Paths.get("studies",requestId,"script.txt"), scriptContent, StandardCharsets.UTF_8);
		Files.write(Paths.get("studies",requestId,"script.bat"), scriptContent, StandardCharsets.UTF_8);
		
	}

}
