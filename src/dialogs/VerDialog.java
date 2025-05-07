package dialogs;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import dao.GestionTrabajadores;
import exceptions.BDException;
import modelo.Empresa;

public class VerDialog extends JDialog implements ActionListener {

    private Empresa empresa;
    private JTable tabla;
    private TableRowSorter<TableModel> sorter;
    private JTextField filtroNombre, filtroDNI;
    private JButton cerrar, limpiarFiltros;

    public VerDialog(Empresa empresa) {
        this.empresa = empresa;

        setTitle("Buscar Trabajadores");
        setSize(800, 700);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setModal(true);

        try {
            inicializarTabla();
            inicializarFiltros();
            inicializarBotones();
        } catch (BDException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los trabajadores", "Error", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    private void inicializarTabla() throws BDException {
        String[] columnas = { "ID", "DNI", "Nombre", "Apellidos", "Dirección", "Teléfono", "Puesto" };
        String[][] datos = GestionTrabajadores.listarTrabajadores();

        tabla = new JTable(datos, columnas);
        sorter = new TableRowSorter<>(tabla.getModel());
        tabla.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(750, 550));
        add(scroll, BorderLayout.CENTER);
    }

    private void inicializarFiltros() {
        JPanel panelFiltros = new JPanel(new FlowLayout());

        panelFiltros.add(new JLabel("Filtrar por Nombre:"));
        filtroNombre = new JTextField(10);
        filtroNombre.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
        });
        panelFiltros.add(filtroNombre);

        panelFiltros.add(new JLabel("Filtrar por DNI:"));
        filtroDNI = new JTextField(10);
        filtroDNI.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
        });
        panelFiltros.add(filtroDNI);

        add(panelFiltros, BorderLayout.NORTH);
    }

    private void inicializarBotones() {
        JPanel panelBotones = new JPanel();

        limpiarFiltros = new JButton("Limpiar filtros");
        limpiarFiltros.addActionListener(this);
        panelBotones.add(limpiarFiltros);

        cerrar = new JButton("Cerrar");
        cerrar.addActionListener(this);
        panelBotones.add(cerrar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void aplicarFiltro() {
        RowFilter<TableModel, Object> rf = null;
        try {
            String nombre = Pattern.quote(filtroNombre.getText());
            String dni = Pattern.quote(filtroDNI.getText());

            rf = RowFilter.regexFilter("(?i).*" + nombre + ".*", 2); // columna nombre
            RowFilter<TableModel, Object> rfDNI = RowFilter.regexFilter("(?i).*" + dni + ".*", 1); // columna dni

            sorter.setRowFilter(RowFilter.andFilter(java.util.Arrays.asList(rf, rfDNI)));
        } catch (PatternSyntaxException e) {
            System.out.println("Filtro inválido");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cerrar) {
            dispose();
        } else if (e.getSource() == limpiarFiltros) {
            filtroNombre.setText("");
            filtroDNI.setText("");
            sorter.setRowFilter(null);
        }
    }
}
