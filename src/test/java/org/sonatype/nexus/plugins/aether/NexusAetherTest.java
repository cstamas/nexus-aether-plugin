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

    protected void tearDown()
        throws Exception
    {
        // noop, to prevent cleanup for now, I want to see remains
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

        resolve( participants, gav );
    }

    public void testAetherResolveAgainstCentralRepository()
        throws Exception
    {
        ArrayList<MavenRepository> participants = new ArrayList<MavenRepository>();

        participants.add( repositoryRegistry.getRepositoryWithFacet( "central", MavenProxyRepository.class ) );

        Gav gav = new Gav( "org.apache.maven", "apache-maven", "3.0-beta-1" );

        resolve( participants, gav );
    }

    public void testAetherResolveAgainstReleasesRepositoryThatShouldFail()
        throws Exception
    {
        ArrayList<MavenRepository> participants = new ArrayList<MavenRepository>();

        participants.add( repositoryRegistry.getRepositoryWithFacet( "releases", MavenHostedRepository.class ) );

        Gav gav = new Gav( "org.apache.maven", "apache-maven", "3.0-beta-1" );

        try
        {
            resolve( participants, gav );

            fail( "DependencyCollectionException expected!" );
        }
        catch ( DependencyCollectionException e )
        {
            // good, this should fail, since "releases" hosted repository is empty
        }
    }

    protected void resolve( List<MavenRepository> participants, Gav gav )
        throws DependencyCollectionException, ArtifactResolutionException
    {
        Dependency dependency = nexusAether.createDependencyFromGav( gav, "compile" );

        NexusWorkspace nexusWorkspace = nexusAether.createWorkspace( participants );

        DependencyNode root = nexusAether.collectDependencies( nexusWorkspace, dependency, false );

        dump( root, "" );
    }

    // ==

    private static void dump( DependencyNode node, String indent )
    {
        System.out.println( indent + node.getDependency() );
        indent += "  ";
        for ( DependencyNode child : node.getChildren() )
        {
            dump( child, indent );
        }
    }

}
