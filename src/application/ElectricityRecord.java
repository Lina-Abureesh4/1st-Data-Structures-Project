package application;

public class ElectricityRecord {

	private double occupation_lines;
	private double power_plant;
	private double Egyption_lines;
	private double total_daily_supply;
	private double demand;
	private double power_cuts_hour_day;
	private double temp;
	private String label;

	public ElectricityRecord(double occupation_lines, double power_plant, double egyption_lines, double demand,
			double power_cuts_hour_day, double temp) {
		super();

		setOccupation_lines(occupation_lines);
		setPower_plant(power_plant);
		setEgyption_lines(egyption_lines);
		setDemand(demand);
		setPower_cuts_hour_day(power_cuts_hour_day);
		setTemp(temp);
		calculate_total_daily_supply();
	}

	public double getOccupation_lines() {
		return occupation_lines;
	}

	public void setOccupation_lines(double occupation_lines) {
		if (occupation_lines >= 0)
			this.occupation_lines = occupation_lines;
	}

	public double getPower_plant() {
		return power_plant;
	}

	public void setPower_plant(double power_plant) {
		if (power_plant >= 0)
			this.power_plant = power_plant;
	}

	public double getEgyption_lines() {
		return Egyption_lines;
	}

	public void setEgyption_lines(double egyption_lines) {
		if (egyption_lines >= 0)
			Egyption_lines = egyption_lines;
	}

	public double getTotal_daily_supply() {
		return total_daily_supply;
	}

	public void calculate_total_daily_supply() {
		this.total_daily_supply = getEgyption_lines() + getOccupation_lines() + getPower_plant();
	}

	public double getDemand() {
		return demand;
	}

	public void setDemand(double demand) {
		if (demand >= 0)
			this.demand = demand;
	}

	public double getPower_cuts_hour_day() {
		return power_cuts_hour_day;
	}

	public void setPower_cuts_hour_day(double power_cuts_hour_day) {
		if (power_cuts_hour_day >= 0)
			this.power_cuts_hour_day = power_cuts_hour_day;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "occupation_lines= " + occupation_lines + ", Gaza_power_plant= " + power_plant + ", Egyption_lines= "
				+ Egyption_lines + ", tota_daily_supply= " + total_daily_supply + ", demand= " + demand
				+ ", power_cuts_hour_day= " + power_cuts_hour_day + ", temp= " + temp;
	}

}
