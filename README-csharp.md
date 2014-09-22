# Om C#-utgangspunktet

Denne koden bruker [Akka.NET](http://akkadotnet.github.io/wiki/), som er en .NET-port av Akka. Akka.NET fungerer veldig likt som Java-varianten av Akka.

Det er to hovedklasser du trenger å tenke på: `TjenesteSjef` og `KlientAnsvarlig`. Disse to Actor-ene har tilsvarende roller som i Java-oppgavene. Du skal i utgangspunktet ikke trenge å gjøre noen endringer i WinForms-koden.

Output fra KlientAnsvarlig går til en loggfil som heter `output.txt`. Det kan være lurt å kjøre en `tail`-variant på den filen for å følge med på endringer mens programmet kjører.
