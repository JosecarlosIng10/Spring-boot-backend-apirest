/* Populate tabla clientes */
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Andrés', 'Guzmán', 'profesor@bolsadeideas.com','18', '2018-01-01');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Mr. John', 'Doe', 'john.doe@gmail.com','19', '2018-01-02');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '15','2018-01-03');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '32','2018-01-04');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '17','2018-02-01');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '47','2018-02-10');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com','20', '2018-02-18');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '28','2018-02-28');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Dr. James', 'Gosling', 'james.gosling@gmail.com','16', '2018-03-03');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Magma', 'Lee', 'magma.lee@gmail.com','15', '2018-03-04');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Tornado', 'Roe', 'tornado.roe@gmail.com', '20','2018-03-05');
INSERT INTO clientes (nombre, apellido, email, edad, create_at) VALUES('Jade', 'Doe', 'jane.doe@gmail.com', '21','2018-03-06');

INSERT INTO usuarios (username, password, enabled) VALUES ('andres','$2a$10$nHI0ZxYGIOxf1.K7pJAw9OahaVNGpFzF6zZT5rldPHQC7RdDxE.Yy',1);
INSERT INTO usuarios (username, password, enabled) VALUES ('admin','$2a$10$OoinWJ0w.VPPLcnip0hl0ejiP14dEBLL5qa4tykux9GR5U0jc6.BC',1);

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuario_roles (usuario_id, role_id) VALUES (1,1);
INSERT INTO usuario_roles (usuario_id, role_id) VALUES (2,2);
INSERT INTO usuario_roles (usuario_id, role_id) VALUES (2,1);