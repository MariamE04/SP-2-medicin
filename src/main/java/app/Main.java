package app;

import app.config.ApplicationConfig;

public class  Main {
    public static void main(String[] args) {
        //EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        System.out.println("test");

        ApplicationConfig.startServer(7007);


    }

}