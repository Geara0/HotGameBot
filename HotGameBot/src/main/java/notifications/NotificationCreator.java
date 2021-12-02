package notifications;

import entities.*;

public class NotificationCreator {
    public static String create(Game game, Title titleUpdated) {
        String notification = null;
        Title title = game.getTitle();
        if (titleUpdated.getPrice() < title.getPrice()) {
            notification = String.format("New best price for %s:\n Prev: %s,\nCurrent: %s\nHotGame link: %s,\nBuy link: %s",
                    title.getName(), title.getPrice(), titleUpdated.getPrice(), title.getLink(), titleUpdated.getBuyLink());
        }
        return notification;
    }
}
