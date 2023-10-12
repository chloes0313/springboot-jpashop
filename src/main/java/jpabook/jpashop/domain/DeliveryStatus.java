package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

@Embeddable
public enum DeliveryStatus {
    PENDING, SHIPPED, DELIVERED, CANCELED
}
