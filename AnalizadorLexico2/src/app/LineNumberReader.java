package app;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class LineNumberReader extends JComponent {
    private JTextArea textArea;
    private final Color COLOR_BG = new Color(38, 38, 45); 

    public LineNumberReader(JTextArea textArea) {
        this.textArea = textArea;
        
        // Estilo: Negro y Negrita
        setForeground(new Color(180, 180, 180)); // gris claro elegante
        setFont(new Font("Consolas", Font.BOLD, 12)); 

        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
        });
    }

    private void actualizar() {
        revalidate(); 
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        int lineCount = Math.max(textArea.getLineCount(), 1);
        int digits = String.valueOf(lineCount).length();
        FontMetrics fm = getFontMetrics(getFont());
        int width = Math.max(35, fm.charWidth('0') * digits + 20);
        return new Dimension(width, textArea.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(COLOR_BG);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(getForeground());
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        int ascent = fm.getAscent();
        
        Element root = textArea.getDocument().getDefaultRootElement();
        int lineCount = root.getElementCount();

        for (int i = 0; i < lineCount; i++) {
            try {
                // modelToView asegura que el número se dibuje exactamente donde está la línea de texto
                Rectangle r = textArea.modelToView(root.getElement(i).getStartOffset());
                if (r != null) {
                    String number = String.valueOf(i + 1);
                    int x = getWidth() - fm.stringWidth(number) - 15;
                    g.drawString(number, x, r.y + ascent);
                }
            } catch (BadLocationException e) {}
        }
    }
}