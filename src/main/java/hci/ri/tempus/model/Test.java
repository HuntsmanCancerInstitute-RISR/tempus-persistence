package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "TpsTest")
public class Test {


    private long idTest;
    private String code;
    private String name;
    private String description;
    private Order order;



    /**
     * No args constructor for use in serialization
     *
     */
    public Test() {
    }

    /**
     *
     * @param description
     * @param name
     * @param code
     */
    public Test(String code, String name, String description) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTest" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdTest() { return idTest; }
    public void setIdTest(long idTest) { this.idTest = idTest; }

    @Column(name="code")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name="description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonBackReference("order_test")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idOrder", nullable = false)
    public Order getOrder() { return order; }
    public void setOrder(Order o) { this.order = o; }


    @Override
    public int hashCode() {
        return Objects.hash(description,name, code);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Test) == false) {
            return false;
        }
        Test rhs = ((Test) other);


        return rhs.idTest == this.idTest;
    }

}
