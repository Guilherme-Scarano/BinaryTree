package binarytree;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;

// Esta classe é criada para estabelecer os itens necessários para desenhar a árvore dentro do JPanel.
public class TheWindow extends JPanel implements ActionListener {
    
    static JFrame frame;
    static JOptionPane message = new JOptionPane();
    // árvore binária
    private BinaryTree tree = null;
    // localização dos nós da árvore
    private HashMap nodeLocations = null;
    // os tamanhos das subárvores
    private HashMap subtreeSizes = null;
    // precisamos calcular as localizações?
    private boolean dirty = true;
    // Espaço padrão entre os nós
    private int parent2child = 10, child2child = 20;
    // ajudantes
    private Dimension empty = new Dimension(0, 0);
    private FontMetrics fm = null;
    
    /* Quando um botão é pressionado no menu, como "A S D H", fará uma opção diferente. */
    public TheWindow(BinaryTree tree){
      this.tree = tree;
      nodeLocations = new HashMap();
      subtreeSizes = new HashMap();
      registerKeyboardAction(this, "add", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), WHEN_IN_FOCUSED_WINDOW);
      registerKeyboardAction(this, "search", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), WHEN_IN_FOCUSED_WINDOW);
      registerKeyboardAction(this, "delete", KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), WHEN_IN_FOCUSED_WINDOW);
      registerKeyboardAction(this, "help", KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), WHEN_IN_FOCUSED_WINDOW);
      registerKeyboardAction(this, "loadCsv", KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), WHEN_IN_FOCUSED_WINDOW);
    }
    
    /* Manipulador de eventos para pressionar um botão no menu, isso também mostrará
    caixas de diálogo e fará coisas diferentes, dependendo da opção selecionada. */
    public void actionPerformed(ActionEvent e) {
      String input;
       if (e.getActionCommand().equals("add")) {
            input = JOptionPane.showInputDialog("Adicione um número inteiro:");
            try {
                int a = Integer.parseInt(input);
                Node addedNode = tree.addNode(a);
                int comparisons = tree.getComparisons(); // Obter o número de comparações
                dirty = true;
                repaint();
                JOptionPane.showMessageDialog(frame, "Número " + a + " adicionado à árvore.\nComparações: " + comparisons);
            } catch (NumberFormatException z) {
                JOptionPane.showMessageDialog(frame, "Por favor, escreva um número inteiro");
            }
        }
      
      if (e.getActionCommand().equals("addFrom")) {
            JOptionPane.showInputDialog("Ainda não implementado");
        }
      
      if (e.getActionCommand().equals("delete")) {
        input = JOptionPane.showInputDialog("Delete um número inteiro:");
        try{ // O usuário introduziu um número?
            int a = Integer.parseInt(input);
                tree.deleteNode(a, tree.getRoot());
                dirty = true;
                repaint();
            }
        // Lembrar de escrever um número
        catch(NumberFormatException z){
                JOptionPane.showMessageDialog(frame, "Por favor, digite um número inteiro");
            }
      }
      
      if (e.getActionCommand().equals("search")) {
        input = JOptionPane.showInputDialog("Pesquise um número inteiro:");
        try{ // O usuário introduziu um número?
            int a = Integer.parseInt(input);
                Node aux = tree.searchNode(a, tree.getRoot());
                if (aux == null)
                    JOptionPane.showMessageDialog(frame, "O número " + a + " não foi encontrado");
                else
                    JOptionPane.showMessageDialog(frame, "O número " + a + " foi encontrado");
                dirty = true;
                repaint();
            }
        // Lembrar de escrever um número
        catch(NumberFormatException z){
                JOptionPane.showMessageDialog(frame, "Por favor, digite um número inteiro");
            }
      }
      
      if (e.getActionCommand().equals("loadCsv")) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    input = line;
                    try {
                        int a = Integer.parseInt(input);
                        tree.addNode(a);
                        dirty = true;
                        repaint();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Por favor, digite um número inteiro");
                    }
                }
                reader.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro ao carregar o arquivo CSV: " + ex.getMessage());
            }
        }
    }   

      
     if (e.getActionCommand().equals("help")) {
        JOptionPane.showMessageDialog(frame, "As operações que você pode usar:"
               + "\n a --- Adiciona um número inteiro"
               + "\n s --- Pesquisa um número inteiro"
               + "\n d --- Deleta um número inteiro"
               + "\n l --- Carrega um arquivo CSV"
               + "\n h --- Ajuda (Caso esqueça");
    }
}
    
    // Este método calcula as localizações dos nós, para torná-las aparentemente estáveis.
    private void calculateLocations() {
      nodeLocations.clear();
      subtreeSizes.clear();
      Node root = tree.getRoot();
      if (root != null) {
        calculateSubtreeSize(root);
        calculateLocation(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
      }
    }
    
    // Este método calcula o tamanho de uma subárvore com raiz em n
    private Dimension calculateSubtreeSize(Node n) {
      if (n == null) return new Dimension(0, 0);
      String s = Integer.toString(n.getContent());
      Dimension ld = calculateSubtreeSize(n.getLeft());
      Dimension rd = calculateSubtreeSize(n.getRight());
      int h = fm.getHeight() + parent2child + Math.max(ld.height, rd.height);
      int w = ld.width + child2child + rd.width;
      Dimension d = new Dimension(w, h);
      subtreeSizes.put(n, d);
      return d;
    }
    
    // Este método calcula a localização dos nós na subárvore com raiz em n
    private void calculateLocation(Node n, int left, int right, int top) {
      if (n == null) return;
      Dimension ld = (Dimension) subtreeSizes.get(n.getLeft());
      if (ld == null) ld = empty;
      Dimension rd = (Dimension) subtreeSizes.get(n.getRight());
      if (rd == null) rd = empty;
      int center = 0;
      if (right != Integer.MAX_VALUE)
        center = right - rd.width - child2child/2;
      else if (left != Integer.MAX_VALUE)
        center = left + ld.width + child2child/2;
      int width = fm.stringWidth(Integer.toString(n.getContent()));
      Rectangle r = new Rectangle(center - width/2 - 3, top, width + 6, fm.getHeight());
      nodeLocations.put(n, r);
      calculateLocation(n.getLeft(), Integer.MAX_VALUE, center - child2child/2, top + fm.getHeight() + parent2child);
      calculateLocation(n.getRight(), center + child2child/2, Integer.MAX_VALUE, top + fm.getHeight() + parent2child);
    }
    
    // Este método desenha a árvore usando as localizações pré-calculadas. Precisamos de um objeto gráfico para isso.
    private void drawTree(Graphics2D g, Node n, int px, int py, int yoffs) {
      if (n == null) return;
      Rectangle r = (Rectangle) nodeLocations.get(n);
      g.draw(r);
      g.drawString(Integer.toString(n.getContent()), r.x + 3, r.y + yoffs);
     if (px != Integer.MAX_VALUE)
       g.drawLine(px, py, r.x + r.width/2, r.y);
     drawTree(g, n.getLeft(), r.x + r.width/2, r.y + r.height, yoffs);
     drawTree(g, n.getRight(), r.x + r.width/2, r.y + r.height, yoffs);
   }
   
    // Este método irá desenhar nossa árvore, isso recebe um objeto gráfico chamado "g"
    public void paint(Graphics g) {
     super.paint(g);
     fm = g.getFontMetrics();
     // se as localizações dos nós não foram calculadas
     if (dirty) {
       calculateLocations();
       dirty = false;
     }
     Graphics2D g2d = (Graphics2D) g;
     g2d.translate(getWidth() / 2, parent2child);
     drawTree(g2d, tree.getRoot(), Integer.MAX_VALUE, Integer.MAX_VALUE, fm.getLeading() + fm.getAscent());
     fm = null;
   }
   
   /* No início do programa, mostrará uma caixa de diálogo com todos os comandos que
    podem ser usados para operar este programa corretamente, também define a dimensão da janela principal. */
   public static void main(String[] args) {
        // Lógica
       BinaryTree tree = new BinaryTree();
       JFrame f = new JFrame("Árvore Binária");
       JOptionPane.showMessageDialog(frame, "Bem-vindo(a)"
               + "\n\nEste programa funciona digitando algumas letras do seu teclado"
               + "\nAs operações que você pode usar no momento:"
               + "\n a --- Adiciona um número inteiro"
               + "\n s --- Pesquisa um número inteiro"
               + "\n d --- Deleta um número inteiro"
               + "\n l --- Carrega um arquivo CSV"
               + "\n h --- Ajuda (Caso esqueça");
        f.getContentPane().add(new TheWindow(tree));
        // cria e adiciona um manipulador de eventos para o evento de fechamento da janela
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
     });
     f.setBounds(50, 50, 700, 700);
     f.show();
    }    
}
