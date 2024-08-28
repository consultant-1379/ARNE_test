package com.ericsson.oss.arne.test.operators;

import java.io.FileNotFoundException;
public interface FtpImportOperator {
	
	 /**
	    * Create {@link Shell} and execute the command on it<br />
	    * Command will be like a single command, or a list of commands that can be executed one after the other
	    *
	    * @param commands executed commands
	    * @return new shell object, representing the shell result of the executed command
	    */
	long executeCommand(String ImportXML);
	/**
     * Send a file to a remote server
     * @param hostname
     */
	void deployFiletoServer(String importXml,String pathinServer) throws FileNotFoundException;
	int makeMAFOffline();
	int makeMAFOnline();
}


