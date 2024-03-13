package io.github.joao100101.ameizintags.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Tag {
    private String name = "ERROR";
    private Integer slot = 0;
    private String item = "STONE";
    private Double valor = 0.0;
    private String prefixo = "ERROR";
    private boolean vip = false;
    private List<String> lore = new ArrayList<>();

    public Tag(String name, Integer slot, Boolean vip,String item, Double valor, String prefixo, List<String> lore) {
       if(name != null){
           this.name = name;
       }
       if(slot != null){
        this.slot = slot;
       }
       if(item != null){
        this.item = item;
       }
       if(valor != null){
        this.valor = valor;
       }
       if(prefixo != null){
        this.prefixo = prefixo;
       }
       if(lore != null){
        this.lore = lore;
       }
       if(vip != null){
        this.vip = vip;
       }
    }
}
