// logicaI.js - Lógica JavaScript específica para el rol de Instructor

document.addEventListener('DOMContentLoaded', () => {
    console.log('? logicaI.js - INICIANDO para Instructor');
    
    // --- Selectores de elementos del layout principal ---
    const toggleMenuBtn = document.getElementById('toggleMenuBtn');
    const sidebar = document.getElementById('sidebar');
    const body = document.body;
    const userMenu = document.getElementById('userMenu');
    const userDropdown = document.getElementById('userDropdown');
    const logoutBtn = document.getElementById('logoutBtn');
    const logoutForm = document.getElementById('logoutForm');

    // --- 1. INICIALIZAR MENÚ LATERAL ---
    console.log('? 1. Configurando menú lateral...');
    
    if (sidebar) {
        // Inicia con el menú abierto
        sidebar.classList.add('active');
        body.classList.remove('menu-closed');
        
        if (toggleMenuBtn) {
            toggleMenuBtn.setAttribute('aria-expanded', 'true');
        }
        
        console.log('? Menú lateral inicializado como activo');
    } else {
        console.warn('? No se encontró el elemento sidebar');
    }

    // --- 2. CONFIGURACIÓN DEL BOTÓN DE TOGGLE DEL MENÚ ---
    if (toggleMenuBtn) {
        toggleMenuBtn.addEventListener('click', () => {
            console.log('? Botón menú clickeado');
            const isActive = sidebar.classList.toggle('active');
            body.classList.toggle('menu-closed');
            toggleMenuBtn.setAttribute('aria-expanded', isActive);
            
            // Guardar estado en localStorage
            localStorage.setItem('sidebarCollapsed', !isActive);
        });
        
        // Verificar estado guardado
        const sidebarCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
        if (sidebarCollapsed && sidebar) {
            sidebar.classList.remove('active');
            body.classList.add('menu-closed');
            toggleMenuBtn.setAttribute('aria-expanded', 'false');
        }
    }

    // --- 3. MENÚ DE USUARIO (DROPDOWN) ---
    if (userMenu && userDropdown) {
        console.log('? Configurando menú de usuario...');
        
        userMenu.addEventListener('click', (event) => {
            event.stopPropagation();
            const isShowing = userDropdown.classList.toggle('show');
            console.log('? Menú de usuario ' + (isShowing ? 'mostrado' : 'ocultado'));
        });

        // Cerrar menú al presionar ESC
        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && userDropdown.classList.contains('show')) {
                userDropdown.classList.remove('show');
                console.log('? Menú de usuario cerrado con ESC');
            }
        });
    }

    // Cerrar el dropdown del usuario si se hace clic fuera
    document.addEventListener('click', (event) => {
        if (userDropdown && userDropdown.classList.contains('show') && 
            !userDropdown.contains(event.target) && 
            userMenu && !userMenu.contains(event.target)) {
            userDropdown.classList.remove('show');
            console.log('? Menú de usuario cerrado (clic fuera)');
        }
    });

    // --- 4. LÓGICA DE CERRAR SESIÓN ---
    if (logoutBtn && logoutForm) {
        console.log('? Configurando botón de logout...');
        
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            console.log('? Iniciando proceso de logout...');
            
            // Confirmar antes de cerrar sesión
            if (confirm('¿Está seguro de que desea cerrar sesión?')) {
                logoutForm.submit();
            } else {
                console.log('? Logout cancelado por el usuario');
            }
        });
    }

    // --- 5. NOTIFICACIONES DE NOVEDADES ---
    const novedadesNotificationCount = document.getElementById('novedadesNotificationCount');
    
    function updateNovedadesCount() {
        console.log('? Actualizando contador de novedades...');
        
        // Aquí se puede implementar una llamada AJAX para obtener el número actual de novedades
        if (novedadesNotificationCount) {
            // Opcional: Hacer petición AJAX para obtener el conteo real
            /*
            fetch('/api/instructor/novedades-count')
                .then(response => response.json())
                .then(data => {
                    if (data.count > 0) {
                        novedadesNotificationCount.textContent = data.count;
                        novedadesNotificationCount.style.display = 'inline-block';
                    } else {
                        novedadesNotificationCount.style.display = 'none';
                    }
                })
                .catch(error => console.error('Error al obtener conteo:', error));
            */
            
            // Por ahora, mostrar si hay algún elemento con contador
            const count = parseInt(novedadesNotificationCount.textContent) || 0;
            if (count > 0) {
                novedadesNotificationCount.style.display = 'inline-block';
                console.log('? Hay ' + count + ' novedad(es) pendiente(s)');
            } else {
                novedadesNotificationCount.style.display = 'none';
            }
        }
    }

    // Llamar a la función de actualización al cargar la página
    updateNovedadesCount();

    // --- 6. INICIALIZAR SELECT2 (si está disponible) ---
    console.log('? Verificando disponibilidad de Select2...');
    
    if (typeof $ !== 'undefined' && $.fn.select2) {
        console.log('? Select2 disponible, inicializando...');
        
        $('.select2').select2({
            placeholder: "Seleccione una opción",
            allowClear: true,
            language: {
                noResults: function() {
                    return "No se encontraron resultados";
                }
            }
        });
        
        // Agregar evento para logs
        $('.select2').on('select2:open', function(e) {
            console.log('? Select2 abierto para: ' + $(this).attr('id') || $(this).attr('name'));
        });
    } else {
        console.warn('? Select2 no está disponible');
    }

    // --- 7. MANEJO DE MENSAJES FLASH ---
    function setupFlashMessages() {
        const flashMessages = document.querySelectorAll('.alert');
        
        flashMessages.forEach(message => {
            // Auto-ocultar mensajes después de 5 segundos
            setTimeout(() => {
                message.style.opacity = '0';
                message.style.transition = 'opacity 0.5s ease';
                setTimeout(() => message.remove(), 500);
            }, 5000);
            
            // Botón para cerrar manualmente
            const closeBtn = document.createElement('button');
            closeBtn.innerHTML = '&times;';
            closeBtn.className = 'flash-close-btn';
            closeBtn.style.cssText = `
                background: none;
                border: none;
                font-size: 1.5rem;
                cursor: pointer;
                position: absolute;
                right: 10px;
                top: 5px;
                color: inherit;
            `;
            
            closeBtn.addEventListener('click', () => {
                message.remove();
            });
            
            message.style.position = 'relative';
            message.style.paddingRight = '40px';
            message.appendChild(closeBtn);
        });
    }
    
    setupFlashMessages();

    // --- 8. MEJORAS DE ACCESIBILIDAD ---
    function setupAccessibility() {
        // Mejorar navegación con teclado en menús
        const menuItems = document.querySelectorAll('#menu a, .user-dropdown a');
        
        menuItems.forEach((item, index) => {
            item.setAttribute('tabindex', '0');
            
            item.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    item.click();
                }
                
                // Navegación con flechas en menú principal
                if (e.key === 'ArrowDown' || e.key === 'ArrowUp') {
                    e.preventDefault();
                    const nextIndex = e.key === 'ArrowDown' 
                        ? (index + 1) % menuItems.length 
                        : (index - 1 + menuItems.length) % menuItems.length;
                    menuItems[nextIndex].focus();
                }
            });
        });
        
        // Enfocar contenido principal al saltar navegación
        const skipToContent = document.createElement('a');
        skipToContent.href = '#main-content';
        skipToContent.textContent = 'Saltar al contenido principal';
        skipToContent.style.cssText = `
            position: absolute;
            top: -40px;
            left: 0;
            background: #007bff;
            color: white;
            padding: 8px;
            z-index: 1000;
            text-decoration: none;
        `;
        skipToContent.addEventListener('focus', function() {
            this.style.top = '0';
        });
        skipToContent.addEventListener('blur', function() {
            this.style.top = '-40px';
        });
        
        const mainContent = document.querySelector('.main-content-wrapper');
        if (mainContent) {
            mainContent.id = 'main-content';
            document.body.insertBefore(skipToContent, document.body.firstChild);
        }
    }
    
    setupAccessibility();

    // --- 9. DETECCIÓN DE CONEXIÓN ---
    function setupConnectionMonitor() {
        const connectionStatus = document.createElement('div');
        connectionStatus.id = 'connection-status';
        connectionStatus.style.cssText = `
            position: fixed;
            bottom: 10px;
            right: 10px;
            padding: 5px 10px;
            border-radius: 3px;
            font-size: 12px;
            z-index: 1000;
            display: none;
        `;
        document.body.appendChild(connectionStatus);
        
        function updateOnlineStatus() {
            if (navigator.onLine) {
                connectionStatus.textContent = '🟢 Conectado';
                connectionStatus.style.backgroundColor = '#d4edda';
                connectionStatus.style.color = '#155724';
                connectionStatus.style.display = 'block';
                
                setTimeout(() => {
                    connectionStatus.style.display = 'none';
                }, 3000);
            } else {
                connectionStatus.textContent = '🔴 Sin conexión';
                connectionStatus.style.backgroundColor = '#f8d7da';
                connectionStatus.style.color = '#721c24';
                connectionStatus.style.display = 'block';
            }
        }
        
        window.addEventListener('online', updateOnlineStatus);
        window.addEventListener('offline', updateOnlineStatus);
        updateOnlineStatus(); // Estado inicial
    }
    
    setupConnectionMonitor();

    // --- 10. MANEJO DE FORMULARIOS ---
    function setupFormEnhancements() {
        const forms = document.querySelectorAll('form');
        
        forms.forEach(form => {
            // Prevenir envío doble
            form.addEventListener('submit', function() {
                const submitButtons = this.querySelectorAll('button[type="submit"], input[type="submit"]');
                submitButtons.forEach(btn => {
                    btn.disabled = true;
                    const originalText = btn.textContent;
                    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Procesando...';
                    
                    // Restaurar después de 10 segundos por si hay error
                    setTimeout(() => {
                        btn.disabled = false;
                        btn.textContent = originalText;
                    }, 10000);
                });
            });
            
            // Validación en tiempo real
            const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
            inputs.forEach(input => {
                input.addEventListener('blur', function() {
                    if (!this.value.trim()) {
                        this.style.borderColor = '#dc3545';
                    } else {
                        this.style.borderColor = '#28a745';
                    }
                });
            });
        });
    }
    
    setupFormEnhancements();

    console.log('? logicaI.js - CONFIGURACIÓN COMPLETADA');
});

// --- FUNCIONES GLOBALES ADICIONALES ---

/**
 * Muestra un mensaje de alerta personalizado
 * @param {string} message - Mensaje a mostrar
 * @param {string} type - Tipo de mensaje: 'success', 'error', 'warning', 'info'
 * @param {number} duration - Duración en milisegundos (opcional)
 */
function showAlert(message, type = 'info', duration = 5000) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    alertDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 9999;
        padding: 15px;
        border-radius: 5px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(alertDiv);
    
    // Auto-remover después de la duración especificada
    setTimeout(() => {
        alertDiv.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => alertDiv.remove(), 300);
    }, duration);
    
    // Botón para cerrar
    const closeBtn = document.createElement('button');
    closeBtn.innerHTML = '&times;';
    closeBtn.style.cssText = `
        background: none;
        border: none;
        font-size: 1.5rem;
        cursor: pointer;
        position: absolute;
        right: 10px;
        top: 5px;
        color: inherit;
    `;
    closeBtn.onclick = () => alertDiv.remove();
    alertDiv.appendChild(closeBtn);
}

/**
 * Formatea una fecha para mostrar
 * @param {string} dateString - Fecha en formato ISO
 * @returns {string} Fecha formateada
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

/**
 * Realiza una petición AJAX con manejo de errores
 * @param {string} url - URL de la petición
 * @param {string} method - Método HTTP (GET, POST, etc.)
 * @param {Object} data - Datos a enviar (opcional)
 * @returns {Promise} Promesa con la respuesta
 */
function ajaxRequest(url, method = 'GET', data = null) {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: data ? JSON.stringify(data) : null
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        return response.json();
    })
    .catch(error => {
        console.error('Error en petición AJAX:', error);
        showAlert('Error al procesar la solicitud', 'error');
        throw error;
    });
}

// --- ESTILOS DE ANIMACIÓN ---
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
    
    .alert-success {
        background-color: #d4edda;
        border-color: #c3e6cb;
        color: #155724;
    }
    
    .alert-error {
        background-color: #f8d7da;
        border-color: #f5c6cb;
        color: #721c24;
    }
    
    .alert-warning {
        background-color: #fff3cd;
        border-color: #ffeaa7;
        color: #856404;
    }
    
    .alert-info {
        background-color: #d1ecf1;
        border-color: #bee5eb;
        color: #0c5460;
    }
`;
document.head.appendChild(style);

console.log('? logicaI.js - ARCHIVO COMPLETAMENTE CARGADO');