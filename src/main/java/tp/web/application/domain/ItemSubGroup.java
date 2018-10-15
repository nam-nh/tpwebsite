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
 * A ItemSubGroup.
 */
@Entity
@Table(name = "item_sub_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "itemsubgroup")
public class ItemSubGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_sub_group_name", nullable = false)
    private String itemSubGroupName;

    @Column(name = "item_sub_group_description")
    private String itemSubGroupDescription;

    @ManyToOne
    @JsonIgnoreProperties("itemSubGroups")
    private ItemGroup itemGroup;

    @OneToMany(mappedBy = "itemSubGroup", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Item> items = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemSubGroupName() {
        return itemSubGroupName;
    }

    public ItemSubGroup itemSubGroupName(String itemSubGroupName) {
        this.itemSubGroupName = itemSubGroupName;
        return this;
    }

    public void setItemSubGroupName(String itemSubGroupName) {
        this.itemSubGroupName = itemSubGroupName;
    }

    public String getItemSubGroupDescription() {
        return itemSubGroupDescription;
    }

    public ItemSubGroup itemSubGroupDescription(String itemSubGroupDescription) {
        this.itemSubGroupDescription = itemSubGroupDescription;
        return this;
    }

    public void setItemSubGroupDescription(String itemSubGroupDescription) {
        this.itemSubGroupDescription = itemSubGroupDescription;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public ItemSubGroup itemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
        return this;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public Set<Item> getItems() {
        return items;
    }

    public ItemSubGroup items(Set<Item> items) {
        this.items = items;
        return this;
    }

    public ItemSubGroup addItem(Item item) {
        this.items.add(item);
        item.setItemSubGroup(this);
        return this;
    }

    public ItemSubGroup removeItem(Item item) {
        this.items.remove(item);
        item.setItemSubGroup(null);
        return this;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
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
        ItemSubGroup itemSubGroup = (ItemSubGroup) o;
        if (itemSubGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemSubGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemSubGroup{" +
            "id=" + getId() +
            ", itemSubGroupName='" + getItemSubGroupName() + "'" +
            ", itemSubGroupDescription='" + getItemSubGroupDescription() + "'" +
            "}";
    }
}
