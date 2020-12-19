package club.zarddy.library.bean;

/**
 * 提示信息
 */
public class TipsMessage {

    private TipsStatus status;

    private String message;

    public TipsMessage() {

    }

    public TipsMessage(TipsStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public TipsStatus getStatus() {
        return status;
    }

    public void setStatus(TipsStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
