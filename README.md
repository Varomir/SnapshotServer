#Storage  BaseLineServer

*Build* with dependencies
mvn package

*Run*
mws@mws-tv:/opt/SnapshotServer$ nohup java -Dport=8081 -Dsnapshot.path=./target/baseline/ -jar baseline-1.0.3.jar > serveroutput.log &
