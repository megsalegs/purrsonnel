{\rtf1\ansi\ansicpg1252\cocoartf2822
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww12240\viewh15840\viewkind1
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 BEGIN TRANSACTION;\
\
-- =================================================================================\
-- DROP TABLES (safe order)\
-- =================================================================================\
DROP TABLE IF EXISTS success_story CASCADE;\
DROP TABLE IF EXISTS review CASCADE;\
DROP TABLE IF EXISTS cat_prior_experience CASCADE;\
DROP TABLE IF EXISTS hire_feedback CASCADE;\
DROP TABLE IF EXISTS hire_request CASCADE;\
DROP TABLE IF EXISTS user_bookmark CASCADE;\
DROP TABLE IF EXISTS cat_image CASCADE;\
DROP TABLE IF EXISTS cat CASCADE;\
DROP TABLE IF EXISTS users CASCADE;\
\
-- =================================================================================\
-- USERS\
-- =================================================================================\
CREATE TABLE users (\
  user_id SERIAL PRIMARY KEY,\
  username VARCHAR(50) NOT NULL UNIQUE,\
  password_hash VARCHAR(200) NOT NULL,\
  role VARCHAR(50) NOT NULL\
);\
\
-- =================================================================================\
-- CAT\
-- =================================================================================\
CREATE TABLE cat (\
  cat_id SERIAL PRIMARY KEY,\
  created_by INT,\
  name VARCHAR(50) NOT NULL,\
  age INT CHECK (age >= 0),\
  color VARCHAR(50),\
  fur_length VARCHAR(20),\
  description VARCHAR(500),\
  skills VARCHAR(500),\
  featured BOOLEAN NOT NULL DEFAULT FALSE,\
\
  rank_score NUMERIC(3,1),\
  ranked_by INT,\
  ranked_at TIMESTAMP DEFAULT now(),\
\
  primary_image_path VARCHAR(500),\
\
  is_active BOOLEAN NOT NULL DEFAULT true,\
  removed_by INT,\
  removed_at TIMESTAMP,\
\
  CONSTRAINT FK_cat_created_by FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,\
  CONSTRAINT FK_cat_ranked_by FOREIGN KEY (ranked_by) REFERENCES users(user_id) ON DELETE SET NULL,\
  CONSTRAINT FK_cat_removed_by FOREIGN KEY (removed_by) REFERENCES users(user_id) ON DELETE SET NULL\
);\
\
-- =================================================================================\
-- USER BOOKMARK\
-- =================================================================================\
CREATE TABLE user_bookmark (\
  user_id INT NOT NULL,\
  cat_id INT NOT NULL,\
  created_at TIMESTAMP DEFAULT now(),\
  PRIMARY KEY (user_id, cat_id),\
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,\
  FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE\
);\
\
-- =================================================================================\
-- CAT IMAGE\
-- =================================================================================\
CREATE TABLE cat_image (\
  image_id SERIAL PRIMARY KEY,\
  cat_id INT NOT NULL,\
  uploaded_by INT NOT NULL,\
  file_path VARCHAR(500) NOT NULL,\
  is_primary BOOLEAN NOT NULL DEFAULT FALSE,\
  created_at TIMESTAMP DEFAULT now(),\
  FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE,\
  FOREIGN KEY (uploaded_by) REFERENCES users(user_id) ON DELETE RESTRICT\
);\
\
-- =================================================================================\
-- CAT PRIOR EXPERIENCE\
-- =================================================================================\
CREATE TABLE cat_prior_experience (\
  experience_id SERIAL PRIMARY KEY,\
  cat_id INT NOT NULL,\
  uploaded_by INT,\
  job_title VARCHAR(50) NOT NULL,\
  job_details VARCHAR(500),\
  FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE,\
  FOREIGN KEY (uploaded_by) REFERENCES users(user_id) ON DELETE SET NULL\
);\
\
-- =================================================================================\
-- REVIEW\
-- =================================================================================\
CREATE TABLE review (\
  review_id SERIAL PRIMARY KEY,\
  cat_id INT NOT NULL,\
  created_by INT,\
  review_body VARCHAR(500),\
  rating NUMERIC(2,1),\
  FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE,\
  FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL,\
  CONSTRAINT CK_review_rating CHECK (rating IS NULL OR rating BETWEEN 1 AND 5)\
);\
\
-- =================================================================================\
-- SUCCESS STORY\
-- =================================================================================\
CREATE TABLE success_story (\
  story_id SERIAL PRIMARY KEY,\
  cat_id INT NOT NULL,\
  created_by INT,\
  title VARCHAR(50) NOT NULL,\
  story_body VARCHAR(500) NOT NULL,\
  FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE,\
  FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL\
);\
\
-- =================================================================================\
-- HIRE REQUEST\
-- =================================================================================\
CREATE TABLE hire_request (\
  hire_request_id SERIAL PRIMARY KEY,\
  cat_id INT NOT NULL,\
  requested_by INT NOT NULL,\
  status VARCHAR(50) NOT NULL,\
  requested_start_date DATE,\
  requested_end_date DATE,\
  created_at TIMESTAMP NOT NULL DEFAULT now(),\
  updated_at TIMESTAMP,\
  FOREIGN KEY (cat_id) REFERENCES cat(cat_id) ON DELETE CASCADE,\
  FOREIGN KEY (requested_by) REFERENCES users(user_id) ON DELETE RESTRICT\
);\
\
-- =================================================================================\
-- HIRE FEEDBACK\
-- =================================================================================\
CREATE TABLE hire_feedback (\
  feedback_id SERIAL PRIMARY KEY,\
  hire_request_id INT NOT NULL UNIQUE,\
  created_by INT NOT NULL,\
  rating NUMERIC(2,1),\
  feedback_body VARCHAR(500) NOT NULL,\
  created_at TIMESTAMP DEFAULT now(),\
  FOREIGN KEY (hire_request_id) REFERENCES hire_request(hire_request_id) ON DELETE CASCADE,\
  FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT,\
  CONSTRAINT CK_hire_feedback_rating CHECK (rating IS NULL OR rating BETWEEN 1 AND 5)\
);\
\
-- =================================================================================\
-- SEED DATA\
-- =================================================================================\
INSERT INTO users (username, password_hash, role) VALUES\
('user',  '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_USER'),\
('staff', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_STAFF');\
\
INSERT INTO cat (created_by, name, age, color, fur_length, description, skills, featured)\
VALUES\
((SELECT user_id FROM users WHERE username='staff'), 'Mittens', 3, 'Tabby', 'short', 'Energetic mouser who loves boxes.', 'mousing, cuddles', true),\
((SELECT user_id FROM users WHERE username='staff'), 'Pumpkin', 2, 'Orange', 'short', 'Playful and great with kids.', 'fetch, high-jumps', false),\
((SELECT user_id FROM users WHERE username='user'),  'Luna', 4, 'Black', 'long', 'Calm cuddle buddy.', 'lap-sitting, purring', true),\
((SELECT user_id FROM users WHERE username='staff'), 'Sprocket', 1, 'Gray', 'short', 'Curious kitten.', 'exploration, agility', false),\
((SELECT user_id FROM users WHERE username='staff'), 'Ginger', 6, 'Orange & White', 'medium', 'Experienced barn cat.', 'mousing, watchfulness', false),\
((SELECT user_id FROM users WHERE username='user'),  'Willow', 5, 'Calico', 'long', 'Photogenic show-cat.', 'posing, obedience', true);\
\
-- =================================================================================\
-- AUTO-GENERATE PRIMARY IMAGES\
-- =================================================================================\
INSERT INTO cat_image (cat_id, uploaded_by, file_path, is_primary)\
SELECT\
  c.cat_id,\
  (SELECT user_id FROM users WHERE username='staff'),\
  CONCAT('/images/cats/cat_', LPAD(c.cat_id::text, 3, '0'), '.jpg'),\
  true\
FROM cat c;\
\
UPDATE cat\
SET primary_image_path = ci.file_path\
FROM cat_image ci\
WHERE cat.cat_id = ci.cat_id\
  AND ci.is_primary = true;\
\
-- =================================================================================\
-- REVIEWS (initial + generated)\
-- =================================================================================\
INSERT INTO review (cat_id, created_by, review_body, rating)\
VALUES\
((SELECT cat_id FROM cat WHERE name='Mittens'), (SELECT user_id FROM users WHERE username='user'),  'Fantastic hunter.', 5),\
((SELECT cat_id FROM cat WHERE name='Pumpkin'), (SELECT user_id FROM users WHERE username='staff'), 'Great with kids.', 4),\
((SELECT cat_id FROM cat WHERE name='Luna'),    (SELECT user_id FROM users WHERE username='staff'), 'Very mellow.', 5),\
((SELECT cat_id FROM cat WHERE name='Willow'),  (SELECT user_id FROM users WHERE username='user'),  'Perfect studio companion.', 5);\
\
INSERT INTO review (cat_id, rating, review_body)\
SELECT\
  c.cat_id,\
  ROUND((3.5 + RANDOM() * 1.5)::NUMERIC, 1),\
  msgs.review_body\
FROM cat c\
CROSS JOIN generate_series(1, (FLOOR(RANDOM() * 3) + 2)::INT)\
CROSS JOIN LATERAL (\
  SELECT review_body\
  FROM (VALUES\
    ('Professional and punctual.'),\
    ('Very friendly and easy to work with.'),\
    ('Excellent performance.'),\
    ('Calm and reliable.'),\
    ('Exceeded expectations.')\
  ) AS msgs(review_body)\
  ORDER BY RANDOM()\
  LIMIT 1\
) msgs;\
\
-- =================================================================================\
-- RANK SCORE (derived)\
-- =================================================================================\
UPDATE cat\
SET rank_score = sub.avg_rating\
FROM (\
  SELECT cat_id, ROUND(AVG(rating), 1) AS avg_rating\
  FROM review\
  GROUP BY cat_id\
) sub\
WHERE cat.cat_id = sub.cat_id;\
\
COMMIT TRANSACTION;\
}