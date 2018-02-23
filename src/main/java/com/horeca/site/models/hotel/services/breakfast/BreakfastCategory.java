package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.horeca.site.models.hotel.services.StandardServiceCategoryModel;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "breakfast_id", columnList = "service_id"))
@Audited
public class BreakfastCategory extends StandardServiceCategoryModel<BreakfastItem> {

    @JsonProperty("category") // to preserve the backward compatibility
    @Override
    public String getName() {
        return super.getName();
    }

    @JsonProperty("category") // to preserve the backward compatibility
    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
