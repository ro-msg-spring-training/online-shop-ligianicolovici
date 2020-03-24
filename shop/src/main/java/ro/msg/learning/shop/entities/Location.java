package ro.msg.learning.shop.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ro.msg.learning.shop.utils.Address;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "location")
@EqualsAndHashCode(exclude = {"stocks", "revenues", "orders"})
@ToString(exclude = {"stocks", "revenues", "orders"})
public class Location {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "country", column = @Column(name = "address_country")),
            @AttributeOverride(name = "street", column = @Column(name = "address_street"))
    })
    private Address address;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Stock> stocks;

    @ManyToMany(mappedBy = "shippedFrom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SELECT)
    private Set<Order> orders;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Revenue> revenues;
}
