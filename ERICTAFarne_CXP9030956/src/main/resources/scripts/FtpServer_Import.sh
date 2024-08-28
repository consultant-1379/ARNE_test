#!/bin/bash
#Script to execute import for all the FTPServer XML files
FTP_IMPORT_FILES_PATH=/var/opt/ericsson/arne/FTPServices/
IMPORT_SCRIPT_PATH=/opt/ericsson/arne/bin/import.sh
#FILE_PERMISSION= chmod -R 755 $FTP_IMPORT_FILES_PATH
#$FILE_PERMISSION
LIST_FTPSERVERS=`/usr/bin/ls $FTP_IMPORT_FILES_PATH | /usr/bin/grep -v -i upgraded_temp | /usr/bin/grep -v -i MOD.xml | /usr/bin/grep -v -i DEL.xml`

#echo $LIST_FTPSERVERS
for fileName in $LIST_FTPSERVERS
 do
        $IMPORT_SCRIPT_PATH -f $FTP_IMPORT_FILES_PATH$fileName -import -i_nau
done

