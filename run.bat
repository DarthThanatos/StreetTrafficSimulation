md bin
dir /s /B *.java > sources
javac -d bin -cp "lib/*;bin" @sources
java -cp "bin;lib/*" display.TrafficDisplay 
rem java -cp "out/production/StreetTrafficSimulation;lib/*" display.TrafficDisplay  
