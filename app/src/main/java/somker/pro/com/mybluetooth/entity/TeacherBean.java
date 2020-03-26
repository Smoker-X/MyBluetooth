package somker.pro.com.mybluetooth.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Smoker on 2020/3/19.
 * 说明：
 */
@Entity
public class TeacherBean {

    @Id(autoincrement = true)
    private Long id ;

    private String libId ;

    private String versionCode ;

    private String teaId ;

    private String name;

    private String cardId;

    private String loginAccount;


    private String signName ;

    private String signPath ;

    /**
     * 当前考评员是否 考务人员进行考评分组的（不入库）
     */
    @Transient
    private boolean isExamGroup ;

    @Generated(hash = 1929081527)
    public TeacherBean(Long id, String libId, String versionCode, String teaId,
            String name, String cardId, String loginAccount, String signName,
            String signPath) {
        this.id = id;
        this.libId = libId;
        this.versionCode = versionCode;
        this.teaId = teaId;
        this.name = name;
        this.cardId = cardId;
        this.loginAccount = loginAccount;
        this.signName = signName;
        this.signPath = signPath;
    }

    @Generated(hash = 1376380279)
    public TeacherBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibId() {
        return this.libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getTeaId() {
        return this.teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardId() {
        return this.cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getLoginAccount() {
        return this.loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getSignName() {
        return this.signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getSignPath() {
        return this.signPath;
    }

    public void setSignPath(String signPath) {
        this.signPath = signPath;
    }

}
