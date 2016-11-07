# SocialMediaBackEnd



####Run project as war in a tomcat
#mvn clean install -P tomMySQL
    mvn clean install
    java -jar target\tomcatRun.jar --port 8081 target\social.war
    
or

    insert the war file /target/social.war in TomCat on post 8081

    
    
#test the App from Postman

    http://localhost:8081/social/getEmail/test
    http://localhost:8081/social/ping
    http://localhost:8081/social/ping2?name=Armin

