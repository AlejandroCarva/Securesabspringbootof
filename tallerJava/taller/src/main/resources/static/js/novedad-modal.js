document.addEventListener('DOMContentLoaded', () => {
    console.log('[novedad-modal] Script cargado, iniciando listeners');

    const qs = (sel, ctx = document) => ctx.querySelector(sel);
    const qsa = (sel, ctx = document) => Array.from(ctx.querySelectorAll(sel));
    const nuevaNovedadTexts = [
        'nueva novedad',
        '+ nueva novedad',
        'nueva novedad / comunicado',
        'registrar novedad'
    ];

    const state = { responseModal: null, crearModal: null };

    // ===== DELEGACIÓN DE EVENTOS GLOBAL PARA BOTONES =====
    // Esto captura CUALQUIER click en el documento y verifica si fue en los botones
    document.addEventListener('click', (e) => {
        const target = e.target;

        // Click en botón Ver Detalle
        if (target.closest('.btnVerDetalle')) {
            console.log('[novedad-modal] Click en Ver Detalle (delegación global)');
            e.preventDefault();
            e.stopPropagation();

            const btn = target.closest('.btnVerDetalle');
            const row = btn.closest('tr');
            const cells = row.querySelectorAll('td');

            const modal = qs('#responseModal');
            const content = qs('#responseModalContent');

            if (!modal || !content) {
                console.error('[novedad-modal] Modal o content no encontrado');
                return;
            }

            try {
                const descripcionDiv = cells[3].querySelector('.descripcion-completa');
                const respuestaDiv = cells[4].querySelector('.respuesta-completa');
                const titulo = cells[2].textContent.trim();
                const fecha = cells[0].textContent.trim();
                const ficha = cells[1].textContent.trim();
                const instructor = cells[5].textContent.trim();
                const estado = cells[6].textContent.trim();
                // Debido a la columna "Adjunto" hemos insertado una celda extra,
                // el campo 'tipo' queda ahora en la posición 8 y el adjunto en la 7.
                const tipo = cells[8] ? cells[8].textContent.trim() : 'Comunicado';
                // Adjuntos: columna 8 (índice 7)
                const adjCell = cells[7];
                let adjHref = null;
                if (adjCell) {
                    const a = adjCell.querySelector('a');
                    if (a) adjHref = a.getAttribute('href');
                }
                const descripcion = descripcionDiv ? descripcionDiv.textContent.trim() : 'Sin descripción';
                const respuesta = respuestaDiv ? respuestaDiv.textContent.trim() : 'Sin respuesta';
                const novedadId = btn.getAttribute('data-id');

                content.innerHTML = `
        <div style="display: grid; gap: 1rem;">
          <!-- Ficha afectada -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Ficha afectada <span style="color: #ef4444;">*</span></label>
            <input type="text" value="${ficha}" readonly style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827;">
          </div>
          
          <!-- Instructor responsable -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Instructor responsable <span style="color: #ef4444;">*</span></label>
            <input type="text" value="${instructor}" readonly style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827;">
          </div>
          
          <!-- Fecha -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Fecha <span style="color: #ef4444;">*</span></label>
            <input type="text" value="${fecha}" readonly style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827;">
          </div>
          
          <!-- Tipo de novedad -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Tipo de novedad</label>
            <input type="text" value="${tipo}" readonly style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827;">
          </div>
          
          <!-- Título -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Título / asunto <span style="color: #ef4444;">*</span></label>
            <input type="text" value="${titulo}" readonly style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827;">
          </div>
          
          <!-- Descripción -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Descripción detallada <span style="color: #ef4444;">*</span></label>
            <textarea readonly rows="5" style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827; resize: vertical;">${descripcion}</textarea>
          </div>
          
          <!-- Respuesta del instructor -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Respuesta del instructor</label>
            <textarea readonly rows="5" style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: #f9fafb; color: #111827; resize: vertical;">${respuesta}</textarea>
          </div>

          <!-- Adjunto (vista rápida) -->
          <div>
            <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Adjunto</label>
            ${adjHref ? `<a href="${adjHref}" target="_blank" class="text-blue-600 hover:underline">Ver adjunto</a>` : '<span>No hay adjunto</span>'}
          </div>

          <!-- Estado -->
          <div>
            <label style="display: block; font-size: 0.875rem; margin-bottom: 0.5rem;">Cambiar estado</label>
            <select id="estadoNovedadSelect" style="width: 100%; padding: 0.5rem 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: white; color: #111827;">
              <option value="Pendiente" ${estado === 'Pendiente' ? 'selected' : ''}>Pendiente</option>
              <option value="En proceso" ${estado === 'En proceso' ? 'selected' : ''}>En proceso</option>
              <option value="Resuelto" ${estado === 'Resuelto' ? 'selected' : ''}>Resuelto</option>
              <option value="Aprobado" ${estado === 'Aprobado' ? 'selected' : ''}>Aprobado</option>
              <option value="Rechazado" ${estado === 'Rechazado' ? 'selected' : ''}>Rechazado</option>
            </select>
          </div>
          
          <!-- Botones -->
          <div style="display: flex; gap: 0.75rem; justify-content: flex-end; padding-top: 1rem; border-top: 1px solid #e5e7eb;">
            <button onclick="document.getElementById('responseModal').classList.add('hidden'); document.getElementById('responseModal').classList.remove('flex');" style="padding: 0.5rem 1rem; border: 1px solid #d1d5db; border-radius: 0.375rem; background-color: white; color: #374151; cursor: pointer;" onmouseover="this.style.backgroundColor='#f9fafb'" onmouseout="this.style.backgroundColor='white'">
              Cancelar
            </button>
            <button onclick="guardarEstadoNovedad(${novedadId})" style="padding: 0.5rem 1rem; background-color: #15803d; color: white; border-radius: 0.375rem; border: none; cursor: pointer;" onmouseover="this.style.backgroundColor='#166534'" onmouseout="this.style.backgroundColor='#15803d'">
              Guardar cambios
            </button>
          </div>
        </div>
      `;

                modal.classList.remove('hidden');
                modal.classList.add('flex');
                console.log('[novedad-modal] Modal de detalle mostrado');
            } catch (err) {
                console.error('[novedad-modal] Error mostrando detalle de novedad:', err);
                content.innerHTML = '<div class="text-center text-red-600">Error mostrando detalles de la novedad. Revisa la consola.</div>';
                modal.classList.remove('hidden');
                modal.classList.add('flex');
            }
             return;
         }

         // Click en botón Editar Novedad -> abrir modal con formulario de edición
         if (target.closest('.btnEditarNovedad')) {
            console.log('[novedad-modal] Click en Editar Novedad (delegación global)');
            e.preventDefault();
            e.stopPropagation();

            const btn = target.closest('.btnEditarNovedad');
            if (!btn) return;
            const row = btn.closest('tr');
            if (!row) return;
            const cells = row.querySelectorAll('td');

            const modal = qs('#editarNovedadModal');
            const content = qs('#editarNovedadContent');
            if (!modal || !content) {
                console.error('[novedad-modal] Modal de editar o content no encontrado');
                return;
            }

            try {
                // Extraer valores con seguridad (indices pueden variar si la plantilla cambia)
                const getCellText = (index) => (cells[index] ? (cells[index].textContent || '').trim() : '');
                const titulo = getCellText(2);
                const fecha = getCellText(0);
                const descripcion = (cells[3] && cells[3].querySelector('.descripcion-completa')) ? cells[3].querySelector('.descripcion-completa').textContent.trim() : '';
                const respuesta = (cells[4] && cells[4].querySelector('.respuesta-completa')) ? cells[4].querySelector('.respuesta-completa').textContent.trim() : '';
                const fichaId = row.getAttribute('data-ficha-id') || '';
                const instructorId = row.getAttribute('data-instructor-id') || '';
                const estado = getCellText(6) || 'Pendiente';
                // Adjuntos y tipo
                const adjCell = cells[7];
                let adjHref = null;
                if (adjCell) {
                    const a = adjCell.querySelector('a');
                    if (a) adjHref = a.getAttribute('href');
                }
                const tipo = (cells[8] ? (cells[8].textContent || '').trim() : '') || 'Comunicado';

                // Obtener opciones desde los selects existentes de la página
                const fichasSelect = qs('#novedadFicha');
                const instructoresSelect = qs('#novedadInstructor');

                let fichasOptions = '<option value="">Seleccione la ficha</option>';
                if (fichasSelect) {
                    Array.from(fichasSelect.options).forEach(opt => {
                        const val = opt.value || '';
                        const text = opt.textContent || opt.innerText || val;
                        const selected = (val && String(val) === String(fichaId)) ? 'selected' : '';
                        fichasOptions += `<option value="${val}" ${selected}>${text}</option>`;
                    });
                }

                let instructoresOptions = '<option value="">Seleccione un instructor</option>';
                if (instructoresSelect) {
                    Array.from(instructoresSelect.options).forEach(opt => {
                        const val = opt.value || '';
                        const text = opt.textContent || opt.innerText || val;
                        const selected = (val && String(val) === String(instructorId)) ? 'selected' : '';
                        instructoresOptions += `<option value="${val}" ${selected}>${text}</option>`;
                    });
                }

                content.innerHTML = `
        <form id="formEditarNovedadModal" onsubmit="guardarEdicionNovedad(event, ${btn.getAttribute('data-id')})" enctype="multipart/form-data">
          <div style="display: grid; gap: 1rem;">
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Ficha afectada <span style="color:#ef4444;">*</span></label>
              <select id="editFichaId" name="fichaId" required style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;">${fichasOptions}</select>
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Instructor responsable <span style="color:#ef4444;">*</span></label>
              <select id="editInstructorId" name="instructorId" required style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;">${instructoresOptions}</select>
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Fecha</label>
              <input type="text" value="${fecha}" readonly style="width:100%; padding:0.5rem; background:#f9fafb; border:1px solid #d1d5db; border-radius:0.375rem;" />
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Tipo de novedad</label>
              <select id="editTipo" name="tipo" style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;">
                <option value="Comunicado" ${tipo === 'Comunicado' ? 'selected' : ''}>Comunicado</option>
                <option value="Seguimiento" ${tipo === 'Seguimiento' ? 'selected' : ''}>Seguimiento</option>
                <option value="Alerta" ${tipo === 'Alerta' ? 'selected' : ''}>Alerta</option>
              </select>
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Estado</label>
              <select id="editEstado" name="estado" style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;">
                <option value="Pendiente" ${estado === 'Pendiente' ? 'selected' : ''}>Pendiente</option>
                <option value="En proceso" ${estado === 'En proceso' ? 'selected' : ''}>En proceso</option>
                <option value="Resuelto" ${estado === 'Resuelto' ? 'selected' : ''}>Resuelto</option>
                <option value="Aprobado" ${estado === 'Aprobado' ? 'selected' : ''}>Aprobado</option>
                <option value="Rechazado" ${estado === 'Rechazado' ? 'selected' : ''}>Rechazado</option>
              </select>
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Título / asunto <span style="color:#ef4444;">*</span></label>
              <input id="editTitulo" name="titulo" type="text" value="${titulo}" required style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;" />
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Descripción detallada <span style="color:#ef4444;">*</span></label>
              <textarea id="editDescripcion" name="descripcion" rows="5" required style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;">${descripcion}</textarea>
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Respuesta del coordinador (opcional)</label>
              <textarea id="editRespuesta" name="respuesta" rows="4" style="width:100%; padding:0.5rem; border:1px solid #d1d5db; border-radius:0.375rem;">${respuesta}</textarea>
            </div>
            <div>
              <label style="display:block; font-size:0.875rem; margin-bottom:0.5rem;">Adjunto (opcional)</label>
              <div style="margin-bottom:0.5rem;">${adjHref ? `<a href="${adjHref}" target="_blank" class="text-blue-600 hover:underline">Ver adjunto actual</a>` : '<span>No hay adjunto</span>'}</div>
              <input type="file" id="editArchivo" name="archivo" />
            </div>
            <div style="display:flex; gap:0.75rem; justify-content:flex-end; padding-top:1rem; border-top:1px solid #e5e7eb;">
              <button type="button" onclick="document.getElementById('editarNovedadModal').classList.add('hidden'); document.getElementById('editarNovedadModal').classList.remove('flex');" style="padding:0.5rem 1rem; border:1px solid #d1d5db; border-radius:0.375rem; background:white;">Cancelar</button>
              <button type="submit" style="padding:0.5rem 1rem; background:#15803d; color:white; border-radius:0.375rem; border:none;">Actualizar novedad</button>
            </div>
          </div>
        </form>
      `;

                // colocar selects seleccionados correctamente (por si no se rellenaron en el HTML)
                try { if (fichaId && qs('#editFichaId')) qs('#editFichaId').value = fichaId; } catch(e){}
                try { if (instructorId && qs('#editInstructorId')) qs('#editInstructorId').value = instructorId; } catch(e){}

                modal.classList.remove('hidden');
                modal.classList.add('flex');
                console.log('[novedad-modal] Modal de editar mostrado (dinámico)');
            } catch (err) {
                console.error('[novedad-modal] Error mostrando editor dinámico de novedad:', err);
                content.innerHTML = '<div class="text-center text-red-600">Error al abrir editor de novedad. Revisa la consola.</div>';
                modal.classList.remove('hidden');
                modal.classList.add('flex');
            }
            return;
        }

        // Click en botón Registrar -> delegar al handler centralizado (handleCrearSubmit)
        if (target.id === 'btnRegistrarNovedad' || target.closest('#btnRegistrarNovedad')) {
            console.log('[novedad-modal] Click en Registrar (delegación) -> usar handleCrearSubmit');
            e.preventDefault();
            e.stopPropagation();
            const form = qs('#formCrearNovedad');
            if (form && typeof handleCrearSubmit === 'function') {
                try { handleCrearSubmit(e, form); } catch (err) { console.error('[novedad-modal] Error ejecutando handleCrearSubmit desde delegación:', err); }
            } else if (form) {
                // último recurso: enviar por el flujo tradicional
                try { form.submit(); } catch (err) { console.error('[novedad-modal] No se pudo enviar el formulario tradicionalmente', err); alert('No se pudo crear la novedad (ver consola).'); }
            } else {
                console.error('[novedad-modal] No se encontró el formulario #formCrearNovedad para registrar');
            }
            return;
        }

        // Click en botón Cancelar
        if (target.id === 'btnCancelarNovedad' || target.closest('#btnCancelarNovedad')) {
            console.log('[novedad-modal] Click en Cancelar (delegación global)');
            e.preventDefault();
            hideModal('#crearNovedadModal');
            return;
        }

        // Click en botón Nueva Novedad
        if (target.id === 'btnNuevaNovedad' || target.closest('#btnNuevaNovedad')) {
            console.log('[novedad-modal] Click en Nueva Novedad (delegación global)');
            e.preventDefault();
            const modal = qs('#crearNovedadModal');
            const form = qs('#formCrearNovedad');
            if (form) {
                form.reset();
                const inputs = form.querySelectorAll('input[type="text"], textarea');
                inputs.forEach(i => i.value = '');
            }
            showModal(modal);
            return;
        }
    }, true); // CAPTURE PHASE

    console.log('[novedad-modal] Delegación global de eventos configurada');

    // --- CSRF helpers and AJAX helpers ---------------------------------
    function getCsrfTokenFromCookie() {
        try {
            const parts = document.cookie.split(';').map(s => s.trim());
            for (const p of parts) {
                if (p.startsWith('XSRF-TOKEN=')) {
                    return decodeURIComponent(p.substring('XSRF-TOKEN='.length));
                }
            }
        } catch (e) {
            console.warn('[novedad-modal] getCsrfTokenFromCookie error', e);
        }
        return null;
    }

    function csrfFetch(url, opts = {}) {
        const token = getCsrfTokenFromCookie();
        opts.headers = opts.headers || {};
        if (token) {
            opts.headers['X-XSRF-TOKEN'] = token; // Spring expects this header name
        }
        return fetch(url, opts);
    }

    window.ajaxEliminarNovedad = function(id) {
        if (!id) return;
        if (!confirm('¿Seguro que desea eliminar esta novedad?')) return;
        const body = new URLSearchParams();
        body.append('id', id);
        csrfFetch('/coordinador/novedades/eliminar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: body.toString()
        })
        .then(resp => {
            if (!resp.ok) throw new Error('Error al eliminar');
            return resp.text();
        })
        .then(() => {
            // remover fila de la tabla si existe
            const row = document.querySelector(`tr[data-novedad-id="${id}"]`);
            if (row && row.parentNode) row.parentNode.removeChild(row);
            insertTemporaryAlert('Novedad eliminada correctamente', 'success');
        })
        .catch(err => {
            console.error('[novedad-modal] Error eliminando novedad', err);
            alert('No se pudo eliminar la novedad. Ver consola para más detalles.');
        });
    };

    // Interceptar envíos de formularios dinámicos para endpoints de novedades
    document.addEventListener('submit', (e) => {
        const form = e.target;
        if (!form || !form.action) return;
        try {
            const action = form.action || '';
            if (action.includes('/coordinador/novedades/eliminar')) {
                e.preventDefault();
                const idField = form.querySelector('input[name="id"]');
                const id = idField ? idField.value : null;
                window.ajaxEliminarNovedad(id);
            } else if (action.includes('/coordinador/novedades/cambiar-estado')) {
                e.preventDefault();
                const data = new URLSearchParams(new FormData(form));
                csrfFetch('/coordinador/novedades/cambiar-estado', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: data.toString()
                })
                .then(resp => {
                    if (!resp.ok) throw new Error('Error al cambiar estado');
                    return resp.text();
                })
                .then(() => {
                    // actualizar fila si es posible
                    const id = form.querySelector('input[name="id"]')?.value;
                    const estado = form.querySelector('select[name="estado"]')?.value;
                    if (id && estado) {
                        const row = document.querySelector(`tr[data-novedad-id="${id}"]`);
                        if (row) {
                            const estadoCell = row.querySelector('td:nth-child(7)');
                            if (estadoCell) estadoCell.textContent = estado;
                        }
                    }
                    insertTemporaryAlert('Estado actualizado', 'success');
                })
                .catch(err => {
                    console.error('[novedad-modal] Error quick-state submit', err);
                    alert('No se pudo cambiar el estado. Ver consola para más detalles.');
                });
            }
        } catch (err) { /* ignore */ }
    });


    // Función global para guardar el estado de la novedad
    window.guardarEstadoNovedad = function(novedadId) {
        const estadoSelect = document.getElementById('estadoNovedadSelect');
        if (!estadoSelect) {
            alert('No se pudo encontrar el selector de estado');
            return;
        }

        const nuevoEstado = estadoSelect.value;

        if (!confirm(`¿Estás seguro de cambiar el estado a "${nuevoEstado}"?`)) {
            return;
        }

        const formData = new URLSearchParams();
        formData.append('id', novedadId);
        formData.append('estado', nuevoEstado);

        csrfFetch('/coordinador/novedades/cambiar-estado', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
            .then(resp => {
                console.log('[novedad-modal] Respuesta cambio estado:', resp.status);
                if (!resp.ok) throw new Error('Error al cambiar estado');
                return resp.text();
            })
            .then(() => {
                alert('Estado actualizado correctamente');
                // Actualizar la tabla
                const row = document.querySelector(`tr[data-novedad-id="${novedadId}"]`);
                if (row) {
                    const estadoCell = row.querySelector('td:nth-child(7)');
                    if (estadoCell) estadoCell.textContent = nuevoEstado;
                }
                // Cerrar modal
                const modal = document.getElementById('responseModal');
                if (modal) {
                    modal.classList.add('hidden');
                    modal.classList.remove('flex');
                }
            })
            .catch(err => {
                console.error('[novedad-modal] Error al cambiar estado:', err);
                alert('No se pudo cambiar el estado. Ver consola para más detalles.');
            });
    };

    // Función global para guardar la edición de la novedad
    window.guardarEdicionNovedad = function(event, novedadId) {
        event.preventDefault();

        const titulo = document.getElementById('editTitulo').value;
        const descripcion = document.getElementById('editDescripcion').value;
        const respuesta = document.getElementById('editRespuesta').value;
        const estado = document.getElementById('editEstado').value;
        const tipo = document.getElementById('editTipo').value;
        const fichaId = document.getElementById('editFichaId').value;
        const instructorId = document.getElementById('editInstructorId').value;

        if (!titulo || !descripcion) {
            alert('El título y la descripción son obligatorios');
            return;
        }

        if (!confirm('¿Estás seguro de actualizar esta novedad?')) {
            return;
        }

        // Construir FormData para incluir archivo si se selecciona
        const fd = new FormData();
        fd.append('id', novedadId);
        fd.append('titulo', titulo);
        fd.append('descripcion', descripcion);
        fd.append('respuesta', respuesta);
        fd.append('estado', estado);
        fd.append('tipo', tipo);
        fd.append('fichaId', fichaId);
        fd.append('instructorId', instructorId);
        const fileInput = document.getElementById('editArchivo');
        if (fileInput && fileInput.files && fileInput.files.length > 0) {
            fd.append('archivo', fileInput.files[0]);
        }

        csrfFetch('/coordinador/novedades/actualizar', {
            method: 'POST',
            body: fd
        })
            .then(resp => {
                console.log('[novedad-modal] Respuesta actualización:', resp.status);
                if (!resp.ok) throw new Error('Error al actualizar novedad');
                return resp.text();
            })
            .then(() => {
                alert('Novedad actualizada correctamente');
                // Cerrar modal
                const modal = document.getElementById('editarNovedadModal');
                if (modal) {
                    modal.classList.add('hidden');
                    modal.classList.remove('flex');
                }
                // Recargar página para ver cambios
                window.location.reload();
            })
            .catch(err => {
                console.error('[novedad-modal] Error al actualizar novedad:', err);
                alert('No se pudo actualizar la novedad. Ver consola para más detalles.');
            });
    };

    function refreshModalRefs() {
        state.responseModal = qs('#responseModal');
        state.crearModal = qs('#crearNovedadModal');
    }

    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
    }

    // Helper para logs estilo servlet
    function logServletResponse(resp, url) {
        try {
            const s = resp && resp.status ? resp.status : 'unknown';
            const t = resp && resp.statusText ? resp.statusText : '';
            console.log(`Servlet : Completed ${s} ${t}` + (url ? ` [${url}]` : ''));
        } catch (e) { /* noop */ }
    }

    function showModal(target) {
        const modal = typeof target === 'string' ? qs(target) : target;
        if (!modal) {
            console.error('[novedad-modal] showModal: modal no encontrado');
            return;
        }
        // Mostrar overlay ligero primero para que el fondo sea visible
        modal.classList.remove('hidden');
        modal.style.opacity = '0';
        // Forzar layout y luego animar a opacidad 1 (overlay visible)
        window.requestAnimationFrame(() => {
            modal.style.transition = 'opacity 180ms ease-out';
            modal.style.opacity = '1';
        });
        // Esperar un pequeño retardo antes de mostrar el contenido del modal (fade-in)
        // Añadimos una clase para activar estilos CSS (ya usamos animate-fade-in en plantilla)
        setTimeout(() => {
            modal.classList.add('flex');
            modal.setAttribute('aria-hidden', 'false');
            // asegurarse que el contenido interno tenga la animación
            const inner = modal.querySelector('div');
            if (inner) inner.classList.add('animate-fade-in');
        }, 150);
    }

    function hideModal(target) {
        const modal = typeof target === 'string' ? qs(target) : target;
        if (!modal) return;
        // Hacer fade-out del overlay y luego ocultar por completo
        try {
            modal.style.transition = 'opacity 140ms ease-in';
            modal.style.opacity = '0';
            // remover clase flex para evitar scroll issues
            modal.classList.remove('flex');
            setTimeout(() => {
                modal.classList.add('hidden');
                modal.setAttribute('aria-hidden', 'true');
                // limpiar animaciones internas
                const inner = modal.querySelector('div');
                if (inner) inner.classList.remove('animate-fade-in');
                // restore inline styles
                modal.style.transition = '';
                modal.style.opacity = '';
            }, 170);
        } catch (e) {
            modal.classList.add('hidden');
            modal.classList.remove('flex');
            modal.setAttribute('aria-hidden', 'true');
        }
    }

    function insertTemporaryAlert(message, type = 'success') {
        const alertEl = document.createElement('div');
        alertEl.className = `alert ${type === 'success' ? 'alert-success' : 'alert-error'}`;
        alertEl.textContent = message;
        const sectionHeader = qs('.section-header');
        if (sectionHeader && sectionHeader.parentNode) {
            sectionHeader.parentNode.insertBefore(alertEl, sectionHeader.nextSibling);
        } else {
            document.body.appendChild(alertEl);
        }
        setTimeout(() => alertEl.remove(), 3000);
    }

    function bindBackdrop(modal) {
        if (!modal || modal.dataset.nmBackdropBound === '1') return;
        modal.addEventListener('click', (e) => {
            if (e.target === modal) hideModal(modal);
        });
        modal.dataset.nmBackdropBound = '1';
    }

    function bindCloseButtons() {
        qsa('.modal-close').forEach(btn => {
            if (btn.dataset.nmCloseBound === '1') return;
            btn.addEventListener('click', () => hideModal(btn.closest('.modal')));
            btn.dataset.nmCloseBound = '1';
        });
    }

    function clearInstructorInfo() {
        const set = (sel, value) => {
            const el = qs(sel);
            if (el) el.textContent = value;
        };
        set('#instNombre', '-');
        set('#instCedula', '');
        set('#instCorreo', '-');
        set('#instTelefono', '-');
    }

    function fetchInstructor(id) {
        if (!id) {
            clearInstructorInfo();
            return;
        }
        fetch(`/coordinador/novedades/instructor/${id}`)
            .then(resp => {
                logServletResponse(resp, `/coordinador/novedades/instructor/${id}`);
                if (!resp.ok) throw new Error('No se pudo obtener instructor');
                return resp.json();
            })
            .then(data => {
                const nombre = `${data.nombre || ''}${data.apellido ? ' ' + data.apellido : ''}`.trim() || '-';
                const nombreEl = qs('#instNombre');
                if (nombreEl) nombreEl.textContent = nombre;
                const cedulaEl = qs('#instCedula');
                if (cedulaEl) cedulaEl.textContent = data.cedula ? `- ${data.cedula}` : '';
                const correoEl = qs('#instCorreo');
                if (correoEl) correoEl.textContent = data.correo || '-';
                const telEl = qs('#instTelefono');
                if (telEl) telEl.textContent = data.telefono || '-';
            })
            .catch(err => {
                console.error('[novedad-modal] Error cargando instructor', err);
                clearInstructorInfo();
            });
    }

    function fetchNovedadDetalles(id) {
        if (!id) return;
        fetch(`/coordinador/novedades/detalles/${id}`)
            .then(resp => {
                logServletResponse(resp, `/coordinador/novedades/detalles/${id}`);
                if (!resp.ok) throw new Error('No se pudo obtener detalles');
                return resp.json();
            })
            .then(data => {
                const setText = (sel, value) => {
                    const el = qs(sel);
                    if (el) el.textContent = value;
                };
                setText('#detTitulo', data.titulo || '-');
                setText('#detDescripcion', data.descripcion || '-');
                setText('#detFicha', data.fichaNumero || (data.fichaId ? `F-${data.fichaId}` : '-'));
                setText('#detEstado', data.estado || '-');
                setText('#detRespuestaInstructor', data.respuestaInstructor || '-');

                const adjEl = qs('#detAdjunto');
                if (adjEl) {
                    if (data.archivoAdjunto) {
                        const url = `/coordinador/novedades/archivo/${encodeURIComponent(data.archivoAdjunto)}`;
                        adjEl.innerHTML = `<a href="${url}" target="_blank" rel="noopener">Ver archivo</a>`;
                    } else {
                        adjEl.textContent = 'No hay archivo';
                    }
                }

                const respCoord = qs('#modalRespuesta');
                if (respCoord && data.respuestaCoordinador !== undefined) {
                    respCoord.value = data.respuestaCoordinador || '';
                }
            })
            .catch(err => {
                console.error('[novedad-modal] Error cargando detalles', err);
                ['#detTitulo', '#detDescripcion', '#detFicha', '#detEstado', '#detAdjunto', '#detRespuestaInstructor'].forEach(sel => {
                    const el = qs(sel);
                    if (!el) return;
                    el.textContent = sel === '#detAdjunto' ? 'No hay archivo' : '-';
                });
            });
    }

    function openResponseModal(data) {
        refreshModalRefs();
        if (!state.responseModal) {
            console.error('[novedad-modal] responseModal no está en el DOM');
            return;
        }
        const setValue = (sel, value) => {
            const el = qs(sel);
            if (el) el.value = value;
        };
        setValue('#modalNovedadId', data.id || '');
        setValue('#modalFichaId', data.fichaId || '');
        setValue('#modalInstructorId', data.instructorId || '');
        setValue('#modalRespuesta', data.respuesta || '');
        setValue('#modalEstado', data.estado || 'Pendiente');

        if (data.instructorId) {
            fetchInstructor(data.instructorId);
        } else {
            clearInstructorInfo();
        }
        if (data.id) fetchNovedadDetalles(data.id);

        showModal(state.responseModal);
    }

    function updateRowAfterResponseSave({ id, respuesta, estado }) {
        const row = qs(`tr[data-novedad-id="${id}"]`);
        if (!row) return;
        const previewCell = row.querySelector('td:nth-child(4) span');
        if (previewCell) {
            const shortText = respuesta ? (respuesta.length > 60 ? `${respuesta.substring(0, 60)}...` : respuesta) : 'Sin respuesta';
            previewCell.textContent = shortText;
            previewCell.setAttribute('title', respuesta || '');
        }
        const hidden = row.querySelector('.hidden-response');
        if (hidden) hidden.textContent = respuesta || '';
        const estadoCell = row.querySelector('td:nth-child(6)');
        if (estadoCell) estadoCell.textContent = estado;
    }

    function handleResponseSave() {
        const id = qs('#modalNovedadId')?.value;
        const fichaId = qs('#modalFichaId')?.value;
        const instructorId = qs('#modalInstructorId')?.value;
        const respuesta = qs('#modalRespuesta')?.value || '';
        const estado = qs('#modalEstado')?.value || 'Pendiente';

        if (!id) {
            alert('Id de novedad no encontrado');
            return;
        }

        const formData = new URLSearchParams();
        formData.append('id', id);
        formData.append('fichaId', fichaId || '');
        formData.append('instructorId', instructorId || '');
        formData.append('respuesta', respuesta);
        formData.append('estado', estado);

        csrfFetch('/coordinador/novedades/actualizar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
            .then(resp => {
                logServletResponse(resp, '/coordinador/novedades/actualizar');
                if (!resp.ok) throw new Error('Respuesta no OK');
                return resp.text();
            })
            .then(() => {
                updateRowAfterResponseSave({ id, respuesta, estado });
                hideModal(state.responseModal);
                insertTemporaryAlert('Respuesta guardada', 'success');
            })
            .catch(err => {
                console.error('[novedad-modal] Error guardando respuesta', err);
                alert('No se pudo guardar la respuesta (ver consola).');
            });
    }

    function handleResponseExport() {
        const id = qs('#modalNovedadId')?.value;
        if (!id) {
            alert('Id de novedad no encontrado');
            return;
        }
        window.open(`/coordinador/novedades/exportar/${id}`, '_blank');
    }

    function handleViewResponseClick(e) {
        const btn = e.target.closest('.view-response-btn');
        if (!btn) return;
        const row = btn.closest('tr');
        if (!row) return;

        e.preventDefault();
        openResponseModal({
            id: row.getAttribute('data-novedad-id'),
            fichaId: row.getAttribute('data-ficha-id'),
            instructorId: row.getAttribute('data-instructor-id'),
            respuesta: row.querySelector('.hidden-response')?.textContent.trim() || '',
            estado: row.querySelector('td:nth-child(6)')?.textContent.trim() || 'Pendiente'
        });
    }

    function gatherCrearFormValues(form) {
        const field = (selector) => form.querySelector(selector)?.value || '';
        const textField = (selector) => (form.querySelector(selector)?.value || '').trim();
        return {
            titulo: textField('#novedadTitulo'),
            descripcion: textField('#novedadDescripcion'),
            instructorId: field('#novedadInstructor'),
            fichaId: field('#novedadFicha')
        };
    }

    function buildNovedadRowHtml(nov) {
        const estados = ['Pendiente', 'En proceso', 'Resuelto', 'Revisado', 'Aprobado', 'Rechazado'];
        const estadoOpts = estados
            .map(estado => `<option value="${estado}" ${estado === (nov.estado || 'Pendiente') ? 'selected' : ''}>${estado}</option>`)
            .join('');
        const respuestaPreview = nov.respuesta && nov.respuesta.length > 40
            ? `${nov.respuesta.substring(0, 40)}...`
            : (nov.respuesta || 'Sin respuesta');
        const instructorNombre = nov.instructor
            ? `${nov.instructor.nombre || ''}${nov.instructor.apellido ? ' ' + nov.instructor.apellido : ''}`.trim() || 'N/A'
            : 'N/A';

                return `
            <td class="px-6 py-3">${escapeHtml(nov.fecha || '-')}</td>
            <td class="px-6 py-3">${escapeHtml(nov.fichaNumero || 'N/A')}</td>
            <td class="px-6 py-3">${escapeHtml(nov.titulo || '-')}</td>
            <td class="px-6 py-3" style="max-width:200px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                <span>${escapeHtml(nov.descripcion && nov.descripcion.length > 40 ? nov.descripcion.substring(0,40) + '...' : (nov.descripcion || 'Sin descripción'))}</span>
                <div class="hidden descripcion-completa" style="display:none">${escapeHtml(nov.descripcion || '')}</div>
            </td>
            <td class="px-6 py-3" style="max-width:200px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                <span>${escapeHtml(nov.respuesta && nov.respuesta.length > 40 ? nov.respuesta.substring(0,40) + '...' : (nov.respuesta || 'Sin respuesta'))}</span>
                <div class="hidden respuesta-completa" style="display:none">${escapeHtml(nov.respuesta || '')}</div>
            </td>
            <td class="px-6 py-3">${escapeHtml(instructorNombre)}</td>
            <td class="px-6 py-3">${escapeHtml(nov.estado || 'Pendiente')}</td>
            <td class="px-6 py-3 text-center">${nov.archivoAdjunto ? `<a href="/coordinador/novedades/archivo/${encodeURIComponent(nov.archivoAdjunto)}" target="_blank" class="text-blue-600 hover:underline">Adjunto</a>` : '-'}</td>
            <td class="px-6 py-3" style="display:none;" data-tipo="tipo">${escapeHtml(nov.tipo || 'Comunicado')}</td>
            <td class="px-6 py-3 text-center">
                <div class="flex justify-center gap-2">
                    <button type="button"
                            class="btnVerDetalle p-2 text-blue-600 hover:bg-blue-50 rounded"
                            title="Ver detalle"
                            data-id="${nov.id}">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                        </svg>
                    </button>
                    <button type="button"
                            class="btnEditarNovedad p-2 text-green-600 hover:bg-green-50 rounded"
                            title="Editar"
                            data-id="${nov.id}">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                        </svg>
                    </button>
                    <form action="/coordinador/novedades/eliminar" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${nov.id}"/>
                        <button type="submit"
                                class="p-2 text-red-600 hover:bg-red-50 rounded"
                                title="Eliminar"
                                onclick="return confirm('¿Seguro que desea eliminar esta novedad?');">
                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                            </svg>
                        </button>
                    </form>
                </div>
            </td>
        `;
    }

    function insertOrUpdateNovedadRow(nov) {
        if (!nov) return;
        const tbody = qs('#novedadesRecientesTabla');
        if (!tbody) return;

        let row = tbody.querySelector(`tr[data-novedad-id="${nov.id}"]`);
        if (!row) {
            row = document.createElement('tr');
            row.setAttribute('data-novedad-id', nov.id);
            if (nov.fichaId) row.setAttribute('data-ficha-id', nov.fichaId);
            if (nov.instructor?.id) row.setAttribute('data-instructor-id', nov.instructor.id);
            row.innerHTML = buildNovedadRowHtml(nov);
            tbody.insertBefore(row, tbody.firstChild);

            const count = qs('#novedadesRecientesCount');
            if (count) {
                const current = parseInt(count.textContent || '0', 10) || 0;
                count.textContent = String(current + 1);
            }
            return;
        }

        if (nov.fichaId) row.setAttribute('data-ficha-id', nov.fichaId);
        if (nov.instructor?.id) row.setAttribute('data-instructor-id', nov.instructor.id);
        row.innerHTML = buildNovedadRowHtml(nov);
    }

    function handleCrearSubmit(event, form) {
        event.preventDefault();

        const values = gatherCrearFormValues(form);
        const errors = [];
        if (!values.fichaId) errors.push('Seleccione la ficha afectada.');
        if (!values.instructorId) errors.push('Seleccione el instructor responsable.');
        if (!values.titulo) errors.push('El título es obligatorio.');
        if (!values.descripcion) errors.push('La descripción es obligatoria.');

        if (errors.length) {
            alert(`Errores:\n${errors.join('\n')}`);
            return;
        }

        // Usar FormData para incluir archivo si se seleccionó
        const formData = new FormData(form);
        formData.set('titulo', values.titulo);
        formData.set('descripcion', values.descripcion);
        formData.set('fichaId', values.fichaId);
        formData.set('instructorId', values.instructorId);

        try { console.log('[novedad-modal] Enviando AJAX multipart -> /coordinador/novedades/guardar-ajax'); } catch(e){}
        csrfFetch('/coordinador/novedades/guardar-ajax', {
            method: 'POST',
            body: formData
        })
            .then(resp => {
                logServletResponse(resp, '/coordinador/novedades/guardar-ajax');
                const ct = resp.headers && resp.headers.get ? (resp.headers.get('content-type') || '') : '';
                if (resp.ok && ct.includes('application/json')) return resp.json();
                if (resp.ok) return { success: true, novedad: null, __fromNonJson: true };
                throw new Error('Respuesta no OK');
            })
            .then(json => {
                if (!json.success) {
                    const errs = Array.isArray(json.errors) ? json.errors : ['Error desconocido'];
                    alert(`Errores:\n${errs.join('\n')}`);
                    return;
                }

                if (json.novedad) insertOrUpdateNovedadRow(json.novedad);
                try { form.reset(); } catch (e) { /* noop */ }

                try { hideModal(form.closest('#crearNovedadModal')); } catch (e) { try { hideModal(state.crearModal); } catch (e) { /* noop */ } }

                insertTemporaryAlert('Novedad creada correctamente', 'success');

                // Si el backend respondió con HTML/redirect en lugar de JSON, recargar para reflejar cambios
                if (json.__fromNonJson) {
                    try { window.location.reload(); } catch (e) { /* noop */ }
                }
            })
            .catch(err => {
                console.error('[novedad-modal] Error creando novedad', err);
                // Intentar fallback por envío tradicional del formulario
                try {
                    console.warn('[novedad-modal] AJAX falló, reintentando envío tradicional del formulario (fallback flag)');
                    form.dataset.nmSkipSubmit = '1';
                } catch (e) { /* noop */ }
                try { form.submit(); } catch (e) {
                    console.error('[novedad-modal] No se pudo enviar el formulario tradicionalmente', e);
                    alert('No se pudo crear la novedad (ver consola).');
                }
            })
            .finally(() => {
                try {
                    // reactivar el botón de enviar para permitir nuevos envíos
                    const submitBtn = form.querySelector('#btnRegistrarNovedad') || qs('#btnRegistrarNovedad');
                    if (submitBtn) {
                        submitBtn.disabled = false;
                        // restaurar texto por si fue cambiado
                        submitBtn.innerText = 'Registrar novedad';
                    }
                } catch (e) { /* noop */ }
            });
    }

    function getPrimaryCrearForm() {
        return qsa('#formCrearNovedad').find(form => !form.closest('#crearNovedadModal')) || null;
    }

    function bindCreateForms() {
        qsa('#formCrearNovedad').forEach(form => {
            if (form.dataset.nmSubmitBound === '1') return;
            console.log('[novedad-modal] Vinculando submit del formulario #formCrearNovedad');
            form.addEventListener('submit', function(event) {
                if (form.dataset.nmSkipSubmit === '1') {
                    // permitir envío tradicional (flag establecido por fallback)
                    form.dataset.nmSkipSubmit = '0';
                    return true;
                }
                console.log('[novedad-modal] Evento SUBMIT capturado en formulario');
                handleCrearSubmit(event, form);
            });

            // Agregar listener para detectar validación inválida
            form.addEventListener('invalid', (event) => {
                console.log('[novedad-modal] Validación HTML5 falló en:', event.target.id || event.target.name, 'valor:', event.target.value);
            }, true);

            form.dataset.nmSubmitBound = '1';
        });

        // Vincular botón Cancelar
        const btnCancelar = qs('#btnCancelarNovedad');
        if (btnCancelar && btnCancelar.dataset.nmCancelBound !== '1') {
            console.log('[novedad-modal] Vinculando botón Cancelar');
            btnCancelar.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('[novedad-modal] Click en Cancelar');
                hideModal('#crearNovedadModal');
            });
            btnCancelar.dataset.nmCancelBound = '1';
        }

        // Vincular botón Registrar (ejecutar directamente, no esperar submit)
        const btnRegistrar = qs('#btnRegistrarNovedad');
        if (btnRegistrar && btnRegistrar.dataset.nmRegBound !== '1') {
            console.log('[novedad-modal] Vinculando botón Registrar en ID:', btnRegistrar.id, 'Tipo:', btnRegistrar.type);
            // Usar capture: true para ejecutar ANTES que cualquier otro listener
            btnRegistrar.addEventListener('click', (e) => {
                console.log('[novedad-modal] *** CLICK EN REGISTRAR DETECTADO! ***');
                e.preventDefault(); // PREVENIR el submit normal
                e.stopPropagation(); // DETENER propagación a otros listeners
                e.stopImmediatePropagation(); // DETENER otros listeners en este mismo elemento

                const form = qs('#formCrearNovedad');
                if (form) {
                    console.log('[novedad-modal] Formulario encontrado, ejecutando handleCrearSubmit');
                    handleCrearSubmit(e, form);
                } else {
                    console.error('[novedad-modal] No se encontró el formulario #formCrearNovedad');
                }
            }, { capture: true }); // CAPTURE PHASE
            btnRegistrar.dataset.nmRegBound = '1';
            console.log('[novedad-modal] Listener agregado al botón Registrar con capture:true');
        } else if (!btnRegistrar) {
            console.error('[novedad-modal] NO SE ENCONTRÓ el botón #btnRegistrarNovedad');
        } else {
            console.log('[novedad-modal] Botón Registrar ya tenía listener vinculado');
        }
    }

    function bindResetButton() {
        const btn = qs('#resetFormularioNovedadBtn');
        if (!btn || btn.dataset.nmResetBound === '1') return;
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const form = getPrimaryCrearForm();
            if (form) form.reset();
            insertTemporaryAlert('Formulario limpiado', 'success');
        });
        btn.dataset.nmResetBound = '1';
    }

    function initNovedadModals() {
        refreshModalRefs();
        console.log('[novedad-modal] init -> responseModal:', !!state.responseModal, 'crearModal:', !!state.crearModal);

        if (state.responseModal) bindBackdrop(state.responseModal);
        if (state.crearModal) bindBackdrop(state.crearModal);
        bindCloseButtons();
        bindCreateForms();
        bindResetButton();

        // Listeners explícitos para botones de cierre con ids (asegura que la 'x' cierre el modal)
        try {
            const btnResp = qs('#btnCloseResponseModal');
            if (btnResp && !btnResp.dataset.nmCloseIdBound) {
                btnResp.addEventListener('click', (e) => { e.preventDefault(); hideModal('#responseModal'); });
                btnResp.dataset.nmCloseIdBound = '1';
            }
            const btnEditar = qs('#btnCloseEditarNovedad');
            if (btnEditar && !btnEditar.dataset.nmCloseIdBound) {
                btnEditar.addEventListener('click', (e) => { e.preventDefault(); hideModal('#editarNovedadModal'); });
                btnEditar.dataset.nmCloseIdBound = '1';
            }
            const btnCrear = qs('#btnCloseCrearNovedad');
            if (btnCrear && !btnCrear.dataset.nmCloseIdBound) {
                btnCrear.addEventListener('click', (e) => { e.preventDefault(); hideModal('#crearNovedadModal'); });
                btnCrear.dataset.nmCloseIdBound = '1';
            }
        } catch (e) { console.warn('[novedad-modal] Error ligando botones de cierre por id', e); }

        const saveBtn = qs('#modalSaveBtn');
        if (saveBtn && saveBtn.dataset.nmClickBound !== '1') {
            saveBtn.addEventListener('click', handleResponseSave);
            saveBtn.dataset.nmClickBound = '1';
        }

        const exportBtn = qs('#modalExportBtn');
        if (exportBtn && exportBtn.dataset.nmClickBound !== '1') {
            exportBtn.addEventListener('click', handleResponseExport);
            exportBtn.dataset.nmClickBound = '1';
        }
    }

    function handleNuevaNovedadTrigger(e) {
        const selectors = [
            '[data-open-modal="crear-novedad"]',
            '[data-modal-target="#crearNovedadModal"]',
            '[data-target="#crearNovedadModal"]',
            '[href="#crearNovedadModal"]',
            '.open-crear-novedad',
            '.btn-nueva-novedad',
            '#btnNuevaNovedad'
        ];

        const directTrigger = e.target.closest(selectors.join(','));
        let shouldOpen = !!directTrigger;

        if (!shouldOpen) {
            const text = (e.target.textContent || '').replace(/\s+/g, ' ').trim().toLowerCase();
            if (text) {
                shouldOpen = nuevaNovedadTexts.some(fragment => text.includes(fragment));
            }
        }

        if (!shouldOpen) return;

        e.preventDefault();
        e.stopPropagation();
        refreshModalRefs();

        if (!state.crearModal) {
            console.error('[novedad-modal] No se encontró #crearNovedadModal para abrir');
            return;
        }

        // Antes de mostrar, limpiar el formulario dentro del modal (si existe)
        try {
            const crearForm = state.crearModal.querySelector('#formCrearNovedad');
            if (crearForm) {
                crearForm.reset();
                const inputs = Array.from(crearForm.querySelectorAll('input[type="text"], input[type="number"], textarea'));
                inputs.forEach(i => i.value = '');
                const selects = Array.from(crearForm.querySelectorAll('select'));
                selects.forEach(s => s.selectedIndex = 0);
                const file = crearForm.querySelector('input[type="file"]'); if (file) file.value = '';
                const first = crearForm.querySelector('select, input, textarea'); if (first) first.focus();
            }
        } catch (err) { console.warn('[novedad-modal] No se pudo limpiar formulario antes de abrir', err); }

        showModal(state.crearModal);
    }

    function handleEscape(e) {
        if (e.key !== 'Escape') return;
        hideModal(state.responseModal);
        hideModal(state.crearModal);
    }

    document.addEventListener('click', handleViewResponseClick);
    document.addEventListener('click', handleNuevaNovedadTrigger, true);
    document.addEventListener('keydown', handleEscape);

    initNovedadModals();

    // Listener de captura global para asegurar que los botones 'x' cierren los modales
    document.addEventListener('click', (e) => {
        const btnCloseEditar = e.target.closest('#btnCloseEditarNovedad');
        if (btnCloseEditar) {
            e.preventDefault();
            hideModal('#editarNovedadModal');
            return;
        }
        const btnCloseResp = e.target.closest('#btnCloseResponseModal');
        if (btnCloseResp) {
            e.preventDefault();
            hideModal('#responseModal');
            return;
        }
        const btnCloseCrear = e.target.closest('#btnCloseCrearNovedad');
        if (btnCloseCrear) {
            e.preventDefault();
            hideModal('#crearNovedadModal');
            return;
        }
    }, true);

    document.addEventListener('novedadSectionLoaded', () => {
        console.log('[novedad-modal] Sección crear-novedad inyectada, reinicializando listeners');
        initNovedadModals();
    });

    // Fallback directo: asegurar que el botón #btnNuevaNovedad abra el modal y deje rastro en consola
    try {
        const directBtn = qs('#btnNuevaNovedad');
        if (directBtn && !directBtn.dataset.nmDirectBound) {
            directBtn.addEventListener('click', (ev) => {
                console.log('[novedad-modal] click directo en #btnNuevaNovedad recibido');
                ev.preventDefault();
                ev.stopPropagation();
                refreshModalRefs();
                if (state.crearModal) {
                    // limpiar formulario si existe
                    try {
                        const crearForm = state.crearModal.querySelector('#formCrearNovedad');
                        if (crearForm) {
                            crearForm.reset();
                            const inputs = Array.from(crearForm.querySelectorAll('input[type="text"], input[type="number"], textarea'));
                            inputs.forEach(i => i.value = '');
                            const selects = Array.from(crearForm.querySelectorAll('select'));
                            selects.forEach(s => s.selectedIndex = 0);
                            const file = crearForm.querySelector('input[type="file"]'); if (file) file.value = '';
                        }
                    } catch (e) { console.warn('[novedad-modal] fallo limpiando formulario en fallback', e); }
                    showModal(state.crearModal);
                } else {
                    console.warn('[novedad-modal] fallback: no existe state.crearModal');
                }
            }, { capture: true });
            directBtn.dataset.nmDirectBound = '1';
        }
    } catch (e) { console.error('[novedad-modal] error al ligar fallback directo', e); }
});
