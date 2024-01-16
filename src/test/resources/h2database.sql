CREATE SCHEMA IF NOT EXISTS sklep;

SET SCHEMA sklep;

-- Table structure for table `uzytkownik`
CREATE TABLE IF NOT EXISTS uzytkownik (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          login VARCHAR(100) NOT NULL,
                                          haslo VARCHAR(100) NOT NULL,
                                          imie VARCHAR(100) NOT NULL,
                                          nazwisko VARCHAR(100) NOT NULL,
                                          admin BOOLEAN NOT NULL
);

-- Table structure for table `produkty`
CREATE TABLE IF NOT EXISTS produkty (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        nazwa_produktu VARCHAR(100) NOT NULL,
                                        data_waznosci DATE NOT NULL,
                                        kategoria VARCHAR(100),
                                        ilosc INT NOT NULL
);

-- Table structure for table `grafik_pracy`
CREATE TABLE IF NOT EXISTS grafik_pracy (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            id_uzytkownika INT,
                                            dzien_tygodnia DATE,
                                            godzina_rozpoczecia TIME,
                                            godzina_zakonczenia TIME,
                                            FOREIGN KEY (id_uzytkownika) REFERENCES uzytkownik(id)
);

-- Insert sample data
INSERT INTO produkty (nazwa_produktu, data_waznosci, kategoria, ilosc)
VALUES
    ('Product1', '2023-12-31', 'Category1', 10),
    ('Product2', '2023-11-15', 'Category2', 5),
    ('Product3', '2024-02-28', 'Category1', 20);
