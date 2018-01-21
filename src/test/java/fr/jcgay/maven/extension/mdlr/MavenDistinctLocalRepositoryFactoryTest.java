package fr.jcgay.maven.extension.mdlr;

import fr.jcgay.maven.extension.mdlr.aether.EnhancedLocalRepositoryManager;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.NoLocalRepositoryManagerException;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

class MavenDistinctLocalRepositoryFactoryTest {

    @Mock
    LoggerFactory logger;

    @Mock
    RepositorySystemSession session;

    @InjectMocks
    MavenDistinctLocalRepositoryFactory factory;

    Path tempDirectory;

    @BeforeEach
    void setUp() throws IOException {
        initMocks(this);
        tempDirectory = Files.createTempDirectory("distinct-local-repository");
    }

    @AfterEach
    void reset_property() {
        System.clearProperty("distinct.local.repository");
    }

    @Test
    void activated_when_property_is_true() throws NoLocalRepositoryManagerException {
        System.setProperty("distinct.local.repository", "true");

        LocalRepositoryManager result = factory.newInstance(session, new LocalRepository(tempDirectory.toFile()));

        assertThat(result).isExactlyInstanceOf(MavenDistinctLocalRepositoryManager.class);
    }

    @Test
    void not_activated_when_property_is_false() throws NoLocalRepositoryManagerException {
        System.setProperty("distinct.local.repository", "false");

        LocalRepositoryManager result = factory.newInstance(session, new LocalRepository(tempDirectory.toFile()));

        assertThat(result).isExactlyInstanceOf(EnhancedLocalRepositoryManager.class);
    }
}
