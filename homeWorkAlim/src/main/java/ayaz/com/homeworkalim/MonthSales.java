package ayaz.com.homeworkalim;

public class MonthSales{
    private String name;
    private double sum;
    public MonthSales(String name, double sum) {
        this.name = name;
        this.sum = sum;
    }


    public String getName() {
        return name;
    }

    public double getSum() {
        return sum;
    }
    public void addSales(double sales) {
        this.sum += sales;
    }
}