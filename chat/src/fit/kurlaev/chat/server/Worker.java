package fit.kurlaev.chat.server;

import fit.kurlaev.chat.protocol.Message;
import fit.kurlaev.chat.protocol.Serializer;
import fit.kurlaev.chat.protocol.message.Command;

import java.nio.channels.Selector;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Worker
{
    private Thread[] threads;
    private BlockingQueue<Request> inQueue;
    private BlockingQueue<Request> outQueue;
    private Serializer serializer;
    private volatile Collection<Client> clients;
    private Selector selector;
    private Server server;

    public Worker(int threadCount, BlockingQueue<Request> inQueue, BlockingQueue<Request> outQueue,
                  Serializer serializer, Collection<Client> clients, Server server)
    {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.serializer = serializer;
        this.clients = clients;
        this.server = server;
        threads = new Thread[threadCount];
        for(int i=0;i<threadCount;i++)
        {
            threads[i] = new Thread(new Runnable()
            {
                public void run()
                {
                    workLoop();
                }
            });
        }
    }

    public void setSelector(Selector selector)
    {
        this.selector = selector;
    }

    public void start()
    {
        for(Thread thread : threads)
        {
            thread.start();
        }
    }

    private void workLoop()
    {
        for(;;)
        {
            try
            {
                Request inRequest = inQueue.take();
                Message inMessage = serializer.deserialize(inRequest.getByteArray());
                Message outMessage = inRequest.getClient().processMessage(inMessage);
                if(outMessage!=null)
                {
                    Logger.getLogger("Server").info("Message to write: "+outMessage);
                    byte outByteArray[] = serializer.serialize(outMessage);
                    Request request = new Request(inRequest.getClient(),outByteArray);
                    if(inMessage instanceof Command && ((Command) inMessage).getType()== Command.Type.LOGOUT)
                    {
                        request.setToDisconnect();
                    }
                    outQueue.put(request);
                    server.newWriteRequest();
                    selector.wakeup();
                }
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt(); //I think, it must never happen...
            }

        }
    }

    public void broadcast(Message message)
    {
        byte[] byteArray = serializer.serialize(message);
        for(Client client : clients)
        {
            if(client.isLoggedIn())
            {
                try
                {
                    outQueue.put(new Request(client,byteArray));
                }
                catch(InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
                server.newWriteRequest();
                selector.wakeup();
            }
        }
    }
}
