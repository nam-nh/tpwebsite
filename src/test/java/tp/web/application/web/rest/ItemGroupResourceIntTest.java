package tp.web.application.web.rest;

import tp.web.application.TpwebsiteApp;

import tp.web.application.domain.ItemGroup;
import tp.web.application.repository.ItemGroupRepository;
import tp.web.application.repository.search.ItemGroupSearchRepository;
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
 * Test class for the ItemGroupResource REST controller.
 *
 * @see ItemGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TpwebsiteApp.class)
public class ItemGroupResourceIntTest {

    private static final String DEFAULT_ITEM_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_GROUP_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_GROUP_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ItemGroupRepository itemGroupRepository;

    /**
     * This repository is mocked in the tp.web.application.repository.search test package.
     *
     * @see tp.web.application.repository.search.ItemGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private ItemGroupSearchRepository mockItemGroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemGroupMockMvc;

    private ItemGroup itemGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemGroupResource itemGroupResource = new ItemGroupResource(itemGroupRepository, mockItemGroupSearchRepository);
        this.restItemGroupMockMvc = MockMvcBuilders.standaloneSetup(itemGroupResource)
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
    public static ItemGroup createEntity(EntityManager em) {
        ItemGroup itemGroup = new ItemGroup()
            .itemGroupName(DEFAULT_ITEM_GROUP_NAME)
            .itemGroupDescription(DEFAULT_ITEM_GROUP_DESCRIPTION);
        return itemGroup;
    }

    @Before
    public void initTest() {
        itemGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemGroup() throws Exception {
        int databaseSizeBeforeCreate = itemGroupRepository.findAll().size();

        // Create the ItemGroup
        restItemGroupMockMvc.perform(post("/api/item-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
            .andExpect(status().isCreated());

        // Validate the ItemGroup in the database
        List<ItemGroup> itemGroupList = itemGroupRepository.findAll();
        assertThat(itemGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ItemGroup testItemGroup = itemGroupList.get(itemGroupList.size() - 1);
        assertThat(testItemGroup.getItemGroupName()).isEqualTo(DEFAULT_ITEM_GROUP_NAME);
        assertThat(testItemGroup.getItemGroupDescription()).isEqualTo(DEFAULT_ITEM_GROUP_DESCRIPTION);

        // Validate the ItemGroup in Elasticsearch
        verify(mockItemGroupSearchRepository, times(1)).save(testItemGroup);
    }

    @Test
    @Transactional
    public void createItemGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemGroupRepository.findAll().size();

        // Create the ItemGroup with an existing ID
        itemGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemGroupMockMvc.perform(post("/api/item-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ItemGroup in the database
        List<ItemGroup> itemGroupList = itemGroupRepository.findAll();
        assertThat(itemGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the ItemGroup in Elasticsearch
        verify(mockItemGroupSearchRepository, times(0)).save(itemGroup);
    }

    @Test
    @Transactional
    public void checkItemGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemGroupRepository.findAll().size();
        // set the field null
        itemGroup.setItemGroupName(null);

        // Create the ItemGroup, which fails.

        restItemGroupMockMvc.perform(post("/api/item-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
            .andExpect(status().isBadRequest());

        List<ItemGroup> itemGroupList = itemGroupRepository.findAll();
        assertThat(itemGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemGroups() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

        // Get all the itemGroupList
        restItemGroupMockMvc.perform(get("/api/item-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemGroupName").value(hasItem(DEFAULT_ITEM_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemGroupDescription").value(hasItem(DEFAULT_ITEM_GROUP_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

        // Get the itemGroup
        restItemGroupMockMvc.perform(get("/api/item-groups/{id}", itemGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemGroup.getId().intValue()))
            .andExpect(jsonPath("$.itemGroupName").value(DEFAULT_ITEM_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.itemGroupDescription").value(DEFAULT_ITEM_GROUP_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemGroup() throws Exception {
        // Get the itemGroup
        restItemGroupMockMvc.perform(get("/api/item-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

        int databaseSizeBeforeUpdate = itemGroupRepository.findAll().size();

        // Update the itemGroup
        ItemGroup updatedItemGroup = itemGroupRepository.findById(itemGroup.getId()).get();
        // Disconnect from session so that the updates on updatedItemGroup are not directly saved in db
        em.detach(updatedItemGroup);
        updatedItemGroup
            .itemGroupName(UPDATED_ITEM_GROUP_NAME)
            .itemGroupDescription(UPDATED_ITEM_GROUP_DESCRIPTION);

        restItemGroupMockMvc.perform(put("/api/item-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemGroup)))
            .andExpect(status().isOk());

        // Validate the ItemGroup in the database
        List<ItemGroup> itemGroupList = itemGroupRepository.findAll();
        assertThat(itemGroupList).hasSize(databaseSizeBeforeUpdate);
        ItemGroup testItemGroup = itemGroupList.get(itemGroupList.size() - 1);
        assertThat(testItemGroup.getItemGroupName()).isEqualTo(UPDATED_ITEM_GROUP_NAME);
        assertThat(testItemGroup.getItemGroupDescription()).isEqualTo(UPDATED_ITEM_GROUP_DESCRIPTION);

        // Validate the ItemGroup in Elasticsearch
        verify(mockItemGroupSearchRepository, times(1)).save(testItemGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingItemGroup() throws Exception {
        int databaseSizeBeforeUpdate = itemGroupRepository.findAll().size();

        // Create the ItemGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemGroupMockMvc.perform(put("/api/item-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ItemGroup in the database
        List<ItemGroup> itemGroupList = itemGroupRepository.findAll();
        assertThat(itemGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ItemGroup in Elasticsearch
        verify(mockItemGroupSearchRepository, times(0)).save(itemGroup);
    }

    @Test
    @Transactional
    public void deleteItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);

        int databaseSizeBeforeDelete = itemGroupRepository.findAll().size();

        // Get the itemGroup
        restItemGroupMockMvc.perform(delete("/api/item-groups/{id}", itemGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemGroup> itemGroupList = itemGroupRepository.findAll();
        assertThat(itemGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ItemGroup in Elasticsearch
        verify(mockItemGroupSearchRepository, times(1)).deleteById(itemGroup.getId());
    }

    @Test
    @Transactional
    public void searchItemGroup() throws Exception {
        // Initialize the database
        itemGroupRepository.saveAndFlush(itemGroup);
        when(mockItemGroupSearchRepository.search(queryStringQuery("id:" + itemGroup.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(itemGroup), PageRequest.of(0, 1), 1));
        // Search the itemGroup
        restItemGroupMockMvc.perform(get("/api/_search/item-groups?query=id:" + itemGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemGroupName").value(hasItem(DEFAULT_ITEM_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemGroupDescription").value(hasItem(DEFAULT_ITEM_GROUP_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemGroup.class);
        ItemGroup itemGroup1 = new ItemGroup();
        itemGroup1.setId(1L);
        ItemGroup itemGroup2 = new ItemGroup();
        itemGroup2.setId(itemGroup1.getId());
        assertThat(itemGroup1).isEqualTo(itemGroup2);
        itemGroup2.setId(2L);
        assertThat(itemGroup1).isNotEqualTo(itemGroup2);
        itemGroup1.setId(null);
        assertThat(itemGroup1).isNotEqualTo(itemGroup2);
    }
}
