import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/25.
 */
public class CreateFreemarker {
    public static void main(String[] args) {
        try {
            //1.创建配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //2.设置模板所在的目录
            configuration.setDirectoryForTemplateLoading(new File("D:\\parent318\\solrdemo\\src\\main\\resources"));
            //3.设置字符集
            configuration.setDefaultEncoding("utf-8");
            //4.加载模板
            Template template = configuration.getTemplate("test.ftl");
            //5.创建数据模型
            Map map = new HashMap();
            map.put("name", "张三 ");
            map.put("message", "欢迎来到神奇的品优购世界！");
            //6.创建Writer对象
            Writer out = new FileWriter(new File("d:\\test.html"));
            //创建Writer对象创建Writer对象创建Writer对象创建Writer对象创建Writer对象
            //7.输出
            template.process(map, out);
            //8.关闭Writer对象
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
