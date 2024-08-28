package com.ericsson.oss.arne.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.HostType;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.taf.tools.cli.CLIOperator;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.cifwk.taf.utils.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

@Operator(context = Context.CLI)
public class DeleteCliOperator extends CLIOperator implements DeleteOperator {
	
	private static Logger logger = Logger.getLogger( DeleteCliOperator.class);
	
	@Override
	public long executeCommand (String ImportXML) {
	
		String ImportXml = ImportXML;
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();		
		String startTime = cLICommandHelper.simpleExec("date '+%m/%d/%y %H:%M:%S'");
		String command = getCommand(ImportXml);
		logger.info("Executing ****  "+command+"  ****");		
		String importResponse = cLICommandHelper.simpleExec(command);
		int exitCode = cLICommandHelper.getShellExitValue();
		String endTime = cLICommandHelper.simpleExec("date '+%m/%d/%y %H:%M:%S'");
		logger.info(importResponse);
		logger.info("Starttime"+startTime+"& EndTime" +endTime);
		long executionTime = calcExecutionTime(startTime,endTime);
		
		logger.info("Execution Time +++++++++++++++++++" + executionTime);
		
		if(exitCode != 0){
			if(!importResponse.contains("No Errors Reported")){
				logger.error("Import not completed successfully");
				executionTime = -1;
			}
		}
		
		return executionTime;
		
	}
	
	public String getCommand(String ImportXml) {
		
		String ImportXML = ImportXml;
		String command = "/opt/ericsson/arne/bin/import.sh -f " + ImportXML + " -import" ; 
		return command;
			}
	
public long calcExecutionTime(String sDate, String eDate) {
		
		long diffSeconds = 0;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		try {
			Date d1 = format.parse(sDate);
			Date d2 = format.parse(eDate);
			long diff = d2.getTime() - d1.getTime();
			logger.info("************************************Time diff in milliseconds"+diff);
			diffSeconds = diff / 1000;
			
		}catch (ParseException e) {
			
			logger.error("Couldn't format dates correctly");
			logger.error("Provided Start Date: " + sDate);
			logger.error("Provided End Date: " + eDate);
			diffSeconds = -1;
		}
		return diffSeconds;
	}

	public void deployXMLFiletoServer(String fileToDeployName,String remoteFileLocation){
	
		Host host = DataHandler.getHostByType(HostType.RC);
		
		logger.info("*******************"+host+"*******************");
		
		RemoteFileHandler remote = new RemoteFileHandler(host);

		logger.info("fileToDeployName"+fileToDeployName);
		logger.info("remoteFileLocation" + remoteFileLocation);
		
		List<String> fileNames = FileFinder.findFile(fileToDeployName);
        if (fileNames.size() == 0)
            fileNames = FileFinder.findFile(fileToDeployName, new File(FileUtils.getCurrentDir()).getParentFile().getAbsolutePath());
        logger.info("File Path ==" +fileNames.get(0));
        String filetoCopy = fileNames.get(0);
        logger.info("Copying the tar file with xml file to the server...");
        boolean fileTransferd = remote.copyLocalFileToRemote(filetoCopy,remoteFileLocation);
        logger.info("File transfered to Remote? :"+fileTransferd);
        
	}


	public int executeSetUp (String ImportXML) {
				
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		
		String command = "/opt/ericsson/arne/bin/import.sh -f " +ImportXML+ " -import -i_nau";
		logger.info("Executing setup****  "+command+"  ****");
		
		String importResponse = cLICommandHelper.simpleExec(command);
		int exitCode = cLICommandHelper.getShellExitValue();
		logger.info(importResponse);

		if(importResponse.contains("No Errors Reported")){
			logger.info("Setup completed successfully");
			
		}
		
		return exitCode;		
	}
	
	public void executeTeardown(String importXML, String deleteXML){
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		
		String remFile = "rm -rf " + importXML + " " + deleteXML;
		cLICommandHelper.simpleExec(remFile);
		logger.info("Teardown executed");
	}

    public Map<String, String> get(String step) {
        return loadData("Delete_CliTestData.csv", step);
    }

}
