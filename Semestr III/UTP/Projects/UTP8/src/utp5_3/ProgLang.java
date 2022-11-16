package utp5_3;


import java.io.FileReader;
import java.util.*;
import java.util.function.Predicate;



public class ProgLang {

    private List<List<String>> file;
    private Set<String> languages;
    private Set<String> usernames;

    public ProgLang(String path) throws Exception {
        file = new ArrayList<>();
        languages = new LinkedHashSet<>();
        usernames = new LinkedHashSet<>();

        Scanner reader = new Scanner(new FileReader(path));

        while (reader.hasNext()) {
            file.add(Arrays.asList(reader.nextLine().split("\t")));
        }

        separate();
    }

    private void separate() {
        for (List<String> item : file) {
            languages.add(item.get(0));
            usernames.addAll(item.subList(1, item.size()));
        }
    }


    public Map<String, Set<String>> getLangsMap() {
        Map<String, Set<String>> map = new LinkedHashMap<>();

        for (List<String> item : file)
            map.put(item.get(0), new LinkedHashSet<>(item.subList(1, item.size())));

        return map;
    }

    public Map<String, Set<String>> getProgsMap() {
        Map<String, Set<String>> map = new LinkedHashMap<>();

        for (String username : usernames) {
            Set<String> set = new LinkedHashSet<>();
            for (List<String> list : file) {
                if (list.contains(username))
                    set.add(list.get(0));
            }
            map.put(username, set);
        }
        return map;
    }

    public Map<String, Set<String>> getLangsMapSortedByNumOfProgs() {
        return sorted(getLangsMap(), (o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size()));
    }


    public Map<String, Set<String>> getProgsMapSortedByNumOfLangs() {
        return sorted(getProgsMap(), (o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size()));
    }

    public Map<String, Set<String>> getProgsMapForNumOfLangsGreaterThan(int i) {

        return filtered(getProgsMap(), (o -> o.getValue().size() > i));
    }

    public <X, Y> Map<X, Y> filtered(Map<X, Y> map, Predicate<Map.Entry<X, Y>> predicate) {
        Map<X, Y> result = new LinkedHashMap<>();
        List<Map.Entry<X, Y>> list = new ArrayList<>(map.entrySet());

        list.removeIf(predicate.negate());

        for (Map.Entry<X, Y> item : list)
            result.put(item.getKey(), item.getValue());

        return result;
    }

    public <X, Y> Map<X, Y> sorted(Map<X, Y> map, Comparator<Map.Entry<X, Y>> comparator) {
        Map<X, Y> result = new LinkedHashMap<>();
        List<Map.Entry<X, Y>> list = new ArrayList<>(map.entrySet());

        list.sort(comparator);

        for (Map.Entry<X, Y> item : list)
            result.put(item.getKey(), item.getValue());

        return result;
    }


}
