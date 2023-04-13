#!/bin/bash

# Service
SERVICE_NAME=""
PATH_TO_JAR="build/libs/$SERVICE_NAME-0.0.1-SNAPSHOT.jar"

# Logging
mkdir -p "logs"

LOG_PATH="logs"
LOG_FILE="application.log"
LOGGED=$LOG_PATH/$LOG_FILE
DEV_NULL=/dev/null

# PID
PID_DIR="."
PID_FILE="$PID_DIR/app.pid"
PID=""

test_pid() {
    PID=`ps -p $PID | grep $PID | grep -v grep | awk '{print $1}' | tail -1`
    if [ "X$PID" = "X" ]
    then
        rm -f "$PID_FILE"
        PID=""
    fi
}

get_pid() {
    if  [ -f "$PID_FILE" ]
    then
        if [ -r "$PID_FILE" ]
        then
            PID=`cat "$PID_FILE"`
            if [ "X$PID" != "X" ]
            then
                pid_test=`ps -p $PID -o args | grep java | tail -1`
                if [ "X$pid_test" = "X" ]
                then
                    rm -f "$PID_FILE"
                    echo "Delete stale pid file."
                    PID=""
                fi
            fi
        fi
    fi
}

case $1 in
    'start')
        echo "Starting $SERVICE_NAME"
        if [ ! -f $PID_FILE ]; then
            nohup java -jar $PATH_TO_JAR >> $LOGGED 2>&1 &
            echo $! > $PID_FILE
            echo "$SERVICE_NAME started."
        else
            echo "$SERVICE_NAME is already running"
        fi
        ;;
    'stop')
        if [ -f $PID_FILE ]; then
            PID=$(cat $PID_FILE);
            echo "$SERVICE_NAME stopping"
            kill $PID;
            echo "$SERVICE_NAME stopped."
            rm $PID_FILE
        else
            echo "$SERVICE_NAME is not running"
        fi
        ;;
    'restart')
        if [ -f $PID_FILE ]; then
            PID=$(cat $PID_FILE);
            echo "$SERVICE_NAME stopping";
            kill $PID;
            echo "$SERVICE_NAME stopped.";
            rm $PID_FILE
            echo "$SERVICE_NAME starting"
            nohup java -jar $PATH_TO_JAR >> $LOGGED 2>&1 &
            echo $! > $PID_FILE
            echo "$SERVICE_NAME started."
        else
            echo "$SERVICE_NAME is not running"
        fi
        ;;
    'status')
        get_pid
        if [ "X$PID" = "X" ]
        then
            echo "$SERVICE_NAME is not running."
            exit 1
        else
            echo "$SERVICE_NAME is running ($PID)"
            exit 0
        fi
        ;;
    *)
    echo "***---***---***---***---***---***---***---***"
    echo "Usage: $0 { start | stop | restart | status }"
    echo "***---***---***---***---***---***---***---***"
    exit 1
esac

exit 0
