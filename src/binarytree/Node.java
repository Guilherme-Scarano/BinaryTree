package binarytree;

// Define as ferramentas principais necessárias para construir a árvore
public class Node {
    // Construtor
    int content;
    Node left;
    Node right;
    
    // Este método coloca dados no nó da árvore
    public Node(int c) {
        this.content = c;
    }
    
    // Este método obtém os dados de um nó na árvore
    public int getContent() { return content; }
    
    // Este método obtém o próximo nó localizado à esquerda de outro nó
    public Node getLeft() { return left; }
    /* Em uma árvore binária, define o nó à esquerda apenas se for menor que o outro nó.
       Também recebe um inteiro que será armazenado no nó definido. */
    public Node setLeft(int content) {
        left = new Node(content);
        return left;
    }
    
    // Este método obtém o próximo nó localizado à direita de outro nó
    public Node getRight() { return right; }
    /* Em uma árvore binária, define o nó à direita apenas se for maior que o outro nó.
       Também recebe um inteiro que será armazenado no nó definido. */
    public Node setRight(int content) {
        right = new Node(content);
        return right;
    }   
}
