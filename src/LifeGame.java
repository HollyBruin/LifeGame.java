import java.io.IOException;

public class LifeGame {
    final int habitatSatir = 19;
    final int habitatSutun = 19;
    int hucreHabitat[][];
    int hucreHabitatTmp[][];
    int[] pulsarSatir;
    int[] pSatir;
    int pulsarSutun[];

    public LifeGame() {
        // pulsar deseni oluşması için gerekli ön tanımlamalar
        pSatir = new int[]{3, 8, 10, 15};
        pulsarSatir = new int[]{0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0};
        pulsarSutun = new int[]{0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0};
        // gerçek yaşam alanı ve değişikliklerin yapılacağı yedek yaşam alanı tanımı
        hucreHabitat = new int[habitatSatir][habitatSutun];
        hucreHabitatTmp = new int[habitatSatir][habitatSutun];
        // tüm yedek ve gerçek yaşam alanı sıfırlanıyor
        int c = 0;
        for (int i = 0; i < habitatSatir; i++) {
            for (int y = 0; y < habitatSutun; y++) {
                hucreHabitatTmp[i][y] = c;
                hucreHabitat[i][y] = c;
            }
        }
        // pulsar deseni gerçek yaşam alanına atanıyor
        for (int satir = 0; satir < pSatir.length; satir++) {
            for (int sutun = 0; sutun < habitatSutun; sutun++) {
                hucreHabitat[pSatir[satir]][sutun] = pulsarSatir[sutun];
            }
        }
        for (int sutun = 0; sutun < pSatir.length; sutun++) {
            for (int satir = 0; satir < habitatSutun; satir++) {
                hucreHabitat[satir][pSatir[sutun]] = pulsarSatir[satir];
            }
        }
    }

    public void drawHabitat() {
        // gerçek yaşam alanı (hucreHabitat) ekrana çizdiriliyor
        for (int i = 0; i < habitatSatir; i++) {
            for (int y = 0; y < habitatSutun; y++) {
                if (hucreHabitat[i][y] == 1) {
                    System.out.print("X "); // Assuming 'X' represents a living cell
                } else {
                    System.out.print(". "); // Assuming '.' represents a dead cell
                }
            }
            System.out.println(); // Move to the next line after each row
        }
    }

    public int komsuCanliSayisi(int satir, int sutun) {
        int canliKomsuSayisi = 0;
        // koordinatları girilen hücre merkezde olmak üzere 3x3 lük alanda
        // canlı komşu sayısı tespiti yapılıyor. Eğer kendisi de canlı ise
        // canlı komşuya eklenmemelidir.
        for (int i = -1; i <= 1; i++) {
            for (int y = -1; y <= 1; y++) {
                int newSatir = satir + i;
                int newSutun = sutun + y;
                if (newSatir >= 0 && newSatir < habitatSatir && newSutun >= 0 && newSutun < habitatSutun) {
                    canliKomsuSayisi += hucreHabitat[newSatir][newSutun];
                }
            }
        }
        canliKomsuSayisi -= hucreHabitat[satir][sutun]; // kendisini düşürdük
        return canliKomsuSayisi;
    }

    public void newHabitatRule() {
        // Life Game'in 4 kuralına göre gerçek habitata bakılarak
        // bir sonraki iterasyon için geçici habitat (hucreHabitatTmp)
        // güncelleniyor
        for (int i = 0; i < habitatSatir; i++) {
            for (int j = 0; j < habitatSutun; j++) {
                int canliKomsuSayisi = komsuCanliSayisi(i, j);

                // Canlı hücre
                if (hucreHabitat[i][j] == 1) {
                    if (canliKomsuSayisi < 2 || canliKomsuSayisi > 3) {
                        // Kural 1 ve Kural 3: Az komşu veya fazla komşu, hücre ölür
                        hucreHabitatTmp[i][j] = 0; // Ölü hücre
                    } else {
                        // Kural 2: 2 veya 3 komşu, hücre yaşar
                        hucreHabitatTmp[i][j] = 1; // Canlı hücre devam eder
                    }
                }
                // Ölü hücre
                else {
                    if (canliKomsuSayisi == 3) {
                        // Kural 4: 3 komşu, ölü hücre canlanır
                        hucreHabitatTmp[i][j] = 1; // Canlı hücre
                    } else {
                        hucreHabitatTmp[i][j] = 0; // Ölü hücre devam eder
                    }
                }
            }
        }
        copyHabitat();
        // Life Game'in kurallarına göre güncellenen habitatı kopyala
    }

    public void copyHabitat() {
        // yedek hücreden tekrar orjinaline yükleme yap
        for (int i = 0; i < habitatSatir; i++) {
            System.arraycopy(hucreHabitatTmp[i], 0, hucreHabitat[i], 0, habitatSutun);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        LifeGame lg = new LifeGame();
        for (int i = 0; i < 20; i++) {
            lg.drawHabitat();
            lg.newHabitatRule();
            System.out.println();
            Thread.sleep(1500);
            clearScreen();  // Ekranı temizleme işlemi
        }
    }

    // Ekranı temizleme metodu
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
