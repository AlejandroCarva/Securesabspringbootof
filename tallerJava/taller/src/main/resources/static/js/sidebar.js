document.addEventListener('DOMContentLoaded', () => {
    const toggleMenuBtn = document.getElementById('toggleMenuBtn');
    const sidebar = document.querySelector('.sidebar');
    const body = document.body;

    // Inicia con el menú abierto
    if (sidebar) {
        sidebar.classList.add('active');
        body.classList.remove('menu-closed');
        if (toggleMenuBtn) {
            toggleMenuBtn.setAttribute('aria-expanded', 'true');
        }
    }

    // Toggle del menú lateral
    if (toggleMenuBtn) {
        toggleMenuBtn.addEventListener('click', () => {
            sidebar.classList.toggle('active');
            body.classList.toggle('menu-closed');
            toggleMenuBtn.setAttribute('aria-expanded', sidebar.classList.contains('active'));
        });
    }

    // Marcar enlace activo en el menú
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll('#menu a');
    
    menuLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });
});