-- =========================
-- APARTMENTS TABLE DATA
-- =========================
-- INSERT INTO apartment (id, city, name) VALUES
-- (1, 'Paris', 'Eiffel Tower View Apartment'),
-- (2, 'Paris', 'Montmartre Cozy Studio'),
-- (3, 'Paris', 'Seine Riverside Loft'),

-- (4, 'Berlin', 'Alexanderplatz Central Flat'),
-- (5, 'Berlin', 'Berlin Wall Historic Stay'),

-- (6, 'London', 'Westminster Luxury Apartment'),
-- (7, 'London', 'Camden Town Studio'),
-- (10, 'London', 'Canary Wharf Business Stay'),

-- (8, 'Rome', 'Colosseum View Apartment'),
-- (9, 'Rome', 'Vatican City Guesthouse'),
-- (11, 'Rome', 'Trastevere Riverside Apartment');


-- =========================
-- BOOKINGS TABLE DATA
-- (ONLY for availability simulation)
-- =========================

-- INSERT INTO bookings (id, apartment_id, check_in_date, check_out_date) VALUES
-- -- Paris: one apartment booked in Jan 2025
-- (1, 1, '2025-01-01', '2025-01-05'),

-- -- Berlin: one apartment booked in Feb 2025
-- (2, 4, '2025-02-10', '2025-02-15'),

-- -- London: one apartment booked in March 2025
-- (3, 6, '2025-03-01', '2025-03-07'),

-- -- Rome: one apartment booked in April 2025
-- (4, 8, '2025-04-05', '2025-04-10');

-- INSERT INTO apartment (
--   id,
--   address,
--   areaInSquareMeters,
--   createdBy,
--   createdDate,
--   deletedBy,
--   deletedDate,
--   description,
--   distanceToCenterInKm,
--   floor,
--   isDeleted,
--   isFurnished,
--   isVisible,
--   name,
--   noiseLevel,
--   pricePerDay,
--   updatedBy,
--   updatedDate
-- ) VALUES (
--   '3fa85f64-5717-4562-b3fc-2c963f66afa6', -- UUID
--   'Ataturk Bulvari 123, Cankaya, Ankara',  -- address
--   55.5,                                    -- areaInSquareMeters
--   'system',                                -- createdBy
--   '2025-01-04T23:36:00+02:00',             -- createdDate (ISO offset)
--   NULL,                                    -- deletedBy
--   NULL,                                    -- deletedDate
--   'Cozy two-room apartment near the city center', -- description
--   1.2,                                     -- distanceToCenterInKm
--   3,                                       -- floor
--   FALSE,                                   -- isDeleted
--   TRUE,                                    -- isFurnished
--   TRUE,                                    -- isVisible
--   'Central Cozy Apt',                      -- name
--   1,                                       -- noiseLevel (EnumType.ORDINAL)
--   79.99,                                   -- pricePerDay
--   NULL,                                    -- updatedBy
--   NULL                                     -- updatedDate
-- );

select * from apartment;