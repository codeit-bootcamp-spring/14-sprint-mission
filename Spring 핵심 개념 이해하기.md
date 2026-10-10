## JavaApplication과 DiscodeitApplication에서 Service를 초기화 하는 방식의 차이에 대해 이해하기  
이전 JavaApplication은 직접 new 로 객체를 생성하여 주입하였으나  
DiscodeitApplication은 스프링 컨테이너를 통해 프레임워크에서 객체를 생성하여 주입한다.  

이 처럼 프레인 워크가 대신 제어권을 갖는 걸을 제어의 역전(IoC) 라고 하며,  
프레임워크가 대신 빈(Bean)의 의존관계를 주입해 주는 것을 의존성 주입(Dependency Injection)이라고 한다.  

**※Bean이란**: 스프링 Ioc 컨테이너가 생성하고 관리하는 객체

