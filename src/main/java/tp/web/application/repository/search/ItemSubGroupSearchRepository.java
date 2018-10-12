package tp.web.application.repository.search;

import tp.web.application.domain.ItemSubGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ItemSubGroup entity.
 */
public interface ItemSubGroupSearchRepository extends ElasticsearchRepository<ItemSubGroup, Long> {
}
