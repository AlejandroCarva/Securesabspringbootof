-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 03-12-2025 a las 01:50:32
-- Versión del servidor: 9.1.0
-- Versión de PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `securesab1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencia_ambiente`
--

DROP TABLE IF EXISTS `asistencia_ambiente`;
CREATE TABLE IF NOT EXISTS `asistencia_ambiente` (
  `id_asistencia_ambiente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_instructor` int DEFAULT NULL,
  `id_competencia` int DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `estado_asistencia` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_asistencia_ambiente`),
  KEY `id_usuario` (`id_usuario`),
  KEY `asistencia_ambiente_id_instructor_foreign` (`id_instructor`),
  KEY `asistencia_ambiente_id_competencia_foreign` (`id_competencia`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asistencia_ambiente`
--

INSERT INTO `asistencia_ambiente` (`id_asistencia_ambiente`, `id_usuario`, `id_instructor`, `id_competencia`, `fecha`, `estado_asistencia`) VALUES
(19, 1, 15, 1, '2025-09-28', 'Inasistio'),
(20, 1, 15, 1, '2025-09-11', 'Inasistio'),
(22, 1, 15, 1, '2025-09-13', 'Inasistio'),
(24, 1, 15, 1, '2025-12-05', 'Asistio'),
(27, 1, 15, 1, '2025-11-19', 'Asistio'),
(28, 1, 15, 1, '2025-10-10', 'Asistio'),
(29, 1, 15, 1, '2025-09-26', 'Inasistio'),
(30, 23, 15, 1, '2025-09-26', 'Asistio'),
(31, 1, 15, 1, '2025-09-22', 'Inasistio'),
(32, 23, 15, 1, '2025-09-22', 'Inasistio'),
(33, 23, 14, 1, '2025-09-23', 'Asistio'),
(34, 23, 14, 1, '2025-09-24', 'Inasistio'),
(35, 1, 14, 1, '2025-09-25', 'Asistio'),
(36, 23, 14, 1, '2025-09-25', 'Asistio'),
(37, 1, 14, 1, '2025-08-31', 'Justificado'),
(38, 23, 14, 1, '2025-08-31', 'Inasistio'),
(39, 1, 15, 1, '2025-10-31', 'Inasistio'),
(40, 23, 15, 1, '2025-10-31', 'Inasistio'),
(41, 1, 15, 1, '2025-12-01', 'Justificado'),
(42, 23, 15, 1, '2025-12-01', 'Justificado'),
(43, 1, 15, 1, '2025-12-03', 'Asistio'),
(44, 23, 15, 1, '2025-12-03', 'Asistio'),
(45, 1, 15, 1, '2025-12-31', 'Asistio'),
(46, 23, 15, 1, '2025-12-31', 'Asistio'),
(47, 1, 15, 1, '2025-12-24', 'Justificado'),
(48, 23, 15, 1, '2025-12-24', 'Justificado'),
(49, 1, 15, 1, '2025-12-02', 'Justificado'),
(50, 23, 15, 1, '2025-12-02', 'Justificado'),
(51, 1, 15, 1, '2025-12-22', 'Justificado'),
(52, 23, 15, 1, '2025-12-22', 'Justificado'),
(53, 1, 15, 1, '2026-01-01', 'Inasistio'),
(54, 23, 15, 1, '2026-01-01', 'Inasistio'),
(55, 1, 15, 1, '2026-01-02', 'Justificado'),
(56, 23, 15, 1, '2026-01-02', 'Inasistio'),
(57, 1, 15, 1, '2026-01-07', 'Inasistio'),
(58, 23, 15, 1, '2026-01-07', 'Justificado'),
(59, 1, 15, 1, '2026-01-31', 'Inasistio'),
(60, 23, 15, 1, '2026-01-31', 'Inasistio');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencia_sede`
--

DROP TABLE IF EXISTS `asistencia_sede`;
CREATE TABLE IF NOT EXISTS `asistencia_sede` (
  `id_asistencia` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `fecha` date DEFAULT NULL,
  `hora_entrada` time DEFAULT NULL,
  `hora_salida` time DEFAULT NULL,
  PRIMARY KEY (`id_asistencia`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asistencia_sede`
--

INSERT INTO `asistencia_sede` (`id_asistencia`, `id_usuario`, `fecha`, `hora_entrada`, `hora_salida`) VALUES
(11, 1, '2025-09-01', '08:00:00', '12:00:00'),
(13, 1, '2025-09-01', '08:05:00', '12:05:00'),
(15, 1, '2025-09-02', '08:10:00', '12:08:00'),
(17, 1, '2025-09-01', '07:55:00', '12:00:00'),
(19, 14, '2025-09-01', '08:20:00', '12:15:00'),
(21, 25, '2025-09-21', '02:02:13', NULL),
(22, 25, '2025-09-21', '02:05:46', '03:57:14'),
(23, 25, '2025-09-21', '02:06:04', '02:06:17'),
(24, 25, '2025-09-21', '04:04:11', '23:15:39'),
(25, 25, '2025-09-20', '23:17:28', '23:18:40');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cache`
--

DROP TABLE IF EXISTS `cache`;
CREATE TABLE IF NOT EXISTS `cache` (
  `key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiration` int NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cache`
--

INSERT INTO `cache` (`key`, `value`, `expiration`) VALUES
('laravel-cache-1000856955|127.0.0.1', 'i:1;', 1758581913),
('laravel-cache-1000856955|127.0.0.1:timer', 'i:1758581913;', 1758581913),
('laravel-cache-spatie.permission.cache', 'a:3:{s:5:\"alias\";a:4:{s:1:\"a\";s:2:\"id\";s:1:\"b\";s:4:\"name\";s:1:\"c\";s:10:\"guard_name\";s:1:\"r\";s:5:\"roles\";}s:11:\"permissions\";a:5:{i:0;a:4:{s:1:\"a\";i:1;s:1:\"b\";s:10:\"ver_perfil\";s:1:\"c\";s:3:\"web\";s:1:\"r\";a:1:{i:0;i:2;}}i:1;a:4:{s:1:\"a\";i:2;s:1:\"b\";s:20:\"consultar_asistencia\";s:1:\"c\";s:3:\"web\";s:1:\"r\";a:1:{i:0;i:2;}}i:2;a:4:{s:1:\"a\";i:3;s:1:\"b\";s:21:\"radicar_justificacion\";s:1:\"c\";s:3:\"web\";s:1:\"r\";a:1:{i:0;i:2;}}i:3;a:4:{s:1:\"a\";i:4;s:1:\"b\";s:22:\"ver_justificaciones_ap\";s:1:\"c\";s:3:\"web\";s:1:\"r\";a:1:{i:0;i:2;}}i:4;a:4:{s:1:\"a\";i:5;s:1:\"b\";s:13:\"ver_evidencia\";s:1:\"c\";s:3:\"web\";s:1:\"r\";a:1:{i:0;i:2;}}}s:5:\"roles\";a:1:{i:0;a:3:{s:1:\"a\";i:2;s:1:\"b\";s:8:\"aprendiz\";s:1:\"c\";s:3:\"web\";}}}', 1758668590);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cache_locks`
--

DROP TABLE IF EXISTS `cache_locks`;
CREATE TABLE IF NOT EXISTS `cache_locks` (
  `key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `expiration` int NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `competencia`
--

DROP TABLE IF EXISTS `competencia`;
CREATE TABLE IF NOT EXISTS `competencia` (
  `id_competencia` int NOT NULL AUTO_INCREMENT,
  `nombre_competencia` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_programa` int NOT NULL,
  `estado` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_competencia`),
  KEY `id_programa` (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `competencia`
--

INSERT INTO `competencia` (`id_competencia`, `nombre_competencia`, `descripcion`, `id_programa`, `estado`) VALUES
(1, 'Bases de Datos', 'Competencia en la implementación y manejo de bases de datos.', 1, 'ACTIVA'),
(2, 'Selección de Personal', 'Competencia en procesos de selección y contratación.', 2, 'ACTIVA'),
(32, 'Instalar y configurar software', 'Instalar, configurar y mantener sistemas informáticos.', 11, 'ACTIVA'),
(33, 'Registrar transacciones contables', 'Gestionar registros de transacciones financieras de la empresa.', 12, 'ACTIVA');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coordinacion`
--

DROP TABLE IF EXISTS `coordinacion`;
CREATE TABLE IF NOT EXISTS `coordinacion` (
  `id_coordinacion` int NOT NULL AUTO_INCREMENT,
  `nombre_coordinacion` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `descripcion` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id_coordinacion`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `coordinacion`
--

INSERT INTO `coordinacion` (`id_coordinacion`, `nombre_coordinacion`, `descripcion`) VALUES
(1, 'Coordinacción ADSO', 'Coordinación enfocada en programas de tecnología e informática.'),
(2, 'Coordinación Gestión Administrativa', 'Coordinación enfocada en programas de gestión y administración.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `failed_jobs`
--

DROP TABLE IF EXISTS `failed_jobs`;
CREATE TABLE IF NOT EXISTS `failed_jobs` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `connection` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `exception` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ficha`
--

DROP TABLE IF EXISTS `ficha`;
CREATE TABLE IF NOT EXISTS `ficha` (
  `id_ficha` int NOT NULL AUTO_INCREMENT,
  `numeroFicha` varchar(40) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_programa` int NOT NULL,
  `id_jornada` int NOT NULL,
  `numero_ficha` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_ficha`),
  KEY `id_programa` (`id_programa`),
  KEY `id_jornada` (`id_jornada`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ficha`
--

INSERT INTO `ficha` (`id_ficha`, `numeroFicha`, `fecha`, `estado`, `id_programa`, `id_jornada`, `numero_ficha`) VALUES
(1, '3065834', '2024-01-15 00:00:00.000000', 'Activa', 1, 1, NULL),
(2, '3065835', '2024-02-20 00:00:00.000000', 'Activa', 2, 2, NULL),
(11, '3065836', '2025-09-17 00:00:00.000000', 'Activa', 11, 1, NULL),
(12, '3065837', '2025-09-17 00:00:00.000000', 'Activa', 12, 2, NULL),
(13, '11', '2025-09-19 00:00:00.000000', 'Activa', 1, 1, NULL),
(14, '112', '2025-09-19 00:00:00.000000', 'Activa', 1, 2, NULL),
(15, '1126', '2025-09-19 00:00:00.000000', 'Activa', 1, 3, NULL),
(16, '00000', '2025-09-19 00:00:00.000000', 'Activa', 11, 4, NULL),
(17, '1111', '2025-09-19 00:00:00.000000', 'Activa', 1, 2, NULL),
(18, '2222222', '2025-09-19 00:00:00.000000', 'Activa', 11, 3, NULL),
(19, '22222223', '2025-09-19 00:00:00.000000', 'Activa', 11, 3, NULL),
(20, '22222224', '2025-09-19 00:00:00.000000', 'Activa', 11, 3, NULL),
(21, '22222225', '2025-09-19 00:00:00.000000', 'Activa', 11, 3, NULL),
(22, '222222255', '2025-09-19 00:00:00.000000', 'Activa', 2, 1, NULL),
(23, '2222222557', '2025-09-19 00:00:00.000000', 'Activa', 2, 1, NULL),
(24, '30658355', '2025-09-19 00:00:00.000000', 'Activa', 2, 1, NULL),
(25, '3455', '2025-09-19 00:00:00.000000', 'Activa', 2, 1, NULL),
(26, '254689575', '2025-09-22 00:00:00.000000', 'Activa', 1, 1, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ficha_instructor`
--

DROP TABLE IF EXISTS `ficha_instructor`;
CREATE TABLE IF NOT EXISTS `ficha_instructor` (
  `id_ficha` int NOT NULL,
  `id_instructor` int NOT NULL,
  PRIMARY KEY (`id_ficha`,`id_instructor`),
  KEY `fk_ficha_instructor_usuarios` (`id_instructor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ficha_instructor`
--

INSERT INTO `ficha_instructor` (`id_ficha`, `id_instructor`) VALUES
(1, 14),
(2, 14),
(11, 14),
(13, 14),
(14, 14),
(17, 14),
(22, 14),
(23, 14),
(24, 14),
(25, 14),
(26, 14),
(1, 15),
(2, 15),
(12, 15),
(15, 15),
(16, 15),
(18, 15),
(19, 15),
(20, 15),
(21, 15);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `huella`
--

DROP TABLE IF EXISTS `huella`;
CREATE TABLE IF NOT EXISTS `huella` (
  `id_huella` int NOT NULL,
  `datos_huella_dactilar` blob NOT NULL,
  `fecha_registro` date DEFAULT NULL,
  `id_usuario` int NOT NULL,
  PRIMARY KEY (`id_huella`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jobs`
--

DROP TABLE IF EXISTS `jobs`;
CREATE TABLE IF NOT EXISTS `jobs` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `queue` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `attempts` tinyint UNSIGNED NOT NULL,
  `reserved_at` int UNSIGNED DEFAULT NULL,
  `available_at` int UNSIGNED NOT NULL,
  `created_at` int UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `jobs_queue_index` (`queue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `job_batches`
--

DROP TABLE IF EXISTS `job_batches`;
CREATE TABLE IF NOT EXISTS `job_batches` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_jobs` int NOT NULL,
  `pending_jobs` int NOT NULL,
  `failed_jobs` int NOT NULL,
  `failed_job_ids` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `options` mediumtext COLLATE utf8mb4_unicode_ci,
  `cancelled_at` int DEFAULT NULL,
  `created_at` int NOT NULL,
  `finished_at` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jornada`
--

DROP TABLE IF EXISTS `jornada`;
CREATE TABLE IF NOT EXISTS `jornada` (
  `id_jornada` int NOT NULL AUTO_INCREMENT,
  `nombre_jornada` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  PRIMARY KEY (`id_jornada`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jornada`
--

INSERT INTO `jornada` (`id_jornada`, `nombre_jornada`, `hora_inicio`, `hora_fin`) VALUES
(1, 'Mañana', '06:00:00', '12:00:00'),
(2, 'Tarde', '12:00:00', '18:00:00'),
(3, 'Noche', '18:00:00', '22:00:00'),
(4, 'Fin de Semana', '08:00:00', '17:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `justificacion`
--

DROP TABLE IF EXISTS `justificacion`;
CREATE TABLE IF NOT EXISTS `justificacion` (
  `id_justificacion` bigint NOT NULL AUTO_INCREMENT,
  `id_asistencia_ambiente` int NOT NULL,
  `motivo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `soporte` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `observaciones` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_justificacion`),
  KEY `id_asistencia_ambiente` (`id_asistencia_ambiente`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `justificacion`
--

INSERT INTO `justificacion` (`id_justificacion`, `id_asistencia_ambiente`, `motivo`, `soporte`, `fecha`, `estado`, `observaciones`) VALUES
(3, 22, 'Cita Médica', 'evidencias_justificaciones/73c29456-dd86-4f15-a1a2-0a9711761fd4.png', NULL, 'Pendiente', NULL),
(4, 20, 'Cita Médica', 'evidencias_justificaciones/5a106238-c37f-475b-888e-5c22e4e6b7f7.png', NULL, 'Pendiente', NULL),
(5, 29, 'Diligencia Personal', 'evidencias_justificaciones/b83f96f9-a348-49c7-a059-866c199d5e0c.png', NULL, 'Pendiente', NULL),
(6, 19, 'Cita Médica', 'evidencias_justificaciones/7e3ae92f-0851-483c-8ce4-9fdafb8659bc.png', NULL, 'Pendiente', NULL),
(7, 37, 'Cita Médica', 'evidencias_justificaciones/2c5d299f-3878-4a39-8f2c-a70cd89d0d75.png', NULL, 'Aprobada', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `migrations`
--

DROP TABLE IF EXISTS `migrations`;
CREATE TABLE IF NOT EXISTS `migrations` (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(10, '0001_01_01_000000_create_users_table', 1),
(11, '0001_01_01_000001_create_cache_table', 1),
(12, '0001_01_01_000002_create_jobs_table', 1),
(13, '2025_09_06_012158_add_instructor_and_competencia_to_asistencia_ambiente_table', 1),
(14, '2025_09_13_003946_add_observaciones_to_justificacion_table', 2),
(15, '2025_09_15_004901_create_permission_tables', 3),
(16, '2025_09_15_225706_create_permission_tables', 4),
(19, '2025_09_16_212046_add_foto_perfil_to_usuarios_table', 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `model_has_permissions`
--

DROP TABLE IF EXISTS `model_has_permissions`;
CREATE TABLE IF NOT EXISTS `model_has_permissions` (
  `permission_id` bigint UNSIGNED NOT NULL,
  `model_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_id` bigint UNSIGNED NOT NULL,
  PRIMARY KEY (`permission_id`,`model_id`,`model_type`),
  KEY `model_has_permissions_model_id_model_type_index` (`model_id`,`model_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `model_has_roles`
--

DROP TABLE IF EXISTS `model_has_roles`;
CREATE TABLE IF NOT EXISTS `model_has_roles` (
  `role_id` bigint UNSIGNED NOT NULL,
  `model_id` bigint UNSIGNED NOT NULL,
  PRIMARY KEY (`role_id`,`model_id`),
  KEY `model_has_roles_model_id_model_type_index` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `model_has_roles`
--

INSERT INTO `model_has_roles` (`role_id`, `model_id`) VALUES
(2, 1),
(3, 14),
(3, 15),
(5, 22),
(2, 23),
(2, 24),
(6, 25),
(7, 26);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permissions`
--

DROP TABLE IF EXISTS `permissions`;
CREATE TABLE IF NOT EXISTS `permissions` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `guard_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permissions_name_guard_name_unique` (`name`,`guard_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `permissions`
--

INSERT INTO `permissions` (`id`, `name`, `guard_name`, `created_at`, `updated_at`) VALUES
(1, 'ver_perfil', 'web', '2025-09-17 23:41:15', '2025-09-17 23:41:15'),
(2, 'consultar_asistencia', 'web', '2025-09-17 23:41:15', '2025-09-17 23:41:15'),
(3, 'radicar_justificacion', 'web', '2025-09-17 23:41:15', '2025-09-17 23:41:15'),
(4, 'ver_justificaciones_ap', 'web', '2025-09-17 23:41:15', '2025-09-17 23:41:15'),
(5, 'ver_evidencia', 'web', '2025-09-17 23:41:15', '2025-09-17 23:41:15');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `programa`
--

DROP TABLE IF EXISTS `programa`;
CREATE TABLE IF NOT EXISTS `programa` (
  `id_programa` int NOT NULL AUTO_INCREMENT,
  `nombre_programa` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tipo_programa` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_coordinacion` int NOT NULL,
  PRIMARY KEY (`id_programa`),
  KEY `id_coordinacion` (`id_coordinacion`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `programa`
--

INSERT INTO `programa` (`id_programa`, `nombre_programa`, `tipo_programa`, `id_coordinacion`) VALUES
(1, 'Análisis y Desarrollo de Software', 'Tecnólogo', 1),
(2, 'Gestión de Talento Humano', 'Tecnólogo', 2),
(11, 'Tecnico en Contabilidad', 'Técnico', 1),
(12, 'Técnico en Sistemas', 'Técnico', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `programa_competencia`
--

DROP TABLE IF EXISTS `programa_competencia`;
CREATE TABLE IF NOT EXISTS `programa_competencia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `programa_id` int NOT NULL,
  `competencia_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_programa_id` (`programa_id`),
  KEY `fk_competencia_id` (`competencia_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `programa_competencia`
--

INSERT INTO `programa_competencia` (`id`, `programa_id`, `competencia_id`) VALUES
(1, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registro_manual`
--

DROP TABLE IF EXISTS `registro_manual`;
CREATE TABLE IF NOT EXISTS `registro_manual` (
  `id_registro_manual` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `documento` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tipo_movimiento` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `motivo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id_registro_manual`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `resultado_aprendizaje`
--

DROP TABLE IF EXISTS `resultado_aprendizaje`;
CREATE TABLE IF NOT EXISTS `resultado_aprendizaje` (
  `id_Resultado_Aprendizaje` int NOT NULL AUTO_INCREMENT,
  `Resultado_Aprendizaje` text COLLATE utf8mb4_general_ci,
  `id_competencia` int NOT NULL,
  PRIMARY KEY (`id_Resultado_Aprendizaje`),
  KEY `id_competencia` (`id_competencia`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `resultado_aprendizaje`
--

INSERT INTO `resultado_aprendizaje` (`id_Resultado_Aprendizaje`, `Resultado_Aprendizaje`, `id_competencia`) VALUES
(1, 'Diseñar el modelo de datos de acuerdo con los requerimientos', 1),
(2, 'Identificar el perfil de los candidatos para la selección de personal', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `guard_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roles_name_guard_name_unique` (`name`,`guard_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`, `guard_name`, `created_at`, `updated_at`) VALUES
(2, 'aprendiz', 'web', '2025-09-17 05:41:47', '2025-09-17 05:41:47'),
(3, 'instructor', 'web', NULL, NULL),
(5, 'coordinador', 'web', '2025-09-19 14:40:43', '2025-09-19 14:40:43'),
(6, 'vigilante', 'web', '2025-09-20 23:15:58', '2025-09-20 23:15:58'),
(7, 'gestor', 'web', '2025-09-20 23:15:58', '2025-09-20 23:15:58');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `role_has_permissions`
--

DROP TABLE IF EXISTS `role_has_permissions`;
CREATE TABLE IF NOT EXISTS `role_has_permissions` (
  `permission_id` bigint UNSIGNED NOT NULL,
  `role_id` bigint UNSIGNED NOT NULL,
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `role_has_permissions_role_id_foreign` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `role_has_permissions`
--

INSERT INTO `role_has_permissions` (`permission_id`, `role_id`) VALUES
(1, 2),
(2, 2),
(3, 2),
(4, 2),
(5, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sessions`
--

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE IF NOT EXISTS `sessions` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint UNSIGNED DEFAULT NULL,
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_agent` text COLLATE utf8mb4_unicode_ci,
  `payload` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_activity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sessions_user_id_index` (`user_id`),
  KEY `sessions_last_activity_index` (`last_activity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `sessions`
--

INSERT INTO `sessions` (`id`, `user_id`, `ip_address`, `user_agent`, `payload`, `last_activity`) VALUES
('HrkDjIOYkiltBprUEQBf31DF43LU6wXYzGK79n7v', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 Edg/140.0.0.0', 'YTozOntzOjY6Il90b2tlbiI7czo0MDoiZGtVUkpCdzQyMDhWaThSSHJhM1ZQbzBZdTZFRW1nVkRBQU90UWhYZiI7czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319czo5OiJfcHJldmlvdXMiO2E6MTp7czozOiJ1cmwiO3M6MjE6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMCI7fX0=', 1758585801),
('n17Z9DaD7ZhjlpizfYR360SjDTecGSCPs8cftSDt', 15, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36 Edg/140.0.0.0', 'YTo1OntzOjY6Il90b2tlbiI7czo0MDoiNE5Gc1hxd2pRZzJMVU9ncTBZNVdFYUpZNGRaNzNPVElJazNGU0ltZiI7czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319czo5OiJfcHJldmlvdXMiO2E6MTp7czozOiJ1cmwiO3M6MjE6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMCI7fXM6NTA6ImxvZ2luX3dlYl81OWJhMzZhZGRjMmIyZjk0MDE1ODBmMDE0YzdmNThlYTRlMzA5ODlkIjtpOjE1O3M6NDoiYXV0aCI7YToxOntzOjIxOiJwYXNzd29yZF9jb25maXJtZWRfYXQiO2k6MTc1ODU4NDEwNzt9fQ==', 1758585111),
('zDZtU7a9E94kqYh2itA6OEiPv3MpuwGvYTJNHoR3', 14, '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36', 'YTo1OntzOjY6Il90b2tlbiI7czo0MDoiZ1RlM2VjRW82d2xsMDNQTkFtWkkzY1dVWFFYVnB1SVNQclBENElsdSI7czo5OiJfcHJldmlvdXMiO2E6MTp7czozOiJ1cmwiO3M6NTM6Imh0dHA6Ly8xMjcuMC4wLjE6ODAwMC9pbnN0cnVjdG9yL2dlc3Rpb25hci1hc2lzdGVuY2lhIjt9czo2OiJfZmxhc2giO2E6Mjp7czozOiJvbGQiO2E6MDp7fXM6MzoibmV3IjthOjA6e319czo1MDoibG9naW5fd2ViXzU5YmEzNmFkZGMyYjJmOTQwMTU4MGYwMTRjN2Y1OGVhNGUzMDk4OWQiO2k6MTQ7czo0OiJhdXRoIjthOjE6e3M6MjE6InBhc3N3b3JkX2NvbmZpcm1lZF9hdCI7aToxNzU4NTkyMDAxO319', 1758592013);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `cedula` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `correo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `nombre` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellido` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_ficha` int DEFAULT NULL,
  `telefono` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `estado` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `foto_perfil` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_rol` bigint DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  KEY `id_ficha` (`id_ficha`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `cedula`, `correo`, `email_verified_at`, `nombre`, `apellido`, `id_ficha`, `telefono`, `estado`, `password`, `foto_perfil`, `id_rol`) VALUES
(1, '1000856944', 'aprendiz1@sena.edu.co', NULL, 'Juan', 'Caballero', 1, '3022160778', 'activo', '$2a$12$u5K7AJ50OGNvd4f608GVduoA9hlh2LKErtnLuFvZlhOUvriW3F28K', NULL, NULL),
(14, '1000856966', 'instructor1@sena.edu.co', NULL, 'Jose', 'Paez', NULL, '3022160778', 'Activo', '$2y$12$wROAo/64YVZgCqmcNEerMel.4LcjLu8Rqo4LaQADFLB.bDEb/JcNq', NULL, NULL),
(15, '1000856977', 'instructor2@sena.edu.co', NULL, 'Daniel', 'Diaz', NULL, '3025643876', 'Activo', '$2y$12$kdaxd74XnjSJ18br0SwxLOL3XO2jjKk4t8x8TOvNXw0aEx3ryBtoa', NULL, NULL),
(22, '1000856988', 'coordinador@sena.edu.co', NULL, 'Laura', 'Martínez', NULL, '3009876543', 'Activo', '$2y$12$WPDdCYo/6HsR2Npx3bjqe.h11Phcv8sRHY/MYy/cM7OgS5vUW8FlS', NULL, NULL),
(23, '1000856934', 'gomez@example.com', NULL, 'Juanchito', 'Gomez', 1, '3022160778', 'Activo', '$2y$12$LMqA8as0.APKZBXm5V7snefQL/jiUYaR0DB7HkuK8fF5.cOKcrYH.', NULL, NULL),
(24, '1000856942', 'vacalolca@gmail.com', NULL, 'Luisa', 'Castro', 13, '3022160778', 'Activo', '$2y$12$i.LtSFYcfpv52afID.ye6.1v/4HcXbVHN0cFgRJCwPuGCKWl9btNi', NULL, NULL),
(25, '1000856999', 'carlos.sanchez@example.com', NULL, 'Carlos', 'Sánchez', NULL, '3001112233', 'activo', '$2y$12$IIEC2ABStwOKFTCCy/CKm.xx0c4gygbSXMAuOQUxpk6hJz7Xr3b5m', NULL, NULL),
(26, '1000856933', 'maria.lopez@example.com', NULL, 'María', 'López', NULL, '3014445566', 'activo', '$2y$12$qfRsKOuz/zzHafVtBUKu0uaBknFJqnL72NJtdj1uGjP2VMmrV6eqi', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_roles`
--

DROP TABLE IF EXISTS `usuario_roles`;
CREATE TABLE IF NOT EXISTS `usuario_roles` (
  `id_usuario` int NOT NULL,
  `id_rol` int NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `visitante`
--

DROP TABLE IF EXISTS `visitante`;
CREATE TABLE IF NOT EXISTS `visitante` (
  `id_visitante` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `apellido` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cedula` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `motivo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id_asistencia_sede` int NOT NULL,
  PRIMARY KEY (`id_visitante`),
  KEY `fk_visitante_asistencia` (`id_asistencia_sede`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `visitante`
--

INSERT INTO `visitante` (`id_visitante`, `nombre`, `apellido`, `cedula`, `motivo`, `id_asistencia_sede`) VALUES
(1, 'paty', 'lola', '25648595', 'Reunión', 22),
(2, 'kathee', 'ñola', '24568495', 'Reunión', 23),
(3, 'lolita', 'arca', '25468595', 'Reunión', 24),
(4, 'sara', 'sofia', '24526958', 'Reunión', 25);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asistencia_ambiente`
--
ALTER TABLE `asistencia_ambiente`
  ADD CONSTRAINT `asistencia_ambiente_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
  ADD CONSTRAINT `asistencia_ambiente_id_competencia_foreign` FOREIGN KEY (`id_competencia`) REFERENCES `competencia` (`id_competencia`),
  ADD CONSTRAINT `asistencia_ambiente_id_instructor_foreign` FOREIGN KEY (`id_instructor`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `asistencia_sede`
--
ALTER TABLE `asistencia_sede`
  ADD CONSTRAINT `asistencia_sede_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `competencia`
--
ALTER TABLE `competencia`
  ADD CONSTRAINT `competencia_ibfk_1` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`);

--
-- Filtros para la tabla `ficha`
--
ALTER TABLE `ficha`
  ADD CONSTRAINT `ficha_ibfk_1` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`),
  ADD CONSTRAINT `ficha_ibfk_2` FOREIGN KEY (`id_jornada`) REFERENCES `jornada` (`id_jornada`);

--
-- Filtros para la tabla `ficha_instructor`
--
ALTER TABLE `ficha_instructor`
  ADD CONSTRAINT `fk_ficha_instructor_ficha` FOREIGN KEY (`id_ficha`) REFERENCES `ficha` (`id_ficha`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_ficha_instructor_usuarios` FOREIGN KEY (`id_instructor`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `huella`
--
ALTER TABLE `huella`
  ADD CONSTRAINT `huella_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `justificacion`
--
ALTER TABLE `justificacion`
  ADD CONSTRAINT `justificacion_ibfk_1` FOREIGN KEY (`id_asistencia_ambiente`) REFERENCES `asistencia_ambiente` (`id_asistencia_ambiente`);

--
-- Filtros para la tabla `model_has_permissions`
--
ALTER TABLE `model_has_permissions`
  ADD CONSTRAINT `model_has_permissions_permission_id_foreign` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `model_has_roles`
--
ALTER TABLE `model_has_roles`
  ADD CONSTRAINT `model_has_roles_role_id_foreign` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `programa`
--
ALTER TABLE `programa`
  ADD CONSTRAINT `programa_ibfk_1` FOREIGN KEY (`id_coordinacion`) REFERENCES `coordinacion` (`id_coordinacion`);

--
-- Filtros para la tabla `programa_competencia`
--
ALTER TABLE `programa_competencia`
  ADD CONSTRAINT `fk_competencia_id` FOREIGN KEY (`competencia_id`) REFERENCES `competencia` (`id_competencia`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_programa_id` FOREIGN KEY (`programa_id`) REFERENCES `programa` (`id_programa`) ON DELETE CASCADE;

--
-- Filtros para la tabla `registro_manual`
--
ALTER TABLE `registro_manual`
  ADD CONSTRAINT `registro_manual_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;

--
-- Filtros para la tabla `resultado_aprendizaje`
--
ALTER TABLE `resultado_aprendizaje`
  ADD CONSTRAINT `resultado_aprendizaje_ibfk_1` FOREIGN KEY (`id_competencia`) REFERENCES `competencia` (`id_competencia`);

--
-- Filtros para la tabla `role_has_permissions`
--
ALTER TABLE `role_has_permissions`
  ADD CONSTRAINT `role_has_permissions_permission_id_foreign` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `role_has_permissions_role_id_foreign` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_2` FOREIGN KEY (`id_ficha`) REFERENCES `ficha` (`id_ficha`);

--
-- Filtros para la tabla `usuario_roles`
--
ALTER TABLE `usuario_roles`
  ADD CONSTRAINT `FKor5vdfg7bv12b37vaawa9lee2` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`);

--
-- Filtros para la tabla `visitante`
--
ALTER TABLE `visitante`
  ADD CONSTRAINT `fk_visitante_asistencia` FOREIGN KEY (`id_asistencia_sede`) REFERENCES `asistencia_sede` (`id_asistencia`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
