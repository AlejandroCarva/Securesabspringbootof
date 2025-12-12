// logicaI.js - Lógica JavaScript específica para el rol de Instructor

console.log('✅ logicaI.js CARGADO');

document.addEventListener('DOMContentLoaded', () => {
    console.log('✅ logicaI.js - INICIALIZANDO');

    // --- Selectores de elementos del layout principal ---
    const userMenu = document.getElementById('userMenu');
    const userDropdown = document.getElementById('userDropdown');

    // --- MENÚ DE USUARIO (DROPDOWN) ---
    if (userMenu && userDropdown) {
        console.log('✅ Configurando menú de usuario...');

        userMenu.addEventListener('click', (event) => {
            event.stopPropagation();
            const isShowing = userDropdown.classList.toggle('active');
            userMenu.setAttribute('aria-expanded', isShowing);
            console.log('✅ Menú de usuario toggled:', isShowing ? 'ABIERTO' : 'CERRADO');
        });

        // Cerrar menú al presionar ESC
        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && userDropdown.classList.contains('active')) {
                userDropdown.classList.remove('active');
                userMenu.setAttribute('aria-expanded', 'false');
                console.log('✅ Menú de usuario cerrado con ESC');
            }
        });
    }

    // Cerrar el dropdown del usuario si se hace clic fuera
    document.addEventListener('click', (event) => {
        if (userDropdown && userDropdown.classList.contains('active') &&
            !userDropdown.contains(event.target) &&
            userMenu && !userMenu.contains(event.target)) {
            userDropdown.classList.remove('active');
            userMenu.setAttribute('aria-expanded', 'false');
            console.log('✅ Menú de usuario cerrado (clic fuera)');
        }
    });

    console.log('✅ logicaI.js - CONFIGURACIÓN COMPLETADA');
});

