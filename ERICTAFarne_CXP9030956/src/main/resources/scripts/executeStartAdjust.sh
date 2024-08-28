#!/bin/bash
/opt/ericsson/nms_cif_sm/bin/smtool -online MAF
/opt/ericsson/nms_cif_sm/bin/smtool -p
sleep 60
echo "MAF started....."
/opt/ericsson/nms_cif_sm/bin/smtool -trace MAF 0-199 ARNE_MAF_CREATE.txt.bak
/opt/ericsson/nms_cif_sm/bin/smtool -trace cms_snad_reg 0-50 ARNE_MAF_CREATE.txt.bak
echo "Traces enabled"
sleep 30
echo "Starting Synch"
/opt/ericsson/fwSysConf/bin/startAdjust.sh

