"use strict";
/// <reference path="./types.ts" />
function lerInput(id) {
    return document.getElementById(id)?.value.trim() || '';
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
