package com.ericsson.oss.arne.test.operators;

public interface StartAdjustOperator {

	int executeImportwithMAFOffline(String importXML);
	int executeStartAdjustwithMAFOnline();
	int checkSeg_masterservice_CS_for_importedNodes();
	int checkSeg_masterservice_CS_for_deletedNodes();
	String getCommand(String ImportXml);
	void deployXMLFiletoServer(String fileToDeployName,String remoteFileLocation);
	
}


