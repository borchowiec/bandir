#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
BASEDIR=`cd $BASEDIR && pwd`

cd $BASEDIR/gateway && mvn clean package -DskipTests=true
cd $BASEDIR/naming-server && mvn clean package -DskipTests=true
cd $BASEDIR/user && mvn clean package -DskipTests=true