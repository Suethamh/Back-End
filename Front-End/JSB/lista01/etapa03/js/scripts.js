nome = prompt("Escreva seu nome completo:");
idade = parseInt(prompt("Escreva sua idade:"));

restante = 18 - idade;

if(idade >= 18) {
    alert(`${nome}, voce já POSSUI idade para tirar carteira`)
}else {
    alert(`${nome}, você ainda NÃO POSSUI idade para tirar carteira, ainda falta(m) ${restante} anos`)
}