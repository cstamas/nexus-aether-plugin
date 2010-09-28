package org.sonatype.nexus.plugins.aether;

import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.connector.wagon.PlexusWagonProvider;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.impl.ArtifactDescriptorReader;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.DefaultRepositorySystemSession;
import org.sonatype.nexus.configuration.application.ApplicationConfiguration;
import org.sonatype.nexus.plugins.aether.workspace.NexusWorkspace;

@Component( role = AetherProvider.class )
public class DefaultAetherProvider
    implements AetherProvider
{
    @Requirement
    private ApplicationConfiguration applicationConfiguration;

    @Requirement
    private RepositorySystem repositorySystem;

    public synchronized RepositorySystem getRepositorySystem()
    {
        if ( repositorySystem == null )
        {
            repositorySystem = constructRepositorySystem();
        }

        return repositorySystem;
    }

    public RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem )
    {
        MavenRepositorySystemSession session = new MavenRepositorySystemSession();

        LocalRepository localRepo =
            new LocalRepository( applicationConfiguration.getWorkingDirectory( "aether-local-repository" ) );
        session.setLocalRepositoryManager( repositorySystem.newLocalRepositoryManager( localRepo ) );

        // session.setTransferListener( new ConsoleTransferListener( System.out ) );
        // session.setRepositoryListener( new ConsoleRepositoryListener( System.out ) );

        // session.setUpdatePolicy( "" );

        return session;
    }

    public RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem,
                                                                      NexusWorkspace nexusWorkspace )
    {
        DefaultRepositorySystemSession session =
            (DefaultRepositorySystemSession) getDefaultRepositorySystemSession( repositorySystem );

        session.setWorkspaceReader( nexusWorkspace.getWorkspaceReader() );

        return session;
    }

    // ==

    protected RepositorySystem constructRepositorySystem()
    {
        
        // TODO: We use "manually" managed aether, since it relies on new Plexus capabilities (Optional components)
        // that is unsupported by Plexus used in Nexus!
        // Obviously, this is true for UTs only (since on deploy, plexus-shim is used) :(

        DefaultServiceLocator locator = new DefaultServiceLocator();
        locator.setServices( WagonProvider.class, new PlexusWagonProvider() );
        locator.addService( RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class );
        locator.addService( ArtifactDescriptorReader.class, DefaultArtifactDescriptorReader.class );

        return locator.getService( RepositorySystem.class );
    }

}
