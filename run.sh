mkdir bin
find -name "*.java" > sources
javac -d bin -cp "lib/*:bin" @sources
java -cp "bin:lib/*" display.TrafficDisplay
# java -cp "out/production/StreetTrafficSimulation;lib/*" display.TrafficDisplay
# ^ if sb wished to run sth produced by intellij ide
