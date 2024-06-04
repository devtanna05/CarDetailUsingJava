

-- Table for storing car details
CREATE TABLE car_rental_db.cars (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price VARCHAR(50) NOT NULL,
    engine VARCHAR(50) NOT NULL,
    fule VARCHAR(50) NOT NULL,
    transmission VARCHAR(50) NOT NULL,
    seating VARCHAR(50) NOT NULL
);
