package com.ericsson.oss.arne.test.operators;

public interface ModifyOperator {
	int executeCommand (String modifyXML);
	String getCommand(String ImportXml);
	void deployXMLFiletoServer(String fileToDeployName,String remoteFileLocation);
	int executeSetUp (String ImportXML);
	int executeTearDown (String ImportXML, String ModifyXML, String DeleteXML);
	
}


