nota1 = parseFloat(prompt("Qual sua nota no 1º bimestre"));
nota2 = parseFloat(prompt("Qual sua nota no 2º bimestre"));

soma = nota1 + nota2;
restante = 60 - soma;

if(soma >= 60) {
    alert(`Aprovado, NOTA: ${soma}.`)
}else {
    alert(`Reprovado, NOTA: ${soma}, faltam ${restante} para aprovação.`)
}