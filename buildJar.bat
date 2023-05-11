dir *.java /s /b > FilesList.txt
javac @FilesList.txt -cp src;jdom-2.0.6.1.jar
cd src
jar cfm ../DemoModules.jar META-INF\MANIFEST.MF .   
cd ..
java -jar DemoModules.jar en np
