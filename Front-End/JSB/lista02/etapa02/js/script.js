var codigo = ["P2", "AP", "POO1", "FDB", "JSB"];
var disciplina = ["Projeto Sistema Web MVC e SQL", "Algoritmos e Programação", "Programação Orientada a Objetos 1", "Fundamentos de Banco de Dados", "JavaScript Básico"];

document.write(`<table>`)
document.write(`    <tr>`)
document.write(`        <td><p>Código</p></td>`)
document.write(`        <td><p>Disciplina</p></td>`)
document.write(`    </tr>`)
for(var i=0; i<codigo.length; i++) {
    document.write(`    <tr>`)
    document.write(`        <td><p>${codigo[i]}</p></td>`)
    document.write(`        <td><p>${disciplina[i]}</p></td>`)
    document.write(`    </tr>`)
}
document.write(`</table>`)