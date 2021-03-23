/*
    Theater Kiosk

    Author: Erik Rodriguez
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException {

        // Checking if the auditorium files work properly
        Auditorium A1 = null;
        Auditorium A2 = null;
        Auditorium A3 = null;
        try {
            A1 = new Auditorium(new File("A1.txt"));
            A2 = new Auditorium(new File("A2.txt"));
            A3 = new Auditorium(new File("A3.txt"));
        } catch (Exception e){
            System.out.println("There is something wrong with Auditorium files");
            System.out.println("Please check them and restart the program");
            System.exit(0);
        }

        Scanner entrada = new Scanner(System.in);

        File file = null;

        try {
            file = new File("userdb.dat"); // checking the file exists
        } catch (Exception e) {
            System.out.println("There is something wrong with user database file");
            System.out.println("Please check them and restart the program");
            System.exit(0);
        }

        Scanner scanFile = new Scanner(file);

        HashMap<String, Customer> users = new HashMap<>();

        while(scanFile.hasNextLine()){
            String info = scanFile.nextLine(); // this is the user name and password
            String[] details = info.split(" "); // split by space
            users.put(details[0], new Customer(details[0], details[1]));  // I put the username and password
        }

        boolean exit = false; // variable to exit the program


        do {
            System.out.println("Welcome to Los Pollos Hermanos theater"); // shows menu
            System.out.println();
            String username;
            // if the map contains the username continue
            do {
                System.out.print("Enter your username: ");
                username = entrada.nextLine();
            } while (!users.containsKey(username));
            int wrongCount = 0;
            while(wrongCount < 3){ // to ensure a user doesn't try to enter with more than 3 attempts
                System.out.print("Enter your password: ");
                String password = entrada.nextLine();
                Customer currentCustomer = users.get(username);
                if (currentCustomer.getPassword().equals(password)){
                    if (currentCustomer.getUsername().equals("admin")){ // if the admin is logging in
                        exit = AdminMenu(entrada, A1, A2, A3); // open admin menu
                    }
                    else
                    {
                        MainMenu(currentCustomer, entrada, A1,A2,A3); // open user menu
                    }
                    break;
                }
                else {
                    wrongCount++;
                    switch (wrongCount){ // playful message
                        case 1:
                            System.out.println("not quite right - Invalid password");
                            break;
                        case 2:
                            System.out.println("that would be incorrect - Invalid Password");
                            break;
                        case 3:
                            System.out.println("ight, just leave for me -_- Invalid Password");
                            break;
                    }

                }
            }

        } while (!exit); // exits if the Admin clicks exit

        A1.WriteReport("A1Final.txt"); // write the updated theaters
        A2.WriteReport("A2Final.txt");
        A3.WriteReport("A3Final.txt");
    }

    /**
     * This is the user menu
     * @param usr the current user
     * @param scan scanner
     * @param A1 auditorium 1
     * @param A2 auditorium 2
     * @param A3 auditorium 3
     */
    public static void MainMenu(Customer usr, Scanner scan, Auditorium A1, Auditorium A2, Auditorium A3){
        String x = "-1";

        System.out.println();
        while (!x.equals("5")){
            System.out.println("1. Reserve Seats");
            System.out.println("2. View Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Display Receipt");
            System.out.println("5. Log Out");
            x = scan.nextLine();
            switch(x){
                case "1":
                    Auditorium audit = SelectAuditorium(scan,A1,A2,A3); // select auditorium
                    ReserveSeats(usr, audit, scan); // reserve seats
                    System.out.println();
                    break;
                case "2":
                    if (usr.getOrders().isEmpty()) System.out.println("\nNo orders"); // if no orders are associated
                    else {
                        for (int i = 0; i < usr.getOrders().size(); i++){ // looping through all orders
                            usr.getOrders().get(i).viewOrder(A1, A2, A3); // view all orders
                            System.out.println();
                        }
                    }
                    break;
                case "3":
                    if (usr.getOrders().isEmpty()) System.out.println("\nNo orders"); // // if they have no orders
                    else UpdateOrder(usr,scan,A1,A2,A3); // update their order
                    break;
                case "4":
                    if (usr.getOrders().isEmpty()) System.out.println("\nCustomer Total: $0.00"); // if they have no orders
                    else {
                        usr.viewCustomerReceipt(A1, A2, A3); // show their current spending
                    }
                    break;
                case "5":
                    x = "5";
                    break;
                default:
                    System.out.println("Invalid number");
            }
        }
    }

    /**
     * Reserves seats in the given auditorium
     * @param usr customer
     * @param auditorium the selected auditorium
     */
    public static void ReserveSeats(Customer usr, Auditorium auditorium, Scanner scan){

        int row = -1;
        char seat = '0';
        int adults = 0, children = 0, seniors = 0;

        boolean validInput = false;

        while (!validInput){
            try {
                System.out.print("Enter the Row Number: ");
                row = Integer.parseInt(scan.nextLine());
                if (row >= 1 && row <= auditorium.getColCount()){
                    validInput = true;
                }
                else {
                    System.out.println("The row number is invalid");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            System.out.print("Enter the Seat Letter: ");
            String strInput = scan.nextLine().toUpperCase();
            try {
                seat = strInput.charAt(0);
                if (seat > '@' && seat <= (char)(auditorium.getColCount()+64) && strInput.length() == 1) {
                    validInput = true;
                } else
                {
                    System.out.println("Invalid seat selection");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            try {
                System.out.print("Enter the number of adults: ");
                adults = Integer.parseInt(scan.nextLine());
                if (adults >= 0 && adults <= auditorium.getColCount()){
                    validInput = true;
                }
                else {
                    System.out.println("The amount of tickets are unable to be reserved, check layout");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            try {
                System.out.print("Enter the number of children: ");
                children = Integer.parseInt(scan.nextLine());
                if (children >= 0 && adults+children <= auditorium.getColCount())
                {
                    validInput = true;
                }
                else {
                    System.out.println("The amount of tickets are unable to be reserved, check layout");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            try {
                System.out.print("Enter the number of seniors: ");
                seniors = Integer.parseInt(scan.nextLine());
                if (seniors >= 0 && adults+children+seniors <= auditorium.getColCount())
                {
                    validInput = true;
                }
                else {
                    System.out.println("The amount of tickets are unable to be reserved, check layout");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }

        int totalTicketCount = adults + children + seniors;
        String[] BestSeats = auditorium.BestAvailableSeat(totalTicketCount, auditorium.getRowCount(), auditorium.getColCount());

        if (auditorium.checkAvailability(row, seat, totalTicketCount)) { // if seats are available
            auditorium.ReserveSeats(row, seat, adults, children, seniors);
            usr.AddOrder(new Orders(auditorium, adults, children, seniors, row, seat));
        }
        else if(!BestSeats[0].equals("?")){ // we found seats
            String[] details =  auditorium.BestAvailableSeat(totalTicketCount, auditorium.getRowCount(), auditorium.getColCount());
            char BestSeat = details[1].charAt(0);
            int BestRow = Integer.parseInt(details[0]);
            char last = BestSeat;
            for(int i=0; i<(seniors+adults+children-1);i++){
                last++;
            }
            System.out.println("Seats you wanted are unavailable");

            while (true){
                if (BestSeat == last){
                    System.out.print("Would you like the best seat - |"+ BestRow +""+BestSeat +"|? type 'Y' for yes, 'N' for no :");
                }
                else {
                    System.out.print("Would you like best available seats instead");
                    System.out.print("- |"+BestRow+""+BestSeat+"-"+BestRow+""+last+"|? type 'Y' for yes, 'N' for no: ");
                }
                String userChoice = scan.nextLine().toUpperCase();
                if (userChoice.equals("Y")){
                    auditorium.ReserveSeats(BestRow, BestSeat, adults, children, seniors);
                    usr.AddOrder(new Orders(auditorium, adults, children, seniors, BestRow, BestSeat));
                    System.out.println("Tickets Reserved");
                    break;
                } else if (userChoice.equals("N")){
                    break;
                }
                else{
                    System.out.println("What was that again?");
                }
            }
        }
        else{
            System.out.println("no seats available");
        }
    }

    /**
     * Update Order Menu
     * @param usr current Customer
     * @param scan System.in
     * @param A1 Auditorium 1
     * @param A2 Auditorium 2
     * @param A3 Auditorium 3
     */
    public static void UpdateOrder(Customer usr, Scanner scan, Auditorium A1, Auditorium A2, Auditorium A3){

        for (int i=0; i<usr.getOrders().size();i++){
            System.out.println("Order #"+(i+1));  // display all current orders
            usr.getOrders().get(i).viewOrder(A1,A2,A3);
            System.out.println();
        }
        boolean inputIsValid = false;

        int orderIndex = 0;

        while (!inputIsValid) { //  validate selection

            try {

                System.out.print("Select Order: ");
                String orderNum = scan.nextLine();

                orderIndex = (Integer.parseInt(orderNum) - 1);
                if (orderIndex >= 0 && orderIndex < usr.getOrders().size()){
                    inputIsValid = true;
                }
                else{
                    System.out.println("Invalid order selection");
                }
                System.out.println();
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }


        boolean isTaskDone = false;

        while (!isTaskDone){ // loop until they do something to their order
            System.out.println("1. Add tickets to Order");
            System.out.println("2. Delete ticket from order");
            System.out.println("3. Cancel Order");

            int x;

            while (true) {
                try {
                    String MenuChoice = scan.nextLine();
                    x = Integer.parseInt(MenuChoice);
                    break;
                } catch (Exception e){
                    System.out.println("Select valid menu choice");
                }
            }


            switch (x) {
                case 1: // add
                    isTaskDone = AddTicketsToOrder(usr,orderIndex,scan);
                    break;
                case 2: // delete, if they delete all tickets it has the same effect as cancel
                    isTaskDone = DeleteTicket(usr,orderIndex,scan);
                    break;
                case 3: // cancel
                    isTaskDone = CancelOrder(usr, orderIndex);
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }


    }

    /**
     * Takes current order and adds tickets to it
     * @param user Customer
     * @param orderIndex Location of order in the order array
     * @param scan scanner
     * @return true if operation was completed successfully
     */
    public static boolean AddTicketsToOrder(Customer user, int orderIndex, Scanner scan) {

        Auditorium auditorium = user.getOrders().get(orderIndex).getAuditorium();

        int row = -1;
        char seat = '0';
        int adults = 0, children = 0, seniors = 0;

        boolean validInput = false;

        auditorium.showLayout(auditorium.getColCount(), auditorium.getRowCount());

        while (!validInput){
            try {
                System.out.print("Enter the Row Number: "); // validate row
                row = Integer.parseInt(scan.nextLine());
                if (row >= 1 && row <= auditorium.getColCount()){
                    validInput = true;
                }
                else {
                    System.out.println("The row number is invalid");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            System.out.print("Enter the Seat Letter: "); // validate seat
            String strInput = scan.nextLine().toUpperCase();
            try {
                seat = strInput.charAt(0);
                if (seat > '@' && seat <= (char)(auditorium.getColCount()+64) && strInput.length() == 1) {
                    validInput = true;
                } else
                {
                    System.out.println("Invalid seat selection");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            try {
                System.out.print("Enter the number of adults: "); // validate adult tickets
                adults = Integer.parseInt(scan.nextLine());
                if (adults >= 0 && adults <= auditorium.getColCount()){
                    validInput = true;
                }
                else {
                    System.out.println("The amount of tickets are unable to be reserved, check layout");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            try {
                System.out.print("Enter the number of children: "); // validate child tickets
                children = Integer.parseInt(scan.nextLine());
                if (children >= 0 && adults+children <= auditorium.getColCount())
                {
                    validInput = true;
                }
                else {
                    System.out.println("The amount of tickets are unable to be reserved, check layout");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        validInput = false;
        while (!validInput){
            try {
                System.out.print("Enter the number of seniors: "); // validate senior tickets
                seniors = Integer.parseInt(scan.nextLine());
                if (seniors >= 0 && adults+children+seniors <= auditorium.getColCount())
                {
                    validInput = true;
                }
                else {
                    System.out.println("The amount of tickets are unable to be reserved, check layout");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }

        int totalTicketCount = children + adults + seniors;

        if (auditorium.checkAvailability(row, seat, totalTicketCount)) {
            auditorium.ReserveSeats(row, seat, adults, children, seniors); // reserves seats in auditorium
            for (int i=0; i<totalTicketCount; i++){ // adds seat to array list
                String newSeat = row+""+seat;
                seat++;
                user.getOrders().get(orderIndex).getSeatList().add(newSeat);
            }
            Collections.sort(user.getOrders().get(orderIndex).getSeatList()); // sorting seat list

            user.getOrders().get(orderIndex).addAdultTickets(adults); // updating ticket counts
            user.getOrders().get(orderIndex).addSeniorTickets(seniors);
            user.getOrders().get(orderIndex).addChildTickets(children);

            System.out.println("Tickets successfully added to order");
            System.out.println();

            return true;
        } else {
            System.out.println("seats not available"); // if the seats are taken
            return false;
        }
    }




    /**
     * Deletes a ticket from the customer order
     * @param user user
     * @param orderIndex the location of the order inside the orders array
     * @param scan scanner object
     * @return true if task was completed, false if it failed
     */
    public static boolean DeleteTicket(Customer user, int orderIndex, Scanner scan){
        Auditorium audit = user.getOrders().get(orderIndex).getAuditorium(); // auditorium where order is stored
        Orders order = user.GetOrder(orderIndex); // order
        boolean isInput = false;

        int rowNum = -1;

        System.out.println("Ok, lets delete the ticket from your selected order");
        while (!isInput){
            System.out.print("Enter the row number of the ticket: ");
            try {
                String rowNumber = scan.nextLine();
                rowNum = Integer.parseInt(rowNumber);
                if (rowNum <= 0 && rowNum > audit.getRowCount()){
                    System.out.println("Invalid row number");
                }
                else {
                    isInput = true;
                }
            }
            catch (Exception e) {
                System.out.println("Invalid input");
            }
        }

        char seat = '@';

        isInput = false;
        while (!isInput){
            System.out.print("Now the Seat Letter: ");
            String strInput = scan.nextLine().toUpperCase();
            try {
                seat = strInput.charAt(0);
                if (seat > '@' && seat <= (char)(audit.getColCount()+64) && strInput.length() == 1) {
                    isInput = true;
                } else
                {
                    System.out.println("Invalid seat selection");
                }
            } catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
        String seatToDelete = rowNum +""+ seat; // we will delete from the order
        char seatType = audit.DeleteTicket(rowNum,seat); // deletes and returns the type of ticket

        for(int i=0; i < order.getSeatList().size(); i++) {
            if (order.getSeatList().get(i).equals(seatToDelete)){

                switch (seatType){
                    case 'A':
                        user.getOrders().get(orderIndex).adultminusone();
                        break;
                    case 'C':
                        user.getOrders().get(orderIndex).childminusone();
                        break;
                    case 'S':
                        user.getOrders().get(orderIndex).seniorminusone();
                        break;
                    default:
                        System.out.println("Deletion unsuccessful");
                }
                user.getOrders().get(orderIndex).getSeatList().remove(i); // gets order from user, gets the seats deletes seat


                if (order.getSeatList().isEmpty()) { // delete the order if the seat list is empty
                    CancelOrder(user, orderIndex);
                }

                System.out.println("Ticket successfully deleted");
                System.out.println();

                return true;
            }
        }
        System.out.println("That ticket doesn't exist in the order");
        return false;
    }


    /**
     * Cancels customer order
     * @param user user
     * @param orderIndex location of order inside the array
     * @return true when completed
     */
    public static boolean CancelOrder(Customer user,int orderIndex){
        Auditorium audit = user.getOrders().get(orderIndex).getAuditorium();
        Orders order = user.GetOrder(orderIndex);
        for (int i=0;i<order.seatList.size();i++){ // delete each seat
            String seat = user.GetOrder(orderIndex).seatList.get(i);
            audit.DeleteTicket(Character.getNumericValue(seat.charAt(0)),seat.charAt(1)); // seats are formatted as EX: 1A, 3B
        }
        user.getOrders().get(orderIndex).ClearOrderAndAudit();
        user.getOrders().remove(orderIndex); // get arraylist remove index of order

        return true;
    }

    /**
     * Menu to select auditorium
     * @param scan scanner object
     * @param A1 auditorium 1
     * @param A2 auditorium 2
     * @param A3 auditorium 3
     * @return the selected auditorium
     */
    public static Auditorium SelectAuditorium(Scanner scan, Auditorium A1, Auditorium A2, Auditorium A3) {

        while (true) { // validate input
            try {
                System.out.println("Select Auditorium Below");
                System.out.println("\n1. Auditorium #1");
                System.out.println("2. Auditorium #2");
                System.out.println("3. Auditorium #3 ");
                String audit = scan.nextLine();
                int choice = Integer.parseInt(audit);
                switch (choice) {
                    case 1:
                        A1.showLayout(A1.getColCount(),A1.getRowCount());
                        return A1;
                    case 2:
                        A2.showLayout(A2.getColCount(),A2.getRowCount());
                        return A2;
                    case 3:
                        A3.showLayout(A3.getColCount(),A3.getRowCount());
                        return A3;
                    default:
                        System.out.println("Wrong input");
                }

            } catch (Exception e) {
                System.out.println("Invalid Input");
            }
        }
    }

    /**
     * Shows Admin UI
     * @param entrada scanner
     * @return true if he logs out, false if he terminates program
     */
    public static boolean AdminMenu(Scanner entrada, Auditorium A1, Auditorium A2, Auditorium A3)
    {

        int menuChoice;

        while (true)
        {
            try {
                System.out.println("1. Print Report");
                System.out.println("2. Log out");
                System.out.println("3. Exit");
                menuChoice = Integer.parseInt(entrada.nextLine());
                switch (menuChoice){
                    case 1:
                        PrintReport(A1, A2, A3); // prints auditorium summaries
                        break;
                    case 2: // returns back to main menu
                        return false;
                    case 3: // terminates the program
                        return true;
                    default:
                        System.out.println("Wrong Menu Choice");
                }
            } catch (Exception e) {
                System.out.println("Invalid Input");
            }

        }

    }

    /**
     * Prints the Current Report of all Auditoriums
     * @param A1 auditorium 1
     * @param A2 auditorium 2
     * @param A3 auditorium 3
     */
    public static void PrintReport(Auditorium A1, Auditorium A2, Auditorium A3) throws FileNotFoundException {
        float[] a1 = A1.GetReport();
        float[] a2 = A2.GetReport();
        float[] a3 = A3.GetReport();

        // this prints the stats of all auditoriums
        System.out.println();
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d","Auditorium 1",(int)a1[1],(int)a1[2],(int)a1[3],(int)a1[4],(int)a1[5]);
        System.out.printf("\t$%.2f%n",a1[6]);
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d","Auditorium 2",(int)a2[1],(int)a2[2],(int)a2[3],(int)a2[4],(int)a2[5]);
        System.out.printf("\t$%.2f%n",a2[6]);
        System.out.printf("%s\t%d\t%d\t%d\t%d\t%d","Auditorium 3",(int)a3[1],(int)a3[2],(int)a3[3],(int)a3[4],(int)a3[5]);
        System.out.printf("\t$%.2f%n",a3[6]);

        // the sum of all stats for each category
        int open = (int)(a1[1] + a2[1] + a3[1]);
        int reserved = (int) (a1[2] + a2[2] + a3[2]);
        int aticks = (int) (a1[3] + a2[3] + a3[3]);
        int cticks = (int) (a1[4] + a2[4] + a3[4]);
        int sticks = (int) (a1[5] + a2[5] + a3[5]);
        float sales = a1[6] + a2[6] + a3[6];

        System.out.printf("%s\t\t%d\t%d\t%d\t%d\t%d\t$%.2f","Total",open,reserved,aticks,cticks,sticks,sales);
        System.out.println("\n");
    }
}

/*
The Customer class will have information
based on the order of each user
 */
class Customer
{
    ArrayList<Orders> orders = new ArrayList<>();
    private String password;
    private String username;

    Customer(){}
    Customer(String username,String password){
        this.username = username;
        this.password = password;
    }
    public String getUsername() { return username; }
    public ArrayList<Orders> getOrders() {
        return orders;
    }
    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password; }

    /**
     * Prints all the order receipts and the total of all orders
     * @param A1 auditorium 1
     * @param A2 auditorium 2
     * @param A3 auditorium 3
     */
    public void viewCustomerReceipt(Auditorium A1, Auditorium A2, Auditorium A3){
        float[] priceOfOrders = new float[orders.size()];
        for (int i =0; i < orders.size(); i++) { // stores all prices of each order
            priceOfOrders[i] = orders.get(i).ViewOrderReceipt(A1, A2, A3);
        }

        float totalOfAll = 0f;
        for (float priceOfOrder : priceOfOrders) { // runs through the prices and adds them up
            totalOfAll = totalOfAll + priceOfOrder;
        }

        System.out.printf("%s%.2f%n","Customer Total: $", totalOfAll);
        System.out.println();

    }


    public void AddOrder(Orders order)
    {
        orders.add(order);
    }

    /**
     * Returns the order inside the orders array list
     * @param indexOfOrder the location where the order is located inside the orders array
     * @return the order object
     */
    public Orders GetOrder(int indexOfOrder){
        orders.get(indexOfOrder);

        return orders.get(indexOfOrder);
    }


}

class Orders
{
    float AdultPrice = 10.00f;
    float ChildPrice = 5.00f;
    float SeniorPrice = 7.50f;

    private int child;
    private int senior;
    private int adult;
    int row; // starting row
    char seat; // starting seat
    private Auditorium auditorium;
    ArrayList<String> seatList = new ArrayList<>();

    Orders(Auditorium auditorium, int adult, int child, int senior, int row, char seat){
        this.auditorium = auditorium;
        this.adult = adult;
        this.child = child;
        this.senior = senior;
        this.row = row;
        this.seat = seat;

        char currentSeat = seat;
        for (int i=0; i < adult+child+senior; i++) // filling our array list
        {
            seatList.add(row+""+currentSeat);
            currentSeat++;
        }
    }


    public void setAdult(int adult) {
        this.adult = adult;
    }
    public void setChild(int child) {
        this.child = child;
    }
    public void setSenior(int senior) {
        this.senior = senior;
    }
    public void setAuditorium(Auditorium auditorium) { this.auditorium = auditorium; }

    public Auditorium getAuditorium() { return auditorium; }
    public int getChild() { return child; }
    public int getSenior() {
        return senior;
    }
    public int getAdult() { return adult; }
    public ArrayList<String> getSeatList() { return seatList; }

    public void adultminusone(){ setAdult(getAdult() - 1); }
    public void childminusone(){ setChild(getChild() - 1); }
    public void seniorminusone(){ setSenior(getSenior() - 1); }

    public void addAdultTickets(int n) {adult = adult + n;}
    public void addChildTickets(int n) {child = child + n;}
    public void addSeniorTickets(int n) {senior = senior + n;}


    public void viewOrder(Auditorium A1, Auditorium A2, Auditorium A3){
        if (this.auditorium == A1) System.out.print("Auditorium 1,");
        if (this.auditorium == A2) System.out.print("Auditorium 2, ");
        if (this.auditorium == A3) System.out.print("Auditorium 3, ");

        int i = 0;
        for (String seat : getSeatList()){
            System.out.print(seat);
            if ((i+1) != adult+senior+child) System.out.print(",");
            i++;
        }
        System.out.println();
        System.out.print(adult+" adult, "+child+" child, "+senior+" senior");
        System.out.println();
    }

    /**
     * displays the order total price
     * @param A1 auditorium 1
     * @param A2 auditorium 2
     * @param A3 auditorium 3
     * @return the price of an order
     */
    public float ViewOrderReceipt(Auditorium A1, Auditorium A2, Auditorium A3)
    {
        viewOrder(A1, A2, A3);
        float receiptPrice = ((float)Math.round(getOrderPrice() * 100.00f) / 100.00f);
        System.out.printf("%s%.2f%n","Order Total: $",receiptPrice);
        System.out.println();

        return receiptPrice;
    }

    /**
     * gets the price of the order
     * @return the price of the order
     */
    public float getOrderPrice(){
        return AdultPrice*adult+SeniorPrice*senior+ChildPrice*child;
    }

    /**
     * deletes all seats and auditoriums associated
     */
    public void ClearOrderAndAudit() {
        setAuditorium(null);
        setAdult(0);
        setSenior(0);
        setChild(0);
    }
}
