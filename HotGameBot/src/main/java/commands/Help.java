package commands;

import Entities.User;

public class Help implements ICommand {

    @Override
    public void execute(User user) {
        printHelp();
    }

    private void printHelp(){
        System.out.print(getHelpMessage());
    }

    private String getHelpMessage(){
        return """
                Доступны следующие команды:\r
                1. /wantToPlay переключит вас на рекомендации\r
                2. /sub позволит вам добавить тайтл в ваши подписки\r
                3. /quit выведет вас из меню\r
                4. /unsub позволит удалить тайтл из ваших подписок\r
                5. /mySubs выводит ваши подписки
                """;
    }
}
