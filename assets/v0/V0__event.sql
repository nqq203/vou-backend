-- Creating the Event table
CREATE TABLE Event (
    id_event SERIAL PRIMARY KEY,
    event_name VARCHAR(100),
    image_url VARCHAR(255) DEFAULT NULL,
    number_of_vouchers INT DEFAULT 0,
    start_date TIMESTAMP,
    end_date TIMESTAMP
);

-- Creating the BrandsCooperation table
CREATE TABLE BrandsCooperation (
    id_event_id_brand SERIAL PRIMARY KEY,
    id_event INT,
    id_brand INT,
    FOREIGN KEY (id_event) REFERENCES Event(id_event)
    -- Assuming Brand table exists with id_brand as a key
);

-- Creating the FavouriteEvent table
CREATE TABLE FavouriteEvent (
    id_event_id_player SERIAL PRIMARY KEY,
    id_event INT,
    id_player INT,
    FOREIGN KEY (id_event) REFERENCES Event(id_event)
    -- Assuming Player table exists with id_player as a key
);

-- Inserting mock data into Event table
INSERT INTO Event (event_name, image_url, start_date, end_date) VALUES
('Chinh phục Loopy', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fpororo.fandom.com%2Fwiki%2FLoopy&psig=AOvVaw1eIHaxrTCRTuLkfZHq4Baj&ust=1724559576507000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCKjzypfjjIgDFQAAAAAdAAAAABAE', '2024-09-15 10:00:00', '2024-09-20 18:00:00'),
('Lắc xì moomoo', 'http://example.com/musicfest.png', '2024-10-05 12:00:00', '2024-10-07 23:59:00');



INSERT INTO BrandsCooperation (id_event, id_brand) VALUES
(1, 3), (2, 3);

INSERT INTO FavouriteEvent (id_event, id_player) VALUES
(1, 1),
(2, 1);


