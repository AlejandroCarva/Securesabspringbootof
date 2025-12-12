document.addEventListener('DOMContentLoaded', () => {
    let toggleMenuBtn = document.getElementById('toggleMenuBtn');
    const sidebar = document.querySelector('.sidebar') || document.getElementById('sidebar');

    // Si no existe sidebar, no hacemos nada
    if (!sidebar) {
        console.warn('Sidebar no encontrado en la página. sidebar.js no puede inicializarse.');
        return;
    }

    // Si el botón no existe, lo creamos dinámicamente y lo insertamos en el body
    if (!toggleMenuBtn) {
        toggleMenuBtn = document.createElement('button');
        toggleMenuBtn.id = 'toggleMenuBtn';
        toggleMenuBtn.className = 'menu-toggle';
        toggleMenuBtn.setAttribute('aria-controls', 'sidebar');
        toggleMenuBtn.setAttribute('aria-expanded', 'true');
        toggleMenuBtn.setAttribute('title', 'Alternar menú lateral');
        toggleMenuBtn.setAttribute('aria-label', 'Alternar menú lateral');
        toggleMenuBtn.innerHTML = '<i class="fas fa-bars" aria-hidden="true"></i>';
        // Insertar al inicio del body para que quede visible
        if (document.body.firstChild) {
            document.body.insertBefore(toggleMenuBtn, document.body.firstChild);
        } else {
            document.body.appendChild(toggleMenuBtn);
        }
        // Aplicar estilos mínimos por si la hoja de estilos no se cargó aún
        toggleMenuBtn.style.display = 'inline-flex';
        toggleMenuBtn.style.position = 'fixed';
        toggleMenuBtn.style.top = '12px';
        toggleMenuBtn.style.left = '12px';
        toggleMenuBtn.style.zIndex = '2000';
        toggleMenuBtn.style.background = 'var(--main-green, #00b894)';
        toggleMenuBtn.style.color = '#fff';
        toggleMenuBtn.style.width = '44px';
        toggleMenuBtn.style.height = '44px';
        toggleMenuBtn.style.borderRadius = '8px';
        toggleMenuBtn.style.alignItems = 'center';
        toggleMenuBtn.style.justifyContent = 'center';
        toggleMenuBtn.style.boxShadow = '0 2px 8px rgba(0,0,0,0.18)';
    }

    // Inicializar estado (si el sidebar tiene la clase active se marca como expandido)
    if (sidebar.classList.contains('active')) {
        toggleMenuBtn.setAttribute('aria-expanded', 'true');
    } else {
        toggleMenuBtn.setAttribute('aria-expanded', 'false');
    }

    // --- TOGGLE DEL MENÚ ---
    toggleMenuBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        sidebar.classList.toggle('active');
        const isActive = sidebar.classList.contains('active');
        toggleMenuBtn.setAttribute('aria-expanded', isActive);
    });

    // --- CERRAR AL HACER CLIC FUERA ---
    document.addEventListener('click', (e) => {
        const clickedInsideSidebar = sidebar.contains(e.target);
        const clickedToggle = toggleMenuBtn.contains(e.target);
        if (sidebar.classList.contains('active') && !clickedInsideSidebar && !clickedToggle) {
            sidebar.classList.remove('active');
            toggleMenuBtn.setAttribute('aria-expanded', 'false');
        }
    });

    // --- CERRAR AL HACER CLIC EN UN ENLACE (en móviles) ---
    const menuLinks = document.querySelectorAll('#menu a');
    menuLinks.forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth < 768) { // Solo en móviles
                sidebar.classList.remove('active');
                toggleMenuBtn.setAttribute('aria-expanded', 'false');
            }
        });
    });

    // --- MARCAR ENLACE ACTIVO ---
    const currentPath = window.location.pathname;
    menuLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && (href === currentPath || href === (currentPath + '/') || currentPath.startsWith(href))) {
            link.classList.add('active');
            if (link.parentElement) link.parentElement.classList.add('active');
        }
    });

    // Si el sidebar no tiene clase active por defecto en mobile, la añadimos para escritorio
    if (window.innerWidth >= 769 && !sidebar.classList.contains('active')) {
        sidebar.classList.add('active');
        toggleMenuBtn.setAttribute('aria-expanded', 'true');
    }

    console.log('Sidebar.js inicializado correctamente');
});