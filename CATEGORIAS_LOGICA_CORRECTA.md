# 🎯 Sistema de Categorías - Lógica Correcta (ACTUALIZADO)

## Requiero Capítulo

**"Un jugador podrá jugar en DOS categorías por torneo ya sea Primera O Segunda MAS Veteranos"**

## ✅ Reglas de Validación

### 1️⃣ Máximo 2 Categorías por Torneo
Un jugador NO puede estar en más de 2 categorías en el mismo torneo.

```
Torneo A → Jugador Juan:
✅ Primera + Veteranos  (2 categorías - PERMITIDO)
✅ Segunda + Veteranos  (2 categorías - PERMITIDO)
❌ Primera + Segunda    (2 categorías - NO PERMITIDO)
❌ Primera + Segunda + Veteranos (3 categorías - NO PERMITIDO)
```

### 2️⃣ Combinaciones Permitidas

| Combinación | ¿Permitido? | Notas |
|---|---|---|
| **Primera + Veteranos** | ✅ SÍ | Válida |
| **Segunda + Veteranos** | ✅ SÍ | Válida |
| **Primera + Segunda** | ❌ NO | Prohibido: no puede estar en ambas categorías de competencia |
| **Solo Primera** | ✅ SÍ | Permitido, pero puede agregar Veteranos |
| **Solo Segunda** | ✅ SÍ | Permitido, pero puede agregar Veteranos |
| **Solo Veteranos** | ✅ SÍ | Permitido |

### 3️⃣ Validación de Edad

Cada categoría tiene rangos de edad:
- **Primera**: Edad >= 18 (típicamente adultos)
- **Segunda**: Edad >= 18 (típicamente adultos)
- **Veteranos**: Edad >= 41 (obligatorio)

El jugador debe cumplir con el rango de edad de CADA categoría donde se inscribe.

```java
ejemplo: Jugador Juan (22 años)
✅ Puede inscribirse en Primera
✅ Puede inscribirse en Segunda
❌ NO puede inscribirse en Veteranos (22 < 41)
```

```java
ejemplo: Jugador María (45 años)
✅ Puede inscribirse en Primera
✅ Puede inscribirse en Segunda
✅ Puede inscribirse en Veteranos
```

## 🔄 Flujo de Inscripción

### Paso 1: Usuario elige torneo
```
POST /api/torneos/1/categorias-disponibles?jugadorId=5
→ Retorna las categorías en las que PUEDE inscribirse
```

### Paso 2: Sistema calcula categorías disponibles
```java
// Obtener inscripciones actuales del jugador en el torneo
List<JugadorEnCategoria> inscritos = repository.find(jugador=5, torneo=1, activo=true);
// inscritos.size() = 0 (no tiene ninguna aún)

// Filtrar categorías disponibles:
// 1. ¿Categoría activa en el torneo? SÍ
// 2. ¿Tiene menos de 2 categorías? SÍ (tiene 0)
// 3. ¿Combinación válida? SÍ (primera inscripción siempre es válida)
// 4. ¿Cumple edad? SÍ (si es Veteranos, edad >= 41)

→ Retorna: [Primera, Segunda, Veteranos]
```

### Paso 3: Usuario elige Primera
```
POST /api/jugador-categoria/inscribir?jugadorId=5&categoriaTorneoId=10
  (categoriaTorneoId=10 → Torneo 1, Categoría Primera)

✅ Inscripción exitosa
```

### Paso 4: Usuario intenta agregar Segunda
```
POST /api/torneos/1/categorias-disponibles?jugadorId=5
→ Nueva solicitud al sistema para ver qué puede agregar

// El sistema recalcula:
// inscritos.size() = 1 (Primera)
// El jugador ya tiene Primera → solo puede agregar Veteranos
→ Retorna: [Veteranos] ← Segunda NO está disponible
```

### Paso 5: Usuario elige Veteranos
```
POST /api/jugador-categoria/inscribir?jugadorId=5&categoriaTorneoId=12
  (categoriaTorneoId=12 → Torneo 1, Categoría Veteranos)

✅ Inscripción exitosa → Ahora tiene: Primera + Veteranos
```

## 📋 Ejemplos de Validación

### Ejemplo 1: Jugador Adulto (30 años)
```
Torneo "Copa 2026"
├─ Primera (edad: 18-40)
├─ Segunda (edad: 18-40)
└─ Veteranos (edad: 41+)

Jugador "Carlos" (30 años):

1. Primera + Veteranos?
   ❌ NO → Carlos no cumple edad para Veteranos (30 < 41)

2. Primera + Primera?
   ❌ NO → No puede estar dos veces en Primera

3. Primera + Segunda?
   ❌ NO → Combinación prohibida

4. Primera solo?
   ✅ SÍ → Carlos puede inscribirse en Primera

5. Segunda + Veteranos?
   ❌ NO → Carlos no cumple edad para Veteranos (30 < 41)

6. Veteranos solo?
   ❌ NO → Carlos no cumple edad

Conclusión: Carlos puede inscribirse en Primera O Segunda (max 1 categoría)
```

### Ejemplo 2: Jugador Veterano (50 años)
```
Jugador "Roberto" (50 años) EN TORNEO A:

Elige UNA opción (no puede estar en las 2 a la vez):

1. Primera + Veteranos?
   ✅ SÍ → Roberto cumple todas las edades
   
   RESULTADO: Roberto está inscrito en (Primera + Veteranos)
   ¿Puede agregar Segunda? NO (ya tiene 2 categorías)

2. Segunda + Veteranos?
   ✅ SÍ → Roberto cumple todas las edades
   
   RESULTADO: Roberto está inscrito en (Segunda + Veteranos)
   ¿Puede agregar Primera? NO (ya tiene 2 categorías)

3. Primera + Segunda?
   ❌ NO → Combinación prohibida

Conclusión: En Torneo A, Roberto elige:
           (Primera + Veteranos) O (Segunda + Veteranos), pero NO ambas

PERO: En Torneos DIFERENTES, Roberto SÍ puede estar en diferentes combinaciones:
      Torneo A → Primera + Veteranos
      Torneo B → Segunda + Veteranos
      ✅ ESTO SÍ ESTÁ PERMITIDO
```

## 🏗️ Estructura de BD

### Tabla: categorias
```sql
id | nombre | edad_minima | edad_maxima | permitir_inscripcion | activa | descripcion
---|--------|-------------|-------------|----------------------|--------|----
1  | Primera | 18 | 40 | false | true | Categoría de Primera División
2  | Segunda | 18 | 40 | false | true | Categoría de Segunda División
3  | Veteranos | 41 | NULL | true | true | Categoría para jugadores mayores de 40 años
```

### Tabla: categoria_torneo
```sql
id | id_torneo | id_categoria | activa | orden
---|-----------|--------------|--------|------
1  | 1 | 1 | true | 1
2  | 1 | 2 | true | 2
3  | 1 | 3 | true | 3
```

### Tabla: jugador_en_categoria
```sql
id | id_jugador | id_categoria_torneo | fecha_inscripcion | activo
---|------------|---------------------|-------------------|------
1  | 5 | 1 | 2026-01-15 | true      ← Juan en Primera del Torneo 1
2  | 5 | 3 | 2026-01-15 | true      ← Juan en Veteranos del Torneo 1
3  | 6 | 2 | 2026-01-16 | true      ← María en Segunda del Torneo 1
```

## 🛡️ Validaciones en CategoriaValidationService

### Método Principal
```java
public void validarInscripcionJugadorEnCategoria(
    Jugador jugador, 
    CategoriaTorneo categoriaTorneo
) {
    // 1. Validar que jugador y categoría existan
    // 2. Validar que categoría esté activa en torneo
    // 3. Validar edad del jugador contra requisitos de categoría
    // 4. Validar combinaciones (máximo 2, no Primera+Segunda)
}
```

### Validación de Combinaciones
```java
private void validarCombinacionesCategorias(
    Jugador jugador, 
    CategoriaTorneo categoriaTorneo
) {
    // Obtener inscripciones actuales
    List<JugadorEnCategoria> actuales = 
        jugadorEnCategoriaRepository
            .findByJugadorIdAndCategoriaTorneoTorneoIdAndActivoTrue(
                jugador.getId(),
                categoriaTorneo.getTorneo().getId()
            );

    // Si ya tiene 2, rechazar
    if (actuales.size() >= 2) {
        throw error("Max 2 categorías por torneo");
    }

    // Si intenta Primera + Segunda, rechazar
    if ((newCategory == "Primera" && existing == "Segunda") ||
        (newCategory == "Segunda" && existing == "Primera")) {
        throw error("No se puede Primera + Segunda");
    }
}
```

## 🔗 API Endpoints

### 1. Obtener categorías disponibles para un jugador
```
GET /api/torneos/{torneoId}/categorias-disponibles?jugadorId={jugadorId}

Respuesta:
[
  {
    "id": 1,
    "nombre": "Primera",
    "edadMinima": 18,
    "edadMaxima": 40,
    "activa": true
  },
  {
    "id": 3,
    "nombre": "Veteranos",
    "edadMinima": 41,
    "edadMaxima": null,
    "activa": true
  }
]
```

### 2. Inscribir jugador en categoría
```
POST /api/jugador-categoria/inscribir
Query params:
  - jugadorId={jugadorId}
  - categoriaTorneoId={categoriaTorneoId}

Respuesta exitosa (201):
{
  "id": 1,
  "jugadorId": 5,
  "categoriaTorneoId": 10,
  "fechaInscripcion": "2026-01-15T10:30:00",
  "activo": true
}

Respuesta error (400):
{
  "error": "Combinación no permitida: No se puede estar en Primera y Segunda simultáneamente"
}
```

## ✨ Beneficios de Esta Lógica

1. **Flexibilidad**: Un jugador puede competir en dos categorías (mejora su experiencia)
2. **Equilibrio**: Pero no puede estar en Primera Y Segunda (mantiene fairness)
3. **Inclusión**: Veteranos se combina con cualquiera (incluye jugadores mayores)
4. **Validación clara**: Reglas simples y explícitas

## 📝 Casos de Uso en Testing

```
Test 1: Jugador joven sin Veteranos
  ✅ Primera PERMITIDA
  ✅ Segunda PERMITIDA
  ✅ Primera + Segunda NO PERMITIDA
  ❌ Veteranos NO PERMITIDA (edad)

Test 2: Jugador veterano con edad válida
  ✅ Primera + Veteranos PERMITIDA
  ✅ Segunda + Veteranos PERMITIDA
  ❌ Primera + Segunda NO PERMITIDA
  ✅ Solo Veteranos PERMITIDA

Test 3: Cambiar de una categoría a otra
  ✅ Si está en Primera, puede cambiar a Veteranos (+ Primera)
  ✅ Si está en Segunda, puede cambiar a Veteranos (+ Segunda)
  ❌ No puede cambiar Primera por Segunda (si ya está en Primera)
```

---

**Versión**: 2.0 (Actualizado - Lógica Correcta)
**Última actualización**: 2026-01-03
**Estado**: ✅ En Implementación
