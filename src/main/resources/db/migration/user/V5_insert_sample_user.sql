INSERT INTO userss(username, password, enabled, role)
VALUES ('NAT', '$2a$12$NIvCRGWixWCO1aif8nlJ.ONV3WHZyS1GAmVfPjD.y2M2E4IZgs0Sy', TRUE, 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;