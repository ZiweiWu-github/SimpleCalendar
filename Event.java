/**
 * 
 * @author Ziwei Wu, HW#4 Answer
 * @version 1.0
 *
 */
public class Event {
	private String title;
	private String date;
	private String startTime;
	private String endTime;
	private int year;
	private int month;
	private int day;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	/**
	 * Constructor for events with an unknown end time
	 * @param title: Title of event
	 * @param date: date of event in MM/DD/YYYY format
	 * @param startTime: when the event starts in HH:MM format
	 */
	public Event(String title, String date, String startTime){
		this.title = title;
		this.date = date;
		this.startTime = startTime;
		String[] date1 = date.split("/");
		month = Integer.parseInt(date1[0]);
		day = Integer.parseInt(date1[1]);
		year = Integer.parseInt(date1[2]);
		String[] timey = startTime.split(":");
		startHour = Integer.parseInt(timey[0]);
		startMinute = Integer.parseInt(timey[1]);
		
	}
	/**
	 * Constructor for events with a known end time
	 * @param title: Title of event
	 * @param date: date of event in MM/DD/YYYY format
	 * @param startTime: when the event starts in HH:MM format
	 * @param endTime: when the event ends in HH:MM format
	 */
	public Event(String title, String date, String startTime, String endTime){
		this.title = title;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		String[] date1 = date.split("/");
		month = Integer.parseInt(date1[0]);
		day = Integer.parseInt(date1[1]);
		year = Integer.parseInt(date1[2]);
		String[] timey = startTime.split(":");
		startHour = Integer.parseInt(timey[0]);
		startMinute = Integer.parseInt(timey[1]);
		if(!this.endTime.trim().isEmpty()){
			String[] endTimey = endTime.split(":");
			
			if(endTimey[0].equals("00")){
				endHour = 0;
			}
			else{endHour = Integer.parseInt(endTimey[0]);}
			if(endTimey[1].equals("00")){
				endMinute = 0;
			}
			else{endMinute = Integer.parseInt(endTimey[1]);}
		}
	}
	/**
	 * Returns the hour of the end time
	 * @return endHour
	 */
	public int getEndHour(){
		return this.endHour;
	}
	/**
	 * Returns the minute of the end time
	 * @return endMinute
	 */
	public int getEndMinute(){
		return this.endMinute;
	}
	/**
	 * Returns the starting hour of the event
	 * @return startHour
	 */
	public int getHour(){
		return this.startHour;
	}
	/**
	 * Returns the minute of the starting time
	 * @return startMinute
	 */
	public int getMinute(){
		return this.startMinute;
	}
	/**
	 * Returns the date of the event
	 * @return this.date
	 */
	public String getDate(){
		return this.date;
	}
	/**
	 * Returns the time period the event takes place
	 * @return time
	 */
	public String getTime(){
		if(!this.endTime.trim().isEmpty()){return this.startTime +" - "+ this.endTime;}
		return this.startTime;
	}
	/**
	 * Returns the title of the event
	 * @return title
	 */
	public String getTitle(){
		return this.title;
	}
	/**
	 * Returns the month of the event
	 * @return month
	 */
	public int getMonth(){
		return this.month;
	}
	/**
	 * Returns the day of month of the event
	 * @return day
	 */
	public int getDay(){
		return this.day;
	}
	/**
	 * Returns the year the event is in
	 * @return year
	 */
	public int getYear(){
		return this.year;
	}
	/**
	 * Returns whether or not the event has the specified 'hour' (row in this case)
	 * @param h: the 'hour' in question
	 * @precondition h must be a valid row in the table
	 * @return true or false depending on the event hours
	 */
	public boolean containsHour(int h){
		if(this.startHour == h/2){
			if(h%2 ==0 && this.startMinute < 30){
				return true;
			}
			if(h%2 ==1 && this.startMinute >= 30){
				return true;
			}
		}
		if(this.endHour == h/2){
			if(h%2==0 &&  this.endMinute > 30){
				return true;
			}
			if(h%2==0 &&  this.endMinute > 0){
				return true;
			}
			if(h%2==1 && this.endMinute > 30){
				return true;
			}
		}
		if(this.startHour <= h/2 && this.endHour > h/2){
			if( this.startHour == h/2 && h%2 ==0 && this.startMinute >= 30){
				return false;
			}
			return true;
		}
		return false;
	}
}
