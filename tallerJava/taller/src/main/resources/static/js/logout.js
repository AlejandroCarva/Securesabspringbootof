// logout.js - Manejo centralizado de cierre de sesión

console.log('📋 logout.js INICIANDO...');

document.addEventListener('DOMContentLoaded', function() {
    console.log('✅ logout.js - DOMContentLoaded');

    const logoutForm = document.getElementById('logoutForm');
    const logoutBtn = document.getElementById('logoutBtn');
    const logoutBtnSidebar = document.getElementById('logoutBtnSidebar');

    console.log('=== VERIFICACIÓN DE ELEMENTOS ===');
    console.log('logoutForm existe:', !!logoutForm);
    console.log('logoutBtn existe:', !!logoutBtn);
    console.log('logoutBtnSidebar existe:', !!logoutBtnSidebar);

    // --- Función para cerrar sesión ---
    function handleLogout(event) {
        console.log('🚪 handleLogout EJECUTADA');

        if (event) {
            event.preventDefault();
            event.stopPropagation();
            console.log('Evento prevenido');
        }

        console.log('🚪 === INICIANDO CIERRE DE SESIÓN ===');

        if (logoutForm) {
            console.log('✅ Formulario encontrado');
            console.log('📝 Action:', logoutForm.getAttribute('action'));
            console.log('📝 Method:', logoutForm.getAttribute('method'));

            // Agregar delay para asegurar que se ejecute
            setTimeout(function() {
                console.log('📝 ENVIANDO FORMULARIO...');
                logoutForm.submit();
            }, 50);
        } else {
            console.error('❌ Formulario de logout NO encontrado');
            console.log('Intentando redirección alternativa a /logout');
            window.location.href = '/logout';
        }
    }

    // --- CONFIGURAR BOTÓN HEADER ---
    if (logoutBtn) {
        console.log('✅ Configurando logoutBtn (header)');
        console.log('   Tag:', logoutBtn.tagName);
        console.log('   ID:', logoutBtn.id);
        console.log('   Text:', logoutBtn.innerText || logoutBtn.textContent);

        logoutBtn.addEventListener('click', function(e) {
            console.log('🖱️ CLICK EN logoutBtn');
            handleLogout(e);
        });

        // También permitir Enter si es un elemento interactivo
        logoutBtn.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                console.log('⌨️ ENTER EN logoutBtn');
                handleLogout(e);
            }
        });

        // Listener para cualquier tecla para debugging
        logoutBtn.addEventListener('keydown', function(e) {
            console.log('⌨️ Tecla presionada en logoutBtn:', e.key);
        });
    } else {
        console.warn('⚠️ NO SE ENCONTRÓ logoutBtn');
    }

    // --- CONFIGURAR BOTÓN SIDEBAR ---
    if (logoutBtnSidebar) {
        console.log('✅ Configurando logoutBtnSidebar');

        logoutBtnSidebar.addEventListener('click', function(e) {
            console.log('🖱️ CLICK EN logoutBtnSidebar');
            handleLogout(e);
        });
    } else {
        console.log('ℹ️ logoutBtnSidebar no existe (normal en algunos layouts)');
    }

    // --- VERIFICAR CSRF TOKEN ---
    if (logoutForm) {
        const csrfInputs = logoutForm.querySelectorAll('input[type="hidden"]');
        console.log('✅ Inputs ocultos en formulario:', csrfInputs.length);
        csrfInputs.forEach((input, index) => {
            console.log(`   Input ${index}: name="${input.getAttribute('name')}" value="${input.getAttribute('value').substring(0, 20)}..."`);
        });
    }

    console.log('✅ logout.js - INICIALIZACIÓN COMPLETADA\n');
});

// Fallback para elementos que se cargan dinámicamente
setTimeout(function() {
    console.log('🔄 FALLBACK: Verificando elemento logoutBtn después de 2 segundos...');

    const logoutBtn = document.getElementById('logoutBtn');
    const logoutForm = document.getElementById('logoutForm');

    if (logoutBtn && logoutForm && !logoutBtn._logoutEventAttached) {
        console.log('✅ Agregando listener tardío a logoutBtn');

        logoutBtn.addEventListener('click', function(e) {
            console.log('🖱️ CLICK FALLBACK en logoutBtn');
            e.preventDefault();
            e.stopPropagation();

            console.log('📝 Enviando formulario...');
            logoutForm.submit();
        });

        logoutBtn._logoutEventAttached = true;
    }
}, 2000);

