package sk.uniza.fri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Input {

    private ArrayList<Integer> smernikVrcholov; // smerníky na prvu hranu pre dany vrchol pomocou arraylistu
    private ArrayList<Vrchol> zoznamVrcholov; // zoznam vrcholov grafu (arraylist)
    private ArrayList<Hrana> zoznamHran; // zoznam hran (arraylist) pomocou objektu
    private int[][] hrany; // zoznam hran pomocou matice H[i][0] je zaciatocny vrchol, H[i][1] je koncovy vrchol a H[i][3] je cena hrany
    private int[] smerniky; // smerniky na prvu hranu pre dany vrchol
    private int pocetVrchlov;
    private int pocetHran;
    /**
     * Metóda na načítanie hrán, ich zoradenie podľa prvého a druhého stĺpca a
     * vytvorenie poľa smerníkov na vrcholy
     *
     * @param name Názov súboru, z ktorého načítať hrany
     */
    public void readData(String name) {
        this.readFile(name);
        this.pocetVrchlov++;
        this.createZoznamVrcholov();
        this.sortHByFirstAndSecondColumn();
        this.sortZoznamHranByFirstAndSecondColumn();
        this.createSmernikVrcholov();
    }

    /**
     * Načítanie súboru s hranami a vyzvorenie zoznamov hrán, respektíve poľa
     * hreán H
     *
     * @param name Názov súboru, z ktorého načítať hrany
     */
    public void readFile(String name) {
        this.pocetVrchlov = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(name)));
            int lines = 0;
            while (br.readLine() != null) {
                lines++;
            }
            br.close();
            this.hrany = new int[lines + 1][3];
            this.zoznamHran = new ArrayList<>(lines + 1);
            int index = 1;
            br = new BufferedReader(new FileReader(new File(name)));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String replace = line.trim().replaceAll("\\s+", " ");
                    String[] split = replace.split(" ");
                    int vrchZ = Integer.parseInt(split[0]);
                    int vrchDo = Integer.parseInt(split[1]);
                    int cena = Integer.parseInt(split[2]);
                    this.zoznamHran.add(new Hrana(vrchZ, vrchDo, cena));
                    this.hrany[index][0] = vrchZ;
                    this.hrany[index][1] = vrchDo;
                    this.hrany[index][2] = cena;
                    index++;
                    if (this.pocetVrchlov < vrchZ) {
                        this.pocetVrchlov = vrchZ;
                    }
                    if (this.pocetVrchlov < vrchDo) {
                        this.pocetVrchlov = vrchDo;
                    }
                }
            }
            this.zoznamHran.add(0, new Hrana(0, 0, 0));
            this.pocetHran = this.hrany.length;
            br.close();

        } catch (FileNotFoundException ex) {
            System.err.println("Subor neexistuje");
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }

    /**
     * Metóda na vytvorenie smerníkov do poľa hrán pre jednotlivé vrcholy
     */
    private void createSmernikVrcholov() {
        this.smernikVrcholov = new ArrayList<>(this.pocetVrchlov + 2);
        this.smerniky = new int[this.pocetVrchlov + 2];
        for (int i = 0; i < this.pocetVrchlov + 2; i++) {
            this.smernikVrcholov.add(0);
        }
        for (int i = 1; i < this.zoznamHran.size(); i++) {
            int vrchol = this.zoznamHran.get(i).getVrcholZ();
            if (this.smernikVrcholov.get(vrchol) == 0) {
                this.smernikVrcholov.set(vrchol, i);
            }
            if (this.smerniky[vrchol] == 0) {
                this.smerniky[vrchol] = i;
            }
        }
        this.smernikVrcholov.set(this.pocetVrchlov + 1, this.zoznamHran.size());
        this.smerniky[this.pocetVrchlov + 1] = this.pocetHran;
        for (int i = this.pocetVrchlov; i >= 1; i--) {
            if (this.smernikVrcholov.get(i) == 0) {
                this.smernikVrcholov.set(i, this.smernikVrcholov.get(i + 1));
            }
            if (this.smerniky[i] == 0) {
                this.smerniky[i] = this.smerniky[i + 1];
            }
        }
    }

    /**
     * Metóda na zoradenie poľa H podľa prvého stĺpca (vrchol z)
     */
    public void sortHByFirstColumn() {
        Arrays.sort(this.hrany, (a, b) -> Integer.compare(a[0], b[0]));
    }

    /**
     * Metóda na zoradenie poľa H podľa prvého a druhého stĺpca (vrchol z,
     * vrchol do)
     */
    public void sortHByFirstAndSecondColumn() {
        Arrays.sort(this.hrany, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[0] < o2[0]) {
                    return -1;
                }
                if (o1[0] > o2[0]) {
                    return 1;
                }
                if (o1[1] < o2[1]) {
                    return -1;
                }
                if (o1[1] > o2[1]) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Metóda na zoradenie poľa H podľa druhého stĺpca (vrchol do)
     */
    public void sortHBySecondColumn() {
        Arrays.sort(this.hrany, (a, b) -> Integer.compare(a[1], b[1]));
    }

    /**
     * Metóda na zoradenie poľa H podľa tretieho stĺpca (cena hrany)
     */
    public void sortHByThirdColumn() {
        Arrays.sort(this.hrany, (a, b) -> Integer.compare(a[2], b[2]));
    }

    /**
     * Metóda na zoradenie zoznamu hrán podľa prvého a druhého stĺpca (vrchol z,
     * vrchol do)
     */
    public void sortZoznamHranByFirstAndSecondColumn() {
        this.zoznamHran.sort(new Comparator<Hrana>() {
            @Override
            public int compare(Hrana o1, Hrana o2) {
                if (o1.getVrcholZ() < o2.getVrcholZ()) {
                    return -1;
                }
                if (o1.getVrcholZ() > o2.getVrcholZ()) {
                    return 1;
                }
                if (o1.getVrcholDo() < o2.getVrcholDo()) {
                    return -1;
                }
                if (o1.getVrcholDo() > o2.getVrcholDo()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Metóda na zoradenie zoznamu hrán podľa prvého stĺpca (vrchol z)
     */
    public void sortZoznamHranByFirstColumn() {
        this.zoznamHran.sort(new Comparator<Hrana>() {
            @Override
            public int compare(Hrana o1, Hrana o2) {
                if (o1.getVrcholZ() < o2.getVrcholZ()) {
                    return -1;
                }
                if (o1.getVrcholZ() > o2.getVrcholZ()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Metóda na zoradenie zoznamu hrán podľa druhého stĺpca (vrchol do)
     */
    public void sortZoznamHranBySecondColumn() {
        this.zoznamHran.sort(new Comparator<Hrana>() {
            @Override
            public int compare(Hrana o1, Hrana o2) {
                if (o1.getVrcholDo() < o2.getVrcholDo()) {
                    return -1;
                }
                if (o1.getVrcholDo() > o2.getVrcholDo()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     * Metóda na zoradenie zoznamu hrán podľa tretieho stĺpca (cena hrany)
     */
    public void sortZoznamHranByThirdColumn() {
        this.zoznamHran.sort(new Comparator<Hrana>() {
            @Override
            public int compare(Hrana o1, Hrana o2) {
                if (o1.getCena() < o2.getCena()) {
                    return -1;
                }
                if (o1.getCena() > o2.getCena()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
     *
     * @return zoznam smerníkov do zoznamu hrán pre všetky vrcholy
     */
    public ArrayList<Integer> getSmernikVrcholov() {
        return this.smernikVrcholov;
    }

    /**
     *
     * @return zoznam hrán
     */
    public ArrayList<Hrana> getZoznamHran() {
        return this.zoznamHran;
    }

    /**
     *
     * @return pole hrán H
     */
    public int[][] getH() {
        return this.hrany;
    }

    /**
     *
     * @return pole smerníkov S do poľa hrán H
     */
    public int[] getS() {
        return this.smerniky;
    }

    /**
     *
     * @return počet vrcholov grafu ()
     */
    public int getPocetVrcholov() {
        return this.pocetVrchlov;
    }

    /**
     *
     * @return počet hrán grafu (pripočítaná aj fiktívna hrana 0)
     */
    public int getPocetHran() {
        return this.pocetHran;
    }

    /**
     * Nacitanie zaciatkov cinnosti
     *
     * @param name
     * @param pocetVrcholov
     * @return
     */
    public int[] nacitajTrvaniaCinnosti(String name, int pocetVrcholov) throws IOException, NullPointerException {
        int[] trvania = new int[pocetVrcholov + 1];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(name)));
            br.close();
            br = new BufferedReader(new FileReader(new File(name)));
            String line;
            for (int i = 1; i < pocetVrcholov + 1; i++) {
                line = br.readLine();
                int trv = Integer.parseInt(line);
                trvania[i] = trv;
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Subor neexistuje");
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        } finally{
            br.close();
        }
        return trvania;
    }

    /**
     * Metóda na zoradenie poľa H podľa tretieho stĺpca (cena hrany) od
     * najdrahšej
     */
    public void sortHByThirdColumnDesc() {
        Arrays.sort(this.hrany, (a, b) -> Integer.compare(b[2], a[2]));
    }

    public ArrayList<Vrchol> getZoznamVrcholov() {
        return zoznamVrcholov;
    }

    private void createZoznamVrcholov() {
        zoznamVrcholov = new ArrayList<>();
        for (int i = 0; i < pocetVrchlov; i++) {
            zoznamVrcholov.add(new Vrchol(i));
        }
    }
    public void readDataToky(String filename) {
        this.readFileToky(filename);
        this.pocetVrchlov++;
        this.createZoznamVrcholov();
        this.sortHByFirstAndSecondColumn();
        this.sortZoznamHranByFirstAndSecondColumn();
        this.createSmernikVrcholov();
    }

    private void readFileToky(String filename) {
        this.pocetVrchlov = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            int lines = 0;
            while (br.readLine() != null) {
                lines++;
            }
            br.close();
            this.hrany = new int[lines + 1][4];
            this.zoznamHran = new ArrayList<>(lines + 1);
            int index = 1;
            br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()){
                    String replace = line.trim().replaceAll("\\s+", " ");
                    String[] split = replace.split(" ");
                    int vrchZ = Integer.parseInt(split[0]);
                    int vrchDo = Integer.parseInt(split[1]);
                    int cena = Integer.parseInt(split[2]);
                    int kapacita = Integer.parseInt(split[3]);
                    this.zoznamHran.add(new Hrana(vrchZ, vrchDo, cena, kapacita));


                    this.hrany[index][0] = vrchZ;
                    this.hrany[index][1] = vrchDo;
                    this.hrany[index][2] = cena;
                    this.hrany[index][3] = kapacita;
                    index++;
                    if (this.pocetVrchlov < vrchZ) {
                        this.pocetVrchlov = vrchZ;
                    }
                    if (this.pocetVrchlov < vrchDo) {
                        this.pocetVrchlov = vrchDo;
                    }
                }
            }
            this.zoznamHran.add(0, new Hrana(0, 0, 0, 0));
            this.pocetHran = this.hrany.length;
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
