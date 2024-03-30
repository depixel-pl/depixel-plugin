package pl.nivse.depixel.services;

import pl.nivse.depixel.objects.Group;

import java.util.HashMap;
import java.util.Map;

public class GroupService {
    private final Map<String, Group> groups = new HashMap<>();

    public void addGroup(String name, Group group){
        groups.put(name, group);
    }

    public Group getGroup(String groupName){
        return groups.get(groupName);
    }

    public void removeGroup(String groupName){
        groups.remove(groupName);
    }

    public Map<String, Group> getGroups() {
        return groups;
    }
}
