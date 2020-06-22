package com.fasten.wp4;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasten.wp4.optimizator.tactical.util.UnzipUtility;

import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

@Component
public class AfterApplicationStartup {

	@Value("${HOME}")
	private String home;

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private String destDirectory=null;
	
	private void loadJNIFromZip(String zipFileName) throws Exception {
		LOGGER.info("Setup CPLEX JNI files for this Operational System");
		InputStream cplexJNIZipFile = this.getClass().getClassLoader().getResourceAsStream(Paths.get("cplex",zipFileName).toString());
		File tempZipJNIFile = File.createTempFile(zipFileName.replace(".zip", ""), ".zip");
		LOGGER.info("Copy files from: "+ Paths.get("cplex",zipFileName).toString() + " to temporary folder " + tempZipJNIFile.getAbsolutePath());
		Files.copy(cplexJNIZipFile, Paths.get(tempZipJNIFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
		
		LOGGER.info("Unzip from: " + tempZipJNIFile.getAbsolutePath()+ " to " + tempZipJNIFile.getAbsolutePath().replace(".zip", ""));
		String zipFilePath = tempZipJNIFile.getAbsolutePath();
        String destDirectory = tempZipJNIFile.getAbsolutePath().replace(".zip", "");
        
        UnzipUtility unzipper = new UnzipUtility();
        unzipper.unzip(zipFilePath, destDirectory);
        
        LOGGER.info("Deleted zip file from: " + tempZipJNIFile.getAbsolutePath());
        Files.delete(Paths.get(tempZipJNIFile.getAbsolutePath()));
        
        LOGGER.info("Loading JNI CPLEX: -Djava.library.path="+destDirectory);
        addLibraryPath(destDirectory);
        
        this.destDirectory=destDirectory;
        System.out.println(destDirectory);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doAfterStartup() throws Exception  {
		
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
		
		String operSys = System.getProperty("os.name").toLowerCase();
		
		//WINDOWS
		if (operSys.contains("win")) {
			String zipFileName = "x64_win64.zip";
			loadJNIFromZip(zipFileName);
			//LINUX
		} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
			String zipFileName = "x86-64_linux.zip";
			loadJNIFromZip(zipFileName);
	        
			//MAC
		} else if (operSys.contains("mac")) {
			String zipFileName = "x86-64_osx.zip";
			loadJNIFromZip(zipFileName);
			
			//SOLARIS
		} else if (operSys.contains("sunos")) {
			LOGGER.info("No JNI CPLEX for this system");
		}
	}
	
	@EventListener(ContextClosedEvent.class)
	public void doBeforeShutdown() throws Exception  {
		FileUtils.forceDelete(new File(destDirectory));
        LOGGER.info("Deleted temp CPLEX folder from: " + destDirectory);
	}

	//http://fahdshariff.blogspot.com/2011/08/changing-java-library-path-at-runtime.html
	public void addLibraryPath(String pathToAdd) throws Exception{
		final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		//get array of paths
		final String[] paths = (String[])usrPathsField.get(null);

		//check if the path to add is already present
		for(String path : paths) {
			if(path.equals(pathToAdd)) {
				return;
			}
		}

		//add the new path
		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length-1] = pathToAdd;
		usrPathsField.set(null, newPaths);
	}



}
