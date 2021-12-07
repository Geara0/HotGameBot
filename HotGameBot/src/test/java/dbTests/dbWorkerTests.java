package dbTests;

import db.DBWorker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class dbWorkerTests {
    @Test
    public void getSubscriptionsTest() {
        var db = new DBWorker();
        var user1 = db.getSubscriptions(1L);
        var user2 = db.getSubscriptions(2L);
        Assertions.assertArrayEquals("title1".toCharArray(), user1[0].toCharArray());
        Assertions.assertArrayEquals("title2".toCharArray(), user1[1].toCharArray());
        Assertions.assertArrayEquals("title2".toCharArray(), user2[0].toCharArray());
        Assertions.assertArrayEquals("title3".toCharArray(), user2[1].toCharArray());
    }

    @Test
    public void subAndUnsubTest() {
        var db = new DBWorker();
        db.subscribeUser(1L, "title4");
        Assertions.assertEquals(3, db.getSubscriptions(1L).length);
        db.unsubscribeUser(1L, "title4");
        Assertions.assertEquals(2, db.getSubscriptions(1L).length);
    }

    @Test
    public void unsubAllTest() {
        var db = new DBWorker();
        db.unsubscribeAllUser(1L);
        Assertions.assertEquals(0, db.getSubscriptions(1L).length);
        db.subscribeUser(1L, "title1");
        db.subscribeUser(1L, "title2");
        Assertions.assertEquals(2, db.getSubscriptions(1L).length);

    }
}
