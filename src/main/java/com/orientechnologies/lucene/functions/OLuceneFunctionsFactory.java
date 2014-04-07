package com.orientechnologies.lucene.functions;

import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.sql.functions.OSQLFunction;
import com.orientechnologies.orient.core.sql.functions.OSQLFunctionFactory;
import com.orientechnologies.orient.core.sql.functions.math.OSQLFunctionAverage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by enricorisa on 02/04/14.
 */
public class OLuceneFunctionsFactory implements OSQLFunctionFactory {

  private static final Map<String, Object> FUNCTIONS = new HashMap<String, Object>();

  static {
    register(OLuceneNearFunction.NAME, OLuceneNearFunction.class);
  }

  @Override
  public boolean hasFunction(String iName) {
    return FUNCTIONS.containsKey(iName);
  }

  @Override
  public Set<String> getFunctionNames() {
    return FUNCTIONS.keySet();
  }

  @Override
  public OSQLFunction createFunction(String name) throws OCommandExecutionException {
    final Object obj = FUNCTIONS.get(name);

    if (obj == null)
      throw new OCommandExecutionException("Unknown function name :" + name);

    if (obj instanceof OSQLFunction)
      return (OSQLFunction) obj;
    else {
      // it's a class
      final Class<?> clazz = (Class<?>) obj;
      try {
        return (OSQLFunction) clazz.newInstance();
      } catch (Exception e) {
        throw new OCommandExecutionException("Error in creation of function " + name
            + "(). Probably there is not an empty constructor or the constructor generates errors", e);
      }
    }

  }

  public static void register(final String iName, final Object iImplementation) {
    FUNCTIONS.put(iName.toLowerCase(), iImplementation);
  }
}
