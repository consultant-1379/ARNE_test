package com.ericsson.oss.arne.test.operators;

public interface DeleteOperator {
	long executeCommand(String DeleteXML);
	String getCommand(String ImportXml);
	long calcExecutionTime(String sDate, String eDate);
	void deployXMLFiletoServer(String fileToDeployName,String remoteFileLocation);
	int executeSetUp (String ImportXML);
	void executeTeardown(String importXML, String deleteXML);
}


