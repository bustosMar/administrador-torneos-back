-- Agregar columna activo a la tabla torneos
ALTER TABLE torneos 
ADD COLUMN activo BOOLEAN NOT NULL DEFAULT TRUE;
