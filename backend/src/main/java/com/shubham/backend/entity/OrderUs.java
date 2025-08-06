package com.shubham.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Getter
@Setter
@Entity
@Table(name = "order_us", schema = "public")
public class OrderUs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_us_id_gen")
    @SequenceGenerator(name = "order_us_id_gen", sequenceName = "order_us_order_id_seq", allocationSize = 1)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_accepted", nullable = false)
    private Boolean isAccepted = false;

    @ColumnDefault("false")
    @Column(name = "is_completed")
    private Boolean isCompleted;

    public OrderUs(User user, User seller, int price) {
        this.user = user;
        this.seller = seller;
        this.price = price;
    }

    public OrderUs() {

    }
}
