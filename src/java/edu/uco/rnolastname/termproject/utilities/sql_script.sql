-- create table CLIENT_GROUP(    
--     ID INTEGER NOT NULL PRIMARY KEY 
--         GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
--     MODIFIEDDATE TIMESTAMP NOT NULL,
--     USERNAME VARCHAR(255) NOT NULL,
--     GROUP_TYPE VARCHAR(255) NOT NULL,
--     CONSTRAINT USERNAME_FK FOREIGN KEY (USERNAME) REFERENCES CLIENT(USERNAME) ON UPDATE RESTRICT ON DELETE CASCADE,
--     CONSTRAINT GROUP_FK FOREIGN KEY (GROUP_TYPE) REFERENCES GROUPS(GROUP_TYPE) ON UPDATE RESTRICT ON DELETE CASCADE 
-- );
-- 
-- CREATE TABLE ORDER_DETAIL(
--     ID INTEGER NOT NULL PRIMARY KEY 
--         GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),
--     MODIFIEDDATE TIMESTAMP NOT NULL, 
--     USERNAME VARCHAR(255) NOT NULL,
--     ORDERID INTEGER NOT NULL,
--     ITEMID INTEGER NOT NULL,
--     ORDERQUANTITY INTEGER,
--     ORDERTOTAL DECIMAL(7,2),
--     CONSTRAINT CUSTOMER_FK FOREIGN KEY(USERNAME) REFERENCES CLIENT(USERNAME) ON UPDATE RESTRICT,
--     CONSTRAINT ORDER_FK FOREIGN KEY(ORDERID) REFERENCES ORDERS(ID) ON UPDATE RESTRICT,
--     CONSTRAINT ITEM_FK FOREIGN KEY(ITEMID) REFERENCES ITEM(ID) ON UPDATE RESTRICT
-- );

-- INSERT INTO USERS(MODIFIED_DATE,USERNAME,PASSWORD,FIRSTNAME,LASTNAME,PHONE,EMAIL,STREET,CITY,STATE,GENDER) 
-- VALUES(CURRENT_TIMESTAMP,'renaldi',
-- '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',
-- 'Ray',
-- 'Renaldi','405-123-3214',
-- 'Renaldi.sim@gmail.com',
-- '400 chartrand ave', 'edmond', 'OK',
-- 'male');
-- 
-- INSERT INTO USERS(MODIFIED_DATE,USERNAME,PASSWORD,FIRSTNAME,LASTNAME,PHONE,EMAIL,STREET,CITY,STATE,GENDER) 
-- VALUES(CURRENT_TIMESTAMP,'jingying',
-- '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',
-- 'Jing Ying',
-- 'Zhou','405-123-3214',
-- 'Jingying.zhou@gmail.com',
-- '400 chartrand ave', 'edmond', 'OK',
-- 'female');

-- INSERT INTO GROUPS(ID,MODIFIEDDATE,GROUP_TYPE,GROUP_DESCRIPTION) VALUES(1,CURRENT_TIMESTAMP,'ADMIN','Client Administrator Account');
-- INSERT INTO GROUPS(ID,MODIFIEDDATE,GROUP_TYPE,GROUP_DESCRIPTION) VALUES(2,CURRENT_TIMESTAMP,'CLIENT','Regular Client Account');

-- insert into client(ID,MODIFIEDDATE,USERNAME,PASSWORD,
-- FIRSTNAME,LASTNAME,PHONE,EMAIL,CLIENT_STREET,CLIENT_CITY,CLIENT_STATE) values(1,'2015-04-05 22:34:00',
-- 'renaldi','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',
-- 'ray','renaldi','9099187500','renaldi.sim@gmail.com','414 Chartrand Ave','Edmond','OK');
-- insert into groups(ID,MODIFIEDDATE,GROUP_TYPE,GROUP_DESCRIPTION) values(1,'2015-04-05 22:34:00',
-- 'ADMIN','Client Administrator Account');
-- insert into groups(ID,MODIFIEDDATE,GROUP_TYPE,GROUP_DESCRIPTION) values(2,'2015-04-05 22:34:00',
-- 'CLIENT','Regular Client Account');
-- insert into client_group(MODIFIEDDATE,USERNAME,GROUP_TYPE) values(CURRENT_TIMESTAMP,'renaldi','ADMIN');
-- insert into client_group(MODIFIEDDATE,USERNAME,GROUP_TYPE) values(CURRENT_TIMESTAMP,'renaldi','CLIENT');

-- create view CLIENTGROUP_VIEW(USERNAME,GROUP_TYPE) as
-- SELECT CLIENT.USERNAME,GROUPS.GROUP_TYPE FROM CLIENTGROUP 
-- LEFT JOIN CLIENT ON CLIENTGROUP.CLIENTID=CLIENT.ID 
-- RIGHT JOIN GROUPS ON CLIENTGROUP.GROUPID=GROUPS.ID
