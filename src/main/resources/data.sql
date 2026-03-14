-- ── Warehouses ─────────────────────────────────────────────────────
INSERT INTO warehouses (name, latitude, longitude, city, state, active) VALUES
('BLR_Warehouse',  12.99999, 37.923273, 'Bangalore', 'Karnataka',   true),
('MUMB_Warehouse', 11.99999, 27.923273, 'Mumbai',    'Maharashtra', true);


-- ── Sellers ────────────────────────────────────────────────────────
INSERT INTO sellers (name, email, phone, latitude, longitude, city, state, active) VALUES
('Nestle Seller', 'nestle@seller.com', '9000000001', 13.08268, 80.27045, 'Chennai',   'Tamil Nadu',  true),
('Rice Seller',   'rice@seller.com',   '9000000002', 19.07283, 72.88261, 'Mumbai',    'Maharashtra', true),
('Sugar Seller',  'sugar@seller.com',  '9000000003', 28.70406, 77.10249, 'Delhi',     'Delhi',       true);


-- ── Customers ──────────────────────────────────────────────────────
INSERT INTO customers (name, phone, email, latitude, longitude, address, city, state, pincode) VALUES
('Shree Kirana Store', '9847000001', 'shree@kirana.com',  11.232, 23.445, '12 MG Road',    'Faridabad',  'Haryana',        '121001'),
('Andheri Mini Mart',  '9101000002', 'andheri@kirana.com', 17.232, 33.445, '45 SV Road',   'Andheri',    'Maharashtra',    '400053');


-- ── Products (seller_id links to sellers table) ────────────────────
INSERT INTO products (name, price, weight_kg, dimension_length_cm, dimension_width_cm, dimension_height_cm, category, available, seller_id) VALUES
('Maggie 500g Packet', 10.0,  0.5,  10.0,   10.0,  10.0,  'Food',    true, 1),
('Rice Bag 10kg',      500.0, 10.0, 1000.0, 800.0, 500.0, 'Grocery', true, 2),
('Sugar Bag 25kg',     700.0, 25.0, 1000.0, 900.0, 600.0, 'Grocery', true, 3);