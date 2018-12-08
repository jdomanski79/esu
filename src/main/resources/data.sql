insert into equipment (state,asset, to_delete,  name, inventory_number)
values
(0,false,false,' Kompresor AIRPRESS ','  '),
(0,false,false,' Monitor Belinea 17"" ',' 2561/307/ '),
(0,false,false,' Komputer HP Compaq ',' 2388/287 ');



insert into user (username,  password, CREDENTIALS_NON_EXPIRED, name)
values 
('jarek','$2a$04$Q1a7bH9YPQ9S3YlZdPGudejvJRrUmGwph2qBdGoF66aefU5OaU40G', true, 'JD'),
('mariusz', '$2a$12$BEpUZhok00A8oUL.XT4X2ezZ9A01eFKEZxD9qyWv2Plm9X8H8RE/K', true, 'MZ');

insert into equipment_event (equipment_state, entered_by, equipment_id)
values ( 0,1,1 ), (0,1,2), (0,1,3);