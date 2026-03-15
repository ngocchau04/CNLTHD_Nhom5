INSERT INTO inventory (sku_code, quantity) VALUES
  ('iphone_13', 10),
  ('iphone_13_red', 0)
ON DUPLICATE KEY UPDATE quantity = VALUES(quantity);
