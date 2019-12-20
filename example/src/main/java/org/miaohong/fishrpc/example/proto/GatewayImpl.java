package org.miaohong.fishrpc.example.proto;

import java.util.Random;

public class GatewayImpl implements GatewayProto {

    private Random random = new Random();

    @Override
    public String test() {
        return "test";
    }

    @Override
    public Person person() {

        int number = random.nextInt(5);

        Person person = new Person("zh", number);

        System.out.println("num =" + number);
        if (number == 3) {
            System.out.println("person call failed start");
            throw new RuntimeException("person call failed");
        }

        return person;
    }
}
