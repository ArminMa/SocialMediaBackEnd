# SocialMediaBackEnd

####Run project as war in a tomcat
#mvn clean install -P tomMySQL
    //windows
    mvn clean install
    java -jar target\tomcatRun.jar --port 8081 target\ROOT.war
    //linux
    mvn clean install
    java -jar target/tomcatRun.jar --port 8081 target/ROOT.war
or
    insert the war file /target/social.war in TomCat on post 8081

#test the App from Postman
    http://localhost:8081/getEmail/test
    http://localhost:8081/ping
    http://localhost:8081/ping2?name=Armin