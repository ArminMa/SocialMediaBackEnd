DELETE FROM face_user WHERE email= 'svlada@gmail.com';
insert into face_user( password, username,email) values( '$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 'svlada@gmail.com','svlada@gmail.com');
TRUNCATE TABLE user_role;
insert into user_role(id, role, user_id) values(1, 'MEMBER', 1);
insert into user_role(id, role, user_id) values(2, 'PREMIUM_MEMBER', 1);

-- MEMBER, PREMIUM_MEMBER, ADMIN, SUPER_ADMIN , ROLE_ANONYMOUS;