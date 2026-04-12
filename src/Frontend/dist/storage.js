"use strict";
function lerInput(id) {
    var _a;
    return ((_a = document.getElementById(id)) === null || _a === void 0 ? void 0 : _a.value.trim()) || '';
}
function salvarCandidatos(candidatos) {
    localStorage.setItem('candidatos', JSON.stringify(candidatos));
}
function salvarEmpresas(empresas) {
    localStorage.setItem('empresas', JSON.stringify(empresas));
}
function carregarCandidatos() {
    return JSON.parse(localStorage.getItem('candidatos') || '[]');
}
function carregarEmpresas() {
    return JSON.parse(localStorage.getItem('empresas') || '[]');
}
