{
    while (getline > 0) {
        if (match($0, /^Run [0-9]+ transactions in [0-9]+,[0-9]{3} seconds, that is [0-9]+,[0-9]{2} transactions per second!$/)) {
            line1 = $0;
            if (getline > 0 && match($0, /^\tRun [0-9]+ Kontostands TX in  [0-9]+,[0-9]{3} seconds, that is [0-9]+,[0-9]{2} transactions per second!$/)) {
                line2 = $0;
                if (getline > 0 && match($0, /^\tRun [0-9]+ Analyse TX in  [0-9]+,[0-9]{3} seconds, that is [0-9]+,[0-9]{2} transactions per second!$/)) {
                    line3 = $0;
                    if (getline > 0 && match($0, /^\tRun [0-9]+ Einzahlungs TX in  [0-9]+,[0-9]{3} seconds, that is [0-9]+,[0-9]{2} transactions per second!$/)) {
                        line4 = $0;
                        gsub(/,/,".",line1); gsub(/,/,".",line2); gsub(/,/,".",line3); gsub(/,/,".",line4);
                        split(line1, fields1, /[^0-9.]+/); split(line2, fields2, /[^0-9.]+/); split(line3, fields3, /[^0-9.]+/); split(line4, fields4, /[^0-9.]+/);
                        print fields1[2]","fields1[3]","fields2[2]","fields2[3]","fields3[2]","fields3[3]","fields4[2]","fields4[3]
                    }
                }
            }
        }
    }
}
