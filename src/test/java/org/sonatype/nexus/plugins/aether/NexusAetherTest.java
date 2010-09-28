package org.sonatype.nexus.plugins.aether;

import java.util.ArrayList;
import java.util.List;

import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.nexus.AbstractMavenRepoContentTests;
import org.sonatype.nexus.artifact.Gav;
import org.sonatype.nexus.plugins.aether.workspace.NexusWorkspace;
import org.sonatype.nexus.proxy.maven.MavenGroupRepository;
import org.sonatype.nexus.proxy.maven.MavenHostedRepository;
import org.sonatype.nexus.proxy.maven.MavenProxyRepository;
import org.sonatype.nexus.proxy.maven.MavenRepository;
import org.sonatype.nexus.proxy.registry.RepositoryRegistry;

public class NexusAetherTest
    extends AbstractMavenRepoContentTests
{
    protected NexusAether nexusAether;

    protected RepositoryRegistry repositoryRegistry;

    protected void setUp()
        throws Exception
    {
        super.setUp();

        nexusAether = lookup( NexusAether.class );

        repositoryRegistry = lookup( RepositoryRegistry.class );
    }

    protected boolean loadConfigurationAtSetUp()
    {
        return true;
    }

    public void testAetherResolveAgainstPublicGroup()
        throws Exception
    {
        ArrayList<MavenRepository> participants = new ArrayList<MavenRepository>();

        participants.add( repositoryRegistry.getRepositoryWithFacet( "public", MavenGroupRepository.class ) );

        Gav gav = new Gav( "org.apache.maven", "apache-maven", "3.0-beta-1" );

        assertEquals( "Root with 27 nodes was expected!", 27, resolve( participants, gav ) );
    }

    public void testAetherResolveAgainstCentralRepository()
        throws Exception
    {
        ArrayList<MavenRepository> participants = new ArrayList<MavenRepository>();

        participants.add( repositoryRegistry.getRepositoryWithFacet( "central", MavenProxyRepository.class ) );

        Gav gav = new Gav( "org.apache.maven", "apache-maven", "3.0-beta-1" );

        assertEquals( "Root with 27 nodes was expected!", 27, resolve( participants, gav ) );
    }

    public void testAetherResolveAgainstReleasesRepositoryThatShouldFail()
        throws Exception
    {
        ArrayList<MavenRepository> participants = new ArrayList<MavenRepository>();

        participants.add( repositoryRegistry.getRepositoryWithFacet( "releases", MavenHostedRepository.class ) );

        Gav gav = new Gav( "org.apache.maven", "apache-maven", "3.0-beta-1" );

        assertEquals( "Only the root node was expected!", 1, resolve( participants, gav ) );
    }

    protected int resolve( List<MavenRepository> participants, Gav gav )
        throws DependencyCollectionException, ArtifactResolutionException
    {
        Dependency dependency = nexusAether.createDependencyFromGav( gav, "compile" );

        NexusWorkspace nexusWorkspace = nexusAether.createWorkspace( participants );

        DependencyNode root = nexusAether.collectDependencies( nexusWorkspace, dependency, false );

        return dump( root );
    }

    // ==

    protected static int dump( DependencyNode node )
    {
        return dump( node, "", 0 );
    }

    protected static int dump( DependencyNode node, String indent, int count )
    {
        System.out.println( indent + node.getDependency() );
        indent += "  ";
        int result = count + 1;
        for ( DependencyNode child : node.getChildren() )
        {
            result += dump( child, indent, count );
        }
        return result;
    }

}
