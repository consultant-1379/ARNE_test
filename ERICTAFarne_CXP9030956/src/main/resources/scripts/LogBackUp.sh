#!/bin/sh
#
# "@(#)Version 1.0 09.06.2010"
#
# NAME:    Log File Backup Script 
# VERSION: 1.0
# DATE:    09.06.2010
# AUTHOR:  Shane McCarron (ESHAMCC)
#
# This script is used to make back ups of log files produced on a server.
#
# To use the script call it with two parameters for log file to back up
# and the directory in which you wish to store the archived files e.g.
#
#    ./LogBackUp.sh /var/opt/ericsson/log/MAF.log.bak /home/nmsadm/logarchive/ 
#
#
# Updates:
#
# 1.0 Initial version
#

SLEEP_TIMER=20

usage() {
    echo "Usage:"
    echo "   $0 /path/to/logfile.bak /path/to/archives"
}


# validation and initialisation
if [ $# -lt 2 ]
then
    usage
    exit 1
else
    BKLOGFILE=$1
    ARCHIVEDIR=$2
    if [ ! -d "${ARCHIVEDIR}" ]
    then
        echo "ERROR: Directory ${BKUPDIR} does not exist."
        exit 2
    fi
    OLD_IFS="$IFS"
    IFS="/"
    for i in $BKLOGFILE
    do
        BKLOGFILE_ONLY=$i
    done
    IFS="$OLD_IFS"
fi


# file checking and backup mechanism
while true
do
    if [ -f ${BKLOGFILE} ]
    then
        sleep 10             # allow file to be written 
        DATE_DDMMYYYYHHMM=`date +%d-%m-%Y.%H.%M`
        DATE_SECS=`date +%S`
        DATE_STAMP=${DATE_DDMMYYYYHHMM}.${DATE_SECS}
        mv ${BKLOGFILE} ${ARCHIVEDIR}/${BKLOGFILE_ONLY}.${DATE_STAMP}.log
    else
        sleep $SLEEP_TIMER
    fi
done


