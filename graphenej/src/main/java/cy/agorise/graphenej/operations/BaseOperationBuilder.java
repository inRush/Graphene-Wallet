package cy.agorise.graphenej.operations;

import cy.agorise.graphenej.BaseOperation;

/**
 * Base template for all operation-specific factory classes.
 */
public abstract class BaseOperationBuilder {

    /**
     * Must be implemented and return the specific operation the
     * factory is supposed to build.
     *
     * @return: A usable instance of a given operation.
     */
    public abstract BaseOperation build();
}
