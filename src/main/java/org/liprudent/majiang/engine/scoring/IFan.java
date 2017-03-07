package org.liprudent.majiang.engine.scoring;

import java.io.Serializable;
import java.util.Collection;

public interface IFan extends Serializable{

    /**
     *
     * @return the number as stated in chines official rules
     */
    Short getNumber();

    /**
     *
     * @return The english name
     */
    String getName();

    /**
     *
     * @return A fan implies other fans that shouldn't be added to final score
     */
    Collection<IFan> getImplied();
    
}
