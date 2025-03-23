ALTER TABLE customer
ADD profile_image_id VARCHAR(255);

ALTER TABLE customer
ADD CONSTRAINT profile_image_id_unique UNIQUE (profile_image_id);