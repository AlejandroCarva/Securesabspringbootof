// Script para exportar novedades con filtros multicriterio
// Las funciones están en el scope global para ser accesibles desde onclick

// Función para mostrar/ocultar el panel de filtros avanzados
function toggleFiltros() {
    const panel = document.getElementById('filtrosPanel');
    if (panel) {
        panel.classList.toggle('hidden');
    }
}

// Función para limpiar todos los filtros
function limpiarFiltros() {
    document.getElementById('busqueda').value = '';
    document.getElementById('filtroEstado').value = '';
    document.getElementById('filtroTipo').value = '';
    document.getElementById('filtroInstructor').value = '';
    document.getElementById('filtroFicha').value = '';
    filtrarNovedades();
}

// Función para filtrar las novedades en la tabla
function filtrarNovedades() {
    const busqueda = document.getElementById('busqueda')?.value.toLowerCase() || '';
    const estado = document.getElementById('filtroEstado')?.value || '';
    const tipo = document.getElementById('filtroTipo')?.value || '';
    const instructor = document.getElementById('filtroInstructor')?.value || '';
    const ficha = document.getElementById('filtroFicha')?.value || '';

    console.log('[filtrarNovedades] Filtros:', { busqueda, estado, tipo, instructor, ficha });

    const tbody = document.getElementById('novedadesRecientesTabla');
    if (!tbody) return;

    const rows = tbody.querySelectorAll('tr');
    let visibleCount = 0;

    rows.forEach(row => {
        const cells = row.cells;

        // Saltar la fila de "no hay novedades"
        if (!cells || cells.length < 8) {
            return;
        }

        // Extraer datos de las celdas (orden: Fecha, Ficha, Título, Descripción, Respuesta, Instructor, Estado, Tipo-oculta)
        const fecha = cells[0]?.textContent.trim() || '';
        const fichaNum = cells[1]?.textContent.trim().toLowerCase() || '';
        const titulo = cells[2]?.textContent.trim().toLowerCase() || '';
        const descripcion = cells[3]?.textContent.trim().toLowerCase() || '';
        const respuesta = cells[4]?.textContent.trim().toLowerCase() || '';
        const instructorNombre = cells[5]?.textContent.trim().toLowerCase() || '';
        const estadoNovedad = cells[6]?.textContent.trim() || '';
        const tipoNovedad = cells[7]?.textContent.trim() || '';

        // Aplicar filtros
        let mostrar = true;

        // Filtro de búsqueda (busca en título, descripción, instructor y ficha)
        if (busqueda) {
            const hayCoincidencia =
                titulo.includes(busqueda) ||
                descripcion.includes(busqueda) ||
                instructorNombre.includes(busqueda) ||
                fichaNum.includes(busqueda);
            if (!hayCoincidencia) {
                mostrar = false;
            }
        }

        // Filtro de estado
        if (estado && estadoNovedad !== estado) {
            mostrar = false;
        }

        // Filtro de tipo
        if (tipo && tipoNovedad !== tipo) {
            mostrar = false;
        }

        // Filtro de instructor (por nombre completo)
        if (instructor && instructorNombre !== instructor.toLowerCase()) {
            mostrar = false;
        }

        // Filtro de ficha
        if (ficha && !fichaNum.includes(ficha.toLowerCase())) {
            mostrar = false;
        }

        // Mostrar u ocultar fila
        row.style.display = mostrar ? '' : 'none';
        if (mostrar) visibleCount++;
    });

    // Actualizar contador
    const countSpan = document.getElementById('novedadesRecientesCount');
    if (countSpan) {
        countSpan.textContent = visibleCount;
    }

    console.log('[filtrarNovedades] Mostrando', visibleCount, 'novedades');
}

// Función para obtener los filtros activos actuales (usada por exportación)
function obtenerFiltrosActivos() {
    const filtros = {
        busqueda: document.getElementById('busqueda')?.value || '',
        estado: document.getElementById('filtroEstado')?.value || '',
        tipo: document.getElementById('filtroTipo')?.value || '',
        instructor: document.getElementById('filtroInstructor')?.value || '',
        ficha: document.getElementById('filtroFicha')?.value || ''
    };
    return filtros;
}

// Función para contar novedades filtradas visibles
function contarNovedadesFiltradas() {
    const tbody = document.getElementById('novedadesRecientesTabla');
    if (!tbody) return 0;
    const rows = tbody.querySelectorAll('tr:not([style*="display: none"])');
    return rows.length;
}

// Función para exportar a Excel
function exportarExcel() {
    const filtros = obtenerFiltrosActivos();
    const count = contarNovedadesFiltradas();

    if (count === 0) {
        alert('No hay novedades para exportar con los filtros actuales.');
        return;
    }

    if (!confirm(`¿Deseas exportar ${count} novedad(es) a Excel con los filtros aplicados?`)) {
        return;
    }

    // Construir URL con parámetros de filtro
    const params = new URLSearchParams();
    if (filtros.busqueda) params.append('busqueda', filtros.busqueda);
    if (filtros.estado) params.append('estado', filtros.estado);
    if (filtros.tipo) params.append('tipo', filtros.tipo);
    if (filtros.instructor) params.append('instructor', filtros.instructor);
    if (filtros.ficha) params.append('ficha', filtros.ficha);

    const url = `/coordinador/novedades/exportar/excel?${params.toString()}`;
    console.log('[exportar-novedades] Exportando a Excel:', url);

    // Abrir en nueva ventana para descargar
    window.location.href = url;
}

// Función para exportar a PDF
function exportarPDF() {
    const filtros = obtenerFiltrosActivos();
    const count = contarNovedadesFiltradas();

    if (count === 0) {
        alert('No hay novedades para exportar con los filtros actuales.');
        return;
    }

    if (!confirm(`¿Deseas exportar ${count} novedad(es) a PDF con los filtros aplicados?`)) {
        return;
    }

    // Construir URL con parámetros de filtro
    const params = new URLSearchParams();
    if (filtros.busqueda) params.append('busqueda', filtros.busqueda);
    if (filtros.estado) params.append('estado', filtros.estado);
    if (filtros.tipo) params.append('tipo', filtros.tipo);
    if (filtros.instructor) params.append('instructor', filtros.instructor);
    if (filtros.ficha) params.append('ficha', filtros.ficha);

    const url = `/coordinador/novedades/exportar/pdf?${params.toString()}`;
    console.log('[exportar-novedades] Exportando a PDF:', url);

    // Abrir en nueva ventana para imprimir
    window.open(url, '_blank');
}

// Función para exportar a CSV
function exportarCSV() {
    const filtros = obtenerFiltrosActivos();
    const count = contarNovedadesFiltradas();

    if (count === 0) {
        alert('No hay novedades para exportar con los filtros actuales.');
        return;
    }

    if (!confirm(`¿Deseas exportar ${count} novedad(es) a CSV con los filtros aplicados?`)) {
        return;
    }

    // Construir URL con parámetros de filtro
    const params = new URLSearchParams();
    if (filtros.busqueda) params.append('busqueda', filtros.busqueda);
    if (filtros.estado) params.append('estado', filtros.estado);
    if (filtros.tipo) params.append('tipo', filtros.tipo);
    if (filtros.instructor) params.append('instructor', filtros.instructor);
    if (filtros.ficha) params.append('ficha', filtros.ficha);

    const url = `/coordinador/novedades/exportar/csv?${params.toString()}`;
    console.log('[exportar-novedades] Exportando a CSV:', url);

    // Descargar directamente
    window.location.href = url;
}

// Inicialización cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    console.log('[exportar-novedades] Script cargado - Funciones de exportación disponibles globalmente');
});
