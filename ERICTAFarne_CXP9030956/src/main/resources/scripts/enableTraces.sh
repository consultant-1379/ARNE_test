#!/bin/ksh
TRACE_FILE_PATH=/var/opt/ericsson/arne/ARNE_KGB/logs
SMTOOL_CMD=/opt/ericsson/nms_cif_sm/bin/smtool
#sleep 180
echo "Stopping All Traces...."
$SMTOOL_CMD -trace stop
mkdir -p $TRACE_FILE_PATH
LOG_BACK_UP=/var/opt/ericsson/arne/ARNE_KGB/LogBackUp.sh
TODAY=`date +%Y-%m-%d.%H:%M:%S`
echo $TODAY
LOGTIMEDIR=$TRACE_FILE_PATH/ARNE_$TODAY
mkdir -p $LOGTIMEDIR
echo "###############Start trace##################"
$LOG_BACK_UP /var/opt/ericsson/log/trace/ARNE_MAF_CREATE.txt.bak $LOGTIMEDIR &

$SMTOOL_CMD -trace ARNEServer 0-199 ARNE_MAF_CREATE.txt.bak
$SMTOOL_CMD -trace MAF 0-199 ARNE_MAF_CREATE.txt.bak

