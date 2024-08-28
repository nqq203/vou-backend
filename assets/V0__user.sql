CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL CHECK (octet_length(password) = 60), -- bcrypt hashes are typically 60 bytes long
    full_name VARCHAR(100),
    email VARCHAR(100),
    phone_number VARCHAR(20),
    locked_date TIMESTAMP,
    role VARCHAR(50) NOT NULL CHECK (role IN ('PLAYER', 'ADMIN', 'BRAND')),
    status VARCHAR(50) DEFAULT 'pending' CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING')),
    address VARCHAR(255),
    avatar_url VARCHAR(255)
);

-- Inserting mock data into the User table
-- Assuming bcrypt hashes for 'password1' and 'password2'
INSERT INTO users (username, password, full_name, email, phone_number, role, status, address, avatar_url)
VALUES
('testplayer', '$2a$10$ejcpe4FIQHIcWmeOq5VzFuTDE8bpDuU.qj4c5l/KQltDAR390XSQK', 'The Anh test player', 'john.doe@example.com', '1234567890', 'PLAYER', 'ACTIVE', 'Somewhere on earth', 'http://example.com/avatar1.jpg'),
('testadmin', '$2a$10$ejcpe4FIQHIcWmeOq5VzFuTDE8bpDuU.qj4c5l/KQltDAR390XSQK', 'The Anh test admin', 'jane.doe@example.com', '0987654321', 'ADMIN', 'ACTIVE', 'Somewhere on earth', 'http://example.com/avatar1.jpg'),
('testbrand', '$2a$10$ejcpe4FIQHIcWmeOq5VzFuTDE8bpDuU.qj4c5l/KQltDAR390XSQK', 'The Anh test brand', 'jane.doe@example.com', '0987654321', 'BRAND', 'ACTIVE', 'Somewhere on earth', 'http://example.com/avatar1.jpg');


-- Creating the Session table
CREATE TABLE sessions (
    id_session VARCHAR(255) PRIMARY KEY,
    id_user INT REFERENCES users(id_user),
    is_active BOOLEAN DEFAULT TRUE,
    logout_at TIMESTAMP,
    expiration_time TIMESTAMP NOT NULL
);

-- Creating the Admin table
CREATE TABLE admins (
    id_user INT PRIMARY KEY REFERENCES users(id_user)
);

-- Inserting mock data into the Admin table
INSERT INTO admins (id_user)
VALUES (2);

-- Creating the Player table
CREATE TABLE players (
    id_user INT PRIMARY KEY REFERENCES users(id_user),
    gender VARCHAR(50) CHECK (gender IN ('MALE', 'FEMALE')),
    facebook VARCHAR(100)
);

-- Inserting mock data into the Player table
INSERT INTO players (id_user, gender, facebook)
VALUES
(1, 'MALE', 'http://facebook.com/chubenoithat3433');

-- Creating the Brand table
CREATE TABLE brands (
    id_user INT PRIMARY KEY REFERENCES users(id_user),
    field VARCHAR(50),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8)
);

-- Inserting mock data into the Brand table
INSERT INTO brands (id_user, field, latitude, longitude)
VALUES
(3, 'Momo', 37.7749, -122.4194);