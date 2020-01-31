package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TpsInheritedVariant")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="variantCategory", discriminatorType = DiscriminatorType.STRING)
public class InheritedVariant {

    private long idInheritedVariant;
    private Result result;
    private String note;
    private Set<InheritedVariantValue> values;


    public InheritedVariant(){
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInheritedVariant" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdInheritedVariant() { return idInheritedVariant; }
    public void setIdInheritedVariant(long idInheritedVariant) {
        this.idInheritedVariant = idInheritedVariant;
    }

    @JsonBackReference("result_inheritedVariant")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idResult", nullable = false)
    public Result getResult() {return result; }
    public void setResult(Result result) {
        this.result = result;
    }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idInheritedVariant", nullable= false)
    public Set<InheritedVariantValue> getValues() { return values; }
    public void setValues(Set<InheritedVariantValue> values) {
        this.values = values;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InheritedVariant that = (InheritedVariant) o;
        return idInheritedVariant == that.idInheritedVariant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInheritedVariant);
    }
}
