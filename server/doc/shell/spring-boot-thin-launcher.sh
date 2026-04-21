#!/bin/bash
#这里指定需要运行的jar包的名字
APP_NAME="mkt-api-1.0.jar"
JVM_ARGS="-Xms512M -Xmx4096M -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/log/heap.hprof"
THIN_ARGS="-Dthin.root=. -Dthin.offline=true"
#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status|run]"
    exit 1
}
 
#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}
 
#后台启动
start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is already running. pid=${pid} ."
  else
    echo "${APP_NAME} running with args: nohup java $THIN_ARGS -jar $JVM_ARGS $APP_NAME "
    nohup java $THIN_ARGS -jar $JVM_ARGS $APP_NAME >> catalina.out 2>&1 &
  fi
}
#前台启动
run(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is already running. pid=${pid} ."
  else
    echo "${APP_NAME} running with args: java $THIN_ARGS -jar $JVM_ARGS $APP_NAME"
    java $THIN_ARGS -jar $JVM_ARGS $APP_NAME 
  fi
}
 
#停止方法
stop(){
  is_exist
  if [ $? -eq "0" ]; then
    kill -9 $pid
  else
    echo "${APP_NAME} is not running"
  fi
}
 
#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is running. Pid is ${pid}"
  else
    echo "${APP_NAME} is NOT running."
  fi
}
 
#重启
restart(){
  stop
  start
}
 
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "run")
    run
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac

