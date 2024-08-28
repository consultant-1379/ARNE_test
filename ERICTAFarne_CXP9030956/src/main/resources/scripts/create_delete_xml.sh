#!/bin/ksh

#
#
# Created: May 2008
# Author: EDENHER
#
# Usage ./create_delete_xml.sh <xml filename>
#
# Example: ./create_delete_xml.sh /opt/ericsson/arne/etc/sample_import_create.xml
#

GREP=/usr/bin/grep
HEAD=/usr/bin/head
SED=/usr/bin/sed
CUT=/usr/bin/cut
NE_C_DATA=/tmp/ne_create_data
CAT=/usr/bin/cat
WC=/usr/bin/wc
TAIL=/usr/bin/tail
HEAD=/usr/bin/head
AWK=/usr/bin/awk
RM=/usr/bin/rm
ADDSTRING='"/>'
XML=$1
DELETE_XML=`echo $XML | $CUT -f1 -d'.'`_delete.xml


if [ -e /opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest ]; then

        CSTEST=/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest

elif [ -e /opt/ericsson/nms_cif_unsupported/nms_cif_cs/bin/cstest ]; then

        CSTEST=/opt/ericsson/nms_cif_unsupported/nms_cif_cs/bin/cstest
else

echo "cstest does not exist"
exit

fi

echo ""
echo "--------------------------------------------------------------------------"
echo ""
echo " NOTE: THE CREATE XML MUST BE IMPORTED BEFORE RUNNING THIS SCRIPT"
echo ""
echo "--------------------------------------------------------------------------"
echo ""



if [ -e /var/tmp/create_delete ]; then

        $RM -r /var/tmp/create_delete

fi

if [ -e $DELETE_XML ]; then

        echo ""
        echo "ERROR"
        echo "A delete xml file, $DELETE_XML already exists"
        echo ""
        exit 10
fi

mkdir /var/tmp/create_delete

grep 'ManagedElementId' $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/ne_list.txt
grep "<name" $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/service_list.txt
grep "<SubNetwork" $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/sub_list.txt
grep "<Group" $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/group_list.txt
grep "<Site" $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/site_list.txt
grep "<FtpServerId" $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/server_list.txt
grep "<ManagementNodeId" $XML | $CUT -f2 -d'"' >> /var/tmp/create_delete/mgtnode_list.txt

$CSTEST -s ONRM_CS -ns masterservice lt ManagedElement > /var/tmp/create_delete/cs_ne_list.txt
for ne_id in `cat /var/tmp/create_delete/ne_list.txt`
do
    grep "t=$ne_id" /var/tmp/create_delete/cs_ne_list.txt | grep -w $ne_id >> /var/tmp/create_delete/fdn_list.txt
done

$CSTEST -s ONRM_CS -ns masterservice  lt Group > /var/tmp/create_delete/cs_group_list.txt
for group_id in `cat /var/tmp/create_delete/group_list.txt`
do
    grep -w $group_id /var/tmp/create_delete/cs_group_list.txt >> /var/tmp/create_delete/fdn_list.txt
done

$CSTEST -s ONRM_CS -ns masterservice  lt SubNetwork > /var/tmp/create_delete/cs_sub_list.txt
for sub_id in `cat /var/tmp/create_delete/sub_list.txt`
do
    grep -w $sub_id /var/tmp/create_delete/cs_sub_list.txt >> /var/tmp/create_delete/fdn_list.txt
done

$CSTEST -s ONRM_CS -ns masterservice  lt FtpService > /var/tmp/create_delete/cs_service_list.txt
for service_id in `cat /var/tmp/create_delete/service_list.txt`
do
    grep -w $service_id /var/tmp/create_delete/cs_service_list.txt >> /var/tmp/create_delete/fdn_list.txt
done

$CSTEST -s ONRM_CS -ns masterservice  lt FtpServer > /var/tmp/create_delete/cs_server_list.txt
for server_id in `cat /var/tmp/create_delete/server_list.txt`
do
    grep -w $server_id /var/tmp/create_delete/cs_server_list.txt >> /var/tmp/create_delete/fdn_list.txt
done

$CSTEST -s ONRM_CS -ns masterservice  lt Site > /var/tmp/create_delete/cs_site_list.txt
for site_id in `cat /var/tmp/create_delete/site_list.txt`
do
    grep -w $site_id /var/tmp/create_delete/cs_site_list.txt >> /var/tmp/create_delete/fdn_list.txt
done

$CSTEST -s ONRM_CS -ns masterservice  lt ManagementNode > /var/tmp/create_delete/cs_mgtnode_list.txt
for mgtnode_id in `cat /var/tmp/create_delete/mgtnode_list.txt`
do
    grep -w $mgtnode_id /var/tmp/create_delete/cs_mgtnode_list.txt >> /var/tmp/create_delete/fdn_list.txt
done






#--------------------------
# Create delete xml file
#--------------------------

echo '<?xml version="1.0" encoding="UTF-8" standalone="no"?>' > $DELETE_XML
echo '<!DOCTYPE Model SYSTEM "/opt/ericsson/arne/etc/arne10_0.dtd">' >> $DELETE_XML
echo '<Model version="1" importVersion="10.0">' >> $DELETE_XML
echo '<Delete>' >> $DELETE_XML

for fdn in `cat /var/tmp/create_delete/fdn_list.txt`
do
echo '<Object fdn="'$fdn$ADDSTRING >> $DELETE_XML
done

echo '</Delete>' >> $DELETE_XML
echo '</Model>' >> $DELETE_XML


echo ""
echo "---------------------------------------------------------------------------------"
echo ""
echo "The delete xml file, $DELETE_XML has been generated"
echo ""
echo "NOTE: IF A RNS EXISTS IN THE CREATE XML, THEN THE DELETE ORDER MUST BE CHANGED"
echo ""
echo "---------------------------------------------------------------------------------"
echo ""

