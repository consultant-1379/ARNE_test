package com.ericsson.oss.arne.test.operators;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.HostType;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.CLIOperator;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.cifwk.taf.tools.cli.TimeoutException;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.sut.test.operators.ARNE_ImportCliOperator;
import com.ericsson.cifwk.taf.tools.cli.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

@Operator(context = Context.CLI)
public class ExportCliOperator extends CLIOperator implements ExportOperator {

	private CLI cli;
	private Shell shell;
	User arneUser;
		
	private static Logger logger = Logger.getLogger( ExportCliOperator.class.getName());
	@Override
	public long executeCommand() throws Exception {
		
		
		Host host = DataHandler.getHostByType(HostType.RC);
		logger.info("host is" + host);
		logger.info("host IP is " + host.getIp() + "host username is " +host.getUser() + "host password is "+ host.getPass());
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		String startTime = cLICommandHelper.simpleExec("date '+%m/%d/%y %H:%M:%S'");
	    String command = getCommand();
		logger.info("Executing ****  "+command+"  ****");
		String exportResponse = cLICommandHelper.simpleExec(command);
		int exitCode = cLICommandHelper.getShellExitValue();
		String endTime = cLICommandHelper.simpleExec("date '+%m/%d/%y %H:%M:%S'");
		logger.info(exportResponse);
		logger.info("Starttime"+startTime+"& EndTime" +endTime);
		long executionTime = calcExecutionTime(startTime,endTime);
		logger.info("Execution Time +++++++++++++++++++" + executionTime);
		logger.info("*************Exit Code is : " +exitCode);
		
		if(exitCode != 0){
			if(!exportResponse.contains("Export Complete")){
				logger.info("Export not completed successfully");
				executionTime = -1;
			}
		}
		
		return executionTime;
		
	}
	
	public String getCommand() {
		
		String command ="/opt/ericsson/arne/bin/export.sh -f export_all_nodes.xml -o"; 
		return command;
			}
	
public int getNumberofNodesInXML(){
		
		Host host = DataHandler.getHostByType(HostType.RC);
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		String  noOfNodes = cLICommandHelper.simpleExec("grep \"</ManagedElement>\" export_all_nodes.xml | wc -l");
		String x=noOfNodes.trim();
		logger.info("************Number of nodes in xml*********"+x);
		int numberOfNodes = Integer.parseInt(x);
		return numberOfNodes;
	}
	
public String calcToAcutalTime( long sTime) {
	long millis = sTime * 1000;
	String format = String.format("%02d:%02d:%02d",
			TimeUnit.MILLISECONDS.toHours(millis),
			TimeUnit.MILLISECONDS.toMinutes(millis)
					- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
							.toHours(millis)),
			TimeUnit.MILLISECONDS.toSeconds(millis)
					- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
							.toMinutes(millis)));
	return format;
}

	public int getNumberofNodesFromCS(){
		
		Host host = DataHandler.getHostByType(HostType.RC);	
		CLICommandHelper cLICommandHelper = new CLICommandHelper(host);
		cLICommandHelper.openShell();
		String noOfNodes = cLICommandHelper.simpleExec(" /opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s ONRM_CS lt ManagedElement | wc -l");
		String x = noOfNodes.trim();		
		logger.info("************Number of nodes in CS********* "+ x);
		int numberOfNodes = Integer.parseInt(x);
		return numberOfNodes;
	}
	
	public long calcExecutionTime(String sDate, String eDate) {
		
		String sdate = sDate;
		String edate = eDate;
		long diffSeconds = 0;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		try {
			Date d1 = format.parse(sDate);
			Date d2 = format.parse(eDate);
			long diff = d2.getTime() - d1.getTime();
			diffSeconds = diff / 1000;
			
		}catch (ParseException e) {
			
			logger.error("Couldn't format dates correctly");
			logger.error("Provided Start Date: " + sDate);
			logger.error("Provided End Date: " + eDate);
			diffSeconds = -1;
		}
		return diffSeconds;
	}
}

