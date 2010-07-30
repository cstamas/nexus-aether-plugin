package org.sonatype.nexus.plugins.aether;

import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.aether.LocalRepository;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.impl.ArtifactDescriptorReader;
import org.sonatype.aether.impl.internal.DefaultServiceLocator;
import org.sonatype.aether.util.DefaultRepositorySystemSession;
import org.sonatype.nexus.configuration.application.ApplicationConfiguration;

@Component( role = AetherProvider.class )
public class DefaultAetherProvider
    implements AetherProvider
{
    @Requirement
    private ApplicationConfiguration applicationConfiguration;

    private RepositorySystem repositorySystem;

    public synchronized RepositorySystem getRepositorySystem()
    {
        if ( repositorySystem == null )
        {
            repositorySystem = constructRepositorySystem();
        }

        return repositorySystem;
    }

    public RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem,
                                                                      NexusWorkspace nexusWorkspace )
    {
        DefaultRepositorySystemSession session = DefaultRepositorySystemSession.newMavenRepositorySystemSession();

        LocalRepository localRepo =
            new LocalRepository( applicationConfiguration.getWorkingDirectory( "aether-local-repository" ) );
        session.setLocalRepositoryManager( repositorySystem.newLocalRepositoryManager( localRepo ) );

        // session.setTransferListener( new ConsoleTransferListener( System.out ) );
        // session.setRepositoryListener( new ConsoleRepositoryListener( System.out ) );

        session.setWorkspaceReader( nexusWorkspace.getWorkspaceReader() );
        // session.setUpdatePolicy( "" );
        
        return session;
    }

    // ==

    protected RepositorySystem constructRepositorySystem()
    {
        // TODO: We use "manually" managed aether, since it relies on new Plexus capabilities (Optional components)
        // that is unsupported by Plexus used in Nexus!
        // Obviously, this is true for UTs only (since on deploy, plexus-shim is used) :(
        
        DefaultServiceLocator locator = new DefaultServiceLocator();
        // locator.setServices( WagonProvider.class, new ManualWagonProvider() );
        // locator.addService( RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class );
        locator.addService( ArtifactDescriptorReader.class, DefaultArtifactDescriptorReader.class );

        return locator.getService( RepositorySystem.class );
    }

}
