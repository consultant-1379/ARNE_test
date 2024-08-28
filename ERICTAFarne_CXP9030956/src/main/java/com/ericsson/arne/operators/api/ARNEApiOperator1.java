package com.ericsson.arne.operators.api;


import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.arne.data.ARNEDataProvider;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;


public class ARNEApiOperator1 implements ApiOperator{
	ARNEDataProvider arnedataProvider = new ARNEDataProvider();

	public boolean operate(Host host){
		RemoteFileHandler remoteFileHandler =  new RemoteFileHandler(host);
		SshRemoteCommandExecutor SSH = new SshRemoteCommandExecutor(host);
		String commandsOutput;
		System.out.println("******************************* COMMAND CREATION START *********************************");
		System.out.println("triggering command Execution in server ...");
		commandsOutput = SSH.simplExec("pwd");
		System.out.println(commandsOutput);
		commandsOutput = SSH.simplExec("hostname");
		System.out.println(commandsOutput);
		//commandsOutput = SSH.simplExec("telnet ossmaster");
		//System.out.println(commandsOutput);
		//commandsOutput = SSH.simplExec("su - nmsadm -c  \"cd /home/nmsadm;/opt/ericsson/atoss/tas/PF_SAPP-FWK/CL/arne/tests/Acceptance_Tests/bin/run_arne_import_test.pl FeatureTest/Import-Manual/create_2NE_Same_IP_pass -v \"");
		commandsOutput = SSH.simplExec("su - nmsadm -c  \"cd /;./vidya1.sh\"");
		//commandsOutput = SSH.simplExec("su - nmsadm -c  \"cd /home/nmsadm/arne/logs;ls -lrt |tail -1\"");
		System.out.println(commandsOutput);
		System.out.println("------------------------------------------");
		System.out.println("command Execution in server done !!");
		System.out.println("******************************** COMMAND CREATION END ********************************");
		return true;
	}

}