package commands;

import Entities.Title;
import Entities.User;

import java.util.Scanner;

public class UnsubscribeFromTitle implements ICommand {

    private User currentUser;
    private final Scanner scanner;

    public UnsubscribeFromTitle(){
        scanner = new Scanner(System.in);
    }

    @Override
    public void execute(User user) {
        currentUser = user;
        unsubscribeUserFromTitle();
    }

    private void unsubscribeUserFromTitle(){
        System.out.println("Тайтлы, на которые вы подписаны:");
        var titleArray = currentUser.getTitles().values().toArray(new Title[]{});
        new MySubs().execute(currentUser);
        if (titleArray.length==0)
            return;
        System.out.println("Выберите тайтл для отписки по индексу");
        int index = getIndex(titleArray.length);
        currentUser.Unwatch(titleArray[index]);
    }

    private int getIndex(int upperBorder){
        int index = -1;
        do {
            try{
                index = Integer.parseInt(scanner.nextLine());//может создать исключение если введено не целое число
                index--;
                if(isNotInBounds(index, upperBorder))
                    throw new Exception();
            }
            catch (Exception e){
                System.out.println("Введнный индекс некорректен");//и тут я их отлавливаю и говорю что индекс некорректный
            }
        } while(isNotInBounds(index, upperBorder));
        return index;
    }

    private boolean isNotInBounds(int i, int upperBorder){
        return !(i > -1 & i < upperBorder);
    }
}
