-- Ejemplos de uso del sistema de categorías
-- REGLA PRINCIPAL: permitir_inscripcion = false → BLOQUEADA (Primera, Segunda)
--                  permitir_inscripcion = true  → ABIERTA (Veteranos)

-- ============================================================
-- 1. VER CATEGORÍAS Y SU ESTADO
-- ============================================================
SELECT id, nombre, edad_minima, edad_maxima, permitir_inscripcion, activa
FROM categorias
WHERE activa = true;

-- Resultado esperado:
-- id | nombre           | edad_min | edad_max | permitir | activa
-- 1  | Primera División | 18       | 30       | FALSE    | TRUE     ← BLOQUEADA
-- 2  | Segunda División | 30       | 40       | FALSE    | TRUE     ← BLOQUEADA
-- 3  | Veteranos        | 41       | NULL     | TRUE     | TRUE     ← ABIERTA

-- ============================================================
-- 2. VER CATEGORÍAS SOLO PERMITIDAS (ABIERTAS)
-- ============================================================
SELECT id, nombre, edad_minima
FROM categorias
WHERE activa = true AND permitir_inscripcion = true;

-- Resultado: Solo Veteranos

-- ============================================================
-- 3. AGREGAR CATEGORÍAS A UN TORNEO (id=1)
-- ============================================================
INSERT INTO categoria_torneo (id_torneo, id_categoria, activa, orden)
VALUES 
(1, 1, true, 1),  -- Primera División
(1, 2, true, 2),  -- Segunda División
(1, 3, true, 3);  -- Veteranos

-- ============================================================
-- 4. VER CATEGORÍAS DEL TORNEO
-- ============================================================
SELECT ct.id, c.nombre, c.edad_minima, c.edad_maxima, c.permitir_inscripcion, ct.activa
FROM categoria_torneo ct
JOIN categorias c ON ct.id_categoria = c.id
WHERE ct.id_torneo = 1 AND ct.activa = true
ORDER BY ct.orden;

-- ============================================================
-- 5. INTENTAR INSCRIBIR JUGADOR (VÍA API - LOS RESULTADOS SON):
-- ============================================================

-- Caso 1: Jugador 50 años → Veteranos
-- URL: POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=3
-- RESULTADO: ✅ 201 Created
-- Razón: edad=50 > 41 (edad_minima), permitir_inscripcion=true

-- Caso 2: Jugador 25 años → Primera División
-- URL: POST /api/jugador-categoria/inscribir?jugadorId=2&categoriaTorneoId=1
-- RESULTADO: ❌ 400 Bad Request
-- Error: "La categoría 'Primera División' no permite inscripción. Solo la de Veteranos permite"
-- Razón: permitir_inscripcion=false

-- Caso 3: Jugador 50 años → Veteranos, luego → Segunda
-- URL: POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=3
-- RESULTADO: ✅ 201 Created
-- URL: POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=2
-- RESULTADO: ❌ 400 Bad Request
-- Error: "Ya está inscrito en categoría 'Veteranos'. Un jugador máximo 1 por torneo"
-- Razón: Ya tiene 1 inscripción en este torneo

-- ============================================================
-- 6. VER JUGADORES INSCRITOS EN VETERANOS
-- ============================================================
SELECT j.id, j.nombre, j.apellido, 
       YEAR(CURDATE()) - YEAR(j.fecha_nacimiento) as edad,
       jec.fecha_inscripcion
FROM jugador_en_categoria jec
JOIN jugadores j ON jec.id_jugador = j.id
JOIN categoria_torneo ct ON jec.id_categoria_torneo = ct.id
JOIN categorias c ON ct.id_categoria = c.id
WHERE c.nombre = 'Veteranos' AND jec.activo = true AND ct.id_torneo = 1;

-- ============================================================
-- 7. VER CATEGORÍAS EN LAS QUE ESTÁ INSCRITO UN JUGADOR
-- ============================================================
SELECT c.nombre, c.edad_minima, 
       YEAR(CURDATE()) - YEAR(j.fecha_nacimiento) as edad_jugador,
       jec.fecha_inscripcion
FROM jugador_en_categoria jec
JOIN jugadores j ON jec.id_jugador = j.id
JOIN categoria_torneo ct ON jec.id_categoria_torneo = ct.id
JOIN categorias c ON ct.id_categoria = c.id
WHERE j.id = 1 AND jec.activo = true;

-- ============================================================
-- 8. VERIFICAR RESTRICCIÓN: 1 CATEGORÍA POR TORNEO
-- ============================================================
-- Contar inscripciones activas de un jugador en un torneo
SELECT COUNT(*) as inscripciones_activas
FROM jugador_en_categoria jec
JOIN categoria_torneo ct ON jec.id_categoria_torneo = ct.id
WHERE jec.id_jugador = 1 AND ct.id_torneo = 1 AND jec.activo = true;

-- Resultado esperado: 0 o 1 (nunca > 1)

-- ============================================================
-- 9. DESACTIVAR INSCRIPCIÓN
-- ============================================================
UPDATE jugador_en_categoria
SET activo = false
WHERE id = 1;

-- ============================================================
-- 10. REPORTE: JUGADORES POR EDAD EN VETERANOS
-- ============================================================
SELECT j.nombre, j.apellido,
       YEAR(CURDATE()) - YEAR(j.fecha_nacimiento) as edad,
       CASE 
           WHEN YEAR(CURDATE()) - YEAR(j.fecha_nacimiento) >= 41 THEN 'Apto'
           ELSE 'No Apto'
       END as aptitud
FROM jugador_en_categoria jec
JOIN jugadores j ON jec.id_jugador = j.id
JOIN categoria_torneo ct ON jec.id_categoria_torneo = ct.id
JOIN categorias c ON ct.id_categoria = c.id
WHERE c.nombre = 'Veteranos' AND ct.id_torneo = 1 AND jec.activo = true
ORDER BY edad DESC;

-- ============================================================
-- NOTAS IMPORTANTES:
-- ============================================================
-- 1. permitir_inscripcion = false → BLOQUEADA
--    - No aparece en dropdown de categorías disponibles
--    - Si intentas inscribir, API rechaza con mensaje claro
--    - Validación ocurre EN EL BACKEND (no solo frontend)
--
-- 2. permitir_inscripcion = true → ABIERTA
--    - Aparece en dropdown de categorías disponibles
--    - Requiere cumplir edad_minima
--    - Validación de edad ocurre en tiempo real
--
-- 3. UN JUGADOR = MÁXIMO 1 CATEGORÍA POR TORNEO
--    - UNIQUE constraint en BD: (id_jugador, id_categoria_torneo)
--    - Validación también en servicio Java
--    - Si intenta duplicado, API rechaza inmediatamente
--
-- 4. SOFT DELETE: activo = false (no elimina, solo desactiva)
--    - Permite auditoría completa
--    - Recuperación fácil si es necesario
-- ============================================================
