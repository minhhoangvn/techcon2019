mvn clean install
katalon --args -noSplash  -runMode=console -consoleLog -projectPath="/Users/minhhoang/Katalon Studio/TIL-Techcon/TIL-Techcon.prj" -retry=0 -testSuitePath="Test Suites/Demo" -executionProfile="default" -browserType="Chrome" -installPlugin=$(pwd)/target/plugins-1.0.12.jar  $1 $2
