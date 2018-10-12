package tp.web.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import tp.web.application.domain.ItemSubGroup;
import tp.web.application.repository.ItemSubGroupRepository;
import tp.web.application.repository.search.ItemSubGroupSearchRepository;
import tp.web.application.web.rest.errors.BadRequestAlertException;
import tp.web.application.web.rest.util.HeaderUtil;
import tp.web.application.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ItemSubGroup.
 */
@RestController
@RequestMapping("/api")
public class ItemSubGroupResource {

    private final Logger log = LoggerFactory.getLogger(ItemSubGroupResource.class);

    private static final String ENTITY_NAME = "itemSubGroup";

    private final ItemSubGroupRepository itemSubGroupRepository;

    private final ItemSubGroupSearchRepository itemSubGroupSearchRepository;

    public ItemSubGroupResource(ItemSubGroupRepository itemSubGroupRepository, ItemSubGroupSearchRepository itemSubGroupSearchRepository) {
        this.itemSubGroupRepository = itemSubGroupRepository;
        this.itemSubGroupSearchRepository = itemSubGroupSearchRepository;
    }

    /**
     * POST  /item-sub-groups : Create a new itemSubGroup.
     *
     * @param itemSubGroup the itemSubGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemSubGroup, or with status 400 (Bad Request) if the itemSubGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/item-sub-groups")
    @Timed
    public ResponseEntity<ItemSubGroup> createItemSubGroup(@Valid @RequestBody ItemSubGroup itemSubGroup) throws URISyntaxException {
        log.debug("REST request to save ItemSubGroup : {}", itemSubGroup);
        if (itemSubGroup.getId() != null) {
            throw new BadRequestAlertException("A new itemSubGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemSubGroup result = itemSubGroupRepository.save(itemSubGroup);
        itemSubGroupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/item-sub-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-sub-groups : Updates an existing itemSubGroup.
     *
     * @param itemSubGroup the itemSubGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemSubGroup,
     * or with status 400 (Bad Request) if the itemSubGroup is not valid,
     * or with status 500 (Internal Server Error) if the itemSubGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/item-sub-groups")
    @Timed
    public ResponseEntity<ItemSubGroup> updateItemSubGroup(@Valid @RequestBody ItemSubGroup itemSubGroup) throws URISyntaxException {
        log.debug("REST request to update ItemSubGroup : {}", itemSubGroup);
        if (itemSubGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemSubGroup result = itemSubGroupRepository.save(itemSubGroup);
        itemSubGroupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemSubGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-sub-groups : get all the itemSubGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itemSubGroups in body
     */
    @GetMapping("/item-sub-groups")
    @Timed
    public ResponseEntity<List<ItemSubGroup>> getAllItemSubGroups(Pageable pageable) {
        log.debug("REST request to get a page of ItemSubGroups");
        Page<ItemSubGroup> page = itemSubGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-sub-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /item-sub-groups/:id : get the "id" itemSubGroup.
     *
     * @param id the id of the itemSubGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemSubGroup, or with status 404 (Not Found)
     */
    @GetMapping("/item-sub-groups/{id}")
    @Timed
    public ResponseEntity<ItemSubGroup> getItemSubGroup(@PathVariable Long id) {
        log.debug("REST request to get ItemSubGroup : {}", id);
        Optional<ItemSubGroup> itemSubGroup = itemSubGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemSubGroup);
    }

    /**
     * DELETE  /item-sub-groups/:id : delete the "id" itemSubGroup.
     *
     * @param id the id of the itemSubGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/item-sub-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteItemSubGroup(@PathVariable Long id) {
        log.debug("REST request to delete ItemSubGroup : {}", id);

        itemSubGroupRepository.deleteById(id);
        itemSubGroupSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/item-sub-groups?query=:query : search for the itemSubGroup corresponding
     * to the query.
     *
     * @param query the query of the itemSubGroup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/item-sub-groups")
    @Timed
    public ResponseEntity<List<ItemSubGroup>> searchItemSubGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ItemSubGroups for query {}", query);
        Page<ItemSubGroup> page = itemSubGroupSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/item-sub-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
