package cn.hhchat.server.monitor.commons;

public enum RoleType {

    DEFENDER(0, "防御者", "defender"),
    ATTACKER(1, "攻击者", "attacker"),
    ;

    private Integer roleId;
    private String zhName;
    private String enName;

    RoleType(Integer roleId, String zhName, String enName) {
        this.roleId = roleId;
        this.zhName = zhName;
        this.enName = enName;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

}
