/*
    Class that handles the contents of each seat

    Author: Erik Rodriguez

 */
public class Seat
{
    private int row;
    private char seat;
    private char ticketType;

    public Seat(int row, char seat , char ticketType)
    {
        this.row = row;
        this.seat = seat;
        this.ticketType = ticketType;
    }

    public char getSeat(){return seat;} // Getters
    public char getTicketType() { return ticketType; }
    public int getRow() { return row; }

    public void setSeat(char seat) { this.seat = seat; }  // Setters
    public void setTicketType(char ticketType) { this.ticketType = ticketType; }
    public void setRow(int row) { this.row = row; }
}

