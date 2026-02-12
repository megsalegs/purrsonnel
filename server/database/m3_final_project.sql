-- database m2_final_project
ROLLBACK;
BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order
-- *************************************************************************************************
DROP TABLE IF EXISTS success_story CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS cat_prior_experience CASCADE;
DROP TABLE IF EXISTS hire_feedback CASCADE;
DROP TABLE IF EXISTS hire_request CASCADE;
DROP TABLE IF EXISTS user_bookmark CASCADE;
DROP TABLE IF EXISTS cat_image CASCADE;
DROP TABLE IF EXISTS cat CASCADE;
DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE users (
	user_id SERIAL NOT NULL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE cat (
	cat_id serial NOT NULL,
	created_by int,
	name varchar(50) NOT NULL,
	age int,
	color varchar(50),
	fur_length varchar (20),
	description varchar(500),
	skills varchar(500),
	featured boolean NOT NULL DEFAULT FALSE,
	is_active boolean NOT NULL DEFAULT true,
	removed_by int,
	removed_at timestamp,
	CONSTRAINT PK_cat PRIMARY KEY (cat_id),
	CONSTRAINT FK_cat_created_by FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,
	CONSTRAINT FK_cat_removed_by FOREIGN KEY (removed_by) REFERENCES users(user_id) ON DELETE SET NULL,
	CONSTRAINT CK_cat_age CHECK (age >= 0)
);

CREATE TABLE user_bookmark (
  user_id int NOT NULL,
  cat_id int NOT NULL,
  created_at timestamp NOT NULL DEFAULT now(),
  CONSTRAINT PK_user_bookmark PRIMARY KEY (user_id, cat_id),
  CONSTRAINT FK_user_bookmark_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT FK_user_bookmark_cat FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE
);


CREATE TABLE cat_image (
	image_id SERIAL NOT NULL,
	cat_id int NOT NULL,
	uploaded_by int NOT NULL,
	file_path varchar(500) NOT NULL,
	is_primary boolean NOT NULL DEFAULT FALSE,
	created_at timestamp DEFAULT now(),
	CONSTRAINT PK_cat_image PRIMARY KEY (image_id),
	CONSTRAINT FK_cat_image_cat FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE,
	CONSTRAINT FK_cat_image_users FOREIGN KEY (uploaded_by) REFERENCES users(user_id) ON DELETE RESTRICT
);

CREATE TABLE cat_prior_experience(
	experience_id serial NOT NULL,
	cat_id int NOT NULL,
	uploaded_by int,
	job_title varchar(50) NOT NULL,
	job_details varchar(500),
	CONSTRAINT PK_cat_prior_experience PRIMARY KEY (experience_id),
	CONSTRAINT FK_cat_prior_experience_cat FOREIGN KEY (cat_id) REFERENCES cat (cat_id) ON DELETE CASCADE,
	CONSTRAINT FK_cat_prior_experience_user FOREIGN KEY (uploaded_by) REFERENCES users (user_id) ON DELETE SET NULL
);

CREATE TABLE review (
	review_id serial NOT NULL,
	cat_id int NOT NULL,
	created_by int,
	review_body varchar(500),
	rating int,
	CONSTRAINT PK_review PRIMARY KEY (review_id),
	CONSTRAINT FK_review_cat FOREIGN KEY (cat_id) REFERENCES cat (cat_id) ON DELETE CASCADE,
	CONSTRAINT FK_review_created_by FOREIGN KEY (created_by) REFERENCES users (user_id) ON DELETE SET NULL,
	CONSTRAINT CK_review_rating CHECK (rating IS NULL OR (rating BETWEEN 1 AND 5))
);

CREATE TABLE success_story (
	story_id serial NOT NULL,
	cat_id int NOT NULL,
	created_by int,
	title varchar(50) NOT NULL,
	story_body varchar(500) NOT NULL,
	CONSTRAINT PK_success_story PRIMARY KEY (story_id),
	CONSTRAINT FK_success_story_cat FOREIGN KEY (cat_id) REFERENCES cat (cat_id) ON DELETE CASCADE,
	CONSTRAINT FK_success_story_created_by FOREIGN KEY (created_by) REFERENCES users (user_id) ON DELETE SET NULL
);

CREATE TABLE hire_request (
	hire_request_id serial NOT NULL,
	cat_id int NOT NULL,
	requested_by int NOT NULL,
	status varchar(50) NOT NULL,
	requested_start_date date,
	requested_end_date date,
	created_at timestamp NOT NULL,
	updated_at timestamp,
	CONSTRAINT PK_hire_request PRIMARY KEY (hire_request_id),
	CONSTRAINT FK_hire_request_cat FOREIGN KEY (cat_id) REFERENCES cat (cat_id) ON DELETE CASCADE,
	CONSTRAINT FK_hire_request_users FOREIGN KEY (requested_by) REFERENCES users (user_id) ON DELETE RESTRICT
);

CREATE TABLE hire_feedback (
	feedback_id serial NOT NULL,
	hire_request_id int NOT NULL UNIQUE,
	created_by int NOT NULL,
	rating int,
	feedback_body varchar(500) NOT NULL,
	created_at timestamp DEFAULT now(),
	CONSTRAINT PK_hire_feedback PRIMARY KEY (feedback_id),
	CONSTRAINT FK_hire_feedback_hire_request FOREIGN KEY (hire_request_id) REFERENCES hire_request (hire_request_id) ON DELETE CASCADE,
	CONSTRAINT FK_hire_feedback_users FOREIGN KEY (created_by) REFERENCES users (user_id) ON DELETE RESTRICT,
	CONSTRAINT CK_hire_feedback_rating CHECK (rating IS NULL or (rating BETWEEN 1 AND 5))
);



-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

-- Users
-- Password for all users is password
INSERT INTO
    users (username, password_hash, role)
VALUES
    ('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_USER'),
	('staff', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_STAFF');

INSERT INTO cat (created_by, name, age, color, fur_length, description, skills, featured)
VALUES
  ((SELECT user_id FROM users WHERE username = 'staff'), 'Mittens', 3, 'Tabby', 'short', 'Energetic mouser who loves boxes.', 'mousing, cuddles', true),
  ((SELECT user_id FROM users WHERE username = 'staff'), 'Pumpkin', 2, 'Orange', 'short', 'Playful and great with kids.', 'fetch, high-jumps', false),
  ((SELECT user_id FROM users WHERE username = 'user'), 'Luna', 4, 'Black', 'long', 'Calm cuddle buddy who enjoys windowsills.', 'lap-sitting, purring', true),
  ((SELECT user_id FROM users WHERE username = 'staff'), 'Sprocket', 1, 'Gray', 'short', 'Curious kitten learning the ropes.', 'exploration, agility', false),
  ((SELECT user_id FROM users WHERE username = 'staff'), 'Ginger', 6, 'Orange & White', 'medium', 'Experienced barn cat, reliable and steady.', 'mousing, watch-dogging (sort of)', false),
  ((SELECT user_id FROM users WHERE username = 'user'), 'Willow', 5, 'Calico', 'long', 'Photogenic show-cat with a gentle temperament.', 'posing, obedience', true);

INSERT INTO cat_prior_experience (cat_id, uploaded_by, job_title, job_details)
VALUES
  ((SELECT cat_id FROM cat WHERE name = 'Ginger'), (SELECT user_id FROM users WHERE username = 'staff'), 'Barn Supervisor', 'Kept rodents under control on a small farm for 3 years.'),
  ((SELECT cat_id FROM cat WHERE name = 'Mittens'), (SELECT user_id FROM users WHERE username = 'staff'), 'Office Morale Specialist', 'Provided moral support and occasional desk naps during busy seasons.'),
  ((SELECT cat_id FROM cat WHERE name = 'Luna'), (SELECT user_id FROM users WHERE username = 'staff'), 'Window Watcher', 'Monitored neighborhood birds and alerted staff when deliveries arrived.');

INSERT INTO review (cat_id, created_by, review_body, rating)
VALUES
  ((SELECT cat_id FROM cat WHERE name = 'Mittens'), (SELECT user_id FROM users WHERE username = 'user'), 'Fantastic hunter — our pantry is finally rodent-free!', 5),
  ((SELECT cat_id FROM cat WHERE name = 'Pumpkin'), (SELECT user_id FROM users WHERE username = 'staff'), 'Super playful, great with kids but needs a little training for the harness.', 4),
  ((SELECT cat_id FROM cat WHERE name = 'Luna'), (SELECT user_id FROM users WHERE username = 'staff'), 'Luna is very mellow and easy to groom.', 5),
  ((SELECT cat_id FROM cat WHERE name = 'Sprocket'), (SELECT user_id FROM users WHERE username = 'staff'), 'So young and curious — learns quickly!', NULL),
  ((SELECT cat_id FROM cat WHERE name = 'Willow'), (SELECT user_id FROM users WHERE username = 'user'), 'Perfect studio companion. Loves photoshoots.', 5);

INSERT INTO success_story (cat_id, created_by, title, story_body)
VALUES
  ((SELECT cat_id FROM cat WHERE name = 'Mittens'), (SELECT user_id FROM users WHERE username = 'staff'),
    'From Stray to Store Mascot',
    'Mittens joined our little shop and quickly became the daily morale booster. Within a month, customer foot traffic increased. Now a permanent member of the team.'),
  ((SELECT cat_id FROM cat WHERE name = 'Ginger'), (SELECT user_id FROM users WHERE username = 'staff'),
    'Barn Hero',
    'Ginger reduced the rodent problem and even alerted us to a leak in the feed room. Reliable and no-nonsense.'),
  ((SELECT cat_id FROM cat WHERE name = 'Willow'), (SELECT user_id FROM users WHERE username = 'user'),
    'Show-Ready',
    'Willow was trained for local cat shows and won Best Pose — then was adopted by a photography studio.');

COMMIT TRANSACTION;