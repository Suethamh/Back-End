do {
    numero = parseInt(prompt("digite um numero inteiro maior que 0"));
}while(numero <= 0)

nome = prompt("escreva seu nome")

for(var i=0; i<numero; i++){
    document.write(`<p>${nome}</p>`)
}
