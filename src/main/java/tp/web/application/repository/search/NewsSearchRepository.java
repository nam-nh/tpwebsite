package tp.web.application.repository.search;

import tp.web.application.domain.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the News entity.
 */
public interface NewsSearchRepository extends ElasticsearchRepository<News, Long> {
}
