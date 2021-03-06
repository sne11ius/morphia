package org.mongodb.morphia.mapping;


import java.io.Serializable;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.mongodb.morphia.TestBase;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import com.mongodb.BasicDBObject;
import org.junit.Assert;


/**
 * @author scott hernandez
 */
public class MapWithDotInKey extends TestBase {

  private static class Goo implements Serializable {
    static final long serialVersionUID = 1L;
    @Id ObjectId id = new ObjectId();
    String name;

    Goo() {
    }

    Goo(final String n) {
      name = n;
    }
  }

  private static class E {
    @Id ObjectId id;

    @Embedded
    final MyMap mymap = new MyMap();
  }

  private static class MyMap extends BasicDBObject {
    private static final long serialVersionUID = 1L;
  }

  @Test
  public void testMapping() throws Exception {
    E e = new E();
    e.mymap.put("a.b", "a");
    e.mymap.put("c.e.g", "b");

    try {
      ds.save(e);
    } catch (Exception ex) {
      return;
    }

    Assert.assertFalse("Should have got rejection for dot in field names", true);
    e = ds.get(e);
    Assert.assertEquals("a", e.mymap.get("a.b"));
    Assert.assertEquals("b", e.mymap.get("c.e.g"));
  }
}
