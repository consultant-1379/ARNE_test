#!/bin/ksh          

#####################################################################################
#                                                                                   #
#  Name         : Netsim_Delete_Nodes.sh                                                   #
#  Description  : This script will used to delete left behind ManagedElements       #
#                  from vapp recieved from DM catalog                               #
#  Author       : Shivaji Tikare (XSHITIK)                                          #
#  Script Status: public                                                            #
#  Lang         : Shell Scripts                                                     #
#                                                                                   #
#  Version History                                                                  #
#  ===============                                                                  #
#  Version        Date          Author       Description                            #
#  3.0            11/11/2014    XSHITIK      * Initial Draft                        #
#                                                                                   #
#####################################################################################

# set -x 

#Delete directory if already present
DELETE_XML_DIR=/var/tmp/ARNE_DELETE_SCRIPT
if [ -d $DELETE_XML_DIR ]; then
  rm -rf $DELETE_XML_DIR
fi

#Create New directory
mkdir -m 777 $DELETE_XML_DIR


#Check ONRM_CS MC status
if (smtool -list ONRM_CS | grep -v started > /var/tmp/log.log 2>&1);
then
	echo "Exception. ONRM CS is offline or in failed state. Please make MC online"
	exit 1
fi

#Check for number of ManagedElements already exist before deleting
/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s ONRM_CS lt ManagedElement | wc -l > $DELETE_XML_DIR/total_number_of_elements.log
echo "##########Total number of elemnts already exist before deleting#############"
cat $DELETE_XML_DIR/total_number_of_elements.log 


#Export nodes from OSS
echo "##############Export is getting started###############"
/opt/ericsson/arne/bin/export.sh -f $DELETE_XML_DIR/exported_Netsim.xml

if [ "$?" = "0" ]; then
        echo "Export was successful."
else
        echo "Exception. Failed to Export the nodes."
        exit 1 
fi


#Generating Delete XML
echo "#############Generating Delete xml from exported xml###############"
/var/tmp/create_delete_xml.sh $DELETE_XML_DIR/exported_Netsim.xml 
if [ -e /var/tmp/create_delete/fdn_list.txt  ]; then
                echo "Export was successful. Now deleting the nodes. Keep patience." 
else
                echo "Exiting. There are no nodes to delete."
                exit 2 
fi

#Check ARNEServer MC status
if (smtool -list ARNEServer | grep -v started > /var/tmp/log.log 2>&1);
then
        echo "Exception. ARNE Server should be online to start delete import"
        exit 1
fi


#Import Delete XML to delete nodes
echo "################Importing Delete XML###############"
/opt/ericsson/arne/bin/import.sh -f $DELETE_XML_DIR/exported_Netsim_delete.xml -import

if [ "$?" = "0" ]; then
        echo "No Errors reported. Server is fresh to start testing"

else
        echo "Exception. Failed to delete the nodes."
        exit 1
fi

