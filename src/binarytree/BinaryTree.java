package binarytree;

import static binarytree.TheWindow.frame; // Importa o objeto "frame" da classe "TheWindow"
import javax.swing.JOptionPane; // Importa a classe "JOptionPane" do pacote "javax.swing"

// Esta classe define as funções principais deste programa: adicionar, adicionar a partir de um arquivo, excluir e pesquisar.
public class BinaryTree {
    private Node root = null; // Nó raiz da árvore
    private int comparisons = 0; // Variável para contar comparações

    public Node getRoot() {
        return root;
    }

    public int getComparisons() {
        return comparisons;
    }

    // Adiciona um nó à árvore com o conteúdo especificado.
    public Node addNode(int content) {
        comparisons = 0; // Zera as comparações para cada nova inserção
        if (root == null) {
            root = new Node(content);
            return root;
        }
        return addTo(root, content);
    }

    // Método privado para adicionar um nó à árvore.
    private Node addTo(Node n, int c) {
        comparisons++; // Incrementa a contagem de comparações
        if (c < n.getContent()) {
            Node l = n.getLeft();
            if (l == null) {
                return n.setLeft(c);
            } else {
                return addTo(l, c);
            }
        } else {
            Node r = n.getRight();
            if (r == null) {
                return n.setRight(c);
            } else {
                return addTo(r, c);
            }
        }
    }

    /* Este método percorre toda a árvore em busca do nó com os dados que o usuário procura.
       Se a variável "aux" for nula, significa que seu valor inicial não mudou, porque nunca encontrou os dados solicitados. */
    public Node searchNode(int d, Node n) {
        Node aux = null; // Não encontrado
        if (n != null) {
            if (d == n.getContent()) {
                return n; // Encontrado
            } else {
                if (d < n.getContent()) {
                    Node der = n.getLeft();
                    aux = searchNode(d, der);
                } else {
                    Node izq = n.getRight();
                    aux = searchNode(d, izq);
                }
            }
        }
        return aux;
    }

    /* Este método primeiro utiliza o método searchNode para procurar os dados que o usuário está procurando.
       Se os dados não forem encontrados, será exibida uma mensagem informando que o elemento solicitado não foi encontrado.
       No entanto, se o item estiver na árvore, o programa executará o procedimento de exclusão, e cada procedimento está comentado abaixo em cada seção.
       
       E, se a árvore tiver apenas a raiz, você não pode apagá-la, pois é necessário pelo menos um filho à direita para a raiz neste programa, devido ao código.
    */
    public Node deleteNode(int n, Node root) {
        Node aux = searchNode(n, root);
        if (aux == null)
            JOptionPane.showMessageDialog(frame, "O número " + n + " não foi encontrado");
        else {
            // Caso base, a árvore está vazia
            if (root == null) {
                return root;
            }
            // 1. Um nó está na subárvore esquerda, definindo o filho esquerdo da raiz para o resultado de delete(root.left...)
            else if (n < root.getContent()) {
                root.left = deleteNode(n, root.left);
            }
            // 1. B. Um nó está na subárvore direita, definindo o filho direito da raiz para o resultado de delete(root.right...)
            else if (n > root.getContent()) {
                root.right = deleteNode(n, root.right);
            }
            // 2. Dados encontrados!
            else {
                // Caso 1: sem filho
                // Apenas defina o nó como nulo (remova-o) e retorne-o
                if (root.left == null && root.right == null) {
                    root = null;
                }
                // Caso 2: um filho
                // 2. A: sem filho à esquerda
                else if (root.left == null) {
                    Node temp = root;
                    root = root.right;
                    temp = null;
                }
                // 2. B: sem filho à direita
                else if (root.right == null) {
                    Node temp = root;
                    root = root.left;
                    temp = null;
                }
                // Caso 3: 2 filhos
                else {
                    // Obter o elemento mínimo na subárvore direita
                    // Defina-o como `root` e remova-o de sua posição original
                    Node temp = findMin(root.right);
                    root.content = temp.content;
                    root.right = deleteNode(temp.getContent(), root.right);
                }
            }
        }
        return root;
    }

    // Este método procura o valor mais baixo da árvore.
    public static Node findMin(Node root) {
        while (root.left != null)
            root = root.left;
        return root;
    }
}