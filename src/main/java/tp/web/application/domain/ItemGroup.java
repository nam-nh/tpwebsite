package tp.web.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ItemGroup.
 */
@Entity
@Table(name = "item_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "itemgroup")
public class ItemGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_group_name", nullable = false)
    private String itemGroupName;

    @Column(name = "item_group_description")
    private String itemGroupDescription;

    @ManyToOne
    @JsonIgnoreProperties("itemGroups")
    private Category category;

    @OneToMany(mappedBy = "itemGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ItemSubGroup> itemSubGroups = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemGroupName() {
        return itemGroupName;
    }

    public ItemGroup itemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
        return this;
    }

    public void setItemGroupName(String itemGroupName) {
        this.itemGroupName = itemGroupName;
    }

    public String getItemGroupDescription() {
        return itemGroupDescription;
    }

    public ItemGroup itemGroupDescription(String itemGroupDescription) {
        this.itemGroupDescription = itemGroupDescription;
        return this;
    }

    public void setItemGroupDescription(String itemGroupDescription) {
        this.itemGroupDescription = itemGroupDescription;
    }

    public Category getCategory() {
        return category;
    }

    public ItemGroup category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ItemSubGroup> getItemSubGroups() {
        return itemSubGroups;
    }

    public ItemGroup itemSubGroups(Set<ItemSubGroup> itemSubGroups) {
        this.itemSubGroups = itemSubGroups;
        return this;
    }

    public ItemGroup addItemSubGroup(ItemSubGroup itemSubGroup) {
        this.itemSubGroups.add(itemSubGroup);
        itemSubGroup.setItemGroup(this);
        return this;
    }

    public ItemGroup removeItemSubGroup(ItemSubGroup itemSubGroup) {
        this.itemSubGroups.remove(itemSubGroup);
        itemSubGroup.setItemGroup(null);
        return this;
    }

    public void setItemSubGroups(Set<ItemSubGroup> itemSubGroups) {
        this.itemSubGroups = itemSubGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemGroup itemGroup = (ItemGroup) o;
        if (itemGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemGroup{" +
            "id=" + getId() +
            ", itemGroupName='" + getItemGroupName() + "'" +
            ", itemGroupDescription='" + getItemGroupDescription() + "'" +
            "}";
    }
}
