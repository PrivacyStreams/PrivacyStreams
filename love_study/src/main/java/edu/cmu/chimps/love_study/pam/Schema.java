package edu.cmu.chimps.love_study.pam;


/**
 * Classes that implement this interface represent a json schema properties definitions by defining
 * static nested classes that implement {@link Property} interface.<BR>
 * Objects instantiated out of classes that implement this interface represent one measurement.<BR>
 */
public interface Schema {

    public String getSchemaName();

}
