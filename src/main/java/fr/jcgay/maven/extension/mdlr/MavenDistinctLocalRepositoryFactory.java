package fr.jcgay.maven.extension.mdlr;

import org.codehaus.plexus.component.annotations.Component;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.NoLocalRepositoryManagerException;
import org.eclipse.aether.spi.localrepo.LocalRepositoryManagerFactory;
import org.eclipse.aether.spi.log.Logger;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.spi.log.NullLoggerFactory;

import javax.inject.Inject;

@Component(role = LocalRepositoryManagerFactory.class, hint = "maven-distinct-local-repository", description = "Separate SNAPSHOTS from RELEASES into local repository")
public class MavenDistinctLocalRepositoryFactory implements LocalRepositoryManagerFactory {

    private Logger logger;

    @Inject
    public MavenDistinctLocalRepositoryFactory(LoggerFactory loggerFactory) {
        this.logger = NullLoggerFactory.getSafeLogger(loggerFactory, MavenDistinctLocalRepositoryManager.class);
    }

    @Override
    public LocalRepositoryManager newInstance(RepositorySystemSession session, LocalRepository repository) throws NoLocalRepositoryManagerException {
        if ("".equals(repository.getContentType()) || "default".equals(repository.getContentType())) {
            return new MavenDistinctLocalRepositoryManager(repository.getBasedir(), session).setLogger(logger);
        }

        throw new NoLocalRepositoryManagerException(repository);
    }

    @Override
    public float getPriority() {
        return 20;
    }
}
