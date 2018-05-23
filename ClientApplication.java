import client.Client;
import client.ClientInterface;
import client.ClientWindow;

public class ClientApplication {
    Client c;
    ClientWindow cw;
    public void initialise(){
        c= new Client();
    }
    public void launchGUI(ClientInterface c){cw= new ClientWindow(c);}
    public void starting()
    {
        this.initialise();
        this.launchGUI(c);
    }
    public static void  main(String[] args)
    {
        ClientApplication a= new ClientApplication();
        a.starting();

    }
}
