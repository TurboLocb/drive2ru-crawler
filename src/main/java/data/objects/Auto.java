package data.objects;

public class Auto {
    private String URL;

    private long price;

    private String model;

    private String brand;

    private float engineCapacity;

    private int year;

    public Auto() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(float engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Auto{" +
                "URL='" + URL + '\'' +
                ", price=" + price +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", engineCapacity=" + engineCapacity +
                ", year=" + year +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        Auto input = (Auto) o;
        return input.getURL().equals(this.URL)
                & input.getPrice() == this.price
                & input.getBrand().equals(this.brand)
                & input.getModel().equals(this.model)
                & input.getEngineCapacity() == this.engineCapacity
                & input.getYear() == this.year;
    }
}
