package org.apache.tapestry5.mongodb;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;

import java.util.Collection;


/**
 *
 */
public interface MorphiaSessionSource
{
    public Collection<Class> mappedClass();

    public Datastore openDefaultDatastore();

    public Datastore openDatastore(String dbName);

    public Datastore openDefaultDatastore(String user, String pw);

    public Datastore openDatastore(String dbName, String user, String pw);


}
