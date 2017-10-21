package com.horeca.site.models.hotel.services.roomservice;

import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "room_service_id", columnList = "room_service_id"))
@Audited
public class RoomServiceCategory {

    public enum Category { DRINK, SNACK }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_service_category_id")
    private Set<RoomServiceItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<RoomServiceItem> getItems() {
        return items;
    }

    public void setItems(Set<RoomServiceItem> items) {
        this.items = items;
    }
}
