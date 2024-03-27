package pl.nivse.depixel.services;

import pl.nivse.depixel.objects.Group;

import java.util.HashMap;
import java.util.Map;

public class GroupService {
    private final Map<String, Group> groups = new HashMap<>();

    public void addGroup(String name, Group group){
        groups.put(name, group);
    }

    public Group getGroup(String group){
        return groups.get(group);
    }

    public void removeGroup(Group group){
        groups.remove(group);
    }
}
