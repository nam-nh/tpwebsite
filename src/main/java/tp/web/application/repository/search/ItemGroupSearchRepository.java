package tp.web.application.repository.search;

import tp.web.application.domain.ItemGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ItemGroup entity.
 */
public interface ItemGroupSearchRepository extends ElasticsearchRepository<ItemGroup, Long> {
}
