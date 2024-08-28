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
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

@Operator(context = Context.CLI)
public class GenericCLIOperator extends CLIOperator implements GenericOperator {

	private static Logger logger = Logger.getLogger( GenericCLIOperator.class.getName());
	
	@Override
	public long executeCommand (String command) {
	
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info(host.getIp()+host);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();		
		String output = cLICommandHelper.simpleExec(command); 
		
		int exitCode = cLICommandHelper.getShellExitValue();
		System.out.println("output = "+output);
		System.out.println("===== exitCode ===== "+exitCode);
        return exitCode;
	}
	
	
	
	@Override
	public void deployFiletoServer(String scriptFileToDeployName,String remoteFileLocation) throws FileNotFoundException {
	
		Host host = DataHandler.getHostByType(HostType.RC);
        logger.info("*******************"+host+"*******************");
        logger.info(host.getIp()+host);
        CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		cLICommandHelper.simpleExec("mkdir -p "+remoteFileLocation);
		remoteFileLocation = remoteFileLocation+"/";
        RemoteFileHandler remote = new RemoteFileHandler(host);
        List<String> fileLocation = FileFinder.findFile(scriptFileToDeployName);
        System.out.println(fileLocation.get(0));
        remote.copyLocalFileToRemote(fileLocation.get(0) ,remoteFileLocation);
        logger.debug("Copying " +scriptFileToDeployName+ " to " +remoteFileLocation+ " on remote host");
        //executeCommand("dos2unix /home/nmsadm/script/Pre_check.sh");
        String remoteFileName = remoteFileLocation+scriptFileToDeployName;
        
 		 cLICommandHelper.simpleExec("dos2unix "+ remoteFileName +" "+remoteFileName); 
 		 cLICommandHelper.simpleExec("chmod 777 "+remoteFileName);
        
	}
	
	@Override
	public int makeMAFOffline(){
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
		
		
		return 0;
		
	}
	
	@Override
	public int makeMAFOnline(){
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
			cLICommandHelper.simpleExec("sleep 150");
			return 0;
		}
	}
	

}

