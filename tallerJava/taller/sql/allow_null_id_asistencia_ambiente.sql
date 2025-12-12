-- Script: allow_null_id_asistencia_ambiente.sql
-- Propósito: permitir que la columna FK `id_asistencia_ambiente` de la tabla `justificacion` acepte NULL.
-- Ejecutar en la base de datos MySQL donde esté la BD `securesab1`.

-- Opción 1: comando ALTER simple (MySQL común)
ALTER TABLE justificacion
  MODIFY COLUMN id_asistencia_ambiente INT NULL;

-- Opción 2: alternativa (si la anterior falla en tu versión):
-- ALTER TABLE justificacion CHANGE id_asistencia_ambiente id_asistencia_ambiente INT NULL;

-- Nota: Este script solo cambia la definición de la columna a nullable.
-- Después de ejecutarlo, reinicia la aplicación Spring Boot para probar de nuevo el formulario de radicación.

-- Ejemplo de ejecución desde línea de comandos (Windows / PowerShell):
-- mysql -u root -p securesab1 < sql/allow_null_id_asistencia_ambiente.sql

-- O ejemplo con sentencia directa:
-- mysql -u root -p -e "ALTER TABLE justificacion MODIFY COLUMN id_asistencia_ambiente INT NULL;" securesab1
