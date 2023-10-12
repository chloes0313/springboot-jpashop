package jpabook.jpashop.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;

public enum OrderStatus {
    ORDER, CANCEL
}
