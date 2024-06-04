import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CarDetailSystem extends JFrame implements ActionListener {
    private JTextField carNameField, carTypeField, carPriceField, carEngineField, carFuleField, carTransmissionField, carSeatingField, searchField;
    private JButton addCarButton, deleteCarButton, viewCarButton;
    private Connection connection;
    private JTable carTable;

    public CarDetailSystem() {
        setTitle("Car Detail System");
        setSize(800, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);


        JLabel carNameLabel = new JLabel("Car Name:");
        carNameLabel.setBounds(50, 80, 80, 25);
        add(carNameLabel);

        carNameField = new JTextField();
        carNameField.setBounds(150, 80, 150, 25);
        add(carNameField);

        JLabel carTypeLabel = new JLabel("Car Company:");
        carTypeLabel.setBounds(50, 120, 80, 25);
        add(carTypeLabel);

        carTypeField = new JTextField();
        carTypeField.setBounds(150, 120, 150, 25);
        add(carTypeField);


        JLabel carPriceLabel = new JLabel("Car Price:");
        carPriceLabel.setBounds(50, 160, 80, 25);
        add(carPriceLabel);

        carPriceField = new JTextField();
        carPriceField.setBounds(150, 160, 150, 25);
        add(carPriceField);

        JLabel carEngineLabel = new JLabel("Car Engine:");
        carEngineLabel.setBounds(50, 200, 80, 25);
        add(carEngineLabel);

        carEngineField = new JTextField();
        carEngineField.setBounds(150, 200, 150, 25);
        add(carEngineField);

        JLabel carFuleLabel = new JLabel("Car Fule Type:");
        carFuleLabel.setBounds(50, 240, 80, 25);
        add(carFuleLabel);

        carFuleField = new JTextField();
        carFuleField.setBounds(150, 240, 150, 25);
        add(carFuleField);

        JLabel carTransmissionLabel = new JLabel("Car Transmission:");
        carTransmissionLabel.setBounds(50, 280, 80, 25);
        add(carTransmissionLabel);

        carTransmissionField = new JTextField();
        carTransmissionField.setBounds(150, 280, 150, 25);
        add(carTransmissionField);

        JLabel carSeatingLabel = new JLabel("Car Seating:");
        carSeatingLabel.setBounds(50, 320, 320, 25);
        add(carSeatingLabel);

        carSeatingField = new JTextField();
        carSeatingField.setBounds(150, 320, 150, 25);
        add(carSeatingField);




    
        addCarButton = new JButton("Add Car");
        addCarButton.setBounds(150, 360, 100, 25);
        addCarButton.addActionListener(this);
        add(addCarButton);

        deleteCarButton = new JButton("Delete Car");
        deleteCarButton.setBounds(260, 360, 100, 25);
        deleteCarButton.addActionListener(this);
        add(deleteCarButton);

        searchField = new JTextField();
        searchField.setBounds(50, 400, 200, 25);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(260, 400, 100, 25);
        searchButton.addActionListener(this);
        add(searchButton);

        viewCarButton = new JButton("View Car Details");
        viewCarButton.setBounds(370, 400, 150, 25);
        viewCarButton.addActionListener(this);
        add(viewCarButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 440, 700, 200);
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
            if (e.getSource() == addCarButton) {
            String carName = carNameField.getText();
            String carType = carTypeField.getText();
            String carPrice = carPriceField.getText();
            String carEngine = carEngineField.getText();
            String carFule = carFuleField.getText();
            String carTransmission = carTransmissionField.getText();
            String carSeating = carSeatingField.getText();
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO cars (name, type, price, engine, fule, transmission, seating) VALUES (?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, carName);
                statement.setString(2, carType);
                statement.setString(3, carPrice);
                statement.setString(4, carEngine);
                statement.setString(5, carFule);
                statement.setString(6, carTransmission);
                statement.setString(7, carSeating);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Car added successfully!");
                updateCarTable(); // Refresh car table
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to add car!");
            }
        } else if (e.getSource() == deleteCarButton) {
            int selectedRow = carTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a car to delete!");
                return;
            }
            int carId = (int) carTable.getValueAt(selectedRow, 0);
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM cars WHERE car_id = ?");
                statement.setInt(1, carId);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Car deleted successfully!");
                updateCarTable(); // Refresh car table
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete car!");
            }
        } else if (e.getSource() == viewCarButton) {
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
        CarDetailSystem system = new CarDetailSystem();
        system.setVisible(true);
    }
}
