package ro.msg.learning.shop.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "location")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Location {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @EqualsAndHashCode.Include
    private String name;
    @EqualsAndHashCode.Include
    private String addressCountry;
    @EqualsAndHashCode.Include
    private String addressCity;
    @EqualsAndHashCode.Include
    private String addressStreet;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Stock> stocks;

    @ManyToMany(mappedBy = "shippedFrom")
    @Fetch(value = FetchMode.SELECT)
    private Set<Order> orders;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Revenue> revenues;
}
