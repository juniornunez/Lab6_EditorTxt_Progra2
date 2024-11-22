package editortxtlab6;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GUI extends JFrame {

    private JTextPane textPane;
    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> sizeComboBox;
    private JButton colorButton;
    private Funcionalidad funcionalidad;

    public GUI() {
        
        setTitle("Editor de Texto");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        funcionalidad = new Funcionalidad(textPane);

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox = new JComboBox<>(fonts);
        fontComboBox.addActionListener(e -> actualizarFuente());
        toolbar.add(new JLabel("Fuente:"));
        toolbar.add(fontComboBox);

        Integer[] sizes = {8, 12, 16, 20, 24, 28, 32, 36, 48};
        sizeComboBox = new JComboBox<>(sizes);
        sizeComboBox.addActionListener(e -> actualizarTamano());
        toolbar.add(new JLabel("Tamano:"));
        toolbar.add(sizeComboBox);

        colorButton = new JButton("Color");
        colorButton.addActionListener(e -> mostrarSeleccionDeColor());
        toolbar.add(colorButton);

        add(toolbar, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton abrirButton = new JButton("Abrir");
        abrirButton.addActionListener(e -> funcionalidad.abrir());
        buttonPanel.add(abrirButton);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> funcionalidad.guardar());
        buttonPanel.add(guardarButton);

        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(salirButton);
       
        JButton nuevoButton = new JButton("Nuevo");
        nuevoButton.addActionListener(e -> funcionalidad.nuevoArchivo());
        toolbar.add(nuevoButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void mostrarSeleccionDeColor() {
        JDialog colorDialog = new JDialog(this, "Seleccionar Color", true);
        colorDialog.setSize(250, 150);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(2, 5, 5, 5));

        Color[] colores = {
            Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE,
            Color.CYAN, Color.MAGENTA, Color.GRAY, Color.PINK, Color.LIGHT_GRAY
        };

        for (Color color : colores) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.addActionListener(e -> {
                aplicarColorSeleccionado(color);
                colorDialog.dispose();
            });
            colorPanel.add(colorButton);
        }

        colorDialog.add(colorPanel);
        colorDialog.setLocationRelativeTo(this);
        colorDialog.setVisible(true);
    }

    private void aplicarColorSeleccionado(Color color) {
        StyledDocument doc = textPane.getStyledDocument();
        Style estilo = textPane.addStyle("Color", null);
        StyleConstants.setForeground(estilo, color);
        int inicio = textPane.getSelectionStart();
        int fin = textPane.getSelectionEnd();
        if (inicio != fin) {
            doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
        }
    }

    private void actualizarFuente() {
        String fuente = (String) fontComboBox.getSelectedItem();
        if (fuente != null) {
            StyledDocument doc = textPane.getStyledDocument();
            Style estilo = textPane.addStyle("Fuente", null);
            StyleConstants.setFontFamily(estilo, fuente);
            aplicarEstiloSeleccionado(doc, estilo);
        }
    }

    private void actualizarTamano() {
        Integer tamano = (Integer) sizeComboBox.getSelectedItem();
        if (tamano != null) {
            StyledDocument doc = textPane.getStyledDocument();
            Style estilo = textPane.addStyle("Tamano", null);
            StyleConstants.setFontSize(estilo, tamano);
            aplicarEstiloSeleccionado(doc, estilo);
        }
    }

    private void aplicarEstiloSeleccionado(StyledDocument doc, Style estilo) {
        int inicio = textPane.getSelectionStart();
        int fin = textPane.getSelectionEnd();
        if (inicio != fin) {
            doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
        }
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            GUI editor = new GUI();
            editor.setVisible(true);
            editor.setLocationRelativeTo(null);
        });
    }
}