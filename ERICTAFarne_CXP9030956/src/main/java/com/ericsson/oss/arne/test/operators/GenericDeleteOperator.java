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
import java.util.ArrayList;

import com.ericsson.cifwk.taf.tools.cli.Shell;

public interface GenericDeleteOperator {
	
	 void initialize();
    /**
     * Send a file to a remote server
     * @param hostname
     */
    void sendFileRemotely(String fileName, String remoteFileLocation) throws FileNotFoundException;

    /**
     * Delete a file from remote server
     * @param hostname
     */
    void deleteRemoteFile(String fileName, String remoteFileLocation) throws FileNotFoundException;

    
    /**
    * Create {@link Shell} and execute the command on it<br />
    * Command will be like a single command, or a list of commands that can be executed one after the other
    *
    * @param commands executed commands
    * @return new shell object, representing the shell result of the executed command
    */
    Shell executeCommand(String... commands);
    
    String executeCommandAndGetOuput(String commands);

}
