// script.js para entorno Thymeleaf

// Cambio de color de acento al hacer clic en el logo
let isBittersweet = true;

function setupAccentToggle() {
    const logo = document.querySelector('.logo');
    if (!logo || logo.classList.contains('accent-toggle-set')) return;

    const root = document.documentElement;

    logo.addEventListener('click', () => {
        const cblue = getComputedStyle(root).getPropertyValue('--cblue').trim();
        const bittersweet = '#FE675D';

        const newColor = isBittersweet ? cblue : bittersweet;
        root.style.setProperty('--bittersweet', newColor);
        localStorage.setItem('accentColor', newColor);
        isBittersweet = !isBittersweet;

        document.querySelectorAll('.accent').forEach(el => {
            el.classList.remove('accent-animate');
            void el.offsetWidth;
            el.classList.add('accent-animate');
        });
    });

    logo.classList.add('accent-toggle-set');
}

// Marcar el enlace correspondiente como activo
function setActiveLink(url) {
    document.querySelectorAll('.navbar a').forEach(link => {
        const linkHref = link.getAttribute('href');
        if (normalizeUrl(url) === normalizeUrl(linkHref)) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

function normalizeUrl(url) {
    const path = new URL(url, window.location.origin).pathname;
    return path.replace(/\/$/, '');
}

// Restaurar color de acento desde localStorage al cargar la página
window.addEventListener('DOMContentLoaded', () => {
    const savedAccent = localStorage.getItem('accentColor');
    if (savedAccent) {
        document.documentElement.style.setProperty('--bittersweet', savedAccent);

        const cblue = getComputedStyle(document.documentElement).getPropertyValue('--cblue');
        isBittersweet = savedAccent.trim() !== cblue.trim();
    }

    const currentPath = window.location.pathname;
    setActiveLink(currentPath);
    setupAccentToggle();
});
