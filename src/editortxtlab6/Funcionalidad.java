package editortxtlab6;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;

public class Funcionalidad {

    private JTextPane textPane;
    private String archivoActual; // Ruta del archivo abierto actualmente

    public Funcionalidad(JTextPane textPane) {
        this.textPane = textPane;
        this.archivoActual = null;
    }

    public void cambiarColor() {
        JDialog colorDialog = new JDialog((JFrame) null, "Seleccionar Color", true);
        colorDialog.setSize(250, 150);

        JPanel colorPanel = crearPanelDeColores(e -> {
            JButton source = (JButton) e.getSource();
            Color selectedColor = source.getBackground();
            StyledDocument doc = textPane.getStyledDocument();
            Style estilo = textPane.addStyle("Color", null);
            StyleConstants.setForeground(estilo, selectedColor);
            aplicarEstiloSeleccionado(doc, estilo);
            colorDialog.dispose();
        });

        colorDialog.add(colorPanel);
        colorDialog.setLocationRelativeTo(null);
        colorDialog.setVisible(true);
    }

    private JPanel crearPanelDeColores(ActionListener listener) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 5, 5, 5));

        Color[] colores = {
            Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE,
            Color.CYAN, Color.MAGENTA, Color.GRAY, Color.PINK, Color.LIGHT_GRAY
        };

        for (Color color : colores) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(30, 30));
            colorButton.addActionListener(listener);
            panel.add(colorButton);
        }

        return panel;
    }

    private void aplicarEstiloSeleccionado(StyledDocument doc, Style estilo) {
        int inicio = textPane.getSelectionStart();
        int fin = textPane.getSelectionEnd();
        if (inicio != fin) {
            doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un texto para aplicar el color.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void nuevoArchivo() {
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Deseas crear un nuevo archivo? Se perderán los cambios no guardados.", "Nuevo Archivo", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            textPane.setText("");
            archivoActual = null;
        }
    }

    public void guardar() {
        if (archivoActual == null) {
            guardarComo();
        } else {
            guardarEnRuta(archivoActual);
        }
    }

    public void guardarComo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Archivo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos RTF", "rtf"));

        int seleccion = fileChooser.showSaveDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            if (!archivoSeleccionado.getName().endsWith(".rtf")) {
                archivoSeleccionado = new File(archivoSeleccionado.getAbsolutePath() + ".rtf");
            }
            archivoActual = archivoSeleccionado.getAbsolutePath();
            guardarEnRuta(archivoActual);
        }
    }

    private void guardarEnRuta(String ruta) {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            RTFEditorKit rtfEditorKit = new RTFEditorKit();
            StyledDocument txt = textPane.getStyledDocument();
            rtfEditorKit.write(fos, txt, txt.getStartPosition().getOffset(), txt.getLength());
            JOptionPane.showMessageDialog(null, "Archivo guardado correctamente en " + ruta, "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void abrir() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir Archivo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos RTF", "rtf"));

        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            archivoActual = archivoSeleccionado.getAbsolutePath();
            try (FileInputStream fis = new FileInputStream(archivoSeleccionado)) {
                textPane.setText("");
                RTFEditorKit rtfEditorKit = new RTFEditorKit();
                StyledDocument doc = textPane.getStyledDocument();
                textPane.setDocument(doc);
                rtfEditorKit.read(fis, doc, 0);
                JOptionPane.showMessageDialog(null, "Archivo abierto correctamente: " + archivoSeleccionado.getName(), "Exito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al abrir el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}