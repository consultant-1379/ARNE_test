package com.ericsson.oss.arne.test.operators;

public interface ImportOperator {
	long executeCommand(String ImportXML);
	String getCommand(String ImportXml);
	long calcExecutionTime(String sDate, String eDate);
	void deployXMLFiletoServer(String importXml,String pathinServer);
	int executeTearDown (String deleteXML, String importXML);
}


