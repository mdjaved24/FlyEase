package FlyEase;
import java.sql.*;
import java.util.Scanner;
import java.util.Random;

class UserManager{
    static final String url = "jdbc:mysql://localhost:3306/flight_db";
    static final String username = "root";
    static final String password = "Javed24@";
    static Scanner sc = new Scanner(System.in);
    public boolean isAuthenticated = false;  // flag to check if user is authenticated
    public String loginUsername;

    //LogIn
    public boolean login(){
        System.out.println("\nLogin:");
        System.out.print("Username: ");
        String myUsername = sc.next();
        System.out.print("Password: ");
        String myPassword = sc.next();

        loginUsername = myUsername;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password); 

            String query = "SELECT*FROM Users WHERE username=? AND password=?";
            PreparedStatement psmt = con.prepareStatement(query);
            psmt.setString(1, myUsername);
            psmt.setString(2, myPassword);
            ResultSet rs = psmt.executeQuery();

            if(rs.next()){
                if(rs.getString("username").equals(myUsername) && rs.getString("password").equals(myPassword)){
                    System.out.println("Login Successful");
                    isAuthenticated = true;
                }
            }else{
                System.out.println("Invalid Credentials! Please check Username or Password");
            }

            rs.close();
            psmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //Sign Up
    public boolean signup(){
        System.out.println("\nSignup(Please enter your details below):");
        sc.nextLine();
        System.out.print("Name: ");
        String name=sc.nextLine();
        System.out.print("Username: ");
        String newUsername=sc.next();
        System.out.print("Password: ");
        String newPassword=sc.next();
        System.out.print("Email: ");
        String email=sc.next();
        System.out.print("Phone no: ");
        String phone=sc.next();
        loginUsername = newUsername;
        try{
        Connection con = DriverManager.getConnection(url, username, password);
        String query = "INSERT INTO Users(Name, username, password, email, phone_no) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement psmt = con.prepareStatement(query);
        psmt.setString(1, name);
        psmt.setString(2, newUsername);
        psmt.setString(3, newPassword);
        psmt.setString(4, email);
        psmt.setString(5, phone);

        int rowsAffected = psmt.executeUpdate();
       

        if(rowsAffected>0){
            System.out.println("\nUser Created Successfully!");
            isAuthenticated = true;
            return true;
        }else{
           System.out.println("\nError creating  user! User may already exist."); 
        }
            psmt.close();
            con.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
      return false;
    }
}

class flight_booking {
 
    static Scanner sc = new Scanner(System.in);
    static String loginUsername;
    static boolean isAuthenticated = false;
    
    // ----------------------------------------------
    // Main Method
    public static void main(String[] args) {

        //Create object of UserManager Class
        UserManager userobj = new UserManager();

        boolean running = true;
        System.out.println("Welcome to Flight Booking System!");
        while(running) {
        if(!isAuthenticated){
            System.out.print("\n 1.Login \n 2.Signup \n 3.Exit");
            System.out.print("\n Enter your choice: ");
            int ch = sc.nextInt();
            sc.nextLine();
            switch(ch) {
                case 1:
                 userobj.login();
                 loginUsername = userobj.loginUsername;
                 isAuthenticated = userobj.isAuthenticated;
                  break;  

                case 2: 
                userobj.signup();  
                loginUsername = userobj.loginUsername;
                isAuthenticated = userobj.isAuthenticated;
                break;

                case 3:
                System.out.println("\nThank you for using the application!"); 
                running=false; break;

                default:  
                System.out.println("\nInvalid Choice!");
                         break;
            }
         }
         if(isAuthenticated){
            System.out.print("\n 1.Search a flight \n 2.Book a flight \n 3.My Bookings \n 4.Log Out \n");
            System.out.print("\n Enter your choice: ");
            int opt = sc.nextInt();
            sc.nextLine();
            switch (opt) {
                case 1:
                System.out.print("Please enter the source: ");
                String source = sc.nextLine();
                System.out.print("Please enter the destination: ");
                String destination = sc.nextLine();
                searchflight(source, destination);
                    break;
            
                case 2:
                System.out.print("Please enter the source: ");
                String source1 = sc.nextLine();
                System.out.print("Please enter the destination: ");
                String destination1 = sc.nextLine();
                bookflight(source1, destination1, loginUsername);
                    break;
            
                case 3:
                 myBookings(loginUsername);
                    break;

                case 4:
                isAuthenticated = false;
                break;
            
                default:
                System.out.println("\nInvalid Input!");
                    break;
            }
         }
        }  
    }

    // -------------------------------------------------------------
    //Search Flight
    static boolean searchflight(String source, String destination){
       
        try{
            Connection con = DriverManager.getConnection(UserManager.url, UserManager.username, UserManager.password);
            String query = "SELECT  * FROM Flights WHERE source = ? AND destination = ?";
            PreparedStatement psmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psmt.setString(1, source);
            psmt.setString(2, destination);

            ResultSet rs = psmt.executeQuery();
            
            if(rs.next()){
                rs.beforeFirst();
            while (rs.next()) {
            //Retrieve flight details
            int flightID = rs.getInt("flight_no");
            String flightname = rs.getString("flight_name");
            String flightSource = rs.getString("source");
            String flightDestination = rs.getString("destination");
            String departure_time = rs.getString("departure_time");
            String arrival_time = rs.getString("arrival_time");
            int rate = rs.getInt("rate");

            //Display available flight details
            System.out.println("\nFlight ID: "+flightID);
            System.out.println("Flight Name: "+flightname);
            System.out.println("Source: "+flightSource);
            System.out.println("Destination: "+flightDestination);
            System.out.println("Departure Time: "+departure_time);
            System.out.println("Arrival Time: "+arrival_time);
            System.out.println("Rate: "+rate+"Rs/person");
            return true;
            }
        }else{
            System.out.println("Sorry, There are no flights between "+source+" and "+destination);
        }
            rs.close();
            psmt.close();
            con.close();
    } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    // ---------------------------------------------
    //Book Flight
    static void bookflight(String source, String destination, String username){

        if(searchflight(source, destination)){
       searchflight(source, destination);
       System.out.print("\nEnter Flight ID: ");
       int flight_id = sc.nextInt();
       System.out.print("Number of Tickets: ");
       int no_Of_Tickets = sc.nextInt();
       System.out.print("Enter Date(dd-mm-yyyy): ");
       String booking_date = sc.next();
        Random random = new Random();
        int seatNumber = random.nextInt(99);

       try{
        Connection con = DriverManager.getConnection(UserManager.url, UserManager.username, UserManager.password);
        String bookQuery = "INSERT INTO Bookings(username, flight_id, booking_date, tickets, seat_no) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstStatement = con.prepareStatement(bookQuery);
        pstStatement.setString(1, loginUsername);
        pstStatement.setInt(2, flight_id);
        pstStatement.setString(3, booking_date);
        pstStatement.setInt(4, no_Of_Tickets);
        pstStatement.setInt(5, seatNumber);

        int rowsAffected = pstStatement.executeUpdate();
        
        if(rowsAffected>0){
            System.out.println("Booking Successful!");
        }
        else{
            System.out.println("Error booking your flight!");
        }
            pstStatement.close();
            con.close();
    } catch(SQLException e){
        System.out.println(e.getMessage());
    }
   }
  }

//   -----------------------------------------
  //My Bookings
  static void myBookings(String loginUsername){
    try{
        Connection con = DriverManager.getConnection(UserManager.url, UserManager.username, UserManager.password);
        String query = "SELECT*FROM Flights INNER JOIN Bookings ON Flights.flight_no = Bookings.flight_id WHERE Bookings.username = ?";
        PreparedStatement prepsmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        prepsmt.setString(1, loginUsername);

        ResultSet rSet = prepsmt.executeQuery();

        if(rSet.next()){
            rSet.beforeFirst();
            while (rSet.next()) {
                //Retrieve Booking details
                int  bookId = rSet.getInt("booking_id");
                int flightID = rSet.getInt("flight_no");
                String flightname = rSet.getString("flight_name");
                String flightSource = rSet.getString("source");
                String flightDestination = rSet.getString("destination");
                String departure_time = rSet.getString("departure_time");
                String arrival_time = rSet.getString("arrival_time");
                String booking_date = rSet.getString("booking_date");
                int tickets =rSet.getInt("tickets");
                int rate =rSet.getInt("rate");
                int seat = rSet.getInt("seat_no");

                int amount = rate*tickets;
        
        // Display Booking details
        System.out.println("\nBooking ID: "+bookId+"\nFlight ID: "+flightID+"\nFlight Name: "+flightname+"\nSource: "+flightSource+"\nDestination:"+flightDestination+"\nDeparture time: "+departure_time+"\nArrival time"+arrival_time+"\nDate:"+booking_date+"\nNo of Tickets: "+tickets+ "\nAmount: Rs"+amount+"\nSeat no: "+seat);

    } //end of while loop

        //Cancel Booking
        while(true){
        System.out.println("\nDo you want to cancel a booking(yes/no)?");
        String cncl = sc.next();
        if(cncl.equalsIgnoreCase("yes")){
            System.out.print("\nEnter Booking ID: ");
            int bookingID = sc.nextInt();
            cancelBooking(bookingID);
            break;
        }
        else if(cncl.equalsIgnoreCase("no")){
            break;
        }
        else{
            System.out.println("Invalid input!");
        }
    }
  }
  else{
    System.out.println("You have no bookings currently!");
  }

            rSet.close();
            prepsmt.close();
            con.close();

    } catch(SQLException e){
        System.out.println(e.getMessage());
    }
  }

//   -----------------------------------------------
  //Cancel Booking
  static void  cancelBooking(int booking_id){
    try{
        Connection con = DriverManager.getConnection(UserManager.url, UserManager.username, UserManager.password);
        String cancelQuery = "DELETE FROM Bookings WHERE  booking_id=?";
        PreparedStatement prepStmt = con.prepareStatement(cancelQuery);
        prepStmt.setInt(1, booking_id);

        int rowsAffected = prepStmt.executeUpdate();
        
        if(rowsAffected>0){
            System.out.println("Booking Cancelled!");
        }
        else{
            System.out.println("Error cancelling your flight!");
        }
            prepStmt.close();
            con.close();
    } catch(SQLException e){
        System.out.println(e.getMessage());
    }
  }

} //Class end

