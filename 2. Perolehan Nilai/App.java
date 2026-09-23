import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ===== Baris 1-6: Bobot komponen =====
        int bobotPA = Integer.parseInt(sc.nextLine().trim());
        int bobotT  = Integer.parseInt(sc.nextLine().trim());
        int bobotK  = Integer.parseInt(sc.nextLine().trim());
        int bobotP  = Integer.parseInt(sc.nextLine().trim());
        int bobotUTS = Integer.parseInt(sc.nextLine().trim());
        int bobotUAS = Integer.parseInt(sc.nextLine().trim());

        int totalBobotAwal = bobotPA + bobotT + bobotK + bobotP + bobotUTS + bobotUAS;
        if (totalBobotAwal != 100) {
            System.out.println("Total bobot harus 100");
            return;
        }

        // ===== Akumulator per komponen =====
        int totalBobotPA = 0, totalNilaiPA = 0;
        int totalBobotT  = 0, totalNilaiT  = 0;
        int totalBobotK  = 0, totalNilaiK  = 0;
        int totalBobotP  = 0, totalNilaiP  = 0;
        int totalBobotUTS = 0, totalNilaiUTS = 0;
        int totalBobotUAS = 0, totalNilaiUAS = 0;

        // ===== Baris 7 dst: Data komponen, sampai '---' atau input habis =====
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().equals("---")) {
                break;
            }

            String[] parts = line.split("\\|", -1);
            if (parts.length != 3) {
                System.out.println("Data tidak valid. Silahkan menggunakan format: Simbol|Bobot|Perolehan-Nilai");
                continue;
            }

            String simbol = parts[0].trim();
            String bobotStr = parts[1].trim();
            String nilaiStr = parts[2].trim();

            int bobot, nilai;
            try {
                bobot = Integer.parseInt(bobotStr);
                nilai = Integer.parseInt(nilaiStr);
            } catch (NumberFormatException e) {
                System.out.println("Data tidak valid. Silahkan menggunakan format: Simbol|Bobot|Perolehan-Nilai");
                continue;
            }

            // Clamp perolehan ke rentang [0, bobot]
            if (nilai > bobot) nilai = bobot;
            if (nilai < 0) nilai = 0;

            switch (simbol) {
                case "PA":
                    totalBobotPA += bobot;
                    totalNilaiPA += nilai;
                    break;
                case "T":
                    totalBobotT += bobot;
                    totalNilaiT += nilai;
                    break;
                case "K":
                    totalBobotK += bobot;
                    totalNilaiK += nilai;
                    break;
                case "P":
                    totalBobotP += bobot;
                    totalNilaiP += nilai;
                    break;
                case "UTS":
                    totalBobotUTS += bobot;
                    totalNilaiUTS += nilai;
                    break;
                case "UAS":
                    totalBobotUAS += bobot;
                    totalNilaiUAS += nilai;
                    break;
                default:
                    System.out.println("Simbol tidak dikenal");
                    break;
            }
        }

        // ===== Hitung persentase & kontribusi tiap komponen =====
        int persenPA = hitungPersen(totalNilaiPA, totalBobotPA);
        int persenT  = hitungPersen(totalNilaiT, totalBobotT);
        int persenK  = hitungPersen(totalNilaiK, totalBobotK);
        int persenP  = hitungPersen(totalNilaiP, totalBobotP);
        int persenUTS = hitungPersen(totalNilaiUTS, totalBobotUTS);
        int persenUAS = hitungPersen(totalNilaiUAS, totalBobotUAS);

        // Catatan: urutan (persen * bobot) / 100.0 dipakai (bukan persen/100.0*bobot)
        // untuk menghindari galat pembulatan floating-point (mis. 57.0 bisa jadi
        // 56.999999999999 jika dibagi dulu sebelum dikali), yang bisa menggeser Grade.
        double kontribusiPA  = (persenPA  * (double) bobotPA)  / 100.0;
        double kontribusiT   = (persenT   * (double) bobotT)   / 100.0;
        double kontribusiK   = (persenK   * (double) bobotK)   / 100.0;
        double kontribusiP   = (persenP   * (double) bobotP)   / 100.0;
        double kontribusiUTS = (persenUTS * (double) bobotUTS) / 100.0;
        double kontribusiUAS = (persenUAS * (double) bobotUAS) / 100.0;

        double nilaiAkhir = kontribusiPA + kontribusiT + kontribusiK
                + kontribusiP + kontribusiUTS + kontribusiUAS;

        // ===== Output =====
        System.out.println("Perolehan Nilai:");
        cetakBaris("Partisipatif", persenPA, kontribusiPA, bobotPA);
        cetakBaris("Tugas", persenT, kontribusiT, bobotT);
        cetakBaris("Kuis", persenK, kontribusiK, bobotK);
        cetakBaris("Proyek", persenP, kontribusiP, bobotP);
        cetakBaris("UTS", persenUTS, kontribusiUTS, bobotUTS);
        cetakBaris("UAS", persenUAS, kontribusiUAS, bobotUAS);

        System.out.println();
        System.out.printf(Locale.US, ">> Nilai Akhir: %.2f%n", nilaiAkhir);
        System.out.println(">> Grade: " + hitungGrade(nilaiAkhir));
    }

    // Integer division (truncate), aman karena nilai & totalBobot selalu >= 0
    private static int hitungPersen(int nilai, int totalBobot) {
        if (totalBobot == 0) return 0;
        return (nilai * 100) / totalBobot;
    }

    private static void cetakBaris(String nama, int persen, double kontribusi, int bobot) {
        System.out.printf(Locale.US, ">> %s: %d/100 (%.2f/%d)%n", nama, persen, kontribusi, bobot);
    }

    private static String hitungGrade(double nilaiAkhir) {
        if (nilaiAkhir >= 79.5) return "A";
        if (nilaiAkhir >= 72)   return "AB";
        if (nilaiAkhir >= 64.5) return "B";
        if (nilaiAkhir >= 57)   return "BC";
        if (nilaiAkhir >= 49.5) return "C";
        if (nilaiAkhir >= 34)   return "D";
        return "E";
    }
}