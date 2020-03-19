insert into supplier (name) values ('Alphazap');
insert into supplier (name) values ('Cookley');
insert into supplier (name) values ('Zaam-Dox');
insert into supplier (name) values ('Redhold');
insert into supplier (name) values ('Sub-Ex');
insert into supplier (name) values ('Flowdesk');
insert into supplier (name) values ('Lotlux');
insert into supplier (name) values ('Trippledex');
insert into supplier (name) values ('Home Ing');
insert into supplier (name) values ('Regrant');

insert into product_category (description, name) values ('Assisted pool exercise', 'Namfix');
insert into product_category (description, name) values ('Closed valvotomy NOS', 'Stronghold');
insert into product_category (description, name) values ('Patellar sequestrectomy', 'Temp');
insert into product_category (description, name) values ('Correct everted punctum', 'Keylex');
insert into product_category (description, name) values ('Anterior rect resect NEC', 'Zamit');
insert into product_category (description, name) values ('Closed breast biopsy', 'Zoolab');
insert into product_category (description, name) values ('Applic ext fix-humerus', 'Bitwolf');
insert into product_category (description, name) values ('Pubiotomy to assist del', 'Redhold');
insert into product_category (description, name) values ('Cl red-int fix metat/tar', 'Daltfresh');
insert into product_category (description, name) values ('Exc of accessory spleen', 'Matsoft');

insert into location (address_city, address_country, address_street, name) values ('Firuzabad', 'IR', '9 Shasta Park', 'Wordify');
insert into location (address_city, address_country, address_street, name) values ('Ipoti', 'NG', '26566 Bluejay Center', 'Photolist');
insert into location (address_city, address_country, address_street, name) values ('Piskorevci', 'HR', '506 Mitchell Court', 'Skinte');
insert into location (address_city, address_country, address_street, name) values ('Марnhо', 'MK', '578 Spaight Trail', 'Jaxspan');
insert into location (address_city, address_country, address_street, name) values ('Grebkow', 'PL', '3848 Moland Park', 'Zoomzone');
insert into location (address_city, address_country, address_street, name) values ('Tujing', 'CH', '10 Merry Circle', 'Quimba');
insert into location (address_city, address_country, address_street, name) values ('Kiikala', 'FI', '88 Randy Pass', 'Tekfly');
insert into location (address_city, address_country, address_street, name) values ('Shixian', 'CH', '8009 Pine View Plaza', 'Browseblab');
insert into location (address_city, address_country, address_street, name) values ('Tadou', 'CH', '116 Mayfield Street', 'Twitterbridge');
insert into location (address_city, address_country, address_street, name) values ('Myronivka', 'UA', '30802 West Hill', 'Yakitri');


insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/116x125.jpg/ff4444/ffffff', 'Hypertroph osteoarthrop', 'Ralph Lauren Corporation', 56.87, 77.22, 4, 2);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/125x104.jpg/cc0000/ffffff', 'No medical serv in home', 'Nuveen Select Maturities Municipal Fund', 45.63, 33.29, 2, 4);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/122x249.png/ff4444/ffffff', 'Disloc 6th cerv vert-cl', 'Chemours Company (The)', 91.97, 8.22, 1, 1);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/190x239.bmp/ff4444/ffffff', 'Anemia-delivered w p/p', 'Weibo Corporation', 59.96, 74.58, 5, 6);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/134x110.bmp/cc0000/ffffff', 'Ac embl suprfcl up ext', 'Innovative Industrial Properties, Inc.', 42.58, 10.17, 10, 1);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/131x198.jpg/ff4444/ffffff', 'Spinal cord inj at birth', 'National Holdings Corporation', 19.21, 76.84, 2, 4);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/115x176.png/ff4444/ffffff', 'Dissection iliac artery', 'Globant S.A.', 13.63, 82.25, 3, 3);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/179x213.png/ff4444/ffffff', 'Foc choroiditis post NEC', 'Pieris Pharmaceuticals, Inc.', 46.96, 33.32, 5, 9);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/163x215.jpg/5fa2dd/ffffff', 'Traum arthropathy-mult', 'Modern Media Acquisition Corp.', 24.85, 49.92, 1, 5);
insert into product (image_url, description, name, price, weight, product_category_id, supplier_id) values ('http://dummyimage.com/106x159.bmp/dddddd/000000', 'Alcoholic fatty liver', 'United Continental Holdings, Inc.', 18.05, 90.92, 6, 6);


insert into stock (quantity, location_id, product_id) values (95, 1, 4);
insert into stock (quantity, location_id, product_id) values (35, 4, 6);
insert into stock (quantity, location_id, product_id) values (67, 3, 4);
insert into stock (quantity, location_id, product_id) values (51, 6, 4);
insert into stock (quantity, location_id, product_id) values (83, 10, 5);
insert into stock (quantity, location_id, product_id) values (58, 6, 10);
insert into stock (quantity, location_id, product_id) values (99, 3, 3);
insert into stock (quantity, location_id, product_id) values (22, 9, 4);
insert into stock (quantity, location_id, product_id) values (20, 5, 2);
insert into stock (quantity, location_id, product_id) values (39, 8, 2);

insert into customer (email_address, first_name, last_name, password, username) values ('btschersich0@microsoft.com', 'Boyce', 'Tschersich', 'test', 'test');
insert into customer (email_address, first_name, last_name, password, username) values ('dbliben1@cloudflare.com', 'Desirae', 'Bliben', 'rJNnMDJrXBqg', 'dbliben1');
insert into customer (email_address, first_name, last_name, password, username) values ('tboskell2@ebay.com', 'Thaddus', 'Boskell', 'rjKB8yJ', 'tboskell2');
insert into customer (email_address, first_name, last_name, password, username) values ('kcassidy3@tinyurl.com', 'Keary', 'Cassidy', 'mqUvYMuEvi', 'kcassidy3');
insert into customer (email_address, first_name, last_name, password, username) values ('rrylance4@sciencedaily.com', 'Ramonda', 'Rylance', 'iB9pKrym8ZsF', 'rrylance4');
insert into customer (email_address, first_name, last_name, password, username) values ('tfingleton5@ucsd.edu', 'Tawsha', 'Fingleton', 'L0mdQGuy', 'tfingleton5');
insert into customer (email_address, first_name, last_name, password, username) values ('jjoan6@prnewswire.com', 'Jodee', 'Joan', 'eELcPnMYU2D', 'jjoan6');
insert into customer (email_address, first_name, last_name, password, username) values ('gbacchus7@statcounter.com', 'Gaultiero', 'Bacchus', 'w4UgreU8AM1', 'gbacchus7');
insert into customer (email_address, first_name, last_name, password, username) values ('rclarkewilliams8@scribd.com', 'Rhetta', 'Clarke-Williams', '99uOIrsh', 'rclarkewilliams8');
insert into customer (email_address, first_name, last_name, password, username) values ('gblacklawe9@sun.com', 'Gaylord', 'Blacklawe', 'JJZQxiyp', 'gblacklawe9');


insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('San Mariano', 'Philippines', '158 Saint Paul Pass', '2019-12-01 13:20:07', 10);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Colorado Springs', 'United States', '1362 Old Shore Parkway', '2019-06-19 17:51:27', 4);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Mauloo', 'Indonesia', '96360 Northwestern Place', '2019-08-27 09:40:26', 3);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Kentville', 'Canada', '10 Meadow Vale Junction', '2019-08-07 12:34:16', 3);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Gent', 'Belgium', '6 Eliot Court', '2019-09-13 21:29:45', 3);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Huangshi', 'China', '877 Sunnyside Court', '2019-05-29 06:34:34',  5);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Taozhuang', 'China', '5 Spenser Plaza', '2019-12-04 21:55:15',  2);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Saint Louis', 'United States', '4586 Charing Cross Pass', '2019-10-18 03:26:09',  10);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Ostrowsko', 'Poland', '69789 Jenna Terrace', '2019-10-24 16:08:30', 3);
insert into orders (address_city, address_country, address_street, created_at, customer_id) values ('Cruz Alta', 'Brazil', '938 Sherman Way', '2020-01-29 12:14:16', 2);


insert into revenue (date, sum, location_id) values ('2019-03-02 13:56:04', 25.19, 9);
insert into revenue (date, sum, location_id) values ('2019-09-23 07:18:39', 17.68, 3);
insert into revenue (date, sum, location_id) values ('2019-03-03 00:30:59', 5.24, 2);
insert into revenue (date, sum, location_id) values ('2019-12-01 12:18:47', 27.5, 7);
insert into revenue (date, sum, location_id) values ('2019-10-30 22:30:44', 69.93, 9);
insert into revenue (date, sum, location_id) values ('2020-01-14 21:29:31', 16.13, 4);
insert into revenue (date, sum, location_id) values ('2019-06-09 01:17:51', 5.73, 9);
insert into revenue (date, sum, location_id) values ('2019-08-22 18:47:53', 55.12, 1);
insert into revenue (date, sum, location_id) values ('2019-09-29 15:32:11', 79.27, 8);
insert into revenue (date, sum, location_id) values ('2019-12-31 18:09:04', 40.13, 5);



insert into order_detail (quantity, order_id, product_id) values (5, 2, 3);
insert into order_detail (quantity, order_id, product_id) values (8, 1, 10);
insert into order_detail (quantity, order_id, product_id) values (1, 2, 9);
insert into order_detail (quantity, order_id, product_id) values (1, 2, 10);
insert into order_detail (quantity, order_id, product_id) values (4, 5, 8);
insert into order_detail (quantity, order_id, product_id) values (9, 7, 1);
insert into order_detail (quantity, order_id, product_id) values (5, 7, 9);
insert into order_detail (quantity, order_id, product_id) values (10, 2, 5);
insert into order_detail (quantity, order_id, product_id) values (1, 10, 3);
insert into order_detail (quantity, order_id, product_id) values (4, 10, 10);