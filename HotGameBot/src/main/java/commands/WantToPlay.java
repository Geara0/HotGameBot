package commands;

import Entities.Title;
import Entities.User;

import java.util.HashMap;
import java.util.Scanner;

public class WantToPlay implements ICommand {
    private HashMap<String, Title> titleMapping;

    public WantToPlay(HashMap<String,Title> titles){
        titleMapping = titles;
    }

    @Override
    public void execute(User user){
        printPriceRecommendations();
    }

    private void printPriceRecommendations(){
        String cannotFind = "Мы не можем подобрать тайтл за такую стоимость";
        String recs = getPriceRecommendations();
        String message = recs.length() > 0 ? recs : cannotFind;
        System.out.println(message);
    }

    private String getPriceRecommendations(){
        String greeting = "Пока доступны только рекомендации по цене\r\nВведите жалемую стоимость в целых числах";
        System.out.println(greeting);
        int cash = getUserCash();
        if(cash<0)
            return "";
        StringBuilder recs = new StringBuilder();//стрингбилдер для формирования строки с рекомендациями
        for(var title : titleMapping.values())
            if(cash>=title.getPrice())
                recs.append(title.getStringForm()).append("\r\n");
        return recs.toString();
    }

    private int getUserCash(){
        Scanner scanner = new Scanner(System.in);
        int cash = -1;
        try{
            cash = scanner.nextInt();//может выкинуть эксепшн что ввели не число, поэтому я ловлю их все
        }
        catch (Exception e){
            System.out.println("Ваш ввод не является целым числом");//и вывожу что это не число, повторно ввести не прошу
        }
        return cash;
    }
}
