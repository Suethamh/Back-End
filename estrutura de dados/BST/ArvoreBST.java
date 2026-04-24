class Node {
    int valor;
    Node esq, dir;

    Node(int valor) {
        this.valor = valor;
        this.esq = this.dir = null;
    }
}

public class ArvoreBST {
    Node raiz;

    // Método de Inserção (Auxiliar para testar)
    public void inserir(int valor) {
        if (raiz == null) {
            raiz = new Node(valor);
            return;
        }
        Node atual = raiz;
        Node pai = null;
        while (atual != null) {
            pai = atual;
            if (valor < atual.valor) atual = atual.esq;
            else if (valor > atual.valor) atual = atual.dir;
            else return; // Valor já existe
        }
        if (valor < pai.valor) pai.esq = new Node(valor);
        else pai.dir = new Node(valor);
    }

    // O MÉTODO DA ATIVIDADE: Remoção Iterativa
    public void remover(int chave) {
        Node pai = null;
        Node atual = raiz;

        // 1. Busca pelo nó e seu pai
        while (atual != null && atual.valor != chave) {
            pai = atual;
            if (chave < atual.valor) atual = atual.esq;
            else atual = atual.dir;
        }

        if (atual == null) return; // Não encontrou

        // CASOS 1 e 2: Nó com 0 ou 1 filho
        if (atual.esq == null || atual.dir == null) {
            Node substituto = (atual.esq != null) ? atual.esq : atual.dir;

            if (pai == null) {
                raiz = substituto;
            } else if (pai.esq == atual) {
                pai.esq = substituto;
            } else {
                pai.dir = substituto;
            }
        } 
        // CASO 3: Nó com 2 filhos
        else {
            Node paiSucessor = atual;
            Node sucessor = atual.dir;

            // Encontra o sucessor (menor da subárvore direita)
            while (sucessor.esq != null) {
                paiSucessor = sucessor;
                sucessor = sucessor.esq;
            }

            // Troca os valores
            atual.valor = sucessor.valor;

            // Remove o sucessor (que agora é redundante)
            if (paiSucessor.esq == sucessor) paiSucessor.esq = sucessor.dir;
            else paiSucessor.dir = sucessor.dir;
        }
    }

    public void exibirEmOrdem(Node no) {
        if (no != null) {
            exibirEmOrdem(no.esq);
            System.out.print(no.valor + " ");
            exibirEmOrdem(no.dir);
        }
    }

    public void imprimirArvore(Node no, String indentacao, boolean ultimo) {
    if (no != null) {
        System.out.print(indentacao);
        if (ultimo) {
            System.out.print("R---- ");
            indentacao += "      ";
        } else {
            System.out.print("L---- ");
            indentacao += "|     ";
        }
        System.out.println(no.valor);
        imprimirArvore(no.esq, indentacao, false);
        imprimirArvore(no.dir, indentacao, true);
    }
}

    public static void main(String[] args) {
        ArvoreBST bst = new ArvoreBST();

        // Criando uma árvore de exemplo
        //       50
        //     /    \
        //    30     70
        //   /  \   /  \
        //  20  40 60  80
        int[] valores = {50, 30, 70, 20, 40, 60, 80};
        for (int v : valores) bst.inserir(v);

        System.out.println("Árvore original (em ordem):");
        bst.exibirEmOrdem(bst.raiz);

        System.out.println("\n\n--- Testando as Remoções ---");

        // Caso 1: Remover folha (20)
        System.out.println("Removendo 20 (Folha):");
        bst.remover(20);
        bst.exibirEmOrdem(bst.raiz);
        bst.imprimirArvore(bst.raiz, "", true);

        // Caso 2: Remover nó com um filho (30 - agora só tem o 40)
        System.out.println("\nRemovendo 30 (Um filho):");
        bst.remover(30);
        bst.exibirEmOrdem(bst.raiz);
        bst.imprimirArvore(bst.raiz, "", true);

        // Caso 3: Remover nó com dois filhos (50 - Raiz)
        System.out.println("\nRemovendo 50 (Dois filhos/Raiz):");
        bst.remover(50);
        bst.exibirEmOrdem(bst.raiz);
        bst.imprimirArvore(bst.raiz, "", true);
    }
}
