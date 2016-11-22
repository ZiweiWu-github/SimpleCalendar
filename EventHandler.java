import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * 
 * @author Ziwei Wu, HW#4 Answer
 * @version 1.0
 *
 */
public class EventHandler {
	private ArrayList<ChangeListener> listeners;
	private ArrayList<Event> events;
	private ArrayList<Event> today;
	public EventHandler(){
		listeners = new ArrayList<ChangeListener>();
		events = new ArrayList<Event>();
	}
	/**
	 * Checks if the newly created event has a time conflict with other pre-existing events
	 * If the event does not, then it is added
	 * Otherwise, the method will not add it
	 * @param Event e
	 * @return true if event has been added, false otherwise
	 */
	public boolean addEvent(Event e){
		for(int i=0; i<events.size(); i++){
			Event temp = events.get(i);
			if(temp.getDate().equals(e.getDate())){
				if(temp.getHour() < e.getEndHour() && temp.getEndHour()> e.getEndHour()){
					return false;
				}
				if(temp.getHour()<e.getHour() && temp.getEndHour()>e.getHour()){
					return false;
				}
				if(temp.getHour() > e.getHour() && temp.getEndHour() < e.getEndHour()){
					return false;
				}
				if(temp.getHour() < e.getHour() && temp.getEndHour() > e.getEndHour()){
					return false;
				}
				if(temp.getHour() == e.getHour()){
					if(temp.getEndHour() == e.getHour()){
						if(temp.getEndMinute()> e.getMinute()){return false;}
						}
					else if(e.getMinute() >= temp.getMinute()){return false;}
				}
				if(temp.getHour() == e.getEndHour()){
					if(temp.getMinute() <= e.getEndMinute()){return false;}
				}
				if(temp.getEndHour() == e.getHour()){
					if(temp.getEndMinute() >= e.getMinute()){return false;}
				}
				if(temp.getEndHour() == e.getEndHour()){
					if(temp.getHour() == e.getEndHour()){
						if(temp.getMinute() <=e.getEndMinute()){return false;}
					}
					else if(temp.getEndMinute()>= e.getEndMinute()){return false;}
					else if(temp.getHour() > e.getHour()){return false;}
				}
			}
		}
		events.add(e);
		Collections.sort(events, new EventComparator());
		ChangeEvent event = new ChangeEvent(this);
		for(ChangeListener c : listeners){
			c.stateChanged(event);
		}
		return true;
	}
	/**
	 * adds a ChangeListener
	 * @param ChangeListener listener
	 */
	public void addChangeListener(ChangeListener listener){
		listeners.add(listener); 
	}
	/**
	 * Returns a string containing all of the stored events
	 * @return string of all events
	 */
	public String getString(){
		String s = "";
		for(Event e: events){
			s = s + e.getTitle() +  " " + e.getDate()+ " " + e.getTime() + " \n";
		}
		return s;
	}
	/**
	 * Returns a string of all the events of a given day
	 * @param int month
	 * @param int day
	 * @param int year
	 * @return string of the day's events
	 */
	public String getTodayEvents(int month, int day, int year){
		GregorianCalendar cal = new GregorianCalendar(year, month, day);
		DAYS[] arrayOfDays = DAYS.values();
		String s = arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1 ].toString() + " " +(month+1) +  "/" +day +  "/" + year+"\n" ;
		for(Event e: events){
			if(e.getMonth() == month+1 && e.getDay()==day && e.getYear()==year){
				s = s + e.getTitle() +  " "  + e.getTime() + " \n";
			}
		}
		return s;
	}
	/**
	 * Returns an array containing the events of a specified day
	 * @param month: between 0 and 11
	 * @param day: a valid day in the month
	 * @param year: a valid year
	 * @return ArrayList<Event>
	 */
	public ArrayList<Event> getTodayArray(int month, int day, int year){
		today  = new ArrayList<Event>();
		for(Event e: events){
			if(e.getMonth() == month+1 && e.getDay()==day && e.getYear()==year){
				today.add(e);
			}
		}
		return today;
	}
	/**
	 * Returns the arraylist of all the events
	 * @return arraylist of events
	 */
	public ArrayList<Event> getEvents(){
		return events;
	}

}
