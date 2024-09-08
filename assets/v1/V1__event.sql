-- Creating the Event table
CREATE TABLE Event (
    id_event SERIAL PRIMARY KEY,
    event_name VARCHAR(100),
    image_url VARCHAR(255) DEFAULT NULL,
    number_of_vouchers INT DEFAULT 0,
    remaining_vouchers INT DEFAULT 0,
    share_count INT DEFAULT 0,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    deleted_date TIMESTAMP DEFAULT NULL,
    created_by INT
);

-- Creating the BrandsCooperation table
CREATE TABLE BrandsCooperation (
    id_event_id_brand SERIAL PRIMARY KEY,
    id_event INT,
    id_brand INT,
    name_brand VARCHAR(100),
    FOREIGN KEY (id_event) REFERENCES Event(id_event)
);

-- Creating the FavouriteEvent table
CREATE TABLE FavouriteEvent (
    id_event_id_player SERIAL PRIMARY KEY,
    id_player INT,
    id_event INT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    deleted_date TIMESTAMP,
    FOREIGN KEY (id_event) REFERENCES Event(id_event)
);