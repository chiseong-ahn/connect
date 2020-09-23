-- cstalk.Sample definition

CREATE TABLE Sample (
  id INTEGER AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  regDt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modDt datetime DEFAULT NULL,
  delDt datetime DEFAULT NULL,
  isDel int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (id)
);