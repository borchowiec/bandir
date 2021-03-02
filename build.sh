#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
BASEDIR=`cd $BASEDIR && pwd`

cd $BASEDIR/remote && mvn clean install -DskipTests=true
cd $BASEDIR/gateway && mvn clean package -DskipTests=true
cd $BASEDIR/naming-server && mvn clean package -DskipTests=true
cd $BASEDIR/user && mvn clean package -DskipTests=true
cd $BASEDIR/user-repository && mvn clean package -DskipTests=true
cd $BASEDIR/notification-channel && mvn clean package -DskipTests=true
cd $BASEDIR/auth && mvn clean package -DskipTests=true