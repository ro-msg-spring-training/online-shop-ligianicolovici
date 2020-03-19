CREATE TABLE IF NOT EXISTS `supplier` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100)

);

CREATE TABLE IF NOT EXISTS `customer` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email_address` varchar(60),
    `first_name` varchar(50),
    `last_name` varchar(50),
    `password` varchar(20),
    `username` varchar(30)

);

CREATE TABLE IF NOT EXISTS `location` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `address_city` varchar(30),
    `address_country` varchar(30),
    `address_street` varchar(40),
    `name` varchar(20)

);

CREATE TABLE IF NOT EXISTS `order_detail` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `quantity` integer,
    `order_id` integer,
    `product_id` integer

);

CREATE TABLE IF NOT EXISTS `orders` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `address_city` varchar(30),
    `address_country` varchar(30),
    `address_street` varchar(40),
    `created_at` datetime,
    `customer_id` integer

);
 CREATE TABLE IF NOT EXISTS order_location (

    primary key (order_id, location_id),
    `order_id` integer not null,
    `location_id` integer not null

 );

CREATE TABLE IF NOT EXISTS `product` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `image_url` varchar(120),
    `description` varchar(255),
    `name` varchar(40),
    `price` decimal(19,2),
    `weight` double precision,
    `product_category_id` integer,
    `supplier_id` integer

);

CREATE TABLE IF NOT EXISTS `product_category` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `description` varchar(255),
    `name` varchar(40)

);
CREATE TABLE IF NOT EXISTS `revenue` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `date` date,
    `sum` decimal(19,2),
    `location_id` integer

);
CREATE TABLE IF NOT EXISTS `stock` (

    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    quantity integer,
    location_id integer,
    product_id integer

);

ALTER TABLE `order_detail` ADD CONSTRAINT FK_Product FOREIGN KEY (product_id)  references product (id);
ALTER TABLE `order_detail` ADD CONSTRAINT FK_Order FOREIGN KEY (order_id)  references orders(id);
ALTER TABLE `order_location` ADD CONSTRAINT FK_OrderID FOREIGN KEY (order_id) references orders(id);
ALTER TABLE `order_location` ADD CONSTRAINT FK_LocationID FOREIGN KEY (location_id) references location(id);
ALTER TABLE `orders` ADD CONSTRAINT FK_Customer FOREIGN KEY (customer_id) references customer (id);
ALTER TABLE `product` ADD CONSTRAINT FK_ProductCategory FOREIGN KEY (product_category_id) references product_category (id);
ALTER TABLE `product` ADD CONSTRAINT FK_Supplier FOREIGN KEY (supplier_id) references supplier (id);
ALTER TABLE `revenue` ADD CONSTRAINT FK_Location FOREIGN KEY (location_id) references location (id);
ALTER TABLE `stock` ADD CONSTRAINT FK_StockLocation FOREIGN KEY(location_id) references location (id);
ALTER TABLE `stock` ADD CONSTRAINT FK_ProductStock FOREIGN KEY (product_id) references product (id);
