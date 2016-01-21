#!/bin/sh

export CLASSPATH=".:dist/*"

linuxjava/bin/java -Dwzpath=SharpDEVwz_files/ -Djavax.net.ssl.keyStore=filename.keystore -Djavax.net.ssl.keyStorePassword=passwd -Djavax.net.ssl.trustStore=filename.keystore -Djavax.net.ssl.trustStorePassword=passwd -Xmx100M tools.MonsterDropCreator
