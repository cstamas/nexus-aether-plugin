package org.sonatype.nexus.plugins.aether.connector;

import org.sonatype.aether.NoRepositoryConnectorException;
import org.sonatype.aether.RemoteRepository;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.spi.connector.RepositoryConnector;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;

public class NexusRepositoryConnectorFactory
    implements RepositoryConnectorFactory
{

    public RepositoryConnector newInstance( RepositorySystemSession session, RemoteRepository repository )
        throws NoRepositoryConnectorException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getPriority()
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
