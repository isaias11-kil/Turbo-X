package codigo;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Interfaz Principal — Compilador Turbo X²
 * 
 * Versión actualizada que integra:
 *   1. Análisis léxico con tabla de tokens
 *   2. Tabla de Símbolos con estructura de datos completa
 *   3. Consola de errores
 * 
 * @author Grupo TurboX² — UMG Chimaltenango
 */
public class frmPrincipal extends JFrame {

    // ── Paleta de colores ────────────────────────────────────────────────────
    private static final Color BG_DARK        = new Color(18, 18, 30);
    private static final Color BG_PANEL       = new Color(28, 28, 45);
    private static final Color BG_EDITOR      = new Color(22, 22, 38);
    private static final Color ACCENT_BLUE    = new Color(0, 122, 204);
    private static final Color ACCENT_GREEN   = new Color(0, 200, 100);
    private static final Color ACCENT_RED     = new Color(220, 50, 50);
    private static final Color ACCENT_ORANGE  = new Color(255, 165, 0);
    private static final Color TEXT_PRIMARY   = new Color(220, 220, 220);
    private static final Color TEXT_SECONDARY = new Color(140, 140, 160);
    private static final Color ROW_ODD        = new Color(32, 32, 52);
    private static final Color ROW_EVEN       = new Color(38, 38, 60);
    private static final Color HEADER_BG      = new Color(0, 90, 160);

    // ── Componentes UI ───────────────────────────────────────────────────────
    private JTextArea  txtCodigo;
    private JTextArea  txtConsola;
    private JTable     tblTokens;
    private JTable     tblSimbolos;
    private JTabbedPane tabbedRight;
    private DefaultTableModel modelTokens;
    private DefaultTableModel modelSimbolos;
    private JButton    btnAbrir;
    private JButton    btnAnalizar;
    private JButton    btnLimpiar;
    private JButton    btnSalir;
    private JLabel     lblEstado;

    // ── Lógica ───────────────────────────────────────────────────────────────
    private final TablaDeSimbolos tablaSimbolos = new TablaDeSimbolos();

    // ════════════════════════════════════════════════════════════════════════
    public frmPrincipal() {
        super("Compilador Turbo X²");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        construirUI();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE LA INTERFAZ
    // ════════════════════════════════════════════════════════════════════════
    private void construirUI() {
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        add(crearHeader(),    BorderLayout.NORTH);
        add(crearCentro(),    BorderLayout.CENTER);
        add(crearFooter(),    BorderLayout.SOUTH);
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ACCENT_BLUE);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel("⚙  COMPILADOR TURBO X²  —  Analizador Léxico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Universidad Mariano Gálvez · Centro Chimaltenango · Compiladores");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(200, 225, 255));

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        header.add(textos, BorderLayout.WEST);
        return header;
    }

    // ── Centro: editor izquierda + paneles derecha ───────────────────────────
    private JSplitPane crearCentro() {
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelEditor(),
                crearPanelDerecho());
        split.setDividerLocation(480);
        split.setDividerSize(5);
        split.setBorder(null);
        split.setBackground(BG_DARK);
        return split;
    }

    // Panel editor (izquierda)
    private JPanel crearPanelEditor() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 10, 5, 5));

        JLabel lbl = label("📄  Código Fuente", 13, true);
        panel.add(lbl, BorderLayout.NORTH);

        txtCodigo = new JTextArea();
        txtCodigo.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtCodigo.setBackground(BG_EDITOR);
        txtCodigo.setForeground(TEXT_PRIMARY);
        txtCodigo.setCaretColor(Color.WHITE);
        txtCodigo.setLineWrap(false);
        txtCodigo.setTabSize(4);
        txtCodigo.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(txtCodigo);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 90)));
        // LineNumberReader del paquete app espera un JTextArea
        scroll.setRowHeaderView(new app.LineNumberReader(txtCodigo));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // Panel derecho con pestañas
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 5, 5, 10));

        // Consola arriba
        txtConsola = new JTextArea(5, 0);
        txtConsola.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtConsola.setBackground(new Color(15, 15, 25));
        txtConsola.setForeground(ACCENT_GREEN);
        txtConsola.setEditable(false);
        txtConsola.setBorder(new EmptyBorder(6, 8, 6, 8));
        txtConsola.setText(">>> Listo. Cargue un archivo y ejecute el análisis.\n");

        JScrollPane scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 90)),
                "  Consola de Resultados / Errores  ",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 12), TEXT_SECONDARY));
        scrollConsola.setPreferredSize(new Dimension(0, 160));

        // Pestañas: Tokens | Tabla de Símbolos
        tabbedRight = new JTabbedPane();
        tabbedRight.setBackground(BG_PANEL);
        tabbedRight.setForeground(TEXT_PRIMARY);
        tabbedRight.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedRight.addTab("🔤  Tokens Generados",  crearTabTokens());
        tabbedRight.addTab("📋  Tabla de Símbolos", crearTabSimbolos());

        panel.add(scrollConsola,  BorderLayout.NORTH);
        panel.add(tabbedRight,    BorderLayout.CENTER);
        return panel;
    }

    // Pestaña: tabla de tokens
    private JScrollPane crearTabTokens() {
        modelTokens = new DefaultTableModel(
                new String[]{"Línea", "Token", "Lexema"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTokens = crearTabla(modelTokens);
        tblTokens.getColumnModel().getColumn(0).setPreferredWidth(55);
        tblTokens.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblTokens.getColumnModel().getColumn(2).setPreferredWidth(250);

        JScrollPane sp = new JScrollPane(tblTokens);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_PANEL);
        return sp;
    }

    // Pestaña: tabla de símbolos  ← NUEVA
    private JScrollPane crearTabSimbolos() {
        modelSimbolos = new DefaultTableModel(
                new String[]{"#", "Identificador", "Tipo", "Valor", "Línea", "Ocurrencias"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSimbolos = crearTabla(modelSimbolos);
        tblSimbolos.getColumnModel().getColumn(0).setPreferredWidth(35);
        tblSimbolos.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblSimbolos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblSimbolos.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblSimbolos.getColumnModel().getColumn(4).setPreferredWidth(55);
        tblSimbolos.getColumnModel().getColumn(5).setPreferredWidth(80);

        // Color especial para la columna Tipo
        tblSimbolos.getColumnModel().getColumn(2).setCellRenderer(new TipoColorRenderer());

        JScrollPane sp = new JScrollPane(tblSimbolos);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_PANEL);

        // Panel contenedor con info inferior
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BG_PANEL);
        container.add(sp, BorderLayout.CENTER);

        lblEstado = new JLabel("  Sin símbolos registrados.");
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblEstado.setForeground(TEXT_SECONDARY);
        lblEstado.setBorder(new EmptyBorder(4, 8, 4, 8));
        container.add(lblEstado, BorderLayout.SOUTH);

        JScrollPane wrapper = new JScrollPane(container);
        wrapper.setBorder(null);
        return sp;
    }

    // Helper para crear JTable con estilo oscuro
    private JTable crearTabla(DefaultTableModel model) {
        JTable tabla = new JTable(model);
        tabla.setBackground(ROW_ODD);
        tabla.setForeground(TEXT_PRIMARY);
        tabla.setFont(new Font("Consolas", Font.PLAIN, 13));
        tabla.setRowHeight(26);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionBackground(new Color(0, 100, 180));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);

        // Header
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 32));
        header.setReorderingAllowed(false);

        // Renderer de filas alternas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                    setForeground(TEXT_PRIMARY);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
        return tabla;
    }

    // ── Footer: botones ──────────────────────────────────────────────────────
    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        footer.setBackground(BG_DARK);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 80)));

        btnAbrir    = boton("📂  Abrir Archivo",    new Color(70, 70, 100));
        btnAnalizar = boton("▶  Ejecutar Análisis", ACCENT_BLUE);
        btnLimpiar  = boton("🗑  Limpiar",           new Color(80, 80, 80));
        btnSalir    = boton("✖  Salir",              ACCENT_RED);

        btnAbrir.addActionListener(e -> abrirArchivo());
        btnAnalizar.addActionListener(e -> ejecutarAnalisis());
        btnLimpiar.addActionListener(e -> limpiar());
        btnSalir.addActionListener(e -> System.exit(0));

        footer.add(btnAbrir);
        footer.add(btnAnalizar);
        footer.add(btnLimpiar);
        footer.add(btnSalir);
        return footer;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  LÓGICA DE ANÁLISIS
    // ════════════════════════════════════════════════════════════════════════

    private void abrirArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar archivo de código Turbo X²");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
                StringBuilder sb = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) sb.append(linea).append("\n");
                txtCodigo.setText(sb.toString());
                consolaInfo(">>> Archivo cargado: " + fc.getSelectedFile().getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage());
            }
        }
    }

    private void ejecutarAnalisis() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            consolaError(">>> Error: no hay código para analizar.");
            return;
        }

        // Limpiar resultados anteriores
        modelTokens.setRowCount(0);
        modelSimbolos.setRowCount(0);
        tablaSimbolos.limpiar();
        txtConsola.setText("");

        try {
            Lexer lexer = new Lexer(new java.io.StringReader(codigo));
            int totalTokens = 0;
            int totalErrores = 0;

            // Variables para detectar declaraciones: DEFINIR <id> COMO <tipo>
            String ultimoIdentificador = null;
            boolean esperandoCOMO = false;
            boolean esperandoTipo = false;

            // Variables para detectar asignaciones: <id> <- <valor>
            String idParaAsignar = null;
            boolean esperandoAsignacion = false;

            Tokens token;
            int guardia = 0;

            while ((token = lexer.yylex()) != null) {
                if (++guardia > 50000) break;

                String lexema = lexer.lexeme;
                int    linea  = lexer.getLinea();

                // ── Registrar en tabla de tokens ──────────────────────────
                if (token != Tokens.COMENTARIO) {
                    modelTokens.addRow(new Object[]{linea, token.name(), lexema});
                    totalTokens++;
                }

                // ── Manejo de errores ──────────────────────────────────────
                if (token == Tokens.ERROR) {
                    consolaError(">>> Error léxico: símbolo '" + lexema
                            + "' no reconocido  (línea " + linea + ", col " + lexer.getColumna() + ")");
                    totalErrores++;
                    continue;
                }

                // ── Lógica de Tabla de Símbolos ────────────────────────────
                // Patrón: DEFINIR <Identificador> COMO <Tipo>
                switch (token) {
                    case DEFINIR:
                        esperandoCOMO = true;
                        break;

                    case Identificador:
                        if (esperandoCOMO) {
                            // Es el identificador siendo declarado
                            ultimoIdentificador = lexema;
                            esperandoTipo = false;
                        } else {
                            // Referencia a un identificador existente o nuevo desconocido
                            tablaSimbolos.agregar(lexema, "Desconocido", "", linea);
                            idParaAsignar = lexema;
                        }
                        break;

                    case COMO:
                        if (esperandoCOMO && ultimoIdentificador != null) {
                            esperandoTipo = true;
                            esperandoCOMO = false;
                        }
                        break;

                    case TIPO_ENTERO:
                    case TIPO_REAL:
                    case TIPO_LOGICO:
                    case TIPO_CARACTER:
                    case TIPO_CADENA:
                        if (esperandoTipo && ultimoIdentificador != null) {
                            // Registrar con tipo conocido
                            tablaSimbolos.agregar(ultimoIdentificador, lexema, "", linea);
                            // Si ya existía como Desconocido, actualizar
                            tablaSimbolos.actualizarTipo(ultimoIdentificador, lexema);
                            esperandoTipo       = false;
                            ultimoIdentificador = null;
                        }
                        break;

                    case ASIGNACION:
                        // El siguiente valor literal será el valor del identificador
                        esperandoAsignacion = (idParaAsignar != null);
                        break;

                    case Numero:
                    case Cadena:
                    case VALOR_VERDADERO:
                    case VALOR_FALSO:
                        if (esperandoAsignacion && idParaAsignar != null) {
                            tablaSimbolos.actualizarValor(idParaAsignar, lexema);
                            esperandoAsignacion = false;
                            idParaAsignar = null;
                        }
                        break;

                    default:
                        // Cualquier otro token rompe la cadena de asignación
                        esperandoAsignacion = false;
                        break;
                }
            }

            // ── Poblar tabla de símbolos ───────────────────────────────────
            List<TablaDeSimbolos.Simbolo> simbolos = tablaSimbolos.obtenerTodos();
            int idx = 1;
            for (TablaDeSimbolos.Simbolo s : simbolos) {
                // Omitir palabras reservadas que quedaron como identificadores
                if (!esPalabraReservada(s.getNombre())) {
                    modelSimbolos.addRow(new Object[]{
                        idx++,
                        s.getNombre(),
                        s.getTipo(),
                        s.getValor().isEmpty() ? "—" : s.getValor(),
                        s.getLinea(),
                        s.getOcurrencias()
                    });
                }
            }

            // ── Resumen en consola ─────────────────────────────────────────
            consolaInfo(">>> Análisis finalizado.");
            consolaInfo("    Tokens reconocidos : " + totalTokens);
            consolaInfo("    Símbolos en tabla   : " + (idx - 1));
            if (totalErrores > 0)
                consolaError("    Errores léxicos    : " + totalErrores);
            else
                consolaInfo("    Errores léxicos    : 0  ✓");

            // Cambiar a la pestaña de tokens automáticamente
            tabbedRight.setSelectedIndex(0);

            // Actualizar etiqueta de estado de la tabla de símbolos
            if (lblEstado != null)
                lblEstado.setText("  " + (idx - 1) + " identificador(es) registrado(s).");

        } catch (IOException ex) {
            consolaError(">>> Error de E/S: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtConsola.setText(">>> Listo. Cargue un archivo y ejecute el análisis.\n");
        modelTokens.setRowCount(0);
        modelSimbolos.setRowCount(0);
        tablaSimbolos.limpiar();
        if (lblEstado != null) lblEstado.setText("  Sin símbolos registrados.");
    }

    // ════════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════════════════════

    private void consolaInfo(String msg) {
        txtConsola.setForeground(ACCENT_GREEN);
        txtConsola.append(msg + "\n");
    }

    private void consolaError(String msg) {
        // Append con color rojo usando un truco de atributos
        txtConsola.setForeground(ACCENT_RED);
        txtConsola.append(msg + "\n");
        txtConsola.setForeground(ACCENT_GREEN);
    }

    private JLabel label(String text, int size, boolean bold) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        lbl.setForeground(TEXT_SECONDARY);
        return lbl;
    }

    private JButton boton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 36));
        btn.addMouseListener(new MouseAdapter() {
            final Color original = bg;
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(original.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(original); }
        });
        return btn;
    }

    /** Renderer que colorea la columna Tipo según el tipo de dato */
    private class TipoColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, foc, row, col);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            if (!sel) {
                setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                String tipo = val != null ? val.toString() : "";
                switch (tipo) {
                    case "Entero":    setForeground(new Color(100, 200, 255)); break;
                    case "Real":      setForeground(new Color(150, 255, 150)); break;
                    case "Cadena":    setForeground(new Color(255, 200, 100)); break;
                    case "Logico":    setForeground(new Color(200, 150, 255)); break;
                    case "Caracter":  setForeground(new Color(255, 150, 150)); break;
                    default:          setForeground(TEXT_SECONDARY);           break;
                }
            }
            return this;
        }
    }

    /** Filtra palabras reservadas para no mostrarlas como identificadores */
    private boolean esPalabraReservada(String nombre) {
        switch (nombre.toUpperCase()) {
            case "INICIO": case "FIN": case "SI": case "ENTONCES": case "SINO":
            case "MIENTRAS": case "HACER": case "DEFINIR": case "COMO":
            case "ESCRIBIR": case "LEER": case "VERDADERO": case "FALSO":
                return true;
            default: return false;
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new frmPrincipal().setVisible(true));
    }
}
