Dette program er lavet af gruppe 3.
Gruppemedlemmer Stefan A. Luxhøj, Mads Ø. Hansen, Jacob B. Eriksen
Programmet er pakket som en executable .JAR fil, som ligger i roden af denne mappe.
- se "SkakBotProg.jar"-filen i roden (samme mappe som denne readme)

Sådan køres programmet:
1. Opdater din Java JDK til java 17
   - Direkte link til download for windows x64: https://download.oracle.com/java/17/archive/jdk-17.0.1_windows-x64_bin.exe
   - Ellers se listen af download muligheder gennem dette link: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   - Når det er downloadet så kør installeren og følg instruktionerne i installeren
   - Du bør nu have java 17
        - du kan tjekke version ved at åbne kommandoprompt (aka CMD) og skrive "java --version"
2. Kør "runJar.bat"-filen, som ligger i roden (samme mappe som denne README)
   - Kan gøres ved simpelt dobbeltklik
   - Dette bør starte en kommandoprompt (aka. CMD) og en GUI som viser et skakbræt
3. Vælg den farve du vil spille med ved at trykke på knapperne i bunden af GUI'en
4. Tryk på "start game nedert til højre"
   - note: Hvis du har valgt sort, så går der lidt tid hvor AI beregner sit træk.
   - Hvis du har valgt hvid så ryk brik.
5. Ryk brikker:
    - ryk en brik på brættet ved at klikke på brikken og så klikke der hvor du vil rykke til.
    - Vores GUI opdaterer først EFTER at computeren har evalueret og lavet et træk selv!
        - Dvs. du kan se AI beregner i kommandoprompten efter du har rykket, men GUI opdaterer først når det er din tur igen.
        - Du kan se hvilket træk AI udfører som sidste print i terminalen efter hver træk.
6. Når en af kongerne er døde eller i skakmat er spillet slut. For at spille igen skal programmet lukkes og startes igen (trin 2).

