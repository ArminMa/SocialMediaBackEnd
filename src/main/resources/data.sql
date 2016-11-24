DELETE FROM face_user WHERE email= 'svlada@gmail.com';
insert into face_user( password, username,email) values( '$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 'svlada@gmail.com','svlada@gmail.com');

-- DELETE FROM face_user WHERE email= 'testUser@testUser.testUser';
-- insert into face_user( password, username,email) values( '793148fd08f39ee62a84474fce8e0a544c5f1fc8', 'testUser','testUser@testUser.testUser');
--
-- DELETE FROM face_user WHERE email= 'testUser2@testUser2.testUser2';
-- insert into face_user( password, username,email) values( '793148fd08f39ee62a84474fce8e0a544c5f1fc8', 'testUser2','testUser2@testUser2.testUser2');


DELETE FROM roles_entity WHERE id=1 or id=2 or id= 3 or id = 4 or id = 5;
insert into roles_entity(id, role) values( 1, 'MEMBER');
insert into roles_entity(id, role) values( 2, 'PREMIUM_MEMBER');
insert into roles_entity(id, role) values( 3, 'ADMIN');
insert into roles_entity(id, role) values( 4, 'SUPER_ADMIN');
insert into roles_entity(id, role) values( 5, 'ANONYMOUS');



-- MEMBER, PREMIUM_MEMBER, ADMIN, SUPER_ADMIN , ROLE_ANONYMOUS;