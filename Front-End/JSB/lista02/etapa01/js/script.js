const dataAtual = new Date();

var diaSemana = dataAtual.getDay();

console.log(diaSemana)

// Resolução da a) com vetor
var arraySemana = ["Domingo","Segunda","Terça","Quarta","Quinta","Sexta", "Sábado"];
document.write(`Hoje é ${arraySemana[diaSemana]}! <br>`);

// Resolução da b) com switch
switch(diaSemana) {
    case 0:
        diaSemana = "Hoje é Domingo!";
        break;
    case 1:
        diaSemana = "Hoje é Segunda-feira!"
        break;
    case 2:
        diaSemana = "Hoje é Terça-feira!"
        break;
    case 3:
        diaSemana = "Hoje é Quarta-feira!"
        break;
    case 4:
        diaSemana = "Hoje é Quinta-feira!"
        break;
    case 5:
        diaSemana = "Hoje é Sexta-feira!"
        break;
    default:
        diaSemana = "Hoje é Sábado!!"
}

document.write(diaSemana);