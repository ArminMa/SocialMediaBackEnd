DELETE FROM face_user WHERE email= 'svlada@gmail.com';
insert into face_user( password, username,email) values( '$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 'svlada@gmail.com','svlada@gmail.com');

-- DELETE FROM face_user WHERE email= 'testUser@testUser.testUser';
-- insert into face_user( password, username,email) values( '793148fd08f39ee62a84474fce8e0a544c5f1fc8', 'testUser','testUser@testUser.testUser');
--
-- DELETE FROM face_user WHERE email= 'testUser2@testUser2.testUser2';
-- insert into face_user( password, username,email) values( '793148fd08f39ee62a84474fce8e0a544c5f1fc8', 'testUser2','testUser2@testUser2.testUser2');


TRUNCATE TABLE user_role;
insert into user_role(id, role, user_id) values(1, 'MEMBER', 1);
insert into user_role(id, role, user_id) values(2, 'PREMIUM_MEMBER', 1);

-- insert into user_role(id, role, user_id) values(1, 'MEMBER', 2);
-- insert into user_role(id, role, user_id) values(1, 'MEMBER', 3);
-- insert into user_role(id, role, user_id) values(1, 'MEMBER', 4);

-- MEMBER, PREMIUM_MEMBER, ADMIN, SUPER_ADMIN , ROLE_ANONYMOUS;