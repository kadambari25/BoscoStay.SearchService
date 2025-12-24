-- =========================
-- APARTMENTS TABLE DATA
-- =========================

INSERT INTO apartment (id, city, name) VALUES
(1, 'Paris', 'Eiffel Tower View Apartment'),
(2, 'Paris', 'Montmartre Cozy Studio'),
(3, 'Paris', 'Seine Riverside Loft'),

(4, 'Berlin', 'Alexanderplatz Central Flat'),
(5, 'Berlin', 'Berlin Wall Historic Stay'),

(6, 'London', 'Westminster Luxury Apartment'),
(7, 'London', 'Camden Town Studio'),
(10, 'London', 'Canary Wharf Business Stay'),

(8, 'Rome', 'Colosseum View Apartment'),
(9, 'Rome', 'Vatican City Guesthouse'),
(11, 'Rome', 'Trastevere Riverside Apartment');


-- =========================
-- BOOKINGS TABLE DATA
-- (ONLY for availability simulation)
-- =========================

INSERT INTO bookings (id, apartment_id, check_in_date, check_out_date) VALUES
-- Paris: one apartment booked in Jan 2025
(1, 1, '2025-01-01', '2025-01-05'),

-- Berlin: one apartment booked in Feb 2025
(2, 4, '2025-02-10', '2025-02-15'),

-- London: one apartment booked in March 2025
(3, 6, '2025-03-01', '2025-03-07'),

-- Rome: one apartment booked in April 2025
(4, 8, '2025-04-05', '2025-04-10');
