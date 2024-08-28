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
public class DeltaModelGenerationCliOperator extends CLIOperator implements DeltaModelGenerationOperator {

	Logger logger = Logger.getLogger(DeltaModelGenerationCliOperator.class);
	
	@Override
	public int executeDeltaModelGeneration(String localXML,String peerXML){
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		cLICommandHelper.simpleExec("mkdir delta_generation");
		
		String command = "/opt/ericsson/arne/modeltrans/bin/generateModelDeltaFiles.sh -localModel "+localXML+" -peerModel "+peerXML+" -outputDir /delta_generation/"; 
		logger.info("Executing command ---------" + command);
		cLICommandHelper.simpleExec(command);
		
		String checkForDeltaFile = cLICommandHelper.simpleExec("ls delta_generation");
		
		if(localXML.contains("create")){
			if (checkForDeltaFile.contains("addToNetworkModel.xml")	){
				logger.info("Generated delta file addToNetworkModel.xml");
				return 0;
			}
			else{
				logger.error("Delta file not generated");
				return 1;
			}
		}
		else if(localXML.contains("delete")){
			if (checkForDeltaFile.contains("deleteFromNetworkModel.xml")	){
				logger.info("Generated delta file deleteFromNetworkModel.xml");
				return 0;
			}
			else{
				logger.error("Delta file not generated");
				return 1;
			}
		}
		else if (localXML.contains("modify")){
			if (checkForDeltaFile.contains("modifyNetworkModel.xml")	){
				logger.info("Generated delta file modifyNetworkModel.xml");
				return 0;
			}
			else{
				logger.error("Delta file not generated");
				return 1;
			}
		}
		else{
			return 1;
		}
		
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
	
	public void executeTeardown(){
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		cLICommandHelper.simpleExec("rm -rf delta_generation");
	}

    public Map<String, String> get(String step) {
        return loadData("DeltaModelGeneration_CliTestData.csv", step);
    }

}

