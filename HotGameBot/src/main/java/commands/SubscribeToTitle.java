package commands;

import Entities.Levenshtein;
import Entities.Title;
import Entities.User;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

enum Answer {
    YES,
    NO,
    STOP
}

public class SubscribeToTitle implements ICommand {

    private final HashMap<String, Title> titleMapping;
    private final Set<String> titleNamesSet;
    private final String stoppingLine = "specialStoppingLineThatMightBeAnEnumButIDon'tWantToDoThisKittensAreCute";
    private final Scanner scanner;
    private User currentUser;

    public SubscribeToTitle(HashMap<String, Title> titles) {
        titleMapping = titles;
        titleNamesSet = titles.keySet();
        scanner = new Scanner(System.in);
    }

    @Override
    public void execute(User user) {
        currentUser = user;
        subUserToTitle();
    }

    private void subUserToTitle() {
        String titleName = getTitleName();
        if (stoppingLine.equals(titleName))
            return;
        currentUser.Watch(titleMapping.get(titleName));
    }

    private String getTitleName() {
        Answer answer;
        String name;
        do {
            var input = getUserInput();
            name = new Levenshtein().getClosestName(titleNamesSet, input);
            answer = getUserAnswer(name);
        } while (!Answer.YES.equals(answer) && !Answer.STOP.equals(answer));
        return answer.equals(Answer.YES) ? name : stoppingLine;
    }

    private Answer getUserAnswer(String name) {
        String titleString = titleMapping.get(name).toString();
        System.out.println("Это игра, которую вы искали?(yes/no/stop)\r\n" + titleString);
        var answer = scanner.nextLine().toLowerCase();
        return switch (answer) {
            case "yes" -> Answer.YES;
            case "no" -> Answer.NO;
            case "stop" -> Answer.STOP;
            default -> null;
        };
    }

    private String getUserInput() {
        System.out.println("Введите название игры как можно точнее");
        return scanner.nextLine().toLowerCase();
    }
}