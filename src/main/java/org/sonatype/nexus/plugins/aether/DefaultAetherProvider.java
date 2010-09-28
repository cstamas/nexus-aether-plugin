package org.sonatype.nexus.plugins.aether;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.repository.LocalRepository;
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
        return repositorySystem;
    }

    public RepositorySystemSession getDefaultRepositorySystemSession( RepositorySystem repositorySystem )
    {
        MavenRepositorySystemSession session = new MavenRepositorySystemSession();

        // can't aether work _without_ local repo?
        LocalRepository localRepo =
            new LocalRepository( applicationConfiguration.getWorkingDirectory( "aether-local-repository" ) );
        session.setLocalRepositoryManager( repositorySystem.newLocalRepositoryManager( localRepo ) );
        
        // session.setIgnoreMissingArtifactDescriptor( false );

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
}
