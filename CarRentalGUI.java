import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CarRentalGUI extends JFrame {
	private CarRentalSystem rentalSystem;
	private JTextArea displayArea;
	private JTextField inputField;
	private JTextArea commandListArea;
	private JTextField customerNameField;

	public CarRentalGUI() {
		rentalSystem = new CarRentalSystem();
		initializeGUI();
	}

	private void initializeGUI() {
		setTitle("Car Rental System");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		displayArea = new JTextArea();
		displayArea.setEditable(false);
		add(new JScrollPane(displayArea), BorderLayout.CENTER);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 2));

		JLabel inputLabel = new JLabel("Enter command:");
		inputField = new JTextField();
		JButton submitButton = new JButton("Submit");

		JLabel customerNameLabel = new JLabel("Enter customer name:");
		customerNameField = new JTextField();

		inputPanel.add(inputLabel);
		inputPanel.add(inputField);
		inputPanel.add(customerNameLabel);
		inputPanel.add(customerNameField);
		inputPanel.add(new JLabel(""));
		inputPanel.add(submitButton);

		add(inputPanel, BorderLayout.SOUTH);

		commandListArea = new JTextArea();
		commandListArea.setEditable(false);
		commandListArea.setText("Available Commands:\n" + "1. list cars - Lists all available cars\n"
				+ "2. rent <carId> <days> - Rent a car with the given ID for the specified number of days\n"
				+ "3. return <carId> - Return the car with the given ID\n");
		add(new JScrollPane(commandListArea), BorderLayout.WEST);

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleInput();
			}
		});

		// Initialize the car list for demonstration
		initializeCars();
	}

	private void initializeCars() {
		Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
		Car car2 = new Car("C002", "Honda", "Accord", 70.0);
		Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
		rentalSystem.addCar(car1);
		rentalSystem.addCar(car2);
		rentalSystem.addCar(car3);
	}

	private void handleInput() {
		String command = inputField.getText();
		String customerName = customerNameField.getText();
		inputField.setText("");
		customerNameField.setText("");
		displayArea.append("Command: " + command + "\n");

		if (command.equalsIgnoreCase("list cars")) {
			listAvailableCars();
		} else if (command.startsWith("rent ")) {
			if (customerName.trim().isEmpty()) {
				displayArea.append("Customer name is required for renting a car.\n");
			} else {
				String[] parts = command.split(" ");
				if (parts.length == 3) {
					try {
						int days = Integer.parseInt(parts[2]);
						rentCar(parts[1], days, customerName);
					} catch (NumberFormatException ex) {
						displayArea.append("Invalid number of days. Please enter a valid number.\n");
					}
				} else {
					displayArea.append("Invalid rent command format. Use: rent <carId> <days>\n");
				}
			}
		} else if (command.startsWith("return ")) {
			if (customerName.trim().isEmpty()) {
				displayArea.append("Customer name is required for returning a car.\n");
			} else {
				returnCar(command.split(" ")[1], customerName);
			}
		} else {
			displayArea.append("Unknown command.\n");
		}
	}

	private void listAvailableCars() {
		displayArea.append("Available Cars:\n");
		for (Car car : rentalSystem.getCars()) {
			if (car.isAvailable()) {
				displayArea.append(car.getCarId() + " - " + car.getBrand() + " " + car.getModel() + "\n");
			}
		}
	}

	private void rentCar(String carId, int days, String customerName) {
		Car car = rentalSystem.getCarById(carId);
		if (car != null && car.isAvailable()) {
			Customer customer = new Customer("CUS" + (rentalSystem.getCustomers().size() + 1), customerName);
			rentalSystem.addCustomer(customer);
			rentalSystem.rentCar(car, customer, days);
			displayArea.append("Car rented successfully to " + customer.getName() + ".\n");
		} else {
			displayArea.append("Car is not available.\n");
		}
	}

	private void returnCar(String carId, String customerName) {
		Car car = rentalSystem.getCarById(carId);
		if (car != null && !car.isAvailable()) {
			Rental rental = null;
			for (Rental r : rentalSystem.getRentals()) {
				if (r.getCar().equals(car)) {
					rental = r;
					break;
				}
			}

			if (rental != null && rental.getCustomer().getName().equals(customerName)) {
				rentalSystem.returnCar(car);
				double totalPrice = car.calculatePrice(rental.getDays());
				displayArea.append("Car returned successfully by " + rental.getCustomer().getName() + ".\n");
				displayArea.append("Total billing amount: $" + totalPrice + "\n");
			} else {
				displayArea.append("Car was not rented by " + customerName + " or rental information is missing.\n");
			}
		} else {
			displayArea.append("Car is not rented or invalid car ID.\n");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CarRentalGUI gui = new CarRentalGUI();
			gui.setVisible(true);
		});
	}
}
