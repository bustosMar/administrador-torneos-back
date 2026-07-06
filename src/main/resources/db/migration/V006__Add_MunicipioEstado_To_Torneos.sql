-- Agregar relación MunicipioEstado a Torneo
ALTER TABLE torneos 
ADD COLUMN id_municipio_estado BIGINT,
ADD CONSTRAINT fk_torneos_municipio_estado FOREIGN KEY (id_municipio_estado) 
    REFERENCES municipio_estado(id);

-- Crear índice para mejorar el rendimiento de consultas
CREATE INDEX idx_torneos_municipio_estado ON torneos(id_municipio_estado);
