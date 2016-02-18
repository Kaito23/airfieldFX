package airfield.application;

public class Test {

	public void test(String password) {
		System.out.println("test start");
		TakeInit takeInit = new TakeInit();
		takeInit.loadRepo("C:/Users/koetter/Desktop/gittest", "ssh://git@adesso-azubi-docker01.adesso.local:2222/data/git/loggetta/", password);
		System.out.println("test ende");
	}

}
