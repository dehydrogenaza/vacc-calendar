package org.dehydrogenaza.data.datasources;

import org.dehydrogenaza.data.VaccineType;

import java.util.List;

public interface IVaccineSource {
    List<VaccineType> getVaccines();
}
