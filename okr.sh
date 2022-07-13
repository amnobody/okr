#!/bin/sh

okr_exist=`ps -ef|grep okr|grep -v grep|wc -l`
if [ $project -ne 0 ]
then
  echo '开始结束进程'
  ps -ef|grep okr |grep java |grep -v grep|awk '{print $2}' | xargs -I {} kill -9 {} 2>&1;
fi

echo '开始启动进程'
nohup /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.312.b07-2.el8_5.x86_64/jre/bin/java -server -Dcatalina.base=/var/log/apps/okr -XX:NativeMemoryTracking=summary -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log/apps/okr/oom -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=32 -XX:GCLogFileSize=1m -XX:+UseG1GC -Xloggc:/var/log/apps/okr/gc/heap_gc.log -XX:ErrorFile=/var/log/apps/okr/error/java_err.log -Dspring.profiles.active=prod -jar /home/lighthouse/apps/okr.jar &



