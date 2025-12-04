class AsistenciaManager {
    constructor() {
        this.competenciaSelect = document.getElementById('competencia');
        this.guardarBtn = document.getElementById('guardarAsistenciasBtn');
        this.formPrincipal = document.querySelector('form[action*="guardar-asistencias"]');
        this.asistencias = new Map(); // Para tracking de cambios
        
        this.init();
    }
    
    init() {
        this.setupEventListeners();
        this.validateInitialState();
        this.trackChanges();
    }
    
    setupEventListeners() {
        // Competencia
        if (this.competenciaSelect) {
            this.competenciaSelect.addEventListener('change', () => this.validateForm());
        }
        
        // Asistencias (si hay cambios)
        document.querySelectorAll('.estado-asistencia').forEach(select => {
            select.addEventListener('change', (e) => {
                const aprendizId = e.target.name.match(/\[(\d+)\]/)[1];
                this.asistencias.set(aprendizId, e.target.value);
                this.validateForm();
            });
        });
        
        // Validar antes de enviar
        if (this.formPrincipal) {
            this.formPrincipal.addEventListener('submit', (e) => this.handleSubmit(e));
        }
    }
    
    validateForm() {
        let isValid = true;
        let messages = [];
        
        // 1. Validar competencia
        if (!this.competenciaSelect || !this.competenciaSelect.value) {
            isValid = false;
            messages.push('Seleccione una competencia');
        }
        
        // 2. Validar que haya aprendices
        const totalAprendices = document.querySelectorAll('.estado-asistencia').length;
        if (totalAprendices === 0) {
            isValid = false;
            messages.push('No hay aprendices para registrar asistencia');
        }
        
        // 3. Validar fecha (si existe)
        const fechaInput = document.querySelector('input[name="fecha_asistencia"]');
        if (fechaInput && !fechaInput.value) {
            isValid = false;
            messages.push('Seleccione una fecha');
        }
        
        // Actualizar estado del botón
        this.updateButtonState(isValid, messages);
        
        return isValid;
    }
    
    updateButtonState(isValid, messages = []) {
        if (!this.guardarBtn) return;
        
        this.guardarBtn.disabled = !isValid;
        
        // Tooltip informativo
        if (messages.length > 0) {
            this.guardarBtn.title = messages.join('\n');
        } else {
            this.guardarBtn.title = 'Haga clic para guardar los cambios';
        }
        
        // Feedback visual
        if (isValid) {
            this.guardarBtn.classList.remove('btn-disabled');
            this.guardarBtn.classList.add('btn-enabled');
        } else {
            this.guardarBtn.classList.remove('btn-enabled');
            this.guardarBtn.classList.add('btn-disabled');
        }
    }
    
    handleSubmit(e) {
        if (!this.validateForm()) {
            e.preventDefault();
            this.showAlert('Por favor, complete todos los campos requeridos', 'warning');
            return;
        }
        
        // Confirmar si hay cambios
        if (this.asistencias.size > 0) {
            const confirmar = confirm(`¿Guardar cambios para ${this.asistencias.size} aprendices?`);
            if (!confirmar) {
                e.preventDefault();
                return;
            }
        }
        
        // Mostrar loading
        this.showLoading(true);
    }
    
    trackChanges() {
        // Inicializar con valores actuales
        document.querySelectorAll('.estado-asistencia').forEach(select => {
            const match = select.name.match(/\[(\d+)\]/);
            if (match) {
                this.asistencias.set(match[1], select.value);
            }
        });
    }
    
    validateInitialState() {
        // Verificar si ya hay datos cargados desde el servidor
        const competenciaId = this.competenciaSelect ? this.competenciaSelect.value : null;
        const hasExistingData = document.querySelector('[data-asistencias-existentes]') !== null;
        
        if (hasExistingData && competenciaId) {
            // Ya hay datos cargados, habilitar botón
            this.updateButtonState(true, ['Modifique los estados si es necesario']);
        } else {
            // Validación normal
            this.validateForm();
        }
    }
    
    showAlert(message, type = 'info') {
        // Implementar sistema de notificaciones
        console.log(`${type.toUpperCase()}: ${message}`);
        // Podrías integrar SweetAlert2 o similar aquí
    }
    
    showLoading(show) {
        if (show) {
            this.guardarBtn.innerHTML = '<span class="spinner"></span> Guardando...';
            this.guardarBtn.disabled = true;
        } else {
            this.guardarBtn.innerHTML = 'Guardar cambios';
        }
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new AsistenciaManager();
});