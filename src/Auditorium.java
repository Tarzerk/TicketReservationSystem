/*
    A class that creates and handles the auditorium
    functions

    Author: Erik Rodriguez

 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Auditorium
{
    private Node<Seat> first; // head pointer of the list

    /**
     * Builds the auditorium using a 2D linked list
     * @param file file containing the arrangement of the matrix
     * @throws FileNotFoundException
     */
    public Auditorium(File file) throws FileNotFoundException
    {
        Scanner read = new Scanner(file);
        int rowNum = 1; // counts the number of rows
        char seatLetter = 'A';

        Node<Seat> current = first; // the node that will be moving through the linked list
        Node<Seat> newNode = null; // temporary storage that we use as we create noodles
        Node<Seat> ypointer = first; // this will move down in the first column
        boolean linkTopAndBottom = true;

        while(read.hasNextLine())
        {
            String data = read.nextLine();

            if (rowNum == 1) // creates the first row
            {
                for(int i=1; i<=data.length();i++)
                {

                    if (first== null) //creates the head node
                    {
                        first = new Node<>(new Seat(rowNum, seatLetter, data.charAt(i-1)));
                        seatLetter = (char)(seatLetter + 1);
                        current = first;

                    }
                    else
                    {
                        Node<Seat> temp = new Node<>(new Seat(rowNum, seatLetter, data.charAt(i-1))); // adjacent node right

                        current.setRight(temp);// make current node point to the adjacent node
                        temp.setLeft(current); // make the new node point back
                        current = current.getRight(); // shift the pointer to the right
                        seatLetter = (char)(seatLetter + 1); // increase the seat letter
                    }
                }
                current = first;
                seatLetter = 'A'; // reset alphabet
            }
            else // to create the following rows
            {
                for (int i=0; i < data.length(); i++)
                {
                    if (linkTopAndBottom) // to link top an bottom
                    {
                        newNode = new Node<>(new Seat(rowNum, seatLetter, data.charAt(i))); // create seat on bottom
                        ypointer = newNode;
                        newNode.setUp(current); // link up
                        current.setDown(newNode);// link down
                        current = current.getRight(); // move top pointer to the right
                        seatLetter = (char)(seatLetter + 1);
                        linkTopAndBottom = false;
                    }
                    else
                    {
                        Node<Seat> temp = new Node<>(new Seat(rowNum, seatLetter, data.charAt(i)));
                        temp.setLeft(newNode);
                        newNode.setRight(temp);
                        temp.setUp(current);
                        current = temp;
                        current = current.getRight();
                        newNode = temp;
                        seatLetter = (char)(seatLetter + 1);
                    }
                }

                current = ypointer;
                seatLetter = 'A'; // reset alphabet
                linkTopAndBottom = true;

            }
            rowNum++;

        }
        read.close();
    }

    /**
     * Gets the number of rows on the auditorium
     * @return the number of columns
     */
    int getRowCount(){
        int count = 0;
        Node<Seat> ypointer = first;

        while(ypointer != null) {
            ypointer = ypointer.getDown();
            count++;
        }
        return count;
    }

    /**
     * Gets the number of columns on the theater
     * @return the number of columns
     */
    int getColCount(){
        int count = 0;
        Node<Seat> xpointer = first;

        while(xpointer != null){
            xpointer = xpointer.getRight();
            count++;
        }

        return count;
    }

    /**
     * Marks seat as empty
     * @param row row num
     * @param seat seat letter
     */
    char DeleteTicket(int row, char seat){

        Node<Seat> ypointer = first; // this will keep track of the y axis
        for(int i = 1; i < row; i++)
        {
            ypointer = ypointer.getDown();
        }

        Node<Seat> xpointer = ypointer; // this will keep track of the x axis

        for(char i = 'A'; i <= seat; i++)
        {
            if (i == seat)
                break;
            xpointer = xpointer.getRight();
        }

        char deletionTicket = xpointer.getPayload().getTicketType();

        xpointer.getPayload().setTicketType('.'); // set seat to empty

        return deletionTicket; // return ticket that was deleted

    }

    /**
     * Reserves seats in the given Auditorium
     * @param row row location
     * @param seat seat location
     * @param Aticket Adult tickets
     * @param Cticket child tickets
     * @param Sticket senior tickets
     */
    void ReserveSeats(int row, char seat, int Aticket, int Cticket, int Sticket) // reserves tickets for the user
    {
        int totalTickets = Aticket+Cticket+Sticket;

        Node<Seat> ypointer = first; // this will keep track of the y axis
        for(int i = 1; i < row; i++)
        {
            ypointer = ypointer.getDown();
        }

        Node<Seat> xpointer = ypointer; // this will keep track of the x axis

        for(char i = 'A'; i <= seat; i++)
        {
            if (i == seat)
                break;
            xpointer = xpointer.getRight();
        }

        while(totalTickets != 0) // reserve tickets in the order of Adult, Child, Senior until tickets runt out
        {
            if (Aticket != 0)
            {
                xpointer.getPayload().setTicketType('A');
                xpointer = xpointer.getRight();
                Aticket--;
            }
            else if (Cticket != 0)
            {
                xpointer.getPayload().setTicketType('C');
                xpointer = xpointer.getRight();
                Cticket--;
            }
            else if (Sticket != 0)
            {
                xpointer.getPayload().setTicketType('S');
                xpointer = xpointer.getRight();
                Sticket--;
            }
            totalTickets--;
        }

    }

    /**
     * Shows layout in customer mode
     * @param columnCount
     * @param rowCount
     */
    void showLayout(int columnCount, int rowCount) // function that prints out the auditorium in customer mode
    {


        int rowNum = 1;
        Node<Seat> xpointer = first; // this will keep track of the x axis
        Node<Seat> ypointer = first; // this will keep track of the y axis
        // Print the the first row

        System.out.print("  ");
        for(int i=65; i<=(columnCount+64); i++) // prints out the column label
        {
            System.out.print((char)i);
        }
        System.out.println();

        System.out.print(rowNum+" ");
        rowNum++; // row num increases
        while (xpointer != null)
        {
            if (xpointer.getPayload().getTicketType() == '.')
            {
                System.out.print(xpointer.getPayload().getTicketType());
            }
            else
            {
                System.out.print("#"); // censors the ticket type
            }
            xpointer = xpointer.getRight();
        }

        System.out.println();
        assert ypointer != null;
        ypointer = ypointer.getDown();
        xpointer = ypointer;

        System.out.print(rowNum+" ");
        while (ypointer != null) // print out the remaining rows
        {
            if (xpointer.getPayload().getTicketType() != '.')
            {
                System.out.print('#');
            }
            else
            {
                System.out.print(xpointer.getPayload().getTicketType());
            }

            xpointer = xpointer.getRight();


            if (xpointer == null)
            {
                System.out.println();
                if (rowNum < rowCount){
                    rowNum++;
                    System.out.print(rowNum+" ");
                }
                ypointer = ypointer.getDown();
                xpointer = ypointer;
            }

        }
    }

    /**
     * A function that determines if a selection of seats is available
     * @param row starting row
     * @param seat staring column (seat)
     * @param totalTickets total amount of tickets
     * @return true if available false otherwise
     */
    boolean checkAvailability(int row, char seat, int totalTickets)
    {
        Node<Seat> ypointer = first; //traverse down in the column
        for(int i = 1; i < row; i++)
        {
            ypointer = ypointer.getDown();

        }

        Node<Seat> xpointer = ypointer; // traverse to the right in the row

        for(char i = 'A'; i <= seat; i++)
        {
            if (i == seat)
            {
                break;
            }
            else
            {
                xpointer = xpointer.getRight();
            }
        }

        // checking if each seat is open
        while(totalTickets !=0)
        {
            if(xpointer == null) // selection has gone out of bounds
            {
                return false;
            }
            else if (xpointer.getPayload().getTicketType() == '.'){ // makes sure seats are empty
                xpointer = xpointer.getRight();
                totalTickets--;
            } else { // the seat is reserved
                return false;
            }
        }

        return true;
    }

    /**
     * a function that takes the total amount of tickets
     * and find the best seats in our auditorium
     *
     * @param selection // the total amounts of seats to be reserved
     * @param rowCount // The number of rows in the auditorium
     * @param colCount // the number of columns in the auditorim
     * @return an array containing 2 string where the first one is the row #
     *         and the second one is seat letter example {"6","C"}
     */
    String [] BestAvailableSeat(int selection, int rowCount, int colCount){
        String[] bestSeats = new String[] {"?","?"}; // default return type

        Node<Seat> xpointer = first; // traversal pointers
        Node<Seat> ypointer = first;
        int bestRow = -1; char bestSeat; // this will represent the best left most seat in our selection

        double midPointX = ((double)colCount+1)/2.0;
        double midPointY = ((double)rowCount+1)/2.0;
        int openSeatCount = 0; // to represent consecutive open seats
        double currentCol = 0; // current seat in numerical form›
        double currentRow = 0; // current row


        double distFromMid; // to calculate the distance from our selection to the middle of the auditorium
        double bestDistance = 9999; // flag


        double midPointSelection = (selection-1) * .5;

        while (ypointer !=null){ // we iterate through the whole list
            currentRow++; // increase column when we read a whole row or we start
            //System.out.println("The current row is " + currentRow);
            while(xpointer != null){ // this iterates through every row
                currentCol++; // increase seat column or start at 'A'
                if (xpointer.getPayload().getTicketType() == '.') // when seats are open to the right consecutively
                {
                    openSeatCount++;

                    if (openSeatCount >= selection) // if there is space for our customer
                    {
                        double currentMid = currentCol - midPointSelection; // calculate distance to mid of auditorium
                        distFromMid = Math.sqrt(Math.pow((midPointX - currentMid),2) + Math.pow((midPointY - currentRow),2));
                        if (distFromMid < bestDistance || distFromMid == bestDistance && Math.abs(currentRow - midPointY) < Math.abs(bestRow - midPointY))
                        { // only update if we find a better location
                            bestDistance = distFromMid;
                            bestRow = (int) currentRow;
                            bestSeat = (char) ((currentCol - selection)+65);
                            bestSeats[0] = Integer.toString(bestRow); // store our best
                            bestSeats[1] = Character.toString(bestSeat);
                        }
                    }
                }
                else
                {
                    openSeatCount = 0; // resets when seat isn't open
                }
                xpointer = xpointer.getRight();
            }
            openSeatCount = 0; // resets when a whole row has been read
            currentCol = 0;
            ypointer = ypointer.getDown();
            xpointer = ypointer;
        }

        return bestSeats;
    }


    /**
     * Writes updated results to a file
     * @throws FileNotFoundException
     */
    void WriteReport(String filename) throws FileNotFoundException
    {
        PrintWriter writer = new PrintWriter(filename);
        Node<Seat> xpointer = first; // this will keep track of the x axis
        Node<Seat> ypointer = first; // this will keep track of the y axis
        // Print the the first row
        while (xpointer != null)
        {
            writer.print(xpointer.getPayload().getTicketType());
            xpointer = xpointer.getRight(); // move through the row
        }

        writer.println();
        assert ypointer != null;
        ypointer = ypointer.getDown();
        xpointer = ypointer;

        while(ypointer != null) // print out the remaining rows
        {
            writer.print(xpointer.getPayload().getTicketType());
            xpointer = xpointer.getRight();
            if(xpointer == null)
            {
                ypointer = ypointer.getDown();
                writer.println();
                xpointer = ypointer;
            }
        }

        writer.close(); // closing our writer
    }


    /**
     * Gets the stats of the auditorium
     * @return array with {totalSeats, openseats, reserved, atickets, ctickets, stickets, totalSales}
     * @throws FileNotFoundException
     */
    float[] GetReport() throws FileNotFoundException
    {
        float AdultPrice, ChildPrice, SeniorPrice; // prices for tickets
        AdultPrice = 10.00f;
        ChildPrice = 5.00f;
        SeniorPrice = 7.50f;

        int ACount = 0, SCount = 0, CCount = 0, open = 0; // keep track of the number of the types of tickets we have

        Node<Seat> xpointer = first; // this will keep track of the x axis
        Node<Seat> ypointer = first; // this will keep track of the y axis
        // Print the the first row
        while (xpointer != null)
        {
            switch (xpointer.getPayload().getTicketType()) // count the tickets at each seat
            {
                case 'A':
                    ACount++;
                    break;
                case 'C':
                    CCount++;
                    break;
                case 'S':
                    SCount++;
                    break;
                case '.':
                    open++;
                    break;

            }
            xpointer = xpointer.getRight(); // move through the row
        }
        assert ypointer != null;
        ypointer = ypointer.getDown();
        xpointer = ypointer;

        while(ypointer != null) // print out the remaining rows
        {
            switch (xpointer.getPayload().getTicketType()) // count the instances of tickets
            {
                case 'A':
                    ACount++;
                    break;
                case 'C':
                    CCount++;
                    break;
                case 'S':
                    SCount++;
                    break;
                case '.':
                    open++;
                    break;
            }
            xpointer = xpointer.getRight();
            if(xpointer == null)
            {
                ypointer = ypointer.getDown();
                xpointer = ypointer;
            }
        }

        float TotalSales = (ACount*AdultPrice) + (CCount*ChildPrice) + (SCount*SeniorPrice);

        return new float[] {(open + ACount + SCount + CCount),open, (ACount+SCount+CCount), ACount, CCount, SCount, TotalSales};
    }


}
