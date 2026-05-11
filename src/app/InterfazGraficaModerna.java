package app;

import codigo.Lexer;
import codigo.Tokens;
import codigo.TablaDeSimbolos;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/**
 * Interfaz Gráfica Moderna — Compilador Turbo X²
 * Versión con Tabla de Símbolos integrada.
 */
public class InterfazGraficaModerna extends JFrame {

    // ── Componentes UI ───────────────────────────────────────────────────────
    private JTextArea          areaEntrada;
    private JTextArea          areaResultados;
    private JTable             tablaTokens;
    private DefaultTableModel modeloTabla;
    private JButton            btnAnalizar;
    private JButton            btnLimpiar;

    // ── NUEVO: Tabla de Símbolos ─────────────────────────────────────────────
    private JTable             tablaSimbolos;
    private DefaultTableModel modeloSimbolos;
    private JLabel             lblContadorSimbolos;
    private final TablaDeSimbolos tablaDS = new TablaDeSimbolos();

    // ── Paleta de colores Refinada (Basada en referencia) ──────────────────────
    private final Color COLOR_FONDO   = new Color(25, 25, 25);     // Gris casi negro
    private final Color COLOR_PANEL   = new Color(33, 33, 33);     // Gris intermedio para elevación
    private final Color COLOR_TEXTO   = new Color(230, 230, 230);  // Blanco suave
    private final Color COLOR_ACCENTO = new Color(0, 150, 255);    // Azul vibrante moderno
    private final Color COLOR_BORDE   = new Color(45, 45, 45);     // Bordes sutiles
    private final Color COLOR_HEADERS = new Color(18, 18, 18);     // Fondo de cabeceras de tabla
    private final Color COLOR_FILA_ALT = new Color(30, 30, 35);    // Filas alternas sutiles

    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_MONO   = new Font("Consolas", Font.PLAIN, 13);

    // ════════════════════════════════════════════════════════════════════════
    public InterfazGraficaModerna() {
        // --- CAMBIO SOLICITADO: FORZAR PESTAÑAS NEGRAS ---
        UIManager.put("TabbedPane.selected", new Color(40, 40, 40));
        UIManager.put("TabbedPane.unselectedBackground", new Color(20, 20, 20));
        UIManager.put("TabbedPane.contentAreaColor", COLOR_PANEL);
        UIManager.put("TabbedPane.borderHighlightColor", COLOR_BORDE);
        UIManager.put("TabbedPane.darkShadow", Color.BLACK);
        UIManager.put("TabbedPane.shadow", Color.BLACK);
        // ------------------------------------------------

        setTitle("Compilador Turbo X^2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 820);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));

        add(createHeaderPanel(),  BorderLayout.NORTH);
        add(createCenterPanel(),  BorderLayout.CENTER);
        add(createActionsPanel(), BorderLayout.SOUTH);

        conectarEventosLógica();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE));
        panel.setPreferredSize(new Dimension(100, 60));

        JLabel lbl = new JLabel(" ANALIZADOR LÉXICO");
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(COLOR_TEXTO);
        lbl.setBorder(new EmptyBorder(0, 20, 0, 0));
        panel.add(lbl, BorderLayout.WEST);
        return panel;
    }

    private JSplitPane createCenterPanel() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createInputPanel(), createResultsPanel());
        split.setDividerLocation(420);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 20, 10, 10));

        JLabel lbl = new JLabel("Código Fuente a Analizar");
        lbl.setFont(FUENTE_NORMAL);
        lbl.setForeground(COLOR_TEXTO);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        panel.add(lbl, BorderLayout.NORTH);

        areaEntrada = new JTextArea();
        areaEntrada.setFont(FUENTE_MONO);
        areaEntrada.setBackground(COLOR_PANEL);
        areaEntrada.setForeground(COLOR_TEXTO);
        areaEntrada.setCaretColor(COLOR_TEXTO);

        JScrollPane scroll = new JScrollPane(areaEntrada);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        // Se usa la clase externa mejorada
        scroll.setRowHeaderView(new LineNumberReader(areaEntrada));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(10, 10, 10, 20));

        JPanel panelConsola = new JPanel(new BorderLayout());
        panelConsola.setOpaque(false);
        panelConsola.setPreferredSize(new Dimension(100, 180));

        JLabel lblConsola = new JLabel("Consola de Resultados / Errores");
        lblConsola.setFont(FUENTE_NORMAL);
        lblConsola.setForeground(COLOR_TEXTO);
        lblConsola.setBorder(new EmptyBorder(0, 0, 5, 0));
        panelConsola.add(lblConsola, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(FUENTE_MONO);
        areaResultados.setBackground(COLOR_PANEL);
        areaResultados.setForeground(new Color(150, 250, 150));
        areaResultados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                new EmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollConsola = new JScrollPane(areaResultados);
        scrollConsola.setBorder(null);
        panelConsola.add(scrollConsola, BorderLayout.CENTER);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(true);
        tabs.setBackground(new Color(15, 15, 15)); 
        tabs.setForeground(COLOR_TEXTO);
        tabs.setFont(FUENTE_NORMAL.deriveFont(Font.BOLD));

        tabs.addTab("🔤  Tokens Generados",  crearTabTokens());
        tabs.addTab("📋  Tabla de Símbolos", crearTabSimbolos());

        JSplitPane splitResultados = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitResultados.setDividerLocation(185);
        splitResultados.setBorder(null);
        splitResultados.setOpaque(false);
        splitResultados.setTopComponent(panelConsola);
        splitResultados.setBottomComponent(tabs);

        panel.add(splitResultados, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane crearTabTokens() {
        String[] cols = {"Línea", "Token", "Lexema"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaTokens = estilizarTabla(new JTable(modeloTabla));
        tablaTokens.getColumnModel().getColumn(0).setPreferredWidth(55);
        tablaTokens.getColumnModel().getColumn(1).setPreferredWidth(210);
        tablaTokens.getColumnModel().getColumn(2).setPreferredWidth(220);

        JScrollPane sp = new JScrollPane(tablaTokens);
        sp.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        sp.getViewport().setBackground(COLOR_PANEL);
        return sp;
    }

    private JPanel crearTabSimbolos() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(6, 6, 6, 6));

        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setOpaque(false);

        JLabel lblTitulo = new JLabel("Identificadores encontrados durante el análisis léxico");
        lblTitulo.setFont(FUENTE_NORMAL);
        lblTitulo.setForeground(new Color(160, 160, 180));

        lblContadorSimbolos = new JLabel("0 símbolo(s)");
        lblContadorSimbolos.setFont(FUENTE_NORMAL.deriveFont(Font.BOLD));
        lblContadorSimbolos.setForeground(COLOR_ACCENTO);
        lblContadorSimbolos.setBorder(new EmptyBorder(0, 0, 0, 4));

        barraTop.add(lblTitulo,           BorderLayout.WEST);
        barraTop.add(lblContadorSimbolos, BorderLayout.EAST);
        panel.add(barraTop, BorderLayout.NORTH);

        String[] cols = {"#", "Identificador", "Tipo", "Valor", "Línea", "Ocurrencias"};
        modeloSimbolos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaSimbolos = estilizarTabla(new JTable(modeloSimbolos));
        tablaSimbolos.getColumnModel().getColumn(0).setPreferredWidth(35);
        tablaSimbolos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaSimbolos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaSimbolos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaSimbolos.getColumnModel().getColumn(4).setPreferredWidth(55);
        tablaSimbolos.getColumnModel().getColumn(5).setPreferredWidth(80);

        tablaSimbolos.getColumnModel().getColumn(2).setCellRenderer(
                new TipoColorRenderer(COLOR_PANEL, new Color(45, 45, 55)));

        JScrollPane sp = new JScrollPane(tablaSimbolos);
        sp.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        sp.getViewport().setBackground(COLOR_PANEL);
        panel.add(sp, BorderLayout.CENTER);

        panel.add(crearLeyenda(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearLeyenda() {
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        leyenda.setBackground(new Color(25, 25, 35));
        leyenda.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE));

        Object[][] tipos = {
            {"Entero",   new Color(100, 200, 255)},
            {"Real",     new Color(150, 255, 150)},
            {"Cadena",   new Color(255, 200, 100)},
            {"Logico",   new Color(200, 150, 255)},
            {"Caracter", new Color(255, 150, 150)},
            {"Desconocido", new Color(160, 160, 160)},
        };

        JLabel lbl0 = new JLabel("Tipos: ");
        lbl0.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl0.setForeground(new Color(160, 160, 160));
        leyenda.add(lbl0);

        for (Object[] t : tipos) {
            JLabel lbl = new JLabel("⬤ " + t[0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lbl.setForeground((Color) t[1]);
            leyenda.add(lbl);
        }
        return leyenda;
    }

    private JTable estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_MONO);
        tabla.setBackground(COLOR_PANEL);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setRowHeight(26);
        tabla.setSelectionBackground(COLOR_ACCENTO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(FUENTE_NORMAL.deriveFont(Font.BOLD));
        header.setPreferredSize(new Dimension(0, 35));
        header.setReorderingAllowed(false);
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(15, 15, 15)); 
                label.setForeground(Color.WHITE); 
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, COLOR_BORDE));
                label.setOpaque(true);
                return label;
            }
        });

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? new Color(38, 38, 45) : COLOR_PANEL);
                    setForeground(COLOR_TEXTO);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
        return tabla;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE));

        JButton btnAbrir = crearBoton("Abrir Archivo",    new Color(70, 80, 90));
        btnAnalizar      = crearBoton("Ejecutar Análisis", COLOR_ACCENTO);
        btnLimpiar       = crearBoton("Limpiar",           new Color(100, 100, 100));
        JButton btnSalir = crearBoton("Salir",             new Color(150, 40, 40));

        btnAbrir.addActionListener(e -> abrirArchivo());
        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(btnAbrir);
        panel.add(btnAnalizar);
        panel.add(btnLimpiar);
        panel.add(btnSalir);
        return panel;
    }

    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_NORMAL.deriveFont(Font.BOLD));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private void conectarEventosLógica() {
        btnLimpiar.addActionListener(e -> {
            areaEntrada.setText("");
            areaResultados.setText("");
            modeloTabla.setRowCount(0);
            modeloSimbolos.setRowCount(0);
            tablaDS.limpiar();
            lblContadorSimbolos.setText("0 símbolo(s)");
        });

        btnAnalizar.addActionListener(e -> ejecutarAnalisis());
    }

    private void ejecutarAnalisis() {
        String texto = areaEntrada.getText();
        if (texto.isEmpty()) return;

        // Limpiar interfaz
        modeloTabla.setRowCount(0);
        modeloSimbolos.setRowCount(0);
        areaResultados.setText("");
        
        // Limpiar el cerebro del compilador
        tablaDS.limpiar();
        codigo.Sintax.miJuez.limpiar();

        // 1. FASE LÉXICA (Solo para pintar la tabla de Tokens visual)
        try {
            Lexer lexer = new Lexer(new java.io.StringReader(texto));
            while (true) {
                Tokens tok = lexer.yylex();
                if (tok == null) break;
                if (tok != Tokens.COMENTARIO && tok != Tokens.ERROR) {
                    modeloTabla.addRow(new Object[]{lexer.getLinea(), tok.toString(), lexer.lexeme});
                }
            }
        } catch (Exception e) {
            System.err.println("Error en visualización léxica");
        }

        // 2. FASE SINTÁCTICA Y SEMÁNTICA (El verdadero análisis)
        try {
            codigo.LexerCup lexerCup = new codigo.LexerCup(new java.io.StringReader(texto));
            codigo.Sintax sintactico = new codigo.Sintax(lexerCup);
            
            sintactico.parse(); // Aquí explota si la sintaxis está mal
            
            // --- AVISOS EN LA CONSOLA ---
            if (!codigo.Sintax.miJuez.erroresSemanticos.isEmpty()) {
                areaResultados.append(">> ANÁLISIS FINALIZADO CON ERRORES LÓGICOS:\n\n");
                for (String error : codigo.Sintax.miJuez.erroresSemanticos) {
                    areaResultados.append(error + "\n");
                }
            } else {
                areaResultados.append(">>> Análisis finalizado con éxito. ¡Sin errores!\n");
            }

            // --- PINTAR LA TABLA (SIEMPRE SE EJECUTA) ---
            List<TablaDeSimbolos.Simbolo> lista = codigo.Sintax.miJuez.obtenerTodos();
            int idx = 1;
            for (TablaDeSimbolos.Simbolo s : lista) {
                modeloSimbolos.addRow(new Object[]{
                    idx++, 
                    s.getNombre(), 
                    s.getTipo(), 
                    s.getValor() == null ? "—" : s.getValor().toString(), 
                    s.getLinea(), 
                    s.getOcurrencias()
                });
            }
            lblContadorSimbolos.setText((idx - 1) + " símbolo(s)");

        } catch (Exception e) {
            // Si CUP detecta un error de gramática, caerá aquí
            areaResultados.append("\n>> ERROR DE SINTAXIS DETECTADO. Deteniendo análisis.\n");
        }
    }

    private void abrirArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar Archivo .txt");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Documentos de texto (*.txt)", "txt"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] encoded = java.nio.file.Files.readAllBytes(
                        chooser.getSelectedFile().toPath());
                areaEntrada.setText(new String(encoded,
                        java.nio.charset.StandardCharsets.UTF_8));
                areaResultados.setText(">>> Archivo cargado: "
                        + chooser.getSelectedFile().getName() + "\n");
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo");
            }
        }
    }

    private boolean esPalabraReservada(String nombre) {
        switch (nombre.toUpperCase()) {
            case "INICIO": case "FIN": case "SI": case "ENTONCES": case "SINO":
            case "MIENTRAS": case "HACER": case "DEFINIR": case "COMO":
            case "ESCRIBIR": case "LEER": case "VERDADERO": case "FALSO":
                return true;
            default: return false;
        }
    }

    private class TipoColorRenderer extends DefaultTableCellRenderer {
        private final Color bgPar, bgImpar;
        TipoColorRenderer(Color bgPar, Color bgImpar) {
            this.bgPar   = bgPar;
            this.bgImpar = bgImpar;
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, foc, row, col);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            if (!sel) {
                setBackground(row % 2 == 0 ? bgImpar : bgPar);
                String tipo = val != null ? val.toString() : "";
                switch (tipo) {
                    case "Entero":      setForeground(new Color(100, 200, 255)); break;
                    case "Real":        setForeground(new Color(150, 255, 150)); break;
                    case "Cadena":      setForeground(new Color(255, 200, 100)); break;
                    case "Logico":      setForeground(new Color(200, 150, 255)); break;
                    case "Caracter":    setForeground(new Color(255, 150, 150)); break;
                    default:            setForeground(new Color(160, 160, 160)); break;
                }
            }
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { 
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
            } 
            catch (Exception ignored) {}
            new InterfazGraficaModerna().setVisible(true);
        });
    }
}