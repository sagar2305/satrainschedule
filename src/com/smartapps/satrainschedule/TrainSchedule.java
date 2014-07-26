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
		
		private String stationName;
		public String getStationName() {
			return stationName;
		}
		public void setStationName(String stationName) {
			this.stationName = stationName;
		}
		public String getArrivalTime() {
			return arrivalTime;
		}
		public void setArrivalTime(String arrivalTime) {
			this.arrivalTime = arrivalTime;
		}
		public String getDepartureTime() {
			return departureTime;
		}
		public void setDepartureTime(String departureTime) {
			this.departureTime = departureTime;
		}
		public String getDistance() {
			return distance;
		}
		public void setDistance(String distance) {
			this.distance = distance;
		}
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}
		private String arrivalTime;
		private String departureTime;
		private String distance;
		private String day;
	}
}
