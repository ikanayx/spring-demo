package space.itzkana.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@SuppressWarnings({"GrazieInspection", "CommentedOutCode"})
@Service
public class InjectedService {

    // @Autowired 匹配优先级: type-match > @Primary > @(Priority)Order > name-match
//    @Autowired
//    private ICompA iCompA;


    // @Resource 匹配优先级: name-match > type-match > @Primary > @(Priority)Order
    @Resource
    private ICompA iCompA;

    private final ICompA iCompA2;

    public InjectedService(ICompA iCompA3) {
        this.iCompA2 = iCompA3;
    }

    public void printAutowireCompName() {
        System.out.println(iCompA.name());
        System.out.println(iCompA2.name());
    }
}
