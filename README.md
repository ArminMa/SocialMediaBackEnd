# SocialMediaBackEnd

####Run project as war in a tomcat

#to run this project from terminal/ console with h2 database
    mvn clean install -P h2Development
####windows
    java -jar target\tomcatRun.jar --port 8081 target\ROOT.war
####Linux
    java -jar target/tomcatRun.jar --port 8081 target/ROOT.war

      
###to run this project from terminal/ console with mysql database
    mvn clean install -P mysqlProduction
####windows
    java -jar target\tomcatRun.jar --port 8081 target\ROOT.war
####Linux
    java -jar target/tomcatRun.jar --port 8081 target/ROOT.war


###or insert the war file manually in /target/social.war in TomCat on post 8081

#test the App from Postman
    http://localhost:8081/getEmail/test
    http://localhost:8081/ping
    http://localhost:8081/ping2?name=Armin