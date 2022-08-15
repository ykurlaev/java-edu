package fit.kurlaev.chat.server;

import fit.kurlaev.chat.protocol.ObjectStreamSerializer;
import fit.kurlaev.chat.protocol.Serializer;
import fit.kurlaev.chat.protocol.User;
import fit.kurlaev.chat.protocol.XMLSerializer;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class Server
{
    private volatile Map<SocketChannel,Client> clients = new HashMap<SocketChannel,Client>();
    private Thread thread;
    private Worker worker;
    private BlockingQueue<Request> inQueue = new LinkedBlockingQueue<Request>();
    private BlockingQueue<Request> outQueue = new LinkedBlockingQueue<Request>();
    private Map<SocketChannel,LinkedList<Request>> writeRequests = new HashMap<SocketChannel,LinkedList<Request>>();
    private Map<SocketChannel,Request> readRequests = new HashMap<SocketChannel,Request>();
    private Selector selector;
    private ServerSocketChannel channel;
    private ArrayList<User> users = new ArrayList<User>();
    private int port;
    private volatile Integer newWriteRequests = 0;

    public Server(int threadCount, Serializer serializer,int port)
    {
        this.port = port;
        worker = new Worker(threadCount,inQueue,outQueue,serializer,clients.values(),this);
        thread = new Thread(new Runnable()
        {
            public void run()
            {
                selectLoop();
            }
        });
        Logger.getLogger("Server").info("Server created");
    }

    public void newWriteRequest()
    {
        Logger.getLogger("Server").info("New write request");
     /*   synchronized(newWriteRequests)
        {*/
        newWriteRequests++;
          /*  try
            {
                newWriteRequests.notify();
            }
            catch(IllegalMonitorStateException e) { }
        }*/
    }

    public void start() throws IOException
    {
        channel = ServerSocketChannel.open();
        channel.socket().setReuseAddress(true);
        channel.configureBlocking(false);
        selector = SelectorProvider.provider().openSelector();
        worker.setSelector(selector);
        channel.socket().bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_ACCEPT);
        thread.start();
        worker.start();
        Logger.getLogger("Server").info("Server started");
    /*    wakeUpThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(true)
                {
                    synchronized(newWriteRequests)
                    {
                        while(newWriteRequests>0)
                        {
                            selector.wakeup();
                        }
                    /*    try
                        {
                            newWriteRequests.wait();
                        }
                        catch(InterruptedException e)
                        {
                            Thread.currentThread().interrupt();
                        }/
                    }
                }
            }
        });
        wakeUpThread.start();*/
    }

    private void selectLoop()
    {
        Logger.getLogger("Server").info("Select loop started");
        while(true)
        {   
            try
            {
                selector.select();
                Logger.getLogger("Server").info("Select returned");
                Logger.getLogger("Server").info(newWriteRequests+" new write requests");
                while(newWriteRequests>0)
                {
                    Request request = outQueue.take();
                    /*synchronized(newWriteRequests)
                    {*/
                    newWriteRequests--;
                    /*    try
                        {
                            newWriteRequests.notify();
                        }
                        catch(IllegalMonitorStateException e) { }
                    }*/
                    Client client = request.getClient();
                    SocketChannel channel = client.getChannel();
                    channel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    LinkedList<Request> requestList = writeRequests.get(channel);
                    if(requestList==null)
                    {
                        requestList = new LinkedList<Request>();
                        writeRequests.put(channel,requestList);
                    }
                    requestList.add(request);
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext())
                {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    selector.selectedKeys().remove(key);
                    if(!key.isValid())
                    {
                        continue;
                    }
                    if(key.isAcceptable())
                    {
                        accept((ServerSocketChannel)key.channel());
                    }
                    if(key.isReadable())
                    {
                        read(key);
                    }
                    if(key.isWritable())
                    {
                        write(key);
                    }
                }
            }
            catch(CancelledKeyException e) { }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void accept(ServerSocketChannel channel) throws IOException
    {
        Logger.getLogger("Server").info("Accepting new client");
        SocketChannel clientChannel = channel.accept();
        clientChannel.configureBlocking(false);
        Client client = new Client(worker,users,clientChannel);
        clientChannel.register(selector,SelectionKey.OP_READ);
        clients.put(clientChannel,client);
    }

    private void read(SelectionKey key) throws IOException, InterruptedException
    {
        Logger.getLogger("Server").info("New data to read");
        SocketChannel channel = (SocketChannel)key.channel();
        Client client = clients.get(channel);
        Request request;
        if(!readRequests.containsKey(channel))
        {
            request = new Request(client);
            readRequests.put(channel,request);
        }
        else
        {
            request = readRequests.get(channel);
        }
        try
        {
            if(request.lengthBytes<4)
            {
                int read = channel.read(ByteBuffer.wrap(request.lengthBuffer,request.lengthBytes,4-request.lengthBytes));
                if(read==-1)
                {
                    throw new IOException();
                }
                request.lengthBytes+=read;
                Logger.getLogger("Server").info(read+" bytes of length read, total: "+request.lengthBytes);
            }
            else
            {
                if(request.getByteArray()==null)
                {
                    int length;
                    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(request.lengthBuffer));
                    length = stream.readInt();
                    Logger.getLogger("Server").info("Raw message length: "+length);
                    request.setByteArray(new byte[length]);
                }
                int read = channel.read(ByteBuffer.wrap(request.getByteArray(),request.already,request.getByteArray().length-request.already));
                if(read==-1)
                {
                    throw new IOException();
                }
                request.already+=read;
                Logger.getLogger("Server").info(read+" bytes of message read, total: "+request.already);
                if(request.already==request.getByteArray().length)
                {
                    Logger.getLogger("Server").info("Whole raw message is read");
                    readRequests.remove(channel);
                    inQueue.put(request);
                }
            }
        }
        catch(IOException e)
        {
            Logger.getLogger("Sever").info("Removing client");
            readRequests.remove(channel);
            users.remove(client.user);
            key.cancel();
            channel.close();
            clients.remove(client);
        }

    }

    private void write(SelectionKey key) throws IOException
    {
        Logger.getLogger("Server").info("New data to write");
        SocketChannel channel = (SocketChannel)key.channel();
        if(writeRequests.containsKey(channel))
        {
            LinkedList<Request> requestList = writeRequests.get(channel);
            Request request = requestList.peek();
            try
            {
                if(request.lengthBytes<4)
                {
                    int written = channel.write(ByteBuffer.wrap(request.lengthBuffer,request.lengthBytes,4-request.lengthBytes));
                    if(written==-1)
                    {
                        throw new IOException();
                    }
                    request.lengthBytes+=written;
                    Logger.getLogger("Server").info(written+" bytes written, total: "+request.lengthBytes);
                }
                else
                {
                    Logger.getLogger("Server").info("Wrote raw message length: "+request.getByteArray().length);
                    int written = channel.write(ByteBuffer.wrap(request.getByteArray(),request.already,request.getByteArray().length-request.already));
                    if(written==-1)
                    {
                        throw new IOException();
                    }
                    request.already+=written;
                    Logger.getLogger("Server").info(written+" bytes written, total: "+request.already);
                    if(request.already==request.getByteArray().length)
                    {
                        Logger.getLogger("Server").info("Wrote whole raw message");
                        requestList.remove();
                        if(requestList.isEmpty())
                        {
                            writeRequests.remove(channel);
                        }
                        if(request.isToDisconnect())
                        {
                            throw new IOException();
                        }
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
            catch(IOException e)
            {
                writeRequests.remove(channel);
                Logger.getLogger("Sever").info("Removing client");
                users.remove(request.getClient().user);
                key.cancel();
                channel.close();
                clients.remove(request.getClient());
            }
        }
    }

    public static void main(String[] args)
    {
        Serializer serializer = null;
        if(args.length>0 && args[0].equals("-o"))
        {
            serializer = new ObjectStreamSerializer();
        }
        else
        {
            try
            {
                serializer = new XMLSerializer();
            }
            catch(JAXBException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        Server server = new Server(5,serializer,4444);
        try
        {
            server.start();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
