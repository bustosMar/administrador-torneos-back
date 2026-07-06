-- Add CategoriaTorneo relationship to EquiposEnTorneo
ALTER TABLE equipos_en_torneo ADD COLUMN id_categoria_torneo BIGINT;
ALTER TABLE equipos_en_torneo ADD CONSTRAINT fk_equipos_en_torneo_categoria_torneo 
  FOREIGN KEY (id_categoria_torneo) REFERENCES categoria_torneo(id);
CREATE INDEX idx_equipos_en_torneo_categoria_torneo ON equipos_en_torneo(id_categoria_torneo);
