package com.pwr.gastromate.data;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderItemId  implements Serializable {
    private Integer menuItemId;
    private Integer orderId;
}
