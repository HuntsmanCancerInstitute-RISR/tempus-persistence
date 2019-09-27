package hci.ri.tempus.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("InheritedVariantsOfUnknownSignificance")
public class InheritedUnknownSignificanceVariant extends InheritedVariant {
}
