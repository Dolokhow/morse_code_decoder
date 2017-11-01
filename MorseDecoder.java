public class MorseDecoder {
    private enum Type {
        DOT, DASH, SPACE, SYMBOL_P,LETTER_P
    }
    private static java.io.File source;
    private static final java.util.List<Type> message = new java.util.ArrayList<>();
    private static String decoded_message;
    private static double treshold;

    private static int max_sample_ones;
    private static int min_sample_ones;
    private static int max_sample_zeros;

    private static void findUnitReferencePoints(){
        int temp_ones = 0;
        int temp_zeros = 0;

        boolean positive = true;
        try {
            java.util.Scanner scan = new java.util.Scanner(source);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                double value = Double.parseDouble(line);

                if(value > treshold && positive) {
                    temp_ones++;
                }
                else if(value <treshold && positive) {
                    positive = false;
                    if (temp_ones > max_sample_ones) max_sample_ones = temp_ones;
                    if(temp_ones < min_sample_ones) min_sample_ones = temp_ones;
                    temp_ones = 0;
                    temp_zeros++;
                }
                else if (value < treshold && !positive) {
                    temp_zeros++;
                }
                else if(value > treshold && !positive) {
                    positive = true;
                    if(temp_zeros > max_sample_zeros) max_sample_zeros = temp_zeros;
                    temp_zeros = 0;
                    temp_ones++;
                }
            }
            scan.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void virtualMorse() {
        int temp_ones = 0;
        int temp_zeros = 0;
        int holdup = 0;
        boolean positive = true;
        try {
            java.util.Scanner scan = new java.util.Scanner(source);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                double value = Double.parseDouble(line);

                if(value > treshold && positive) {
                    holdup = 0;
                    temp_ones++;
                    if (!scan.hasNext()) {
                        int dot = temp_ones - min_sample_ones;
                        int dash = max_sample_ones - temp_ones;

                        if (dot <= dash) {
                            message.add(Type.DOT);
                        }
                        else {
                            message.add(Type.DASH);
                        }
                    }
                }

                else if(value <treshold && positive) {
                    if (holdup >= 3) {
                        positive = false;
                        int dot = temp_ones - min_sample_ones;
                        int dash = max_sample_ones - temp_ones;

                        if (dot <= dash) {
                            message.add(Type.DOT);
                        }
                        else {
                            message.add(Type.DASH);
                        }
                        temp_ones = 0;
                        temp_zeros = holdup+1;
                        holdup = 0;
                    }
                    else {
                        holdup++;
                    }

                }
                else if (value < treshold && !positive) {
                    holdup = 0;
                    temp_zeros++;
                    if (!scan.hasNext()) {
                        int dot = temp_zeros - min_sample_ones;
                        int space = max_sample_zeros - temp_zeros;
                        int dash = java.lang.Math.abs(temp_zeros - max_sample_ones);

                        if(dot <= dash) {
                            message.add(Type.SYMBOL_P);
                        }
                        else if (space <= dash) {
                            message.add(Type.SPACE);
                        }
                        else {
                            message.add(Type.LETTER_P);
                        }

                    }
                }
                else if(value > treshold && !positive) {

                    if (holdup >= 3) {
                        positive = true;
                        int dot = temp_zeros - min_sample_ones;
                        int space = max_sample_zeros - temp_zeros;
                        int dash = java.lang.Math.abs(temp_zeros - max_sample_ones);

                        if(dot <= dash) {
                            message.add(Type.SYMBOL_P);
                        }
                        else if (space <= dash) {
                            message.add(Type.SPACE);
                        }
                        else {
                            message.add(Type.LETTER_P);
                        }
                        temp_zeros = 0;
                        temp_ones = holdup + 1;
                    }
                    else {
                        holdup++;
                    }
                }
            }
            scan.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String getWord() {

        String word = "";
        Type temp;
        while(!message.isEmpty() && message.get(0) != Type.LETTER_P && message.get(0) != Type.SPACE ) {
            temp = message.get(0);
            if (temp!=Type.SYMBOL_P) {
                switch (temp) {
                    case DOT:
                        word += ".";
                        break;
                    case DASH:
                        word += "_";
                        break;
                }
            }
            message.remove(0);
        }
        if(!message.isEmpty() && message.get(0) == Type.SPACE) {
            word+="!";
        }
        if (!message.isEmpty())
            message.remove(0);
        return word;
    }

    private static void decodeMorse() {
        while(!message.isEmpty()) {
            String word = getWord();
            boolean space_needed = false;
            if (word.endsWith("!")) {
                space_needed = true;
                word = word.substring(0, word.length() - 1);
            }

            switch (word) {
                case "._":
                    decoded_message += "a";
                    break;
                case "_...":
                    decoded_message += "b";
                    break;
                case "_._.":
                    decoded_message += "c";
                    break;
                case "_..":
                    decoded_message += "d";
                    break;
                case ".":
                    decoded_message += "e";
                    break;
                case ".._.":
                    decoded_message += "f";
                    break;
                case "__.":
                    decoded_message += "g";
                    break;
                case "....":
                    decoded_message += "h";
                    break;
                case "..":
                    decoded_message += "i";
                    break;
                case ".___":
                    decoded_message += "j";
                    break;
                case "_._":
                    decoded_message += "k";
                    break;
                case "._..":
                    decoded_message += "l";
                    break;
                case "__":
                    decoded_message += "m";
                    break;
                case "_.":
                    decoded_message += "n";
                    break;
                case "___":
                    decoded_message += "o";
                    break;
                case ".__.":
                    decoded_message += "p";
                    break;
                case "__._":
                    decoded_message += "q";
                    break;
                case "._.":
                    decoded_message += "r";
                    break;
                case "...":
                    decoded_message += "s";
                    break;
                case "_":
                    decoded_message += "t";
                    break;
                case ".._":
                    decoded_message += "u";
                    break;
                case "..._":
                    decoded_message += "v";
                    break;
                case ".__":
                    decoded_message += "w";
                    break;
                case "_.._":
                    decoded_message += "x";
                    break;
                case "_.__":
                    decoded_message += "y";
                    break;
                case "__..":
                    decoded_message += "z";
                    break;
            }
            if (space_needed)
                decoded_message += " ";
        }
    }



    public static void main(String[] args) {
        decoded_message = "";
        treshold = 0.5;

        max_sample_ones = 0;
        min_sample_ones = 10000;
        max_sample_zeros = 0;

        java.util.Scanner in = new java.util.Scanner(System.in);
        String file_path = in.nextLine();
        in.close();
        source = new java.io.File(file_path);
        findUnitReferencePoints();

        System.out.println("DASH LENGTH REFERENCE: ");
        System.out.println(max_sample_ones);

        System.out.println("DOT/PAUSE LENGTH REFERENCE: ");
        System.out.println(min_sample_ones);

        System.out.println("SPACE LENGTH REFERENCE: ");
        System.out.println(max_sample_zeros);


        virtualMorse();


        decodeMorse();
        System.out.print(decoded_message);
    }
}
