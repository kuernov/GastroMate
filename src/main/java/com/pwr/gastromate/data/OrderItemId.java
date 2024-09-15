package com.pwr.gastromate.data;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class OrderItemId  implements Serializable {
    private Integer menuItemId;
    private Integer orderId;
}
