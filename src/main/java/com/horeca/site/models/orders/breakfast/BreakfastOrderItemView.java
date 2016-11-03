package com.horeca.site.models.orders.breakfast;

import com.horeca.site.models.hotel.services.breakfast.BreakfastItemView;

public class BreakfastOrderItemView {

    private BreakfastItemView item;

    private Integer count;

    public BreakfastItemView getItem() {
        return item;
    }

    public void setItem(BreakfastItemView item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
