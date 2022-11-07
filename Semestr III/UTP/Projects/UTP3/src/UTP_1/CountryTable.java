package UTP_1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CountryTable {
    private String path;
    private Object[] columnsNames;
    private Object[][] rowData;
    private BufferedReader reader;

    public CountryTable(String path) {
        this.path = path;

        readFile();

        readColumnsNames();

        readRowsData();
    }

    private void readFile() {
        try {
            reader = new BufferedReader(new FileReader(path));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Object> separateWords(String lineOfWords, boolean isColumn) {
        StringBuilder word = new StringBuilder();

        List<Object> values = new ArrayList<>();

        int columnIndex = 0;
        for (int i = 0; i < lineOfWords.length(); i++) {

            if (lineOfWords.charAt(i) != '\t') {

                word.append(lineOfWords.charAt(i));

            } else {

                if (!isColumn) {

                    switch (columnIndex) {
                        case 2:
                            values.add(Integer.parseInt(word.toString()));
                            break;
                        case 3:
                            values.add(new ImageIcon("data/flags/" + word + ".png"));
                            break;
                        default:
                            values.add(word.toString());
                    }
                } else {
                    values.add(word.toString());
                }
                word = new StringBuilder();
                columnIndex++;
            }
        }

        values.add(word.toString());     //add last word, because it doesn't have '\t' after

        return values;
    }

    private void readColumnsNames() {
        List<Object> words = separateWords(readLine(), true);

        columnsNames = new Object[words.size()];

        for (int i = 0; i < columnsNames.length; i++) {
            columnsNames[i] = words.get(i);
        }
    }

    private void readRowsData() {
        String tmp = readLine();

        List<Object[]> rows = new ArrayList<>();

        while (tmp != null) {
            List<Object> words = separateWords(tmp, false);

            Object[] rowValues = new Object[words.size() - 1];


            for (int i = 0; i < rowValues.length; i++) {
                rowValues[i] = words.get(i);
            }

            rows.add(rowValues);

            tmp = readLine();
        }

        rowData = new Object[rows.size()][rows.get(0).length];

        for (int i = 0; i < rowData.length; i++) {
            rowData[i] = rows.get(i);
        }
    }

    private static class MyRenderer implements TableCellRenderer {
        JLabel label;

        public MyRenderer() {
            label = new JLabel();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setOpaque(true);
            label.setText("" + value);

            if ((Integer) value > 20000) {
                label.setForeground(Color.red);
            } else {
                label.setForeground(Color.black);
            }
            return label;
        }
    }

    public JTable create() {

        JTable table = new JTable(new DefaultTableModel(rowData, columnsNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 2:
                        return Integer.class;
                    case 3:
                        return ImageIcon.class;
                    default:
                        return Object.class;
                }
            }
        });

        table.setDefaultRenderer(Integer.class, new MyRenderer());
        return table;
    }
}

