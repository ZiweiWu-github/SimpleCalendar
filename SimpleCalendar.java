import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
enum DAYS
{
	Sun("Sunday"), 
	Mon("Monday"), 
	Tue("Tuesday"), 
	Wed("Wednesday"), 
	Thur("Thursday"), 
	Fri("Friday"), 
	Sat("Saturday") ;
	private String day;
	private DAYS(String d){
		day = d;
	}
	public String toString(){return this.day;}
}
/**
 * 
 * @author Ziwei Wu, HW #4 Answer
 * @version 1.0
 * 
 *
 */
public class SimpleCalendar{
    static JLabel lblMonth, lblDate;
    static JButton btnPrev, btnNext, create, quit, mTime, timeNote;
    static JTable tblCalendar, tblDay;
    static JFrame frmMain;
    static Container pane;
    static DefaultTableModel mtblCalendar, mtblDay; //Table model
    static JScrollPane stblCalendar, showEventsScroll, stblDay; //The scrollpane
    static JPanel pnlCalendar, eventPanel, mTimePanel, timePanel;
    static int realYear, realMonth, realDay, currentYear, currentMonth;
    static JTextField date, startTime, endTime, eventName;
    static JTextArea showEvents;
    static GregorianCalendar cal;
    static EventHandler events;
    static Object[] columnNames = {"Time", "Events"};
    static ArrayList<Event> eve;
	static DAYS[] arrayOfDays;
    static int i;
	
    public static void main (String args[]) throws FileNotFoundException{
        //Look and feel
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        arrayOfDays = DAYS.values();
        i = 0;
        //Prepare frame
        frmMain = new JFrame ("Simple Calendar"); //Create frame
        frmMain.setSize(1200, 375); 
        pane = frmMain.getContentPane(); //Get content pane
        pane.setLayout(null); //Apply null layout
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close when X is clicked
        events = new EventHandler();
    	timeNote = new JButton("A small note when reading the day view");
    	
        //create JTextFields
        date = new JTextField(10);
        startTime = new JTextField(10);
        endTime = new JTextField(10);
        eventName = new JTextField("Untitled Event", 10);
        showEvents = new JTextArea();
        showEventsScroll = new JScrollPane(showEvents);
        
        //What on earth is military time, you may ask. This panel explains it.
        mTime = new JButton("What is military time?");
        mTimePanel = new JPanel();
        mTimePanel.setLayout(new GridLayout(5,1));
        mTimePanel.add(new JLabel("Military time uses a 24 hour day from 0:00 to 23:59 (no a.m. or p.m.)"));
        mTimePanel.add(new JLabel("12:00 a.m. is 0:00 and 12:00 p.m. is just 12:00"));
        mTimePanel.add(new JLabel("For any hour after 12:00 p.m., just add 12 (ex. 1:00 p.m. = 13:00)"));
        mTimePanel.add(new JLabel("If you still need help, the rightmost day view has both military and civilian times"));
        mTimePanel.add(new JLabel("For the purposes of this calender please only go from 0:00 to 23:59"));
        
        //The small note panel
        timePanel = new JPanel();
        timePanel.setLayout(new GridLayout(6,1));
        timePanel.add(new JLabel("Each row represents 30 minutes"));
        timePanel.add(new JLabel("So the row of 10:30 is actually from 10:30 to 11:00"));
        timePanel.add(new JLabel("Hope that clears up any confusion for you"));
        timePanel.add(new JLabel("For the colors, each event has its own color for the time it takes"));
        timePanel.add(new JLabel("Colors go from pink to yellow to pink..."));
        timePanel.add(new JLabel("The brown color means that the row has 2 events on it"));
        
        //the panel that pops up to create an event
        eventPanel = new JPanel();
        eventPanel.setLayout(new GridLayout(8,2));
        eventPanel.add(new JLabel("Event Name:"));
        eventPanel.add(eventName);
        eventPanel.add(new JLabel("Date in MM/DD/YYYY:"));
        eventPanel.add(date);
        eventPanel.add(new JLabel("Starting Time in HH:MM (Military Time):"));
        eventPanel.add(startTime);
        eventPanel.add(new JLabel("Ending Time in HH:MM (Military Time):"));
        eventPanel.add(endTime);
        eventPanel.add(mTime);
        eventPanel.add(new JLabel("This calendar can only go from 0:00 to 23:59 for each day"));
        
        //Create controls
        lblMonth = new JLabel ("January");
        lblDate = new JLabel();
        btnPrev = new JButton ("<");
        btnNext = new JButton (">");
        create = new JButton("Create");
        quit = new JButton("Quit");
        mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        mtblDay = new DefaultTableModel(columnNames, 48){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
        tblCalendar = new JTable(mtblCalendar);
        tblDay = new JTable(mtblDay);
        stblDay = new JScrollPane(tblDay);
        stblCalendar = new JScrollPane(tblCalendar);
        pnlCalendar = new JPanel(null);
		
        
        
        //Set border
        pnlCalendar.setBorder(BorderFactory.createTitledBorder("Calendar"));
        
        //Register action listeners
        btnPrev.addActionListener(new btnPrev_Action());
        btnNext.addActionListener(new btnNext_Action());
        timeNote.addActionListener(new timeWhat());
        mTime.addActionListener(new mTimeWhat());
        create.addActionListener(new create_Action());
        quit.addActionListener(new quit_Action());
        
        //Add controls to pane
        pane.add(pnlCalendar);
        pnlCalendar.add(lblMonth);
        pnlCalendar.add(btnPrev);
        pnlCalendar.add(btnNext);
        pnlCalendar.add(create);
        pnlCalendar.add(stblCalendar);
        pnlCalendar.add(stblDay);
        pnlCalendar.add(showEventsScroll);
        pnlCalendar.add(quit);
        pnlCalendar.add(lblDate);
        pnlCalendar.add(timeNote);
        
        
        
        //Set bounds
        pnlCalendar.setBounds(0, 0, 1190, 335);
        lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 100, 25);
        btnPrev.setBounds(10, 25, 50, 25);
        btnNext.setBounds(260, 25, 50, 25);
        create.setBounds(120,0,65,25);
        stblCalendar.setBounds(10, 50, 300, 250);
        stblDay.setBounds(750, 50, 400, 250);
        showEventsScroll.setBounds(320, 50, 400,250);
        quit.setBounds(320, 0, 70, 25);
        lblDate.setBounds(750, 20, 200, 30);
        timeNote.setBounds(900, 20, 250,30);
        
        //Make frame visible
        frmMain.setResizable(false);
        frmMain.setVisible(true);
        
        //Get real month/year
        cal = new GregorianCalendar(); //Create calendar
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
        realMonth = cal.get(GregorianCalendar.MONTH); //Get month
        realYear = cal.get(GregorianCalendar.YEAR); //Get year
        currentMonth = realMonth; //Match month and year
        currentYear = realYear;
        lblDate.setText(arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1 ].toString() + " " +(realMonth+1) +  "/" +realDay + "/" +realYear);
        eve = events.getTodayArray(realMonth, realDay, realYear);
        date.setText((realMonth+1) + "/" + realDay + "/" + realYear);
        DAYS[] arrayOfDays = DAYS.values();
        showEvents.setText(arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1 ].toString() + " " +(realMonth+1) +  "/" +realDay + "/" + realYear);
        
        //Changes the day view when the calendar changes
        ChangeListener eventer = new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				showEvents.setText(events.getTodayEvents(realMonth, realDay, realYear));
				eve = events.getTodayArray(realMonth, realDay, realYear);
				setHours();
			}
        };
        events.addChangeListener(eventer);
        
        //load up events.txt if it exists
        File f = new File("events.txt");
		if(f.exists()){
			Scanner s = new Scanner(new BufferedReader(new FileReader("events.txt")));
			while(s.hasNextLine()){
				String event = s.nextLine();
				String[] eventing = event.split("-");
				if(eventing.length == 3){
					String date = eventing[0].trim();
					String startTime = eventing[1].trim();
					String title = eventing[2].trim();
					Event temp = new Event(title, date, startTime, "");
					events.addEvent(temp);
				}
				if(eventing.length == 4){
					String date = eventing[0].trim();
					String startTime = eventing[1].trim();
					String endTime = eventing[2].trim();
					String title = eventing[3].trim();
					Event temp = new Event(title, date, startTime, endTime);
					events.addEvent(temp);
				}
			}
		}
        
		
        //Add headers
        String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}; //All headers
        for (int i=0; i<7; i++){
            mtblCalendar.addColumn(headers[i]);
        }
        
        tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background
        
        //No resize/reorder
        tblCalendar.getTableHeader().setResizingAllowed(false);
        tblCalendar.getTableHeader().setReorderingAllowed(false);
        
        //Single cell selection
        tblCalendar.setColumnSelectionAllowed(true);
        tblCalendar.setRowSelectionAllowed(true);
        tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Set row/column count
        tblCalendar.setRowHeight(38);
        mtblCalendar.setColumnCount(7);
        mtblCalendar.setRowCount(6);
        
     
        //Refresh calendar
        refreshCalendar (realMonth, realYear); 
        
        //Changes date when you click on the calendar
        tblCalendar.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		int row = tblCalendar.rowAtPoint(e.getPoint());
        		int column = tblCalendar.columnAtPoint(e.getPoint());
        		int j =  (int) tblCalendar.getValueAt(row, column);
        		cal = new GregorianCalendar(realYear, realMonth, j);
        		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
                realMonth = cal.get(GregorianCalendar.MONTH); //Get month
                realYear = cal.get(GregorianCalendar.YEAR); //Get year
                date.setText((realMonth+1) + "/" + realDay + "/" + realYear);
                currentMonth = realMonth; //Match month and year
                currentYear = realYear;
                eve = events.getTodayArray(realMonth, realDay, realYear);
                lblDate.setText(arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1 ].toString() + " " +(realMonth+1) +  "/" +realDay + "/" +realYear);
                showEvents.setText(events.getTodayEvents(realMonth, realDay, realYear));
                setHours();
                refreshCalendar(currentMonth, currentYear);
        	}
        });

       setHours();

    }
    
    //set up the hour count
    public static void setHours(){
    	for(int i=0; i<mtblDay.getRowCount(); i= i+2){
    		String s = Integer.toString(i/2) + ":00";
    		if(i/2>=12){
    			if(i/2 ==12){s = s +  " or 12:00 p.m.";}
    			else{s = s + " or " + ((i/2)-12) + ":00 p.m.";} 
    		}
    		if(i/2 == 0){s  = s + " or 12:00 a.m.";}
    		else if(i/2 <12){s = s + " or " + i/2 + ":00 a.m.";}
    		mtblDay.setValueAt(s, i, 0);
    		
    	}
    	for(int i=1; i<mtblDay.getRowCount(); i= i+2){
    		String s = Integer.toString(i/2) + ":30";
    		if(i/2>=12){
    			if(i/2 ==12){s = s +  " or 12:30 p.m.";}
    			else{s = s + " or " + ((i/2)-12) + ":30 p.m.";} 
    		}
    		if(i/2 == 0){s  = s + " or 12:30 a.m.";}
    		else if(i/2 <12){s = s + " or " + i/2 + ":30 a.m.";}
    		mtblDay.setValueAt(s, i, 0);
    	}
    	for(int i =0; i<mtblDay.getRowCount(); i++){
    		mtblDay.setValueAt(null, i, 1);
    	}
    	
    	for(int i =0; i<mtblDay.getRowCount(); i++){
    		for(Event e: eve){
    			String s = e.getTitle();
    			if(e.containsHour(i)){mtblDay.setValueAt(s, i, 1);}
    		}
    	}
    	tblDay.setDefaultRenderer(tblDay.getColumnClass(0), new tblDayRenderer());
    	
    }
    
    //renders the day view
    static class tblDayRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
           super.getTableCellRendererComponent(table, value, selected, focused, row, column);
           setBackground(new Color(255, 255, 255));
           i = 0;
          		for(Event e: eve){
          			if(e.containsHour(row)){
          				if(i%2==0){
          					if(getBackground().equals(new Color(255, 255, 102))){
          	            		setBackground(new Color(205,133,63));
          	            	}
          					else{setBackground(new Color(255, 220, 220));}
          				}
          				if(i%2==1){
          					if(getBackground().equals(new Color(255, 220, 220))){
          	            		setBackground(new Color(205,133,63));
          	            	}
          					else{setBackground(new Color(255,255,102));}
          				}
          				
          				}
          			i++;
          	   }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }
    
    public static void refreshCalendar(int month, int year){
        //Variables
        String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int nod, som; //Number Of Days, Start Of Month
        
        //Allow/disallow buttons
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
       // if (month == 0 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
        //if (month == 11 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
        lblMonth.setText(months[month] + " "+realYear); //Refresh the month label (at the top)
        lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
        
        
        //Clear table
        for (int i=0; i<6; i++){
            for (int j=0; j<7; j++){
                mtblCalendar.setValueAt(null, i, j);
            }
        }
        
        //Get first day of month and number of days
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        som = cal.get(GregorianCalendar.DAY_OF_WEEK);
        
        //Draw calendar
        for (int i=1; i<=nod; i++){
            int row = new Integer((i+som-2)/7);
            int column  =  (i+som-2)%7;
            mtblCalendar.setValueAt(i, row, column);
        }
        
        //Apply renderers
        tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
    }
    
    static class tblCalendarRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            if (column == 0 || column == 6){ //Week-end
                setBackground(new Color(255, 220, 220));
            }
            else{ //Week
                setBackground(new Color(255, 255, 255));
            }
            if (value != null){
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear){ //Today
                    setBackground(new Color(220, 220, 255));
                }
            }
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }
    
    //changes current day to previous when clicked
    static class btnPrev_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	cal.add(Calendar.DATE, -1);
        	realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
            realMonth = cal.get(GregorianCalendar.MONTH); //Get month
            realYear = cal.get(GregorianCalendar.YEAR); //Get year
            currentMonth = realMonth; //Match month and year
            currentYear = realYear;
            date.setText((realMonth+1) + "/" + realDay + "/" + realYear);
            showEvents.setText(events.getTodayEvents(realMonth, realDay, realYear));
            eve = events.getTodayArray(realMonth, realDay, realYear);
            lblDate.setText(arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1 ].toString() + " " +(realMonth+1) +  "/" +realDay + "/" +realYear);
            setHours();
            refreshCalendar(currentMonth, currentYear);
        }
    }
    //changes current day to next when clicked
    static class btnNext_Action implements ActionListener{
        public void actionPerformed (ActionEvent e){
        	cal.add(Calendar.DATE, 1);
        	realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
            realMonth = cal.get(GregorianCalendar.MONTH); //Get month
            realYear = cal.get(GregorianCalendar.YEAR); //Get year
            currentMonth = realMonth; //Match month and year
            currentYear = realYear;
            date.setText((realMonth+1) + "/" + realDay + "/" + realYear);
            showEvents.setText(events.getTodayEvents(realMonth, realDay, realYear));
            eve = events.getTodayArray(realMonth, realDay, realYear);
            lblDate.setText(arrayOfDays[cal.get(Calendar.DAY_OF_WEEK)-1 ].toString() + " " +(realMonth+1) +  "/" +realDay + "/" +realYear);
            setHours();
            refreshCalendar(currentMonth, currentYear);
        }
    }


    //for the create button
    static class create_Action implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(null, eventPanel, 
		               "Create new event", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				String dateofevent = date.getText();
				String nameofevent = eventName.getText();
				String timeofstart = startTime.getText();
				String timeofend = endTime.getText();
				Boolean added = events.addEvent(new Event(nameofevent, dateofevent, timeofstart, timeofend));
				startTime.setText("");
				endTime.setText("");
				if(added == true){infoBox("Event successfully added","Event adding message");}
				if(added == false){infoBox("Error: time conflict!","Event adding message");}
			}
		}
    }
    
    //cheap method for creating a default message box
    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
    
    //quit button saves events and exits the system
    static class quit_Action implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		PrintWriter writer;
			try {
				writer = new PrintWriter("events.txt", "UTF-8");
				ArrayList<Event> eventing = events.getEvents();
				Collections.sort(eventing, new EventComparator());
				for(Event e1 :eventing){
					writer.println(e1.getDate() + "- " + e1.getTime() + "- " + e1.getTitle());
				}
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
    		System.exit(0);
    	}
    }
    
    //just for military time button
    static class mTimeWhat implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		JOptionPane.showMessageDialog(null, mTimePanel, 
		               "What is military time?", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
    //message box telling you why I do the things I do.
    static class timeWhat implements ActionListener{
    	public void actionPerformed(ActionEvent e){
    		JOptionPane.showMessageDialog(null, timePanel, 
		               "What each row means", JOptionPane.INFORMATION_MESSAGE);
    	}
    }
    
}