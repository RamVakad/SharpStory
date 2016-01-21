@echo off
@title SharpDEV Server v83
set CLASSPATH=.;dist\*
java -Xmx1536m -Djavax.net.ssl.keyStore=filename.keystore -Djavax.net.ssl.keyStorePassword=passwd -Djavax.net.ssl.trustStore=filename.keystore -Djavax.net.ssl.trustStorePassword=passwd -XX:ParallelGCThreads=2 -server -Xincgc -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSIncrementalPacing -XX:+AggressiveOpts -XX:+CMSParallelRemarkEnabled -XX:+DisableExplicitGC -XX:MaxGCPauseMillis=1000 -XX:SurvivorRatio=16 -XX:TargetSurvivorRatio=90 -XX:+UseAdaptiveGCBoundary -XX:-UseGCOverheadLimit -Xnoclassgc -XX:UseSSE=3 -XX:PermSize=128m -XX:LargePageSizeInBytes=4m net.server.Server
pause