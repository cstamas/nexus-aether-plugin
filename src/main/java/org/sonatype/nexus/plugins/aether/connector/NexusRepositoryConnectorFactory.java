package org.sonatype.nexus.plugins.aether.connector;

import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.spi.connector.RepositoryConnector;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.transfer.NoRepositoryConnectorException;

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
