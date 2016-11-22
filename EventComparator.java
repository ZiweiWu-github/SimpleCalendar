import java.util.Comparator;
/**
 * CS151 HW2 Solution
 * @author Ziwei Wu
 * @version 1.0
 */
/** This class is used to sort the events by date and starting time*/
public class EventComparator implements Comparator<Event>{
	public int compare(Event event1, Event event2) {
		if(event1.getYear()>event2.getYear()){return 1;}
		if(event1.getYear()< event2.getYear()){return -1;}
		if(event1.getYear() == event2.getYear()){
			if(event1.getMonth() > event2.getMonth()){return 1;}
			if(event1.getMonth() < event2.getMonth()){return -1;}
			if(event1.getMonth() == event2.getMonth()){
				if(event1.getDay() > event2.getDay()){return 1;}
				if(event1.getDay() < event2.getDay()){return -1;}
				if(event1.getDay() == event2.getDay()){
					if(event1.getHour()>event2.getHour()){return 1;}
					if(event1.getHour()<event2.getHour()){return -1;}
					if(event1.getHour() == event2.getHour()){
						if(event1.getMinute() > event2.getMinute()){ return 1;}
						if(event1.getMinute()<event2.getMinute()){return -1;}
					}
				}
			}
		}
		return 0;
	}

}
