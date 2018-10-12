package tp.web.application.web.rest;

import tp.web.application.TpwebsiteApp;

import tp.web.application.domain.ItemSubGroup;
import tp.web.application.repository.ItemSubGroupRepository;
import tp.web.application.repository.search.ItemSubGroupSearchRepository;
import tp.web.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static tp.web.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemSubGroupResource REST controller.
 *
 * @see ItemSubGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TpwebsiteApp.class)
public class ItemSubGroupResourceIntTest {

    private static final String DEFAULT_ITEM_SUB_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SUB_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_SUB_GROUP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SUB_GROUP_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ItemSubGroupRepository itemSubGroupRepository;

    /**
     * This repository is mocked in the tp.web.application.repository.search test package.
     *
     * @see tp.web.application.repository.search.ItemSubGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemSubGroupSearchRepository mockItemSubGroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemSubGroupMockMvc;

    private ItemSubGroup itemSubGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemSubGroupResource itemSubGroupResource = new ItemSubGroupResource(itemSubGroupRepository, mockItemSubGroupSearchRepository);
        this.restItemSubGroupMockMvc = MockMvcBuilders.standaloneSetup(itemSubGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemSubGroup createEntity(EntityManager em) {
        ItemSubGroup itemSubGroup = new ItemSubGroup()
            .itemSubGroupName(DEFAULT_ITEM_SUB_GROUP_NAME)
            .itemSubGroupDescription(DEFAULT_ITEM_SUB_GROUP_DESCRIPTION);
        return itemSubGroup;
    }

    @Before
    public void initTest() {
        itemSubGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemSubGroup() throws Exception {
        int databaseSizeBeforeCreate = itemSubGroupRepository.findAll().size();

        // Create the ItemSubGroup
        restItemSubGroupMockMvc.perform(post("/api/item-sub-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubGroup)))
            .andExpect(status().isCreated());

        // Validate the ItemSubGroup in the database
        List<ItemSubGroup> itemSubGroupList = itemSubGroupRepository.findAll();
        assertThat(itemSubGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ItemSubGroup testItemSubGroup = itemSubGroupList.get(itemSubGroupList.size() - 1);
        assertThat(testItemSubGroup.getItemSubGroupName()).isEqualTo(DEFAULT_ITEM_SUB_GROUP_NAME);
        assertThat(testItemSubGroup.getItemSubGroupDescription()).isEqualTo(DEFAULT_ITEM_SUB_GROUP_DESCRIPTION);

        // Validate the ItemSubGroup in Elasticsearch
        verify(mockItemSubGroupSearchRepository, times(1)).save(testItemSubGroup);
    }

    @Test
    @Transactional
    public void createItemSubGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemSubGroupRepository.findAll().size();

        // Create the ItemSubGroup with an existing ID
        itemSubGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemSubGroupMockMvc.perform(post("/api/item-sub-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ItemSubGroup in the database
        List<ItemSubGroup> itemSubGroupList = itemSubGroupRepository.findAll();
        assertThat(itemSubGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemSubGroup in Elasticsearch
        verify(mockItemSubGroupSearchRepository, times(0)).save(itemSubGroup);
    }

    @Test
    @Transactional
    public void checkItemSubGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemSubGroupRepository.findAll().size();
        // set the field null
        itemSubGroup.setItemSubGroupName(null);

        // Create the ItemSubGroup, which fails.

        restItemSubGroupMockMvc.perform(post("/api/item-sub-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubGroup)))
            .andExpect(status().isBadRequest());

        List<ItemSubGroup> itemSubGroupList = itemSubGroupRepository.findAll();
        assertThat(itemSubGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemSubGroups() throws Exception {
        // Initialize the database
        itemSubGroupRepository.saveAndFlush(itemSubGroup);

        // Get all the itemSubGroupList
        restItemSubGroupMockMvc.perform(get("/api/item-sub-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemSubGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemSubGroupName").value(hasItem(DEFAULT_ITEM_SUB_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemSubGroupDescription").value(hasItem(DEFAULT_ITEM_SUB_GROUP_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getItemSubGroup() throws Exception {
        // Initialize the database
        itemSubGroupRepository.saveAndFlush(itemSubGroup);

        // Get the itemSubGroup
        restItemSubGroupMockMvc.perform(get("/api/item-sub-groups/{id}", itemSubGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemSubGroup.getId().intValue()))
            .andExpect(jsonPath("$.itemSubGroupName").value(DEFAULT_ITEM_SUB_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.itemSubGroupDescription").value(DEFAULT_ITEM_SUB_GROUP_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemSubGroup() throws Exception {
        // Get the itemSubGroup
        restItemSubGroupMockMvc.perform(get("/api/item-sub-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemSubGroup() throws Exception {
        // Initialize the database
        itemSubGroupRepository.saveAndFlush(itemSubGroup);

        int databaseSizeBeforeUpdate = itemSubGroupRepository.findAll().size();

        // Update the itemSubGroup
        ItemSubGroup updatedItemSubGroup = itemSubGroupRepository.findById(itemSubGroup.getId()).get();
        // Disconnect from session so that the updates on updatedItemSubGroup are not directly saved in db
        em.detach(updatedItemSubGroup);
        updatedItemSubGroup
            .itemSubGroupName(UPDATED_ITEM_SUB_GROUP_NAME)
            .itemSubGroupDescription(UPDATED_ITEM_SUB_GROUP_DESCRIPTION);

        restItemSubGroupMockMvc.perform(put("/api/item-sub-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemSubGroup)))
            .andExpect(status().isOk());

        // Validate the ItemSubGroup in the database
        List<ItemSubGroup> itemSubGroupList = itemSubGroupRepository.findAll();
        assertThat(itemSubGroupList).hasSize(databaseSizeBeforeUpdate);
        ItemSubGroup testItemSubGroup = itemSubGroupList.get(itemSubGroupList.size() - 1);
        assertThat(testItemSubGroup.getItemSubGroupName()).isEqualTo(UPDATED_ITEM_SUB_GROUP_NAME);
        assertThat(testItemSubGroup.getItemSubGroupDescription()).isEqualTo(UPDATED_ITEM_SUB_GROUP_DESCRIPTION);

        // Validate the ItemSubGroup in Elasticsearch
        verify(mockItemSubGroupSearchRepository, times(1)).save(testItemSubGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingItemSubGroup() throws Exception {
        int databaseSizeBeforeUpdate = itemSubGroupRepository.findAll().size();

        // Create the ItemSubGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemSubGroupMockMvc.perform(put("/api/item-sub-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ItemSubGroup in the database
        List<ItemSubGroup> itemSubGroupList = itemSubGroupRepository.findAll();
        assertThat(itemSubGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemSubGroup in Elasticsearch
        verify(mockItemSubGroupSearchRepository, times(0)).save(itemSubGroup);
    }

    @Test
    @Transactional
    public void deleteItemSubGroup() throws Exception {
        // Initialize the database
        itemSubGroupRepository.saveAndFlush(itemSubGroup);

        int databaseSizeBeforeDelete = itemSubGroupRepository.findAll().size();

        // Get the itemSubGroup
        restItemSubGroupMockMvc.perform(delete("/api/item-sub-groups/{id}", itemSubGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemSubGroup> itemSubGroupList = itemSubGroupRepository.findAll();
        assertThat(itemSubGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemSubGroup in Elasticsearch
        verify(mockItemSubGroupSearchRepository, times(1)).deleteById(itemSubGroup.getId());
    }

    @Test
    @Transactional
    public void searchItemSubGroup() throws Exception {
        // Initialize the database
        itemSubGroupRepository.saveAndFlush(itemSubGroup);
        when(mockItemSubGroupSearchRepository.search(queryStringQuery("id:" + itemSubGroup.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(itemSubGroup), PageRequest.of(0, 1), 1));
        // Search the itemSubGroup
        restItemSubGroupMockMvc.perform(get("/api/_search/item-sub-groups?query=id:" + itemSubGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemSubGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemSubGroupName").value(hasItem(DEFAULT_ITEM_SUB_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemSubGroupDescription").value(hasItem(DEFAULT_ITEM_SUB_GROUP_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemSubGroup.class);
        ItemSubGroup itemSubGroup1 = new ItemSubGroup();
        itemSubGroup1.setId(1L);
        ItemSubGroup itemSubGroup2 = new ItemSubGroup();
        itemSubGroup2.setId(itemSubGroup1.getId());
        assertThat(itemSubGroup1).isEqualTo(itemSubGroup2);
        itemSubGroup2.setId(2L);
        assertThat(itemSubGroup1).isNotEqualTo(itemSubGroup2);
        itemSubGroup1.setId(null);
        assertThat(itemSubGroup1).isNotEqualTo(itemSubGroup2);
    }
}
