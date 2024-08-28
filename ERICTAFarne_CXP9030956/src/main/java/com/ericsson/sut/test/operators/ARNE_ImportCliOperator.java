package com.ericsson.sut.test.operators;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.google.inject.Singleton;

import java.util.Map;
import java.io.FileNotFoundException;
import java.util.List;

@Operator(context = Context.CLI)
public class ARNE_ImportCliOperator implements ARNE_ImportOperator {

	private CLI cli;
	private Shell shell;
	
	Logger logger = Logger.getLogger( ARNE_ImportCliOperator.class);
	
	@Override
	public String executeCommand(String hostname, String commandRef, String args) {
		initializeShell(hostname);
		String command = getCommand(commandRef);
		shell.writeln("pwd");
		String pwd = shell.read();
		System.out.println(pwd);
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  "+command+"  ##############################");
		shell.writeln(command);
		String response = shell.read();
		System.out.println(response);
		//shell.writeln("exit");
		synchronized(shell){
			try {
				shell.wait(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		shell.disconnect();
		return response;
	}
	
	public String getCommand(String commandRef) {
		/* 4. Use the commandRef to extract the actual command from a properties file. */
				String command = (String) DataHandler.getAttribute(commandRef);
					System.out.println("##################################  "+command+"  ##############################");
				return command;
			}
	
	private Shell initializeShell(String hostname){
		/* 5. Use the String hostname to construct a Host object which you can pass to the new CLI. */
				Host host = DataHandler.getHostByName(hostname);
			    CLI cli = new CLI(host);
		/* 6. Instantiate a new CLI (assigned at top) passing it the Host object. */
			
				if(shell == null){
					logger.debug("Creating new shell instance");
		/* 7. Get a shell (assigned at top) using the cli. */
					shell = cli.openShell();
					
				}
				return shell;
			}
	
	public void writeln(String command){
		String cmd = getCommand(command);
		logger.trace("Writing " + cmd + " to standard input");
		logger.info("Executing commmand " + cmd);
/* 8. Use the shell to send a command - this is the last step of the exercise. */	
		shell.writeln(command);
	}
	
	public void writeln(String command, String args){
		String cmd = getCommand(command);
		logger.trace("Writing " + cmd + " " + args + " to standard input");
		logger.info("Executing commmand " + cmd + " with args " + args);
		shell.writeln(cmd + " " + args);	
	}

	

	
	public int getExitValue(){
		int exitValue = shell.getExitValue();
		logger.debug("Getting exit value from shell, exit value is :" + exitValue);
		return exitValue;
	}

	
	public String expect(String expectedText) throws TimeoutException{
		logger.debug("Expected return is " +expectedText);
		String found = shell.expect(expectedText);
		return found;
	}

	
	public void expectClose(int timeout) throws TimeoutException{
		shell.expectClose(timeout);
	}

	
	public boolean isClosed() throws TimeoutException{
		return shell.isClosed();
	}

	
	public String checkForNullError(String error){
		if (error == null){
			error = "";
			return error;
		}
		return error;
	}
	
	
	public String getStdOut() {
		String result = shell.read();
		logger.debug("Standard out: " + result);
		return result;
	}

	
	public void disconnect() {
		logger.info("Disconnecting from shell");
		shell.disconnect();
		shell = null;
	}

	
	public void sendFileRemotely(String hostname, String fileName, String fileServerLocation) throws FileNotFoundException {
		Host host = DataHandler.getHostByName(hostname);

		RemoteFileHandler remote = new RemoteFileHandler(host);
		List<String> fileLocation = FileFinder.findFile(fileName);
		String remoteFileLocation = fileServerLocation;         //unix address
		remote.copyLocalFileToRemote(fileLocation.get(0) ,remoteFileLocation);
		logger.debug("Copying " +fileName+ " to " +remoteFileLocation+ " on remote host");

	}

	
	public void deleteRemoteFile(String hostname, String fileName, String fileServerLocation) throws FileNotFoundException {
		Host host = DataHandler.getHostByName(hostname);

		RemoteFileHandler remoteFileHandler = new RemoteFileHandler(host);
		String remoteFileLocation = fileServerLocation;
		remoteFileHandler.deleteRemoteFile(remoteFileLocation+fileName);
		logger.debug("deleting " +fileName+ " at location " +remoteFileLocation+ " on remote host");
	}

	
	public void scriptInput(String message) {
		logger.info("Writing " + message + " to standard in");
		shell.writeln(message);
	}

	
	public Shell executeCommand(String... commands) {
		logger.info("Executing command(s) " + commands);
		return cli.executeCommand(commands);
		
	}
	
   /* public Map<String, String> get(String step) {
        return loadData("ARNE_Import_CliTestData.csv", step);
    }*/

}

