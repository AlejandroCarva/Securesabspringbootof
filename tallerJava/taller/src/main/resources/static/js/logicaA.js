// logicaA.js - Lógica para el aprendiz
console.log("logicaA.js CARGADO EXITOSAMENTE");

// Ejecutar cuando se cargue la pagina
document.addEventListener('DOMContentLoaded', function() {
    console.log("✅ DOM completamente cargado y listo");

    // ✅ INICIALIZAR MENÚ ABIERTO
    const sidebar = document.querySelector('.sidebar');
    const body = document.body;
    const toggleBtn = document.getElementById('toggleMenuBtn');
    const userMenu = document.getElementById('userMenu');
    const userDropdown = document.getElementById('userDropdown');

    // --- 1. INICIALIZAR SIDEBAR ---
    if (sidebar) {
        sidebar.classList.add('active');
        body.classList.remove('menu-closed');
        if (toggleBtn) {
            toggleBtn.setAttribute('aria-expanded', 'true');
        }
        console.log("✅ Menú inicializado como ABIERTO");
    }
    
    // --- 2. TOGGLE SIDEBAR CON BOTÓN HAMBURGUESA ---
    if (toggleBtn) {
        toggleBtn.addEventListener('click', function(e) {
            e.stopPropagation();
            if (sidebar) {
                sidebar.classList.toggle('active');
                body.classList.toggle('menu-closed');
                const isActive = sidebar.classList.contains('active');
                toggleBtn.setAttribute('aria-expanded', isActive);
                console.log("✅ Sidebar toggled:", isActive ? 'ABIERTO' : 'CERRADO');
            }
        });
    }
    
    // --- 3. TOGGLE DROPDOWN DE USUARIO ---
    if (userMenu) {
        userMenu.addEventListener('click', function(e) {
            e.stopPropagation();
            if (userDropdown) {
                userDropdown.classList.toggle('active');
                userMenu.setAttribute('aria-expanded', userDropdown.classList.contains('active'));
                console.log("✅ User dropdown toggled");
            }
        });
    }
    
    // --- 4. CERRAR DROPDOWN AL HACER CLICK FUERA ---
    document.addEventListener('click', function(e) {
        if (userDropdown && !userDropdown.contains(e.target) && !userMenu.contains(e.target)) {
            userDropdown.classList.remove('active');
            if (userMenu) {
                userMenu.setAttribute('aria-expanded', 'false');
            }
        }
    });


    console.log("✅ TODAS las funcionalidades inicializadas");
});
