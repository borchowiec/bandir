#!/usr/bin/env bash

BASEDIR=$(dirname "$0")
BASEDIR=`cd $BASEDIR && pwd`
BASEDIR/build.sh
docker-compose up --build