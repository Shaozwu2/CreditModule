package com.kooppi.nttca.portal.common.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.jodah.lyra.Connections;
import net.jodah.lyra.config.Config;
import net.jodah.lyra.config.RecoveryPolicies;
import net.jodah.lyra.util.Duration;

/**
 * <p>
 * Wrap the standard rabbitMQ connection in order to enhance its functions such as providing the 
 * recover ability when failures occur.
 */
public class RabbitConnection {

    // declared volatile so that  it is visible to all threads at all time
    private volatile Connection connection;
    
    RabbitConnection(){}

    /**
     * Publish the message to the non-existence exchange will cause exception which close the published channel.
     * If you want to caught such exception , the channel recovery should be turn off.
     * 
     * The connection is connected to the default virtual host.
     */
    public RabbitConnection(String rabbitHost,int port, String username, String password , boolean recoveryChannel) throws IOException {
        this(rabbitHost,"/" , port, username, password , recoveryChannel);
    }
    
    public RabbitConnection(String rabbitHost,String virtaulHost, int port, String username, String password , boolean recoveryChannel) throws IOException {
        Config config = new Config()
                .withRecoveryPolicy(RecoveryPolicies.recoverAlways()
                .withBackoff(Duration.seconds(3), Duration.seconds(30)));
        
        if(!recoveryChannel) {
            config.withChannelRecoveryPolicy(RecoveryPolicies.recoverNever());
        } 
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost(virtaulHost);
        factory.setHost(rabbitHost);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        this.connection = Connections.create(factory, config);
    }

    public Channel createChannel() throws IOException {
        return connection.createChannel();
    }

    public void close() throws IOException {
        connection.close();
    }
}
