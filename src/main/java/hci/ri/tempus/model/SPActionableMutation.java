package hci.ri.tempus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TpsSPActionableMutation")
public class SPActionableMutation {

    private long idSPActionableMutations;
    private String gene;
    private String geneDescription;
    private String display;
    private String hgncId;
    private String entrezId;
    private Set<Variant> variants;

    public SPActionableMutation(){

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSPActionableMutation" , nullable = false, unique = true)
    @JsonIgnore
    public long getIdSPActionableMutations() { return idSPActionableMutations; }
    public void setIdSPActionableMutations(long idSPActionableMutations) { this.idSPActionableMutations = idSPActionableMutations; }

    @Column(name="gene")
    public String getGene() { return gene; }
    public void setGene(String gene) { this.gene = gene; }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name="geneDescription")
    public String getGeneDescription() { return geneDescription; }
    public void setGeneDescription(String geneDescription) {  this.geneDescription = geneDescription; }

    @Column(name="display")
    public String getDisplay() { return display; }
    public void setDisplay(String display) { this.display = display; }

    @Column(name="hgncId")
    public String getHgncId() { return hgncId; }
    public void setHgncId(String hgncId) { this.hgncId = hgncId; }

    @Column(name="entrezId")
    public String getEntrezId() { return entrezId; }
    public void setEntrezId(String entrezId) { this.entrezId = entrezId; }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    @JoinColumn(name="idSPActionableMutation", nullable= false)
    public Set<Variant> getVariants() { return variants; }
    public void setVariants(Set<Variant> variants) { this.variants = variants; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SPActionableMutation that = (SPActionableMutation) o;
        return idSPActionableMutations == that.idSPActionableMutations &&
                Objects.equals(gene, that.gene) &&
                Objects.equals(display, that.display) &&
                Objects.equals(hgncId, that.hgncId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSPActionableMutations, gene, display,hgncId);
    }
}
