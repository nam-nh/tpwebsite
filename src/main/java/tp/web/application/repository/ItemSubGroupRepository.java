package tp.web.application.repository;

import tp.web.application.domain.ItemSubGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemSubGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemSubGroupRepository extends JpaRepository<ItemSubGroup, Long> {

}
