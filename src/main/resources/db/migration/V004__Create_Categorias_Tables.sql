-- Flyway Migration: V004__Create_Categorias_Tables.sql
-- Descripción: Crear tablas para sistema de categorías
-- Reglas: 
--   1. Categorías con permitirInscripcion=false están BLOQUEADAS (Primera, Segunda)
--   2. Categorías con permitirInscripcion=true permiten inscripción (Veteranos)
--   3. Un jugador máximo 1 categoría activa por torneo
-- Fecha: 2026-07-03

-- Tabla de Categorías
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    edad_minima INT,
    edad_maxima INT,
    descripcion TEXT,
    permitir_inscripcion BOOLEAN NOT NULL DEFAULT false,  -- false=BLOQUEADA (Primera, Segunda), true=ABIERTA (Veteranos)
    activa BOOLEAN NOT NULL DEFAULT true,
    
    CONSTRAINT chk_edad_rango CHECK (
        edad_minima IS NULL OR 
        edad_maxima IS NULL OR 
        edad_minima <= edad_maxima
    ),
    
    INDEX idx_categorias_nombre (nombre),
    INDEX idx_categorias_activa (activa),
    INDEX idx_categorias_permitir (permitir_inscripcion)
);

-- Tabla de relación Categoría-Torneo
CREATE TABLE IF NOT EXISTS categoria_torneo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_torneo BIGINT NOT NULL,
    id_categoria BIGINT NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT true,
    orden INT,
    
    CONSTRAINT uk_categoria_torneo UNIQUE(id_torneo, id_categoria),
    CONSTRAINT fk_cat_tor_torneo FOREIGN KEY(id_torneo) REFERENCES torneos(id) ON DELETE CASCADE,
    CONSTRAINT fk_cat_tor_categoria FOREIGN KEY(id_categoria) REFERENCES categorias(id) ON DELETE CASCADE,
    
    INDEX idx_categoria_torneo_torneo (id_torneo),
    INDEX idx_categoria_torneo_categoria (id_categoria),
    INDEX idx_categoria_torneo_activa (activa)
);

-- Tabla de inscripción de Jugador en Categoría
CREATE TABLE IF NOT EXISTS jugador_en_categoria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_jugador BIGINT NOT NULL,
    id_categoria_torneo BIGINT NOT NULL,
    fecha_inscripcion DATE NOT NULL DEFAULT CURDATE(),
    activo BOOLEAN NOT NULL DEFAULT true,
    
    CONSTRAINT uk_jugador_categoria UNIQUE(id_jugador, id_categoria_torneo),
    CONSTRAINT fk_jec_jugador FOREIGN KEY(id_jugador) REFERENCES jugadores(id) ON DELETE CASCADE,
    CONSTRAINT fk_jec_categoria_torneo FOREIGN KEY(id_categoria_torneo) REFERENCES categoria_torneo(id) ON DELETE CASCADE,
    
    INDEX idx_jec_jugador (id_jugador),
    INDEX idx_jec_categoria_torneo (id_categoria_torneo),
    INDEX idx_jec_activo (activo),
    INDEX idx_jec_fecha (fecha_inscripcion)
);

-- Insertar categorías por defecto
INSERT IGNORE INTO categorias (nombre, edad_minima, edad_maxima, permitir_inscripcion, descripcion, activa) VALUES
('Primera División', 18, 30, false, 'BLOQUEADA - Categoría de Primera División', true),
('Segunda División', 30, 40, false, 'BLOQUEADA - Categoría de Segunda División', true),
('Veteranos', 41, NULL, true, 'ABIERTA - Categoría para jugadores veteranos (41+ años)', true);
