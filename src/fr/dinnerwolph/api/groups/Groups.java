package fr.dinnerwolph.api.groups;

/**
 * @author Dinnerwolph
 */

public class Groups {

    private int ladder = 0; // default groups
    private String groupName = "";
    private String prefix = "";
    private String sufffix = "";
    private int ScopeboardId;

    public Groups(int ladder, String groupName, String prefix, String sufffix, int scopeboardId) {
        this.ladder = ladder;
        this.groupName = groupName;
        this.prefix = prefix;
        this.sufffix = sufffix;
        ScopeboardId = scopeboardId;
    }

    public int getLadder() {
        return ladder;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSufffix() {
        return sufffix;
    }

    public int getScopeboardId() {
        return ScopeboardId;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "ladder=" + ladder +
                ", prefix='" + prefix + '\'' +
                ", sufffix='" + sufffix + '\'' +
                ", ScopeboardId=" + ScopeboardId +
                '}';
    }
}
