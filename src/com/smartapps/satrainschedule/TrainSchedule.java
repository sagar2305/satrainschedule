package com.smartapps.satrainschedule;

import java.util.ArrayList;
import java.util.List;

public class TrainSchedule {

	private String daysRunning;
	private List<ScheduleData> scheduleData = new ArrayList<TrainSchedule.ScheduleData>();
	
	public String getDaysRunning() {
		return daysRunning;
	}

	public void setDaysRunning(String daysRunning) {
		this.daysRunning = daysRunning;
	}

	public List<ScheduleData> getScheduleData() {
		return scheduleData;
	}

	public void setScheduleData(List<ScheduleData> scheduleData) {
		this.scheduleData = scheduleData;
	}

	public class ScheduleData {
		public String getStation() {
			return station;
		}
		public void setStation(String station) {
			this.station = station;
		}
		public String getArrival() {
			return arrival;
		}
		public void setArrival(String arrival) {
			this.arrival = arrival;
		}
		public String getDeparture() {
			return departure;
		}
		public void setDeparture(String departure) {
			this.departure = departure;
		}
		public String getDistance() {
			return distance;
		}
		public void setDistance(String distance) {
			this.distance = distance;
		}
		public String getDays() {
			return days;
		}
		public void setDays(String days) {
			this.days = days;
		}
		private String station;
		private String arrival;
		private String departure;
		private String distance;
		private String days;
	}
}
