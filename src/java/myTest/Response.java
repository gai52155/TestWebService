package myTest;

/**
 *
 * @author Katawut
 */
public class Response 
{
    public String status;
    public String message;
    
    public String print()
    {
        return "Status : " + status + "\r\nMessage : " +message;
    }
}
