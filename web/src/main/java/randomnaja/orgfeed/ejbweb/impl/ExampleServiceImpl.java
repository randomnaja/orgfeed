package randomnaja.orgfeed.ejbweb.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import randomnaja.orgfeed.ejbweb.remote.ExampleService;

@Component("exampleServiceImpl")
@Transactional
class ExampleServiceImpl implements ExampleService {

}
