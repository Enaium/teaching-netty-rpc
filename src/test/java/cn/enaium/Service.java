package cn.enaium;

/**
 * @author Enaium
 */
public interface Service {
    @Call(klass = "cn.enaium.Target", name = "render")
    String render();
}
