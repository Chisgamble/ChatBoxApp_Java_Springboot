INSERT INTO app_user(username, password, email, is_active)
values ('Sammael', 'justiceWillBeServed', 'sammael@gmail.com', true),
 ('Rodya', 'normalEnd123', 'rodya@gmail.com', false);

INSERT INTO inbox(userA, userB)
SELECT a.id, b.id
FROM app_user a, app_user b
WHERE a.username = 'Sammael'
  AND b.username = 'Rodya';

insert into friend(userA, userB)
values (1,3),
		(2,3);

insert into inbox(id, userA, userB)
values (2, 1, 3),
		(3, 2,3);

INSERT INTO inbox_message (inbox_id, sender_id, "content")
values (2, 1, 'hello'),
		(2,3, 'hi'),
		(2, 3, 'hi'),
		(3, 3, 'hello');

INSERT INTO app_user(id, username, password, email, is_active)
values (4, 'New', 'brandNew', 'new@gmail.com', true),
 (5, 'Chis', 'cheese', 'chis@gmail.com', false);

insert into friend_request (sender_id, receiver_id, status)
values (4,3, 'pending'),
		(5, 3, 'pending');

insert into chat_group (id, group_name)
values (1, 'company'),
		(2, 'bus user');

insert into group_member (group_id, user_id, "role")
values (1, 1, 'admin'),
		(1, 2, 'member'),
		(1,3, 'member'),
		(2, 3, 'admin'),
		(2, 4, 'member'),
		(2, 5, 'member');

insert into group_message (group_id, sender_id, "content")
values (1, 3, 'hey'),
		(1, 2, 'ye'),
		(1, 1, 'yup'),
		(2,3, 'welcome'),
		(2, 4, 'wat'),
		(2, 5, 'why');

SELECT column_default
FROM information_schema.columns
WHERE table_name = 'friend_request' AND column_name = 'id';

alter sequence app_user_id_seq restart with 1;
alter sequence friend_request_id_seq restart with 1;
alter sequence inbox_id_seq restart with 1;
alter sequence chat_group_id_seq restart with 1;
alter sequence group_message_id_seq restart with 1;
alter sequence group_member_id_seq restart with 1;
alter sequence user_block_id_seq restart with 1;

SELECT last_value FROM app_user_seq; 

SELECT MAX(id) FROM app_user;

-- INSERT INTO login_log(email, isSuccess)
-- values ('sammael@gmail.com', true);
--delete from app_user where id = 3;

select * from inbox_message;
select * from inbox;
Select * from app_user;
select * from friend;
select * from friend_request;
select * from group_message;
select * from user_block;
-- select * from login_log;

select * from inbox_message im 
join inbox i on im.inbox_id = i.id
join app_user on im.sender_id = app_user.id

INSERT INTO user_block (blocker_id, blocked_id)
            VALUES (1, 2)
            ON CONFLICT DO NOTHING
