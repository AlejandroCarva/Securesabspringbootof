// logicaV.js - Lógica JavaScript específica para el rol de Vigilante

console.log('✅ logicaV.js CARGADO');

document.addEventListener('DOMContentLoaded', () => {
    console.log('✅ logicaV.js - INICIALIZANDO');

    // --- Selectores de elementos del layout principal ---
    const userMenu = document.getElementById('userMenu');
    const userDropdown = document.getElementById('userDropdown');

    // --- MANEJO DEL DROPDOWN DEL USUARIO ---
    if (userMenu && userDropdown) {
        console.log('✅ Configurando dropdown del usuario...');

        userMenu.addEventListener('click', (event) => {
            event.stopPropagation();
            const isShowing = userDropdown.classList.toggle('active');
            userMenu.setAttribute('aria-expanded', isShowing);
            console.log('✅ Dropdown del usuario toggled:', isShowing ? 'visible' : 'oculto');
        });

        // Cerrar menú al presionar ESC
        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && userDropdown.classList.contains('active')) {
                userDropdown.classList.remove('active');
                userMenu.setAttribute('aria-expanded', 'false');
                console.log('✅ Dropdown cerrado con ESC');
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
            console.log('✅ Dropdown cerrado (clic fuera)');
        }
    });

    console.log('✅ logicaV.js - CONFIGURACIÓN COMPLETADA');
});

