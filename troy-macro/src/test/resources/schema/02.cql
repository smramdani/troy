ALTER TABLE test.posts ADD post_rating int;
CREATE INDEX ON test.posts (post_title);

ALTER TABLE test.post_details ADD comment_userIds list<int>;
ALTER TABLE test.post_details ADD comment_bodies list<text>;
ALTER TABLE test.post_details DROP someField;

CREATE INDEX ON test.post_details (tags);
CREATE INDEX ON test.post_details (comment_userIds);
CREATE INDEX ON test.post_details (KEYS(comments));