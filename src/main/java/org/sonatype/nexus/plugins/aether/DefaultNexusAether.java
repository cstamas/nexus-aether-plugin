package org.sonatype.nexus.plugins.aether;

import java.util.List;
import java.util.UUID;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.nexus.artifact.Gav;
import org.sonatype.nexus.plugins.aether.workspace.NexusWorkspace;
import org.sonatype.nexus.proxy.maven.MavenRepository;

@Component( role = NexusAether.class )
public class DefaultNexusAether
    implements NexusAether
{
    @Requirement
    private AetherProvider aetherProvider;

    public NexusWorkspace createWorkspace( List<MavenRepository> participants )
    {
        return new NexusWorkspace( UUID.randomUUID().toString(), participants );
    }

    public Dependency createDependencyFromGav( Gav gav, String scope )
    {
        Dependency dependency =
            new Dependency( new DefaultArtifact( gav.getGroupId(), gav.getArtifactId(), gav.getExtension(),
                gav.getVersion() ), scope );

        return dependency;
    }

    public DependencyNode collectDependencies( NexusWorkspace nexusWorkspace, Dependency dependency, boolean resolve )
        throws DependencyCollectionException, ArtifactResolutionException
    {
        RepositorySystem repositorySystem = aetherProvider.getRepositorySystem();

        RepositorySystemSession session =
            aetherProvider.getDefaultRepositorySystemSession( repositorySystem, nexusWorkspace );

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( dependency );
        // We don't need any remote repo since we have nexus under the hub as WorkspaceReader
        // RemoteRepository central = new RemoteRepository( "central", "default", "http://repo1.maven.org/maven2-nx/" );
        // collectRequest.addRepository( central );
        DependencyNode node = repositorySystem.collectDependencies( session, collectRequest ).getRoot();

        if ( resolve )
        {
            repositorySystem.resolveDependencies( session, node, null );
        }

        return node;
    }
}
