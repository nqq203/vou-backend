-- Create the Item table
CREATE TABLE Item (
    id_item SERIAL PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    image_url TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_event INT
);

-- Create the ItemRepo table
CREATE TABLE ItemRepo (
    id_itemrepo SERIAL PRIMARY KEY,
    id_player INT NOT NULL,
    id_item INT NOT NULL,
    amount INT DEFAULT 0,
    FOREIGN KEY (id_item) REFERENCES Item(id_item)
);

-- Create the GiftLog table
CREATE TABLE GiftLog (
    id_giftlog SERIAL PRIMARY KEY,
    uid_receiver INT NOT NULL,
    uid_sender INT NOT NULL,
    sender_name VARCHAR(50),
    receiver_name VARCHAR(50),
    id_item INT NOT NULL,
    amount INT DEFAULT 1,
    give_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_item) REFERENCES Item(id_item)
);

-- Create the Voucher table
CREATE TABLE Voucher (
    code VARCHAR(50) PRIMARY KEY,
    qr_code TEXT DEFAULT NULL,
    voucher_name VARCHAR(100) NOT NULL,
    voucher_price INT DEFAULT 10000,
    image_url TEXT DEFAULT NULL,
    expiration_date DATE NOT NULL,
    description TEXT DEFAULT NULL,
    type VARCHAR(10) DEFAULT 'online' CHECK (type IN ('online', 'offline')),
    id_item1 INT DEFAULT NULL,
    id_item2 INT DEFAULT NULL,
    id_item3 INT DEFAULT NULL,
    id_item4 INT DEFAULT NULL,
    id_item5 INT DEFAULT NULL,
    aim_coin INT DEFAULT 0,
    id_event INT DEFAULT NULL,
    status VARCHAR(15) CHECK (status IN ('pending', 'active', 'inactive', 'expired')), 
    FOREIGN KEY (id_item1) REFERENCES Item(id_item),
    FOREIGN KEY (id_item2) REFERENCES Item(id_item),
    FOREIGN KEY (id_item3) REFERENCES Item(id_item),
    FOREIGN KEY (id_item4) REFERENCES Item(id_item),
    FOREIGN KEY (id_item5) REFERENCES Item(id_item)
);

-- Create the VoucherRepo table
CREATE TABLE VoucherRepo (
    id_voucherrepo SERIAL PRIMARY KEY,
    id_player INT NOT NULL,
    code_voucher VARCHAR(50) NOT NULL,
    amount INT DEFAULT 0,
    FOREIGN KEY (code_voucher) REFERENCES Voucher(code)
);

INSERT INTO Item (item_name, image_url)
VALUES
('Chó', 'https://vou-storage.s3.ap-southeast-2.amazonaws.com/happy.png'),
('Mèo', 'https://vou-storage.s3.ap-southeast-2.amazonaws.com/kitty.png'),
('Gà', 'https://vou-storage.s3.ap-southeast-2.amazonaws.com/hen.png'),
('Rùa', 'https://vou-storage.s3.ap-southeast-2.amazonaws.com/turtle.png'),
('Xu', 'https://vou-storage.s3.ap-southeast-2.amazonaws.com/coin.png');