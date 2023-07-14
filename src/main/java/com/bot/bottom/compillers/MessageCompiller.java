package com.bot.bottom.compillers;

import com.bot.bottom.dao.MemDao;
import com.bot.bottom.model.Mem;
import com.bot.bottom.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageCompiller {

    private final Random random = new Random();
    private final DictionaryService dictionaryService;
    private final MemDao memDao;

    public MessageCompiller(DictionaryService dictionaryService, MemDao memDao) {
        this.dictionaryService = dictionaryService;
        this.memDao = memDao;
    }

    public String help() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mem caption: ");
        sb.append('\n');

        sb.append("name/keyword,secondword1,secondword2,....");
        sb.append('\n');

        sb.append("or");
        sb.append('\n');

        sb.append("name/");
        sb.append('\n');

        sb.append("keyword,secondword1,secondword2,....");
        sb.append('\n');

        sb.append("keyword");
        sb.append('\n');
        sb.append('\n');

        sb.append("Request format: ");
        sb.append('\n');

        sb.append("keyword");
        sb.append('\n');

        sb.append("or");
        sb.append('\n');

        sb.append("name/");
        sb.append('\n');
        sb.append('\n');


        sb.append("___Comands___");
        sb.append('\n');
        sb.append('\n');

        sb.append("/=/");
        sb.append('\n');

        sb.append("Add synonym (XXX = YYY || XXX = YYY, ZZZ)");
        sb.append('\n');
        sb.append('\n');

        sb.append("/Key/");
        sb.append('\n');

        sb.append("Add keyword (NAME/key/NEW_KEY_WORD)");
        sb.append('\n');
        sb.append('\n');

        sb.append("Delete/");
        sb.append('\n');

        sb.append("Delete mem file and record (Delete/NAME)");
        sb.append('\n');
        sb.append('\n');


        sb.append("/returnMap/");
        sb.append('\n');


        sb.append("Returns mems mapped by keywords");
        sb.append('\n');
        sb.append('\n');


        sb.append("/fileContent/");
        sb.append('\n');

        sb.append("Returns content of file folder");
        sb.append('\n');
        sb.append('\n');

        sb.append("/clearFiles/");
        sb.append('\n');

        sb.append("Delete all files not presented in the database");
        sb.append('\n');
        sb.append('\n');

        sb.append("/ExportDB/");
        sb.append('\n');

        sb.append("Sends in response current databases and saves them on the server");
        sb.append('\n');
        sb.append('\n');

        sb.append("/resetBases/");
        sb.append('\n');


        sb.append("Clear databases and make its copies on the server");
        sb.append('\n');
        sb.append('\n');


        sb.append("Databases can bi send without captions, but should contain in the file names");
        sb.append('\n');


        sb.append("\"mem\", \"dictionary\" and \"user\" respectively");
        sb.append('\n');

        return sb.toString();
    }


    public String sayThankYou(Mem mem) {
        int cs = 0;
        StringBuilder thank = new StringBuilder();
        thank.append(mem.getSender());
        thank.append(", спасибо, понял, принял обработал\n");
        thank.append("Сохранил ");
        if (mem.getType() == 2) {
            thank.append(" твою картинку c \n");
        } else if (mem.getType() == 4) {
            thank.append(" твоё видео c \n");
        } else if (mem.getType() == 5) {
            thank.append(" твою гифку с\n");
        } else {
            thank.append(" что бы это ни было c \n");
        }
        thank.append("названием " + mem.getName() + '\n');
        thank.append("ключевым словом: " + mem.getKeyWord() + '\n');
        if (mem.getSecondWords() != null) {
            thank.append("и другими словами: ");
            for (String sw : mem.getSecondWords()) {
                thank.append(sw + ", ");
            }
            thank.replace(thank.length() - 2, thank.length() - 1, "\n");
            //  thank.append('\n');
        }
        cs = random.nextInt(3);
        if (cs == 0) {
            thank.append("Пиши исчё!");
        }
        if (cs == 1) {
            thank.append("Смешно");
        }
        if (cs == 2) {
            thank.append("Наверное смешно. Я не знаю - я бот");
        }
        return thank.toString();
    }

    public String setPhotoToString(Set<String> files, String prefix) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String s : files) {
            sb.append(s);
            sb.append(" -> ");
            sb.append(memDao.findDyAddress(prefix + s) == null
                    ? "not in the base" : memDao.findDyAddress(prefix + s).getName());
            sb.append('\n');
            count++;
        }
        sb.append("Total amount of files: ");
        sb.append(count);
        return sb.toString();
    }

    public String makeLabel(Mem mem) {
        StringBuilder label = new StringBuilder();
        boolean flagFirst = false;
        label.append("Name : ");
        label.append(mem.getName());
        label.append('\n');
        label.append("Key word : ");
        label.append(mem.getKeyWord());
        addSynonyms(label, mem.getKeyWord());
        label.append("Other associations : ");
        if (!mem.getSecondWords().isEmpty()) {
            for (String sw : mem.getSecondWords()) {
                if (!flagFirst) {
                    flagFirst = true;
                } else {
                    label.append(", ");
                }
                label.append(sw);
            }
            flagFirst = false;
        }
        label.append('\n');
        label.append("Posted by : ");
        label.append(mem.getSender());
        label.append('\n');
        label.append("In : ");
        label.append(mem.getSaveTime());
        label.append('\n');

        return label.toString();
    }

    public List<String> makeMap(Map<String, List<String>> map) {
        List<String> answers = new ArrayList<>();
        StringBuilder answer = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            answer.append(entry.getKey());
            addSynonyms(answer, entry.getKey());
            for (String name : entry.getValue()) {
                answer.append("          ");
                answer.append(name);
                answer.append('\n');
                count++;
            }
            if (answer.length() > 3800) {
                answers.add(answer.toString());
                answer = new StringBuilder();
            }
        }
        answer.append("Total amount of names: ");
        answer.append(count);
        answers.add(answer.toString());
        return answers;
    }

    private void addSynonyms(StringBuilder sb, String word) {
        if (dictionaryService.findSynonyms(word) != null) {
            boolean flagFirst = false;
            sb.append(" = (");
            for (String s : dictionaryService.findSynonyms(word)) {
                if (!flagFirst) {
                    flagFirst = true;
                } else {
                    sb.append(", ");
                }
                sb.append(s);
            }
            sb.append(')');
        }
        sb.append('\n');
    }

    public List<String> split(String toSplit) {
        return split(toSplit, 3900);
    }

    public List<String> split(String toSplit, int simbols) {
        List<String> answer = new ArrayList<>();
        simbols--;
        char[] arr = toSplit.toCharArray();
        char[] part = new char[simbols];
        int lim = (int) (simbols * 0.9);
        int count = 0;
        for (char ch : arr) {
            part[count++] = ch;
            if (count >= lim && (ch == '\n' || count == simbols)) {
                count = 0;
                answer.add(new String(part).trim());
                part = new char[simbols];
            }
        }
        answer.add(new String(part).trim());
        return answer;
    }


    public String makeTags(Map<String, Integer> tags) {
        StringBuilder sb = new StringBuilder();
        Comparator<Map.Entry<String, Integer>> entryComparator = Comparator.comparing(Map.Entry::getValue);
        entryComparator = entryComparator.thenComparing(Map.Entry::getKey);

       tags.entrySet().stream().sorted(entryComparator)
               .map(s ->  (s.getKey() + " -> " + s.getValue() + '\n')).forEach(sb::append);
        return sb.toString();
    }
    public List<String> tagsForWeb(Map<String, Integer> tags){

        return tags.entrySet().stream().map(s ->  (s.getKey() + " -> " + s.getValue() + '\n')) // check sorting by key
                .sorted().collect(Collectors.toList());
    }
}
