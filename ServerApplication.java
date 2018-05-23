import server.DataPersistence;
import server.Server;
import server.ServerInterface;
import server.ServerWindow;

public class ServerApplication {
    Server s;
    ServerWindow sw;
    public void initialise(){
        s= new Server();
        s.ServerS();
        Thread d= new DataPersistence(s);
        d.start();
    }
    public void launchGUI(ServerInterface s){sw= new ServerWindow(s);}
    public void starting()
    {
        this.initialise();
        this.launchGUI(s);
    }
    public static void  main(String[] args)
    {
        ServerApplication a= new ServerApplication();

        a.starting();


    }
}
