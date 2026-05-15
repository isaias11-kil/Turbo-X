package codigo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Integración con JFreeChart para la instrucción {@code graficar(id);}.
 * <p>
 * Datos admitidos:
 * <ul>
 *   <li>{@code Entero} / {@code Real}: un solo valor.</li>
 *   <li>{@code Cadena}: números separados por comas. Opcionalmente puede comenzar con
 *       {@code LINEAS:} o {@code BARRAS:} (sin distinguir mayúsculas / acentos) para elegir
 *       el tipo de gráfica; si no hay prefijo, se usan barras.</li>
 * </ul>
 */
public final class ServicioGraficasJFreeChart {

    private ServicioGraficasJFreeChart() {
    }

    public static void mostrarDesdeSimbolo(TablaDeSimbolos tabla, TablaDeSimbolos.Simbolo s, int linea) {
        ModoYDatos parsed;
        try {
            parsed = interpretarValor(s);
        } catch (IllegalArgumentException ex) {
            tabla.registrarErrorSemantico(
                    "ERROR DE EJECUCIÓN [Línea " + linea + "]: no se pudo interpretar '"
                            + s.getNombre() + "' como datos numéricos para graficar. " + ex.getMessage());
            return;
        }

        if (parsed.valores.length == 0) {
            tabla.registrarErrorSemantico(
                    "ERROR DE EJECUCIÓN [Línea " + linea + "]: la variable '" + s.getNombre() + "' no contiene valores para graficar.");
            return;
        }

        final String titulo = "Turbo X — " + s.getNombre();
        final DefaultCategoryDataset dataset = construirDataset(parsed.valores);
        final boolean barras = parsed.lineas == false;

        SwingUtilities.invokeLater(() -> mostrarVentana(titulo, dataset, barras));
    }

    private static final class ModoYDatos {
        final boolean lineas;
        final double[] valores;

        ModoYDatos(boolean lineas, double[] valores) {
            this.lineas = lineas;
            this.valores = valores;
        }
    }

    private static ModoYDatos interpretarValor(TablaDeSimbolos.Simbolo s) {
        Object v = s.getValor();
        if (v == null) {
            throw new IllegalArgumentException("Valor nulo.");
        }
        if (v instanceof Number) {
            return new ModoYDatos(false, new double[]{((Number) v).doubleValue()});
        }
        if (v instanceof String) {
            return interpretarCadena((String) v);
        }
        throw new IllegalArgumentException("Tipo '" + s.getTipo() + "' no soportado para gráficas.");
    }

    private static ModoYDatos interpretarCadena(String raw) {
        String t = raw.trim();
        int colon = t.indexOf(':');
        if (colon > 0) {
            String pref = stripAccents(t.substring(0, colon).trim().toLowerCase(Locale.ROOT));
            String resto = t.substring(colon + 1).trim();
            if ("lineas".equals(pref)) {
                return new ModoYDatos(true, parsearListaNumerica(resto));
            }
            if ("barras".equals(pref)) {
                return new ModoYDatos(false, parsearListaNumerica(resto));
            }
        }
        return new ModoYDatos(false, parsearListaNumerica(t));
    }

    private static String stripAccents(String s) {
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}+", "");
    }

    private static double[] parsearListaNumerica(String raw) {
        String[] partes = raw.split(",");
        List<Double> out = new ArrayList<>();
        for (String p : partes) {
            String t = p.trim();
            if (t.isEmpty()) {
                continue;
            }
            out.add(Double.parseDouble(t));
        }
        double[] arr = new double[out.size()];
        for (int i = 0; i < out.size(); i++) {
            arr[i] = out.get(i);
        }
        return arr;
    }

    private static DefaultCategoryDataset construirDataset(double[] valores) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        String serie = "Valores";
        for (int i = 0; i < valores.length; i++) {
            String categoria = "P" + (i + 1);
            ds.addValue(valores[i], serie, categoria);
        }
        return ds;
    }

    private static void mostrarVentana(String titulo, DefaultCategoryDataset dataset, boolean barras) {
        JFreeChart chart = barras
                ? ChartFactory.createBarChart(
                titulo,
                "Punto",
                "Magnitud",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false)
                : ChartFactory.createLineChart(
                titulo,
                "Punto",
                "Magnitud",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        JFrame ventana = new JFrame(titulo);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.add(panel, BorderLayout.CENTER);
        ventana.pack();
        ventana.setLocationByPlatform(true);
        ventana.setVisible(true);
    }
}
