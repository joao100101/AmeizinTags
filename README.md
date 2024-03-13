
# AMEIZIN TAGS

Uma plugin que adiciona um menu de compra de tags customizadas para o seu servidor de minecraft.


## DEPENDÊNCIAS
Plugins necessários para o funcionamento do sistema de tags.

- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- [nChat](https://www.nickuc.com/en/details/nchat)


## FUNCIONALIDADES

- SQLite para salvar a tag atual e anterior.
- Sistema de cache para consultas mais rápidas.
- Funcional na versão 1.20



## COMANDOS E PERMISSÕES

### Comandos

| Comando  | Descrição                           |
| :-------- | :----------------------------------- |
| `/tag`    |  Abre o menu de tags                  |

#### Permissões

| Permissão         | Descrição                                         |
| :------------------------------ | :------------------------------------ |
| `ameizintags.vip`               | Acesso as tags marcadas como vip    |
| `ameizintags.tag.[nome da tag]` | Garante acesso a uma tag específica |




## CONFIGURAÇÃO PADRÃO

```yaml
Config:
    Mensagens:
      TagAdquirida: "&7Voce acaba de comprar a tag %tag% &7por %valor%! &a(TAG ATIVA)"
      TagRemovida: "&7Você removeu sua tag."
      TagVip: "&cEssa tag é exlusiva para vips."
      FaltaMoney: "&cVocê precisa de mais &4%valor% &cpara comprar essa tag."
    TamanhoMenu: 3 # Quantidade de linhas no baú (max 6 linhas)
    Tags:
      Assassino:
        slot: 0
        vip: false
        item: DIAMOND_SWORD
        valor: 12000
        prefixo: "&4[Assassino]&r"
        lore:
          - ''
          - '&7Compre a tag %prefixo% &7por &a%valor%'
          - ''
      Guerreiro:
        slot: 1
        vip: false
        item: DIAMOND_HELMET
        valor: 25000
        prefixo: "&3[Guerreiro]&r"
        lore:
          - ''
          - '&7Compre a tag %prefixo% &7por %valor%'
          - ''
```


## Autores

- [@João Victor Mundel](https://www.github.com/joao100101)

