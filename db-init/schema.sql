CREATE TABLE system.app_users (
  username VARCHAR2(50) NOT NULL PRIMARY KEY,
  password VARCHAR2(100) NOT NULL,
  enabled NUMBER(1) NOT NULL
);

CREATE TABLE system.app_authorities (
  username VARCHAR2(50) NOT NULL,
  authority VARCHAR2(50) NOT NULL,
  CONSTRAINT fk_authorities_users
    FOREIGN KEY(username)
    REFERENCES system.app_users(username)
);

INSERT INTO system.app_users (username, password, enabled)
VALUES (
  'myuser',
  '$2a$10$9zK0aSPGZfVFmEuVADs75eDYgUOE9d/hCPx5P59k6coEK0p4qMgyu',
  1
);

INSERT INTO system.app_authorities (username, authority)
VALUES ('myuser', 'ROLE_USER');

INSERT INTO system.app_authorities (username, authority)
VALUES ('myuser', 'ROLE_ADMIN');
