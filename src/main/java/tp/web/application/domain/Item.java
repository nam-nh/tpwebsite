package tp.web.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @NotNull
    @Column(name = "item_image", nullable = false)
    private String itemImage;

    @Column(name = "item_description")
    private String itemDescription;

    @NotNull
    @Column(name = "item_quantity", nullable = false)
    private Long itemQuantity;

    @NotNull
    @Column(name = "item_price", nullable = false)
    private Long itemPrice;

    @ManyToOne
    @JsonIgnoreProperties("items")
    private ItemSubGroup itemSubGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public Item itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public Item itemImage(String itemImage) {
        this.itemImage = itemImage;
        return this;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public Item itemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
        return this;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Long getItemQuantity() {
        return itemQuantity;
    }

    public Item itemQuantity(Long itemQuantity) {
        this.itemQuantity = itemQuantity;
        return this;
    }

    public void setItemQuantity(Long itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Long getItemPrice() {
        return itemPrice;
    }

    public Item itemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }

    public ItemSubGroup getItemSubGroup() {
        return itemSubGroup;
    }

    public Item itemSubGroup(ItemSubGroup itemSubGroup) {
        this.itemSubGroup = itemSubGroup;
        return this;
    }

    public void setItemSubGroup(ItemSubGroup itemSubGroup) {
        this.itemSubGroup = itemSubGroup;
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
        Item item = (Item) o;
        if (item.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", itemImage='" + getItemImage() + "'" +
            ", itemDescription='" + getItemDescription() + "'" +
            ", itemQuantity=" + getItemQuantity() +
            ", itemPrice=" + getItemPrice() +
            "}";
    }
}
