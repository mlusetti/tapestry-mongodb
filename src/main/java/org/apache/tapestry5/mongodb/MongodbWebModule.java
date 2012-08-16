package org.apache.tapestry5.mongodb;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.internal.mongodb.MorphiaSessionSourceImpl;
import org.apache.tapestry5.internal.mongodb.MorphiaValueEncoder;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.LoggerSource;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.*;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines services which are responsible for MongoDB initializations and connections.
 */
public class MongodbWebModule
{
    public static void bind(ServiceBinder binder)
    {
    }

    @Contribute(value = MorphiaSessionSource.class)
    public static void contributeDefaultPackageName(Configuration<String> configuration,
                                                               @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM)
                                                               String appRootPackage)
    {
        configuration.add(appRootPackage + ".mongodb");
    }


    public static MorphiaSessionSource buildMorphiaSessionSource(
            Logger logger, MongoDBSource mongoDBSource,
            @Symbol(MongoDBSymbols.DEFAULT_DB_NAME) String defaultDbName,
            ClassNameLocator classNameLocator,
            final Collection<String> packageNames)
    {
        MorphiaSessionSource morphiaSessionSource = new MorphiaSessionSourceImpl(
                                                            logger, mongoDBSource,
                                                            defaultDbName, classNameLocator,
                                                            packageNames);


        return morphiaSessionSource;
    }

    /**
     *
     */
//    @SuppressWarnings("unchecked")
//    public static void contributeValueEncoderSource(MappedConfiguration<Class, ValueEncoderFactory> configuration,
//                                                    final MorphiaSessionSource morphiaSessionSource,
//                                                    final TypeCoercer typeCoercer, final PropertyAccess propertyAccess,
//                                                    final LoggerSource loggerSource)
//    {
//        Iterator<Class> mappings = morphiaSessionSource.mappedClass().iterator();
//
//        while (mappings.hasNext())
//        {
//            final Class entityClass = mappings.next();
//
//            if (entityClass != null)
//            {
//                ValueEncoderFactory factory = new ValueEncoderFactory()
//                {
//                    public ValueEncoder create(Class type)
//                    {
//                        return new MorphiaValueEncoder(entityClass, morphiaSessionSource, propertyAccess,
//                                typeCoercer, loggerSource.getLogger(entityClass));
//                    }
//                };
//
//                configuration.add(entityClass, factory);
//
//            }
//        }
//    }

    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration)
    {
        configuration.add(new CoercionTuple(String.class, ObjectId.class,
                new Coercion<String, ObjectId>() {
                    /**
                     * Converts an input value.
                     *
                     * @param input the input value
                     */
                    @Override
                    public ObjectId coerce(String input)
                    {
                        return ObjectId.massageToObjectId(input);
                    }
                }));
    }

}
