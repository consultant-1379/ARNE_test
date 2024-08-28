package com.ericsson.oss.arne.test.operators;

public interface DeltaModelGenerationOperator {
	int executeDeltaModelGeneration(String localXML,String peerXML);
	void deployXMLFiletoServer(String fileToDeployName,String remoteFileLocation);
	void executeTeardown();
	
}


