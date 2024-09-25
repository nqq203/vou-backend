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
    avatar_url TEXT
);

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

-- Creating the Player table
CREATE TABLE players (
    id_user INT PRIMARY KEY REFERENCES users(id_user),
    gender VARCHAR(50) CHECK (gender IN ('MALE', 'FEMALE')),
    facebook_url VARCHAR(100) DEFAULT NULL
);

-- Creating the Brand table
CREATE TABLE brands (
    id_user INT PRIMARY KEY REFERENCES users(id_user),
    field VARCHAR(50),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8)
);