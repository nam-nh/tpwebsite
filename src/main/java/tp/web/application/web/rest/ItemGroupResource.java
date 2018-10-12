package tp.web.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import tp.web.application.domain.ItemGroup;
import tp.web.application.repository.ItemGroupRepository;
import tp.web.application.repository.search.ItemGroupSearchRepository;
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
 * REST controller for managing ItemGroup.
 */
@RestController
@RequestMapping("/api")
public class ItemGroupResource {

    private final Logger log = LoggerFactory.getLogger(ItemGroupResource.class);

    private static final String ENTITY_NAME = "itemGroup";

    private final ItemGroupRepository itemGroupRepository;

    private final ItemGroupSearchRepository itemGroupSearchRepository;

    public ItemGroupResource(ItemGroupRepository itemGroupRepository, ItemGroupSearchRepository itemGroupSearchRepository) {
        this.itemGroupRepository = itemGroupRepository;
        this.itemGroupSearchRepository = itemGroupSearchRepository;
    }

    /**
     * POST  /item-groups : Create a new itemGroup.
     *
     * @param itemGroup the itemGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemGroup, or with status 400 (Bad Request) if the itemGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/item-groups")
    @Timed
    public ResponseEntity<ItemGroup> createItemGroup(@Valid @RequestBody ItemGroup itemGroup) throws URISyntaxException {
        log.debug("REST request to save ItemGroup : {}", itemGroup);
        if (itemGroup.getId() != null) {
            throw new BadRequestAlertException("A new itemGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemGroup result = itemGroupRepository.save(itemGroup);
        itemGroupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/item-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-groups : Updates an existing itemGroup.
     *
     * @param itemGroup the itemGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemGroup,
     * or with status 400 (Bad Request) if the itemGroup is not valid,
     * or with status 500 (Internal Server Error) if the itemGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/item-groups")
    @Timed
    public ResponseEntity<ItemGroup> updateItemGroup(@Valid @RequestBody ItemGroup itemGroup) throws URISyntaxException {
        log.debug("REST request to update ItemGroup : {}", itemGroup);
        if (itemGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemGroup result = itemGroupRepository.save(itemGroup);
        itemGroupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-groups : get all the itemGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itemGroups in body
     */
    @GetMapping("/item-groups")
    @Timed
    public ResponseEntity<List<ItemGroup>> getAllItemGroups(Pageable pageable) {
        log.debug("REST request to get a page of ItemGroups");
        Page<ItemGroup> page = itemGroupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /item-groups/:id : get the "id" itemGroup.
     *
     * @param id the id of the itemGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemGroup, or with status 404 (Not Found)
     */
    @GetMapping("/item-groups/{id}")
    @Timed
    public ResponseEntity<ItemGroup> getItemGroup(@PathVariable Long id) {
        log.debug("REST request to get ItemGroup : {}", id);
        Optional<ItemGroup> itemGroup = itemGroupRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemGroup);
    }

    /**
     * DELETE  /item-groups/:id : delete the "id" itemGroup.
     *
     * @param id the id of the itemGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/item-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteItemGroup(@PathVariable Long id) {
        log.debug("REST request to delete ItemGroup : {}", id);

        itemGroupRepository.deleteById(id);
        itemGroupSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/item-groups?query=:query : search for the itemGroup corresponding
     * to the query.
     *
     * @param query the query of the itemGroup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/item-groups")
    @Timed
    public ResponseEntity<List<ItemGroup>> searchItemGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ItemGroups for query {}", query);
        Page<ItemGroup> page = itemGroupSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/item-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
