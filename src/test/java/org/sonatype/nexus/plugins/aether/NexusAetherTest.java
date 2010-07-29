package org.sonatype.nexus.plugins.aether;

import java.util.ArrayList;

import org.sonatype.aether.DependencyNode;
import org.sonatype.nexus.AbstractMavenRepoContentTests;
import org.sonatype.nexus.artifact.Gav;
import org.sonatype.nexus.proxy.maven.MavenGroupRepository;
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

        NexusWorkspace nw = nexusAether.createWorkspace( participants );

        Gav gav = new Gav( "org.apache.maven", "maven-profile", "2.2.1" );

        DependencyNode root = nexusAether.collectDependencies( nw, gav, false );

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
