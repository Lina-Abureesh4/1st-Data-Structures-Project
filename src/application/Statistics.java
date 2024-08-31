package application;

public class Statistics {

	private double count;
	private ElectricityRecord total;
	private ElectricityRecord min;
	private ElectricityRecord max;
	private ElectricityRecord average;

	public Statistics(double count, ElectricityRecord total, ElectricityRecord min, ElectricityRecord max) {
		super();
		this.count = count;
		this.total = total;
		this.min = min;
		this.max = max;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public ElectricityRecord getTotal() {
		return total;
	}

	public void setTotal(ElectricityRecord total) {
		this.total = total;
	}

	public ElectricityRecord getMin() {
		return min;
	}

	public void setMin(ElectricityRecord min) {
		this.min = min;
	}

	public ElectricityRecord getMax() {
		return max;
	}

	public void setMax(ElectricityRecord max) {
		this.max = max;
	}

	public void calculateAvg() {
		if (total != null && count != 0) {
			this.average = new ElectricityRecord(total.getOccupation_lines() / count, total.getPower_plant() / count,
					total.getEgyption_lines() / count, total.getDemand() / count,
					total.getPower_cuts_hour_day() / count, total.getTemp() / count);
			this.average.setLabel("Average");
		}
	}
	
	public ElectricityRecord getAvg() {
		return this.average;
	}

	@Override
	public String toString() {
		return "Statistics [count=" + count + ", \n\ttotal=[" + total + "], \n\tmin=[" + min + "], \n\tmax=[" + max + "], \n\taverage=["
				+ average + "]]";
	}
	
	
}