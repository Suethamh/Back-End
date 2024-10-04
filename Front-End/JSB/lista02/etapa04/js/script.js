var total = parseInt(prompt("Qual o total de passageiros?"));

var homem = [];
var mulher = [];

var nome;
var sexo

for (var i = 0; i < total; i++) {
    nome = prompt("Qual o seu nome?");
    sexo = prompt(`Qual o seu sexo, ${nome}? (H ou M)`);

    if (sexo == "H") {
        homem.push(nome);
    } else {
        mulher.push(nome);
    }
}

var maior;
var menor;

if (mulher.length > homem.length) {
    maior = mulher.length;
    menor = homem.length;
} else {
    maior = homem.length;
    menor = mulher.length;
}

document.write(`<table>`);

for (var i = 0; i < maior; i++) {
    document.write(`    <tr>`);
    if (homem[i] != undefined) {
        document.write(`        <td><p>${homem[i]}</p></td>`);
    }
    else {
        document.write(`        <td><p>[-]</p></td>`)
    }
    if (mulher[i] != undefined) {
        document.write(`        <td><p>${mulher[i]}</p></td>`);
    } else {
        document.write(`        <td><p>[-]</p></td>`)
    }
    document.write(`    </tr>`);
}

document.write(`</table`);