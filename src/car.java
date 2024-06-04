import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class car extends JFrame implements ActionListener {
    private JTextField searchField;
    // private JPasswordField passwordField;
    private JButton viewCarButton;
    private Connection connection;
    private JTable carTable;

    public car() {
        setTitle("Car Detail System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        searchField = new JTextField();
        searchField.setBounds(50, 60, 200, 25);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(260, 60, 100, 25);
        searchButton.addActionListener(this);
        add(searchButton);

        viewCarButton = new JButton("View Car Details");
        viewCarButton.setBounds(370, 60, 150, 25);
        viewCarButton.addActionListener(this);
        add(viewCarButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 110, 700, 400);
        add(scrollPane);

        carTable = new JTable();
        scrollPane.setViewportView(carTable);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental_db", "root", "admin1132005");
            updateCarTable(); // Load car details initially
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCarTable() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM cars");
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Car ID");
            model.addColumn("Car Name");
            model.addColumn("Car Company");

            while (resultSet.next()) {
                model.addRow(new Object[]{resultSet.getInt("car_id"), resultSet.getString("name"), resultSet.getString("type")});
            }

            carTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
     if (e.getSource() == viewCarButton) {
            int selectedRow = carTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car to view details!");
                return;
            }
            int carId = (int) carTable.getValueAt(selectedRow, 0);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM cars WHERE car_id = ?");
                statement.setInt(1, carId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    JFrame carDetailsFrame = new JFrame("Car Details");
                    carDetailsFrame.setSize(400, 200);
                    carDetailsFrame.setLayout(new GridLayout(9, 2));

                    carDetailsFrame.add(new JLabel("Car ID:"));
                    carDetailsFrame.add(new JLabel(String.valueOf(carId)));

                    carDetailsFrame.add(new JLabel("Car Name:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("name")));

                    carDetailsFrame.add(new JLabel("Car Company:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("type")));

                    carDetailsFrame.add(new JLabel("Car Price:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("price")));

                    carDetailsFrame.add(new JLabel("Car Engine:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("engine")));

                    carDetailsFrame.add(new JLabel("Car Fule:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("fule")));

                    carDetailsFrame.add(new JLabel("Car Transmission:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("transmission")));

                    carDetailsFrame.add(new JLabel("Car Seating:"));
                    carDetailsFrame.add(new JLabel(resultSet.getString("seating")));

                    carDetailsFrame.setVisible(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to fetch car details!");
            }
        } else if (e.getActionCommand().equals("Search")) {
            String searchTerm = searchField.getText();
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM cars WHERE name LIKE ?");
                statement.setString(1, "%" + searchTerm + "%");
                ResultSet resultSet = statement.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Car ID");
                model.addColumn("Car Name");
                model.addColumn("Car Type");

                while (resultSet.next()) {
                    model.addRow(new Object[]{resultSet.getInt("car_id"), resultSet.getString("name"), resultSet.getString("type")});
                }

                carTable.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to search cars!");
            }
        }
    }

    public static void main(String[] args) {
        car system = new car();
        system.setVisible(true);
    }
}
