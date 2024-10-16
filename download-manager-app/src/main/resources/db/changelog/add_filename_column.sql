ALTER TABLE export.users_files ADD COLUMN file_name VARCHAR(255);
ALTER TABLE export.users_files ALTER COLUMN file_name SET DEFAULT ' ';
UPDATE export.users_files SET file_name = ' ';