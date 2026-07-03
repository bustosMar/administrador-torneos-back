# 📋 Sistema de Categorías - Implementación Actualizada

## 🎯 Requisitos Cumplidos

```
✅ Los jugadores NO pueden participar en Primera ni Segunda
✅ Los jugadores SÍ pueden participar en Veteranos (si cumplen edad)
✅ Un jugador MÁXIMO 1 categoría por torneo
✅ Si existe Veteranos, pueden estar en Veteranos
```

## 🏗️ Estructura

### Entidades
- **Categoria** - Con campo `permitirInscripcion` (boolean)
  - `false` = BLOQUEADA (Primera, Segunda) → Validación rechaza inscripción
  - `true` = ABIERTA (Veteranos) → Permite inscripción si cumple edad

- **CategoriaTorneo** - Relación Torneo-Categoria

- **JugadorEnCategoria** - Inscripción con validaciones

### Validaciones (CategoriaValidationService)

```java
// 1. ¿Permite inscripción? (PRIMERA VALIDACIÓN)
if (!categoria.isPermitirInscripcion()) {
    throw new Exception("Solo Veteranos permite inscripción");
}

// 2. ¿Cumple edad?
if (edad < edad_minima) {
    throw new Exception("No cumple edad mínima");
}

// 3. ¿Ya está inscrito en otra categoría?
long inscritos = contar por jugador + torneo
if (inscritos > 0) {
    throw new Exception("Un jugador máximo 1 categoría por torneo");
}
```

## 📡 API Endpoints

### Crear Categoría (BLOQUEADA)
```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Primera División",
    "edadMinima": 18,
    "edadMaxima": 30,
    "permitirInscripcion": false,
    "activa": true
  }'
```

### Crear Categoría (ABIERTA)
```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Veteranos",
    "edadMinima": 41,
    "edadMaxima": null,
    "permitirInscripcion": true,
    "activa": true
  }'
```

### Inscribir Jugador en Veteranos
```bash
curl -X POST "http://localhost:8080/api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=1"
```

**Si intenta en Primera (permitirInscripcion=false):**
```json
{
  "error": "La categoría 'Primera División' no permite inscripción de jugadores. Solo la categoría de Veteranos permite participación."
}
```

**Si cumple edad en Veteranos:**
```json
{
  "id": 1,
  "jugador": { "id": 1, "nombre": "Juan", "apellido": "García" },
  "fechaInscripcion": "03/07/2026",
  "activo": true
}
```

## 📊 Configuración Base de Datos

### Tabla categorias
```sql
┌─────────────────────────────────────────────────┐
│ id | nombre      | edad_min | edad_max │ permitir │
├─────────────────────────────────────────────────┤
│ 1  │ Primera     │ 18       │ 30       │ false ← BLOQUEADA
│ 2  │ Segunda     │ 30       │ 40       │ false ← BLOQUEADA
│ 3  │ Veteranos   │ 41       │ NULL     │ true  ← ABIERTA
└─────────────────────────────────────────────────┘
```

## 🚀 Cómo Usar

### 1. Base de Datos
Las tablas se crean automáticamente con Flyway

### 2. Crear Categorías (en BD o via API)
```sql
INSERT INTO categorias (nombre, edad_minima, edad_maxima, permitir_inscripcion, activa)
VALUES ('Veteranos', 41, NULL, true, true);
```

### 3. Agregar al Torneo
```bash
POST /api/categoria-torneo/torneo/1/categoria/3?orden=1
```

### 4. Inscribir Jugador
```bash
POST /api/jugador-categoria/inscribir?jugadorId=5&categoriaTorneoId=1
```

## ✅ Validaciones que se ejecutan

| # | Validación | Mensaje |
|---|-----------|---------|
| 1 | ¿Permite inscripción? | "Solo Veteranos permite" |
| 2 | ¿Existe jugador? | "Jugador no encontrado" |
| 3 | ¿Existe categoría? | "Categoría no encontrada" |
| 4 | ¿Categoría activa? | "Categoría no está activa" |
| 5 | ¿Tiene fecha nacimiento? | "No tiene fecha nacimiento" |
| 6 | ¿Cumple edad mínima? | "No cumple edad mínima" |
| 7 | ¿Cumple edad máxima? | "Supera edad máxima" |
| 8 | ¿Ya inscrito en otra? | "Ya inscrito en X del torneo" |

## 🎯 Ejemplos de Uso

### Caso 1: Jugador 50 años → Veteranos ✅
```bash
POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=3
→ 201 Created
```

### Caso 2: Jugador 25 años → Primera ❌
```bash
POST /api/jugador-categoria/inscribir?jugadorId=2&categoriaTorneoId=1
→ 400 Bad Request
→ "Solo la categoría de Veteranos permite participación"
```

### Caso 3: Jugador 50 años → Veteranos ✅, luego → Segunda ❌
```bash
POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=3
→ 201 Created (Inscrito en Veteranos)

POST /api/jugador-categoria/inscribir?jugadorId=1&categoriaTorneoId=2
→ 400 Bad Request
→ "Ya está inscrito en la categoría 'Veteranos' de este torneo"
```

## 📦 Archivos Creados

```
src/main/java/com/sistema/torneos/app/
├── domain/
│   ├── entity/
│   │   ├── Categoria.java
│   │   ├── CategoriaTorneo.java
│   │   └── JugadorEnCategoria.java
│   ├── exception/
│   │   └── CategoriaValidationException.java
│   └── repository/
│       ├── CategoriaRepository.java
│       ├── CategoriaTorneoRepository.java
│       └── JugadorEnCategoriaRepository.java
├── service/
│   ├── CategoriaService.java
│   ├── CategoriaTorneoService.java
│   ├── CategoriaValidationService.java ⭐
│   └── JugadorEnCategoriaService.java
└── web/controller/
    ├── CategoriaController.java
    ├── CategoriaTorneoController.java
    └── JugadorEnCategoriaController.java

src/main/resources/
└── db/migration/
    └── V004__Create_Categorias_Tables.sql
```

## 🔧 Compilar

```bash
cd Back/administrador-torneos-back
mvn clean compile
mvn spring-boot:run
```

## 📞 Endpoints Completos

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/jugador-categoria/inscribir` | **Inscribir jugador (PRINCIPAL)** |
| GET | `/api/jugador-categoria/jugador/{id}` | Categorías del jugador |
| GET | `/api/jugador-categoria/categoria/{id}` | Jugadores en categoría |
| GET | `/api/categorias` | Todas las categorías |
| GET | `/api/categorias/permitidas` | Solo categorías abiertas (Veteranos) |
| POST | `/api/categorias` | Crear categoría |

---

**¡Sistema listo para usar!** 🚀
