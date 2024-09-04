class Car {
	private String carId;
	private String brand;
	private String model;
	private double basePricePerDay;
	private boolean isAvailable;

	public Car(String carId, String brand, String model, double basePricePerDay) {
		this.carId = carId;
		this.brand = brand;
		this.model = model;
		this.basePricePerDay = basePricePerDay;
		this.isAvailable = true; // By default, the car is available
	}

	public String getCarId() {
		return carId;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public double calculatePrice(int rentalDays) {
		return basePricePerDay * rentalDays;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void rent() {
		this.isAvailable = false; // Mark the car as rented
	}

	public void returnCar() {
		this.isAvailable = true; // Mark the car as available again
	}
}