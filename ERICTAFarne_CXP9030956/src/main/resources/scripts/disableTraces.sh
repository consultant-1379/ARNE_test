#!/bin/sh
SMTOOL_CMD=/opt/ericsson/nms_cif_sm/bin/smtool
smtool -trace stop
process_id=`ps -eaf | grep LogBackUp | grep -v grep | awk '{print $2}'`
echo "Disabling Traces"
if [ $? -eq "0" ]; then
kill -9 $process_id
fi
