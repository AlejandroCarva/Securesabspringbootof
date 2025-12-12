// Pequeñas utilidades para la página editar-novedad
document.addEventListener('DOMContentLoaded', function () {
    try {
        const fileInput = document.getElementById('archivoNueva');
        const form = document.getElementById('formEditarNovedad');

        if (fileInput) {
            const span = document.createElement('span');
            span.id = 'archivoNuevaNombre';
            span.style.marginLeft = '0.6rem';
            span.className = 'text-sm text-gray-600';
            fileInput.parentNode.insertBefore(span, fileInput.nextSibling);

            fileInput.addEventListener('change', function () {
                const f = fileInput.files && fileInput.files[0];
                span.textContent = f ? f.name : '';
            });
        }

        if (form) {
            form.addEventListener('submit', function (e) {
                const titulo = document.getElementById('novedadTitulo') ? document.getElementById('novedadTitulo').value : '';
                if (!titulo || titulo.trim().length === 0) {
                    alert('El título es obligatorio');
                    e.preventDefault();
                    return;
                }
                if (!confirm('¿Actualizar la novedad "' + (titulo.substring(0, 60)) + '"?')) {
                    e.preventDefault();
                }
            });
        }
    } catch (err) {
        console.error('editar-novedad.js error', err);
    }
});

