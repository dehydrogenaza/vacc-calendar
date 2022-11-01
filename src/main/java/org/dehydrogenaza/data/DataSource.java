package org.dehydrogenaza.data;

import java.util.List;

public class DataSource {

    public List<Vaccine> getVaccines() {
        return List.of(
                new Vaccine("rtęć",                  1, Math.random() > 0.5),
                new Vaccine("autyzm",                2, Math.random() > 0.5),
                new Vaccine("czip od Billa Gatesa",  3, Math.random() > 0.5),
                new Vaccine("chip od Sorosa",        4, Math.random() > 0.5),
                new Vaccine("NOP",                   5, Math.random() > 0.5),
                new Vaccine("sok z buraka",          6, Math.random() > 0.5),
                new Vaccine("darwinizm",             7, Math.random() > 0.5),
                new Vaccine("niebinarność",          8, Math.random() > 0.5),
                new Vaccine("leworęczność",          9, Math.random() > 0.5),
                new Vaccine("wiedźmiństwo",          10, Math.random() > 0.5),
                new Vaccine("piśmienność",           11, Math.random() > 0.5));
    }
}
