package org.example;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.example.entity.ComparisonOperatorEntity;
import org.example.entity.Order;
import org.example.entity.Student;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wjq
 * @CreateTime: 2024-12-27
 * @Description:
 * @Version: 1.0
 */
public class TestDemo1 {

    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        //会话对象，用于和规则引擎交互
        KieSession kieSession = kieClasspathContainer.newKieSession();

        //构造订单对象，设置原始价格，由规则引擎根据优惠规则计算优惠后的价格
        Order order = new Order();
        order.setOriginalPrice(210D);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(order);

        //激活规则引擎，如果规则匹配成功则执行规则
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();

        System.out.println("优惠前原始价格：" + order.getOriginalPrice() +
                "，优惠后价格：" + order.getRealPrice());
    }

    //Pattern模式匹配
    //前面我们已经知道了Drools中的匹配器可以将Rule Base中的所有规则与Working Memory中的Fact对象进行模式匹配，那么我们就需要在规则体的LHS部分定义规则并进行模式匹配。LHS部分由一个或者多个条件组成，条件又称为pattern。
    //pattern的语法结构为：绑定变量名:Object(Field约束) 例如 $order:Order(originalPrice < 200 && originalPrice >= 100)
    //其中绑定变量名可以省略，通常绑定变量名的命名一般建议以$开始。如果定义了绑定变量名，就可以在规则体的RHS部分使用此绑定变量名来操作相应的Fact对象。Field约束部分是需要返回true或者false的0个或多个表达式。


    /**
     * 测试比较操作符<br/>
     * contains	检查一个Fact对象的某个属性值是否包含一个指定的对象值<br/>
     * not contains	检查一个Fact对象的某个属性值是否不包含一个指定的对象值<br/>
     * memberOf	判断一个Fact对象的某个属性是否在一个或多个集合中<br/>
     * not memberOf	判断一个Fact对象的某个属性是否不在一个或多个集合中<br/>
     * matches	判断一个Fact对象的属性是否与提供的标准的Java正则表达式进行匹配<br/>
     * not matches	判断一个Fact对象的属性是否不与提供的标准的Java正则表达式进行匹配<br/>
     * <br/>
     * contains | not contains语法结构<br/>
     * Object(Field[Collection/Array] contains value)<br/>
     * Object(Field[Collection/Array] not contains value)<br/>
     * <br/>
     * memberOf | not memberOf语法结构<br/>
     * Object(field memberOf value[Collection/Array])<br/>
     * Object(field not memberOf value[Collection/Array])<br/>
     * <br/>
     * matches | not matches语法结构<br/>
     * Object(field matches “正则表达式”)<br/>
     * Object(field not matches “正则表达式”)<br/>
     */
    @Test
    public void test3() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();

        ComparisonOperatorEntity comparisonOperatorEntity = new ComparisonOperatorEntity();
        comparisonOperatorEntity.setNames("张三");
        List<String> list = new ArrayList<String>();
        list.add("张三");
        list.add("李四");
        comparisonOperatorEntity.setList(list);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配，如果规则匹配成功则执行规则
        kieSession.insert(comparisonOperatorEntity);

        kieSession.fireAllRules();
        kieSession.dispose();
    }

    /**
     * 执行指定规则
     * 通过前面的案例可以看到，我们在调用规则代码时，满足条件的规则都会被执行。那么如果我们只想执行其中的某个规则如何实现呢？
     * Drools给我们提供的方式是通过规则过滤器来实现执行指定规则。
     */
    @Test
    public void test4() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();

        ComparisonOperatorEntity comparisonOperatorEntity = new ComparisonOperatorEntity();
        comparisonOperatorEntity.setNames("张三");
        List<String> list = new ArrayList<String>();
        list.add("张三");
        list.add("李四");
        comparisonOperatorEntity.setList(list);
        kieSession.insert(comparisonOperatorEntity);

        //通过规则过滤器实现只执行指定规则
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("rule_comparison_memberOf"));

        kieSession.dispose();

    }

    @Test
    public void test5() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieClasspathContainer.newKieSession();

        Student student = new Student();
        student.setAge(5);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配，如果规则匹配成功则执行规则
        kieSession.insert(student);

        kieSession.fireAllRules();
        kieSession.dispose();

        System.out.println(student);

    }

}
