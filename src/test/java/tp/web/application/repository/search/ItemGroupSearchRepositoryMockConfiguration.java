package tp.web.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ItemGroupSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ItemGroupSearchRepositoryMockConfiguration {

    @MockBean
    private ItemGroupSearchRepository mockItemGroupSearchRepository;

}
