package hci.ri.tempus.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("InheritedIncidentalFindings")
public class InheritedIncidentalFinding extends InheritedVariant {
}
