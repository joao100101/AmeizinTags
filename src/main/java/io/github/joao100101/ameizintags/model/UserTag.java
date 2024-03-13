package io.github.joao100101.ameizintags.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTag {
    private String tagAtual;
    private String tagAnterior;
}
