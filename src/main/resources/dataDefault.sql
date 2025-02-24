-- Data for Hibernate to insert on each run after the tables are created
-- To enable the insert data, please rename the file to data.sql and update the field to true spring.jpa.hibernate.ddl-auto=true
-- Insert Users
INSERT INTO user_details (name, birth_date) VALUES ('Alice Johnson', '1990-05-15');
INSERT INTO user_details (name, birth_date) VALUES ('Bob Smith', '1985-10-20');
INSERT INTO user_details (name, birth_date) VALUES ('Charlie Davis', '2000-02-25');
INSERT INTO user_details (name, birth_date) VALUES ('David Williams', '1995-12-10');
INSERT INTO user_details (name, birth_date) VALUES ('Emma Brown', '1992-08-30');

--Insert Categories
INSERT INTO category (name) VALUES ('Adventure Travel');
INSERT INTO category (name) VALUES ('Luxury Travel');
INSERT INTO category (name) VALUES ('Budget Travel');
INSERT INTO category (name) VALUES ('Solo Travel');
INSERT INTO category (name) VALUES ('Family Travel');

-- Insert Posts
INSERT INTO post (description, category_id, user_id) VALUES ('Top 10 Adventure Destinations for 2025', 1, 2);
INSERT INTO post (description, category_id, user_id) VALUES ('Best Luxury Hotels Around the World', 2, 3);
INSERT INTO post (description, category_id, user_id) VALUES ('How to Travel on a Budget', 3, 1);
INSERT INTO post (description, category_id, user_id) VALUES ('Tips for Solo Travelers', 4, 5);
INSERT INTO post (description, category_id, user_id) VALUES ('Best Family-Friendly Destinations', 5, 4);

-- Insert Comments
INSERT INTO comment (content, post_id, user_id) VALUES ('I have been to some of these adventure spots, they are amazing!', 1, 3);
INSERT INTO comment (content, post_id, user_id) VALUES ('The luxury hotels list is perfect for honeymoon planning!', 2, 1);
INSERT INTO comment (content, post_id, user_id) VALUES ('Great budget travel tips, especially for students!', 3, 4);
INSERT INTO comment (content, post_id, user_id) VALUES ('Solo traveling is such a unique experience. Love these tips!', 4, 2);
INSERT INTO comment (content, post_id, user_id) VALUES ('Planning a family vacation next year, this helps a lot!', 5, 5);

-- Insert Likes
INSERT INTO likes (post_id, user_id) VALUES (1, 2);
INSERT INTO likes (post_id, user_id) VALUES (2, 3);
INSERT INTO likes (post_id, user_id) VALUES (3, 1);
INSERT INTO likes (post_id, user_id) VALUES (4, 5);
INSERT INTO likes (post_id, user_id) VALUES (5, 4);

