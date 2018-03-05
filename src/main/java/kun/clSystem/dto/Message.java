package kun.clSystem.dto;

/**
 * Created by pc on 2016/12/30.
 */
public class Message<T> {
    private String flag;
    private T content;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
