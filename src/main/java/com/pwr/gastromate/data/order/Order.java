package com.pwr.gastromate.data.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pwr.gastromate.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;
    private LocalDateTime orderDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;
    @Transient
    public BigDecimal getTotalOrderPrice(){
        BigDecimal sum = new BigDecimal("0");
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem item :orderItems){
            sum = sum.add(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return sum;
    }
}
