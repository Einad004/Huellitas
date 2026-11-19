# 🐾 HUELLITAS
### Aplicación móvil para la gestión integral de mascotas

**Proyecto académico — Ingeniería de Sistemas e Informática**

Universidad de Huánuco | Huánuco, Perú | 2026

---

## 📌 Descripción del proyecto

**Huellitas** es una aplicación móvil desarrollada en Android Studio utilizando Java, cuyo propósito es facilitar la organización y el seguimiento de la información de las mascotas.

La aplicación permite registrar datos generales de cada mascota y administrar información relacionada con sus vacunas, alimentación y visitas veterinarias, manteniendo los registros en una base de datos local.

El proyecto busca reunir la información de cuidado de los animales en una sola aplicación, facilitando su consulta, actualización y administración.

## 🎯 Objetivo general

Desarrollar una aplicación móvil para la gestión integral de mascotas, que permita registrar datos de salud, vacunas, visitas veterinarias y alimentación de cada animal. Contará con recordatorios para medicamentos y revisiones periódicas, historial médico exportable y una sección de consejos según la especie y raza.

## ⚙️ Tecnologías utilizadas

| Tecnología | Utilización |
|---|---|
| Java | Desarrollo de la lógica de la aplicación |
| Android Studio | Entorno de desarrollo |
| XML | Diseño de las interfaces |
| SQLite | Almacenamiento local |
| Room | Gestión de las entidades y operaciones CRUD |
| SharedPreferences | Almacenamiento de la sesión de usuario |
| Git | Control de versiones |
| GitHub | Repositorio del proyecto |
| Gradle | Configuración y compilación |

## 📱 Funcionalidades desarrolladas

### 1. Inicio de sesión
- Pantalla de ingreso a la aplicación.
- Almacenamiento local del identificador del usuario.
- Navegación hacia las pantallas principales.

**Nota:** el inicio de sesión actual utiliza SharedPreferences. No constituye todavía un sistema completo de autenticación segura.

### 2. Gestión de mascotas
- Registro de mascotas.
- Almacenamiento del nombre, especie, raza y fecha de nacimiento.
- Generación de identificadores únicos mediante UUID.
- Búsqueda de mascotas por identificador.
- Actualización y eliminación de registros.

### 3. Gestión de vacunas
- Registro del nombre de la vacuna.
- Registro del lote y fecha de aplicación.
- Asociación de cada vacuna con una mascota.
- Consulta, actualización y eliminación de vacunas mediante Room.

### 4. Gestión de alimentación
- Registro de la marca del alimento.
- Cantidad de ración en gramos.
- Frecuencia diaria de alimentación.
- Consulta de los registros alimentarios de cada mascota.
- Actualización y eliminación de dietas.

### 5. Gestión de visitas veterinarias
- Registro del establecimiento o lugar de atención.
- Fecha de la visita.
- Motivo de consulta.
- Diagnóstico registrado.
- Consulta del historial de visitas por mascota.
- Actualización y eliminación de visitas.

### 6. Navegación de la aplicación
La aplicación utiliza Activities para organizar sus pantallas y una clase base `MenuActivity` que permite compartir el menú de navegación inferior.

## 🗄️ Base de datos

Huellitas utiliza **Room**, una biblioteca de persistencia que permite interactuar con SQLite mediante entidades y DAO.

Las entidades principales implementadas son:

| Entidad | Información almacenada |
|---|---|
| Mascota | Identificador, usuario, nombre, especie, raza y fecha de nacimiento |
| Vacuna | Identificador, mascota, vacuna, lote y fecha de aplicación |
| Dieta | Identificador, mascota, alimento, ración y frecuencia |
| VisitaVeterinaria | Identificador, mascota, fecha, lugar, motivo y diagnóstico |

La relación utilizada es de **una mascota a múltiples registros** de vacunas, alimentación y visitas veterinarias, mediante el campo `idMascota`.

### Operaciones CRUD

- **CREATE:** insertar nuevos registros.
- **READ:** consultar los registros almacenados.
- **UPDATE:** modificar la información existente.
- **DELETE:** eliminar registros seleccionados.

Estas operaciones se realizan a través de los DAO correspondientes.

## 🏗️ Organización del proyecto

Estructura principal de clases y componentes:

```text
com.udh.apphuellitas/
│
├── MainActivity.java
├── MenuActivity.java
├── InicioPantalla.java
├── MascotasPantalla.java
├── PerfilPantalla.java
├── Registro.java
├── BuscarMascota.java
│
└── model/
    ├── AppDatabase.java
    ├── Mascota.java
    ├── MascotaDao.java
    ├── Vacuna.java
    ├── VacunaDao.java
    ├── Dieta.java
    ├── DietaDao.java
    ├── VisitaVeterinaria.java
    └── VisitaVeterinariaDao.java
```

Las pantallas se diseñan mediante archivos XML ubicados en `res/layout`.

## 🚀 Instalación y ejecución

1. Instalar Android Studio.
2. Clonar el repositorio:

```bash
git clone https://github.com/Einad004/Huellitas.git
```

3. Abrir