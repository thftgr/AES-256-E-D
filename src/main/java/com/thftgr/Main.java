package com.thftgr;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("mode = Encode, Decode (e,d) : ");
        String mod = sc.nextLine();
        mod = mod.toLowerCase();

        String input,key;

        if(mod.startsWith("e")){

            while (true){
                System.out.println("value (8 characters or more): ");
                input = sc.nextLine();
                if(input.length() >= 8) break;
            }
            while (true) {
                System.out.println("Key (8 characters or more): ");
                String tmp = sc.nextLine();
                if (tmp.length() >= 8) {
                    key = new CryptoUtil().sha256(tmp);
                    break;
                }
            }



            System.out.println("========================================");
            System.out.println("AES-256 DATA : "+new CryptoUtil().encryptAES256(input, key));
            System.out.println("Key : "+key);
            System.out.println("========================================");

        }else if(mod.startsWith("d")){
            System.out.println("AES-256 DATA : ");
            input = sc.nextLine();
            System.out.println("Key: ");
            key = sc.nextLine();
            System.out.println("========================================");
            System.out.println("value : "+new CryptoUtil().decryptAES256(input, key));
            System.out.println("========================================");
        }
//        AES-256 DATA : L6H3/Iw4Dlbkn/qZDQD3UlDcet4JYT5fqlH4zfibuBiFivWULhe68f54NJtoGBRoRl76zw==
//                Key : 7020e57625b6a6695ffd51ed494fbfc56c699eaceca4e77bf7ea590c7ebf3879






        System.out.println();


    }
}
