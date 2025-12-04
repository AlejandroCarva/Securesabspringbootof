// logicaA.js - VERSION SIMPLIFICADA DE PRUEBA
console.log("logicaA.js CARGADO EXITOSAMENTE");

// Funcion basica para probar
function probarJavaScript() {
    console.log("OK Funcion JavaScript ejecutada");
    alert("JavaScript esta funcionando!");
}

// Ejecutar cuando se cargue la pagina
document.addEventListener('DOMContentLoaded', function() {
    console.log("OK DOM completamente cargado y listo");
    
    // ✅ INICIALIZAR MENÚ ABIERTO
    const sidebar = document.querySelector('.sidebar');
    const body = document.body;
    const toggleBtn = document.getElementById('toggleMenuBtn');
    
    if (sidebar) {
        // ✅ FORZAR MENÚ ABIERTO AL INICIAR
        sidebar.classList.add('active');
        body.classList.remove('menu-closed');
        if (toggleBtn) {
            toggleBtn.setAttribute('aria-expanded', 'true');
        }
        console.log("✅ Menú inicializado como ABIERTO");
    }
    
    // Agregar funcionalidad basica al boton de menu
    if (toggleBtn) {
        toggleBtn.addEventListener('click', function() {
            console.log("OK Boton de menu clickeado");
            if (sidebar) {
                sidebar.classList.toggle('active');
                body.classList.toggle('menu-closed');
                toggleBtn.setAttribute('aria-expanded', sidebar.classList.contains('active'));
                console.log("Menu toggled");
            }
        });
    }
    
    // Probar el select de motivo
    const selectMotivo = document.getElementById('descripcionSolicitud');
    if (selectMotivo) {
        selectMotivo.addEventListener('change', function() {
            console.log("OK Select cambiado a:", this.value);
        });
    }
    
    console.log("TODAS las funcionalidades inicializadas");
});

// Tambien ejecutar inmediatamente
console.log("JavaScript cargado y ejecutandose");