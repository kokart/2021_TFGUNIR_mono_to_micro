DROP TABLE pets IF EXISTS;
DROP TABLE users IF EXISTS;


CREATE TABLE users (
  id         INTEGER IDENTITY PRIMARY KEY,
  nombre VARCHAR(30),
  apellido  VARCHAR_IGNORECASE(30),
  centro    VARCHAR(255),
  silcon  VARCHAR(80),
  aplicacion VARCHAR(80)
);
CREATE INDEX users_apellido ON users (apellido);



