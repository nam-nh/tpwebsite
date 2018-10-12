package tp.web.application.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of NewsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NewsSearchRepositoryMockConfiguration {

    @MockBean
    private NewsSearchRepository mockNewsSearchRepository;

}
