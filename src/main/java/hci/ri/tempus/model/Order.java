package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "TpsOrder")
public class Order {

    private long idOrder;
    private String institution;
    private String physician;
    private String tempusOrderId;
    private String accessionId;
    private Test test;
    private TempusFile tempusFile;

    /**
     * No args constructor for use in serialization
     *
     */
    public Order() {
    }

    /**
     *
     * @param tempusOrderId
     * @param test
     * @param accessionId
     * @param physician
     * @param institution
     */
    public Order(String institution, String physician, String tempusOrderId, String accessionId, Test test) {
        super();
        this.institution = institution;
        this.physician = physician;
        this.tempusOrderId = tempusOrderId;
        this.accessionId = accessionId;
        this.test = test;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrder" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdOrder() { return idOrder; }
    public void setIdOrder(long idOrder) { this.idOrder = idOrder; }

    @Column(name="institution")
    public String getInstitution() {
        return institution;
    }
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @Column(name="physician")
    public String getPhysician() {
        return physician;
    }
    public void setPhysician(String physician) {
        this.physician = physician;
    }

    @JsonIgnore
    @Transient
    public String getTempusOrder_id() {
        return tempusOrderId;
    }
    public void setTempusOrderId(String tempusOrderId) {
        this.tempusOrderId = tempusOrderId;
    }

    @Column(name = "accessionId" )
    public String getAccessionId() { return accessionId; }
    public void setAccessionId(String accessionId) { this.accessionId = accessionId; }

    @JsonManagedReference("order_test")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    public Test getTest() {
        return test;
    }
    public void setTest(Test test) {
        this.test = test;
    }

    @JsonBackReference("tempusFile_order")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTempusFile", nullable = false)
    public TempusFile getTempusFile() { return tempusFile; }
    public void setTempusFile(TempusFile tempusFile) { this.tempusFile = tempusFile; }


    @Override
    public int hashCode() {
        return Objects.hash(tempusOrderId, test,accessionId, physician, institution);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Order) == false) {
            return false;
        }
        Order rhs = ((Order) other);
        return rhs.idOrder == this.idOrder;
    }
}
