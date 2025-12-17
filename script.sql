-- drop table app_user;

-- drop table register_log;
-- drop table login_log;
-- drop table activity_log;

-- drop table inbox;
-- drop table inbox_message;

-- drop table friend_request;
-- drop table friendship;

-- drop table user_block;
-- drop table spam_report;

-- drop table "group";
-- drop table group_message;
-- drop table group_member;

--run these to reset db
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- select last_value from appuser_seq;
-- drop sequence if exists app_user_seq;

CREATE Table app_user(
	ID BigSerial primary key,
	username varchar(50),
	"password" varchar(100),
	"name" varchar(50),
	address varchar(100),
	birthday date,
	gender varchar(10),
	email varchar(50) UNIQUE,
	is_active boolean,
	"role" varchar(30),
	is_locked boolean,
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE register_log(
	ID BigSerial primary key,
	email varchar(50),
	isSuccess boolean,
	reason varchar(100),

	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE login_log(
	ID BigSerial primary key,
	email varchar(50),
	isSuccess boolean,
	reason varchar(100),

	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,

	foreign key (email) references app_user (email) ON DELETE CASCADE
);

CREATE TABLE activity_log(
	ID BigSerial primary key,
	user_id bigint,
	isSuccess boolean,
	reason varchar(100),

	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,

	foreign key (user_id) references app_user (id) ON DELETE CASCADE
);

CREATE TABLE inbox(
	ID BigSerial primary key,
	userA bigint,
	userB bigint,
	CONSTRAINT chk_order CHECK (userA < userB),
	CONSTRAINT uq_inbox_pair UNIQUE (userA, userB),
	userA_last_seen bigint,
	userB_last_seen bigint,
	last_msg bigint,
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	
	foreign key (userA) references app_user (id) ON DELETE CASCADE,
	foreign key (userB) references app_user (id) ON DELETE CASCADE
);

CREATE TABLE inbox_message(
	ID BigSerial primary key,
	inbox_id bigint,
	sender_id bigint,
	status varchar(30),
	"content" varchar(500),
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	
	foreign key (inbox_id) references inbox (id) ON DELETE CASCADE,
	foreign key (sender_id) references app_user (id) ON DELETE CASCADE
);

ALTER TABLE inbox
ADD CONSTRAINT fk_userA_last_seen
FOREIGN KEY (userA_last_seen) REFERENCES inbox_message (id) ON DELETE SET NULL;

ALTER TABLE inbox
ADD CONSTRAINT fk_userB_last_seen
FOREIGN KEY (userB_last_seen) REFERENCES inbox_message (id) ON DELETE SET NULL;

ALTER TABLE inbox
ADD CONSTRAINT fk_last_msg
FOREIGN KEY (last_msg) REFERENCES inbox_message (id) ON DELETE SET NULL;


CREATE TABLE friend_request(
	ID BigSerial primary key,
	sender_id bigint,
	receiver_id bigint,
	CONSTRAINT uq_friend_req_pair UNIQUE (sender_iD, receiver_iD),
	
	status varchar(10),
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,

	foreign key (sender_iD) references app_user (id) ON DELETE CASCADE,
	foreign key (receiver_iD) references app_user (id) ON DELETE CASCADE
);

CREATE TABLE friend(
	ID BigSerial primary key,
	userA bigint,
	userB bigint,
	CONSTRAINT chk_order CHECK (userA < userB),
	CONSTRAINT uq_friend_pair UNIQUE (userA, userB),
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	
	foreign key (userA) references app_user(id) ON DELETE CASCADE,
	foreign key (userB) references app_user(id) ON DELETE CASCADE
);

CREATE TABLE user_block(
	ID BigSerial primary key,
	blocker_id bigint,
	blocked_id bigint,
	CONSTRAINT uq_block_pair UNIQUE (blocker_id, blocked_id),
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	
	foreign key (blocker_id) references app_user(id) ON DELETE CASCADE,
	foreign key (blocked_id) references app_user(id) ON DELETE CASCADE
);

CREATE TABLE spam_report(
	ID BigSerial primary key,
	reporter_id bigint,
	reported_id bigint,
	CONSTRAINT uq_report_pair UNIQUE (reporter_id, reported_id),
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	
	foreign key (reporter_id) references app_user(id) ON DELETE CASCADE,
	foreign key (reported_id) references app_user(id) ON DELETE CASCADE
);

CREATE TABLE chat_group(
	ID BigSerial primary key,
	group_name varchar(100),
	encryptionKey varchar(20),
	last_msg bigint,
	
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE group_message(
	ID BigSerial primary key,
	group_id bigint,
	sender_id bigint,
	"content" varchar(500),

	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,

	foreign key (group_id) references chat_group (id) ON DELETE CASCADE,
	foreign key (sender_id) references app_user(id) ON DELETE CASCADE
);

alter table chat_group
add constraint fk_last_msg
foreign key (last_msg) references group_message (id) ON DELETE SET NULL;

CREATE TABLE group_member(
	ID BigSerial primary key,
	group_id bigint,
	user_id bigint,
	"role" varchar(10),
	last_seen_msg bigint,

	-- join date
	created_at timeStamp DEFAULT CURRENT_TIMESTAMP,
	updated_at timeStamp DEFAULT CURRENT_TIMESTAMP,

	foreign key (group_id) references chat_group (id) ON DELETE CASCADE,
	foreign key (user_id) references app_user(id) ON DELETE CASCADE,
	foreign key (last_seen_msg) references group_message(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION update_inbox_last_msg()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE inbox
    SET last_msg = NEW.id,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.inbox_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_last_msg
AFTER INSERT ON inbox_message
FOR EACH ROW
EXECUTE FUNCTION update_inbox_last_msg();

CREATE OR REPLACE FUNCTION update_group_last_msg()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE chat_group
    SET last_msg = NEW.id,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.group_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_update_group_last_msg
AFTER INSERT ON group_message
FOR EACH ROW
EXECUTE FUNCTION update_group_last_msg();



