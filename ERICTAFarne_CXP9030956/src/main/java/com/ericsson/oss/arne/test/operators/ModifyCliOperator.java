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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

@Operator(context = Context.CLI)
public class ModifyCliOperator extends CLIOperator implements ModifyOperator {

	private static Logger logger = Logger.getLogger( ModifyCliOperator.class);
	
	@Override
	public int executeCommand (String modifyXML) {
	
			
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();		
		String command = getCommand(modifyXML);
		logger.info("Executing ****  "+command+"  ****");		
		String importResponse = cLICommandHelper.simpleExec(command);
		int exitCode = cLICommandHelper.getShellExitValue();
		logger.info(importResponse);
		
		return exitCode;
		
		
		
	}
	
	public String getCommand(String ImportXml) {
		
		String ImportXML = ImportXml;
		String command = "/opt/ericsson/arne/bin/import.sh -f " + ImportXML + " -import" ; 
		return command;
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

		if(!importResponse.contains("Import Finished")){
			logger.info("Setup completed successfully");
			
		}
		
		return exitCode;		
	}
	
	public int executeTearDown (String ImportXML, String ModifyXML, String DeleteXML) {
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		
		String command = "/opt/ericsson/arne/bin/import.sh -f " +DeleteXML+ " -import -i_nau";
		logger.info("Executing Teardown****  "+command+"  ****");
		
		String importResponse = cLICommandHelper.simpleExec(command);
		int exitCode = cLICommandHelper.getShellExitValue();
		logger.info(importResponse);
		
		String remFile = "rm -rf "+ ImportXML+" "+ModifyXML+" "+DeleteXML;
		cLICommandHelper.simpleExec(remFile);

		if(!importResponse.contains("Import Finished")){
			logger.info("Teardown completed successfully");
			
		}
		
		return exitCode;		
	}
	
    public Map<String, String> get(String step) {
        return loadData("Modify_CliTestData.csv", step);
    }


}