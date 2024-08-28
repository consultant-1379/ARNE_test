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
public class StartAdjustCliOperator extends CLIOperator implements StartAdjustOperator {

	Logger logger = Logger.getLogger(StartAdjustCliOperator.class);
	
	@Override
	public int executeImportwithMAFOffline (String importXML) {
	
			
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		String offlineMAF = "/opt/ericsson/nms_cif_sm/bin/smtool -offline MAF -reason=other -reasontext=test";
		logger.info("Executing ......................." +offlineMAF);
		cLICommandHelper.simpleExec(offlineMAF);
		String progress = cLICommandHelper.simpleExec("/opt/ericsson/nms_cif_sm/bin/smtool -p");
		logger.info(progress);
		String MAFstatus = cLICommandHelper.simpleExec("/opt/ericsson/nms_cif_sm/bin/smtool -l | grep MAF");
		logger.info(MAFstatus);
		if (!(MAFstatus.contains("offline"))){
			logger.error("MAF not offline");
			return 1;
		}	
		else {
			
			String command = getCommand(importXML);
			logger.info("Executing ****  "+command+"  ****");		
			String importResponse = cLICommandHelper.simpleExec(command);
			logger.info(importResponse);
		
			return 0;
		}
	}
	
	public int executeStartAdjustwithMAFOnline() {
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		String onlineMAF = "/opt/ericsson/nms_cif_sm/bin/smtool -online MAF";
		logger.info("Executing ......................." +onlineMAF);
		cLICommandHelper.simpleExec(onlineMAF);
		String progress = cLICommandHelper.simpleExec("/opt/ericsson/nms_cif_sm/bin/smtool -p");
		logger.info(progress);
		String MAFstatus = cLICommandHelper.simpleExec("/opt/ericsson/nms_cif_sm/bin/smtool -l | grep MAF");
		logger.info(MAFstatus);
		if (!(MAFstatus.contains("started"))){
			logger.error("MAF not online");
			return 1;
		}
		else {
			
			String command = "/opt/ericsson/fwSysConf/bin/startAdjust.sh";
			logger.info("Executing ****  "+command+"  ****");		
			String startAdjustResponse = cLICommandHelper.simpleExec(command);
			logger.info(startAdjustResponse);
		    if (startAdjustResponse.contains("Adjust Completed.")) {
		    	return 0;
		    }
		    else{
		    	return 1;
		    }
		}
	}
	
	public int checkSeg_masterservice_CS_for_importedNodes(){
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();

		String MEs_output = cLICommandHelper.simpleExec("grep \"<ManagedElementId string=\" Create_WRAN_nodes.xml");
		
		String[] MEs = MEs_output.split("\\r?\\n");
		
		int ME_length = MEs.length;
		String[] ME = new String[ME_length];
		
		for (int index_me = 0; index_me < ME_length; index_me ++){
			
			ME[index_me]=MEs[index_me].substring(MEs[index_me].indexOf("\"")+1,MEs[index_me].lastIndexOf("\"")-1);
			logger.info(ME[index_me]);
		}
				
		String segCSResponse = cLICommandHelper.simpleExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ManagedElement");
		logger.info(segCSResponse);
		int count = 0;
		for (int index=0; index<ME_length; index++ ){
			String MEname = ME[index];
			logger.info("Searching .............................."+ MEname);
			if (segCSResponse.contains(MEname)){
				logger.info("Managed Element added to segmasterCS");
				count++;
			}
		}
		logger.info("Count "+count);
		if (count == ME_length)
		{
			return 0;
		}
		else{
			return 1;
		}
				
	}
	
public int checkSeg_masterservice_CS_for_deletedNodes(){
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
			
		String MEs_output1 = cLICommandHelper.simpleExec("grep ManagedElement Create_WRAN_nodes_delete.xml");
		logger.info("put_--------------"+MEs_output1);
		String[] MEs1 = MEs_output1.split("/>");
		
		int ME_length1 = MEs1.length;
		String[] ME1 = new String[ME_length1];
		
		for (int index_me = 0; index_me < ME_length1-1; index_me ++){
			
			logger.info("Index of ME "+MEs1[index_me]);
			logger.info("Index is "+MEs1[index_me].indexOf("ManagedElement="));
			logger.info("Last index is "+MEs1[index_me].lastIndexOf("\n"));
			if(MEs1[index_me].contains("MeContext"))
				ME1[index_me]=MEs1[index_me].substring(MEs1[index_me].indexOf("MeContext=")+10,MEs1[index_me].lastIndexOf("ManagedElement")-1);
			else
				ME1[index_me]=MEs1[index_me].substring(MEs1[index_me].indexOf("ManagedElement=")+15,MEs1[index_me].lastIndexOf("\""));
			logger.info(ME1[index_me]);
		}
		
		String segCSResponse = cLICommandHelper.simpleExec("/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt ManagedElement");
		logger.info(segCSResponse);
		int count = 0;
		for (int index=0; index < ME_length1-1; index++ ){
			String MEname = ME1[index];
			logger.info("Searching .............................."+ MEname);
			if (!(segCSResponse.contains(MEname))){
				logger.info("Managed Element deleted from segmasterCS");
				count++;
			}
		}
		logger.info("Count "+count);	
		if (count == (ME_length1-1))
		{
			return 0;
		}
		else{
			return 1;
		}		
	}
	
	public String getCommand(String ImportXml) {
		
		String ImportXML = ImportXml;
		String command = "/opt/ericsson/arne/bin/import.sh -f " + ImportXML + " -import -i_nau" ; 
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
	
    public Map<String, String> get(String step) {
        return loadData("StartAdjust_CliTestData.csv", step);
    }

	

}

