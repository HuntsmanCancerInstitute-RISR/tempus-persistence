package hci.ri.tempus.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("InheritedRelevantVariant")
public class InheritedRelevantVariant extends InheritedVariant {
}
