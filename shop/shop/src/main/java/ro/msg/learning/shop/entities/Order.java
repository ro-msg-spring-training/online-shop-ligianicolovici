package ro.msg.learning.shop.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    @EqualsAndHashCode.Include
    private LocalDateTime createdAt;
    @EqualsAndHashCode.Include
    private String addressCountry;
    @EqualsAndHashCode.Include
    private String addressCity;
    @EqualsAndHashCode.Include
    private String addressStreet;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_location",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    @Fetch(value = FetchMode.SELECT )
    private Set<Location> shippedFrom;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<OrderDetail> orderDetails;


}
