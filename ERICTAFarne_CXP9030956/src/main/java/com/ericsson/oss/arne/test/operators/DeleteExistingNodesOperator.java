/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.arne.test.operators;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.HostType;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.*;
import com.ericsson.cifwk.taf.utils.FileFinder;


@Operator(context = Context.CLI)
public class DeleteExistingNodesOperator extends CLIOperator implements GenericDeleteOperator{
	
	 private CLICommandHelper cLICommandHelper;
     Logger logger = Logger.getLogger(CLIOperator.class);
     
     @Override
     public void initialize() {
    	 Host host = DataHandler.getHostByType(HostType.RC);
 		logger.info(host.getIp()+host);
 		cLICommandHelper = new CLICommandHelper(host);
 		cLICommandHelper.openShell();
                              
     }
	 
     @Override
     public void sendFileRemotely(String fileName, String fileServerLocation) throws FileNotFoundException {
             Host host = DataHandler.getHostByType(HostType.RC);
             logger.info("*******************"+host+"*******************");
             logger.info(host.getIp()+host);
             RemoteFileHandler remote = new RemoteFileHandler(host);
             List<String> fileLocation = FileFinder.findFile(fileName);
             System.out.println(fileLocation.get(0));
             String remoteFileLocation = fileServerLocation;         //unix address
             remote.copyLocalFileToRemote(fileLocation.get(0) ,remoteFileLocation);
             logger.debug("Copying " +fileName+ " to " +remoteFileLocation+ " on remote host");
             //executeCommand("dos2unix /home/nmsadm/script/Pre_check.sh");
             String remoteFileName = remoteFileLocation+fileName;
             cLICommandHelper = new CLICommandHelper(host);
      		 cLICommandHelper.openShell();
      		 cLICommandHelper.simpleExec("dos2unix "+ remoteFileName +" "+remoteFileName); 
      		 cLICommandHelper.simpleExec("chmod 777 "+remoteFileName);

     }

     @Override
     public void deleteRemoteFile(String fileName, String fileServerLocation) throws FileNotFoundException {
     //      Host host = DataHandler.getHostByName(hostname);
             Host host = DataHandler.getHostByType(HostType.RC);
             RemoteFileHandler remoteFileHandler = new RemoteFileHandler(host);
             String remoteFileLocation = fileServerLocation;
             remoteFileHandler.deleteRemoteFile(remoteFileLocation+fileName);
             logger.debug("deleting " +fileName+ " at location " +remoteFileLocation+ " on remote host");
     }

     @Override
     public Shell executeCommand(String... commands) {
                             return null;
     }
	
     @Override
     public String executeCommandAndGetOuput(String commands) {
             //Shell shell1 = executeCommand("dos2unix Pre_check.sh Pre_check.sh");
    	 Host host = DataHandler.getHostByType(HostType.RC);
    	 cLICommandHelper = new CLICommandHelper(host);
  		 cLICommandHelper.openShell();
  		String output = cLICommandHelper.simpleExec(commands); 
            /* Shell shell = executeCommand(commands);
             String output = shell.read();
             shell.disconnect();*/
             return output;
     }

}
