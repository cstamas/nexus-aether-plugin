package org.sonatype.nexus.plugins.aether.connector;

import java.util.Collection;

import org.sonatype.aether.spi.connector.ArtifactDownload;
import org.sonatype.aether.spi.connector.ArtifactUpload;
import org.sonatype.aether.spi.connector.MetadataDownload;
import org.sonatype.aether.spi.connector.MetadataUpload;
import org.sonatype.aether.spi.connector.RepositoryConnector;

public class NexusRepositoryConnector
    implements RepositoryConnector
{

    public void get( Collection<? extends ArtifactDownload> artifactDownloads,
                     Collection<? extends MetadataDownload> metadataDownloads )
    {
        // TODO Auto-generated method stub
        
    }

    public void put( Collection<? extends ArtifactUpload> artifactUploads,
                     Collection<? extends MetadataUpload> metadataUploads )
    {
        // TODO Auto-generated method stub
        
    }

    public void close()
    {
        // TODO Auto-generated method stub
        
    }

}
