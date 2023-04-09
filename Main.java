package org.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import static java.lang.System.in;

public class Main {
    public static void main(String[] args) {
        ArrayList<ElitUye> elitUyeler = new ArrayList<>();
        ArrayList<GenelUye> genelUyeler = new ArrayList<>();

        Scanner input = new Scanner(in);

        while (true) {
            System.out.println("Lütfen yapmak istediğiniz işlemi seçin:");
            System.out.println("1- Elit Üye Ekle");
            System.out.println("2- Genel Üye Ekle");
            System.out.println("3- E-posta Gönder");
            System.out.println("4- Çıkış");

            int secim = input.nextInt();
            input.nextLine();

            if (secim == 1) {
                System.out.println("Lütfen adınızı girin: ");
                String ad = input.nextLine();

                System.out.println("Lütfen soyadınızı girin: ");
                String soyad = input.next();

                System.out.println("Lütfen e-posta adresinizi girin(outlook hesabınızı giriniz): ");
                String eposta = input.next();

                ElitUye elitUye = new ElitUye(ad, soyad, eposta);
                elitUyeler.add(elitUye);

                System.out.println("Elit üye başarıyla eklendi.");
            }

            else if (secim == 2) {
                System.out.println("Lütfen adınızı girin: ");
                String ad = input.nextLine();

                System.out.println("Lütfen soyadınızı girin: ");
                String soyad = input.next();

                System.out.println("Lütfen e-posta adresinizi girin(outlook hesabınızı giriniz): ");
                String eposta = input.next();

                GenelUye genelUye = new GenelUye(ad, soyad, eposta);
                genelUyeler.add(genelUye);

                System.out.println("Genel üye başarıyla eklendi.");
            }

            else if (secim == 3) {
                try {
                    sendEmail(elitUyeler, genelUyeler);
                    System.out.println("E-posta başarıyla gönderildi.");
                } catch (Exception e) {
                    System.out.println("E-posta gönderimi başarısız oldu: " + e.getMessage());
                }
            } else if (secim == 4) {
                System.out.println("Program sonlandırılıyor...");
                break;
            } else {
                System.out.println("Geçersiz seçim, lütfen tekrar deneyin.");
            }

            try {
                FileWriter writer = new FileWriter("Kullanıcılar.txt");
                writer.write("Elit Üyeler:\n");
                for (ElitUye elitUye : elitUyeler) {
                    writer.write(elitUye.toString() + "\n");
                }

                writer.write("\nGenel Üyeler:\n");
                for (GenelUye genelUye : genelUyeler) {
                    writer.write(genelUye.toString() + "\n");
                }

                writer.close();
                System.out.println("Dosya başarıyla güncellendi.");
            } catch (IOException e) {     //hata ayıklama
                System.out.println("Dosya güncelleme hatası: " + e.getMessage());
            }



        }
    }

    private static void sendEmail(ArrayList<ElitUye> elitUyeler, ArrayList<GenelUye> genelUyeler) throws IOException {
        Scanner input = new Scanner(in);


        System.out.println("Mailleri göndereceğiniz kendi outlook hesabınızı giriniz:");
        String gonderenhesap=input.nextLine();
        System.out.println("Girdiğiniz outlook hesabınızın sifresini giriniz:");
        String sifre=input.nextLine();
        System.out.println("Lütfen yapmak istediğiniz işlemi seçin:");
        System.out.println("1- Elit Üyelere Mail At");
        System.out.println("2- Genel Üyelere Mail At");
        System.out.println("3- Tüm Üyelere Mail At");
        System.out.println("4- Çıkış");

        int secim = input.nextInt();
        input.nextLine();


        if (secim == 1) {
            Scanner scanner=new Scanner(in);
            System.out.println("Mail konusunu yazınız:");
            String konu=scanner.nextLine();
            System.out.println("Mail iceriğini yazınız:");
            String icerik=scanner.nextLine();
            for (ElitUye elitUye : elitUyeler) {

                String to = elitUye.getEposta(); // alıcının e-posta adresi
                String from = gonderenhesap; // gönderen e-posta adresi
                String password = sifre; // gönderen e-posta hesabının şifresi

                // SMTP sunucusunun bilgileri
                String host = "smtp.office365.com";
                int port = 587;
                String starttls = "true";
                String auth = "true";

                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.starttls.enable", starttls);
                props.put("mail.smtp.auth", auth);
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");

                Session session = Session.getInstance(props, null);

                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(from));
                    InternetAddress[] address = {new InternetAddress(to)};
                    msg.setRecipients(Message.RecipientType.TO, address);

                    msg.setSubject(konu); // E-posta konusu
                    msg.setText(icerik); // E-posta içeriği

                    // SMTP sunucusuna bağlanma, kimlik doğrulama yapma
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, from, password);

                    transport.sendMessage(msg, msg.getAllRecipients());
                    System.out.println("E-posta gönderildi");

                    // Bağlantıyı kapatma
                    transport.close();

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (secim == 2) {
            Scanner scanner=new Scanner(in);
            System.out.println("Mail konusunu yazınız:");
            String konu=scanner.nextLine();
            System.out.println("Mail iceriğini yazınız:");
            String icerik=scanner.nextLine();
            for (GenelUye genelUye : genelUyeler) {
                String to = genelUye.getEposta(); // alıcının e-posta adresi
                String from = gonderenhesap; // gönderen e-posta adresi
                String password = sifre; // gönderen e-posta hesabının şifresi

                // SMTP sunucusunun bilgileri
                String host = "smtp.office365.com";
                int port = 587;
                String starttls = "true";
                String auth = "true";

                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.starttls.enable", starttls);
                props.put("mail.smtp.auth", auth);
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");

                Session session = Session.getInstance(props, null);

                try {
                    // E-posta mesajını oluştur
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(from));
                    InternetAddress[] address = {new InternetAddress(to)};
                    msg.setRecipients(Message.RecipientType.TO, address);
                    msg.setSubject(konu); // E-posta konusu
                    msg.setText(icerik); // E-posta içeriği

                    // SMTP sunucusuna bağlan, kimlik doğrulama yap
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, from, password);

                    // E-postayı gönder
                    transport.sendMessage(msg, msg.getAllRecipients());
                    System.out.println("E-posta gönderildi");

                    // Bağlantıyı kapat
                    transport.close();

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }



        else if (secim == 3){
            Scanner scanner=new Scanner(in);
            System.out.println("Mail konusunu yazınız:");
            String konu=scanner.nextLine();
            System.out.println("Mail iceriğini yazınız:");
            String icerik=scanner.nextLine();
            for (GenelUye genelUye : genelUyeler) {
                String to = genelUye.getEposta(); // alıcının e-posta adresi
                String from = gonderenhesap; // gönderen e-posta adresi
                String password = sifre; // gönderen e-posta hesabının şifresi

                // SMTP sunucusunun bilgileri
                String host = "smtp.office365.com";
                int port = 587;
                String starttls = "true";
                String auth = "true";

                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.starttls.enable", starttls);
                props.put("mail.smtp.auth", auth);
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");

                Session session = Session.getInstance(props, null);

                try {
                    // E-posta mesajını oluştur
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(from));
                    InternetAddress[] address = {new InternetAddress(to)};
                    msg.setRecipients(Message.RecipientType.TO, address);
                    msg.setSubject(konu); // E-posta konusu
                    msg.setText(icerik); // E-posta içeriği

                    // SMTP sunucusuna bağlan, kimlik doğrulama yap
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, from, password);

                    // E-postayı gönder
                    transport.sendMessage(msg, msg.getAllRecipients());
                    System.out.println("E-posta gönderildi");

                    // Bağlantıyı kapat
                    transport.close();

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            for (ElitUye elitUye : elitUyeler) {
                String to = elitUye.getEposta(); // alıcının e-posta adresi
                String from = gonderenhesap; // gönderen e-posta adresi
                String password = sifre; // gönderen e-posta hesabının şifresi

                // SMTP sunucusunun bilgileri
                String host = "smtp.office365.com";
                int port = 587;
                String starttls = "true";
                String auth = "true";

                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.starttls.enable", starttls);
                props.put("mail.smtp.auth", auth);
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                props.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");

                Session session = Session.getInstance(props, null);

                try {
                    // E-posta mesajını oluştur
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(from));
                    InternetAddress[] address = {new InternetAddress(to)};
                    msg.setRecipients(Message.RecipientType.TO, address);
                    msg.setSubject(konu); // E-posta konusu
                    msg.setText(icerik); // E-posta içeriği

                    // SMTP sunucusuna bağlan, kimlik doğrulama yap
                    Transport transport = session.getTransport("smtp");
                    transport.connect(host, from, password);

                    // E-postayı gönder
                    transport.sendMessage(msg, msg.getAllRecipients());
                    System.out.println("E-posta gönderildi");

                    // Bağlantıyı kapat
                    transport.close();

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

    class Uyele {       //2. class- uye bilgilerini yonetmek amaciyla olusturdum
        private String isim;
        private String soyisim;
        private String eposta;

        public Uyele(String isim, String soyisim, String eposta) {
            this.isim = isim;
            this.soyisim = soyisim;
            this.eposta = eposta;
        }

        public String getIsim() {

            return isim;
        }

        public void setIsim(String isim) {

            this.isim = isim;
        }

        public String getSoyisim() {

            return soyisim;
        }

        public void setSoyisim(String soyisim) {

            this.soyisim = soyisim;
        }

        public String getEposta() {

            return eposta;
        }

        public void setEposta(String eposta) {

            this.eposta = eposta;
        }

        @Override
        public String toString() {

            return "\n" + isim + "\t " + soyisim + "\t" + " " + eposta + "";
        }
    }


class ElitUyele extends Uyele {
    public ElitUyele(String isim, String soyisim, String eposta) {

        super(isim, soyisim, eposta);
    }
}

class GenelUyele extends Uyele {
    public GenelUyele(String isim, String soyisim, String eposta) {

        super(isim, soyisim, eposta);
    }
}
