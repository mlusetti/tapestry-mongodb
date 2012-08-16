package org.apache.tapestry5.internal.mongodb;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.mongodb.MongoDBSource;
import org.apache.tapestry5.mongodb.MorphiaSessionSource;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class MorphiaSessionSourceImpl implements MorphiaSessionSource
{
    private final Logger logger;
    private final Mongo mongo;
    private final Morphia morphia;
    private final String defaultDbName;

    private final Collection<Class> mappedClass;

    public MorphiaSessionSourceImpl(Logger logger, MongoDBSource mongoDBSource,
                                    String defaultDbName,
                                    ClassNameLocator classNameLocator, Collection<String> packageNames)
    {
        this.logger = logger;

        this.mongo = mongoDBSource.getMongo();

        this.morphia = new Morphia();

        this.defaultDbName = defaultDbName;

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        mappedClass = new ArrayList<Class>();

        for (String packageName : packageNames)
        {
            for (String className : classNameLocator.locateClassNames(packageName))
            {
                try
                {
                    Class entity = contextClassLoader.loadClass(className);

                    morphia.map(entity);

                    mappedClass.add(entity);

                } catch (ClassNotFoundException cnfe)
                {
                    logger.error("Unable to locate {} within {}",className, packageName);

                    throw new RuntimeException(cnfe);
                }
            }
        }
    }

    @Override
    public Collection<Class> mappedClass()
    {
        return mappedClass;
    }

    @Override
    public Datastore openDatastore(String dbName)
    {
        return morphia.createDatastore(mongo, dbName);
    }

    @Override
    public Datastore openDefaultDatastore()
    {
        return morphia.createDatastore(mongo, defaultDbName);
    }

    @Override
    public Datastore openDatastore(String dbName, String user, String pw)
    {
        return morphia.createDatastore(mongo, dbName, user, pw.toCharArray());
    }

    @Override
    public Datastore openDefaultDatastore(String user, String pw)
    {
        return morphia.createDatastore(mongo, defaultDbName, user, pw.toCharArray());
    }
}
