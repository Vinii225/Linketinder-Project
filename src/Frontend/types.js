"use strict";
const regex = {
    nome: /^[A-Za-zÀ-ÿ]+(?:[ '-][A-Za-zÀ-ÿ]+)*$/,
    nomeEmpresa: /^[A-Za-zÀ-ÿ0-9]+(?:[ .,'&-][A-Za-zÀ-ÿ0-9]+)*$/,
    email: /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/,
    cpf: /^\d{3}\.\d{3}\.\d{3}-\d{2}$/,
    cnpj: /^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/,
    cep: /^\d{5}-\d{3}$/,
    descricao: /^.{5,}$/,
    tags: /^\s*[A-Za-zÀ-ÿ0-9#+.-]{2,}(?:\s*,\s*[A-Za-zÀ-ÿ0-9#+.-]{2,})*\s*$/
};
function parseTags(tags) {
    return tags.split(',').map(t => t.trim()).filter(Boolean);
}
